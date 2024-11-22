package lk.ijse.gdse.loslibros.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import lk.ijse.gdse.loslibros.dto.PublisherDTO;
import lk.ijse.gdse.loslibros.dto.tm.PublisherTM;
import lk.ijse.gdse.loslibros.model.PublisherModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class PublisherFormController {

    @FXML
    private Button btnPublisherDelete;

    @FXML
    private Button btnPublisherSave;

    @FXML
    private Button btnPublisherUpdate;

    @FXML
    private TableColumn<?, ?> colPublisherId;

    @FXML
    private TableColumn<?, ?> colPublisherName;

    @FXML
    private Label lblPublisherId;

    @FXML
    private TableView<PublisherTM> tablePublisher;

    @FXML
    private TextField txtPublisherName;

    @FXML
    void btnPublisherDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        String publisherId = lblPublisherId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = publisherModel.deletePublisher(publisherId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Publisher deleted").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete publisher!").show();
            }
        }

    }

    @FXML
    void btnPublisherResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
    }

    @FXML
    void btnPublisherSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (!validateFields()) {
            return;
        }

        String publisherId = lblPublisherId.getText();
        String publisherName = txtPublisherName.getText();

        PublisherDTO publisherDTO = new PublisherDTO(
                publisherId,
                publisherName
        );

        boolean isSaved = publisherModel.savePublisher(publisherDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Publisher saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save publisher...!").show();
        }
    }

    @FXML
    void btnPublisherUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (!validateFields()) {
            return;
        }

        String publisherId = lblPublisherId.getText();
        String publisherName = txtPublisherName.getText();

        PublisherDTO publisherDTO = new PublisherDTO(
                publisherId,
                publisherName
        );

        boolean isUpdated = publisherModel.updatePublisher(publisherDTO);
        if (isUpdated) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Publisher updated...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update publisher...!").show();
        }

    }

    @FXML
    void onClickPublisherTable(MouseEvent event) {

        PublisherTM publisherTM = tablePublisher.getSelectionModel().getSelectedItem();
        if (publisherTM != null) {
            lblPublisherId.setText(publisherTM.getPublisherId());
            txtPublisherName.setText(publisherTM.getPublisherName());

            btnPublisherSave.setDisable(true);

            btnPublisherDelete.setDisable(false);
            btnPublisherUpdate.setDisable(false);
        }

    }

    public void initialize() {
        colPublisherId.setCellValueFactory(new PropertyValueFactory<>("publisherId"));
        colPublisherName.setCellValueFactory(new PropertyValueFactory<>("publisherName"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load publisher id").show();
        }
    }

    private void loadNextPublisherId() throws SQLException {
        String nextPublisherId = publisherModel.getNextPublisherId();
        lblPublisherId.setText(nextPublisherId);
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextPublisherId();
        loadTableData();

        btnPublisherSave.setDisable(false);

        btnPublisherUpdate.setDisable(true);
        btnPublisherDelete.setDisable(true);

        txtPublisherName.setText("");


    }

    PublisherModel publisherModel = new PublisherModel();

    private void loadTableData() throws SQLException, ClassNotFoundException {
        ArrayList<PublisherDTO> publisherDTOS = publisherModel.getAllPublishers();

        ObservableList<PublisherTM> publisherTMS = FXCollections.observableArrayList();


        for (PublisherDTO publisherDTO : publisherDTOS) {
            PublisherTM publisherTM = new PublisherTM(
                    publisherDTO.getPublisherId(),
                    publisherDTO.getPublisherName()
            );
            publisherTMS.add(publisherTM);
        }

        tablePublisher.setItems(publisherTMS);
    }

    private boolean validateFields() {
        String publisherName = txtPublisherName.getText();

        if (publisherName == null || publisherName.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a publisher name.").show();
            return false;
        }

        return true;
    }

}
