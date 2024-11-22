package lk.ijse.gdse.loslibros.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import lk.ijse.gdse.loslibros.dto.AuthorDTO;
import lk.ijse.gdse.loslibros.dto.tm.AuthorTM;
import lk.ijse.gdse.loslibros.model.AuthorModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class AuthorFormController {

    @FXML
    private Button btnAuthorDelete;

    @FXML
    private Button btnAuthorSave;

    @FXML
    private Button btnAuthorUpdate;

    @FXML
    private TableColumn<?, ?> colAuthorId;

    @FXML
    private TableColumn<?, ?> colAuthorName;

    @FXML
    private Label lblAuthorId;

    @FXML
    private TableView<AuthorTM> tableAuthor;

    @FXML
    private TextField txtAuthorName;

    @FXML
    void btnAuthorDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String authorId = lblAuthorId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = authorModel.deleteAuthor(authorId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Author deleted").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete author!").show();
            }
        }
    }

    @FXML
    void btnAuthorResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
    }

    @FXML
    void btnAuthorSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String authorId = lblAuthorId.getText();
        String authorName = txtAuthorName.getText();

        if (!validateAuthorFields(authorName)) {
            return;
        }

        AuthorDTO authorDTO = new AuthorDTO(
                authorId,
                authorName
        );

        boolean isSaved = authorModel.saveAuthor(authorDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Author saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save Author...!").show();
        }

    }

    @FXML
    void btnAuthorUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String authorId = lblAuthorId.getText();
        String authorName = txtAuthorName.getText();

        if (!validateAuthorFields(authorName)) {
            return;
        }

        AuthorDTO authorDTO = new AuthorDTO(
                authorId,
                authorName
        );

        boolean isUpdate = authorModel.updateAuthor(authorDTO);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Author updated...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update author!").show();
        }
    }


    @FXML
    void onClickAuthorTable(MouseEvent event) {
        AuthorTM authorTM = tableAuthor.getSelectionModel().getSelectedItem();
        if (authorTM != null) {
            lblAuthorId.setText(authorTM.getAuthorId());
            txtAuthorName.setText(authorTM.getAuthorName());

            btnAuthorSave.setDisable(true);

            btnAuthorDelete.setDisable(false);
            btnAuthorUpdate.setDisable(false);
        }

    }

    public void initialize() {
        colAuthorId.setCellValueFactory(new PropertyValueFactory<>("authorId"));
        colAuthorName.setCellValueFactory(new PropertyValueFactory<>("authorName"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load author id").show();
        }
    }

    private void loadNextAuthorId() throws SQLException {
        String nextAuthorId = authorModel.getNextAuthorId();
        lblAuthorId.setText(nextAuthorId);
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextAuthorId();
        loadTableData();

        btnAuthorSave.setDisable(false);

        btnAuthorUpdate.setDisable(true);
        btnAuthorDelete.setDisable(true);

        txtAuthorName.setText("");


    }

    AuthorModel authorModel = new AuthorModel();

    private void loadTableData() throws SQLException, ClassNotFoundException {
        ArrayList<AuthorDTO> authorDTOS = authorModel.getAllAuthors();

        ObservableList<AuthorTM> authorTMS = FXCollections.observableArrayList();


        for (AuthorDTO authorDTO : authorDTOS) {
            AuthorTM authorTM = new AuthorTM(
                    authorDTO.getAuthorId(),
                    authorDTO.getAuthorName()
            );
            authorTMS.add(authorTM);
        }

        tableAuthor.setItems(authorTMS);
    }

    private boolean validateAuthorFields(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Author name cannot be empty").show();
            return false;
        }

        if (!authorName.matches("^[a-zA-Z\\s\\.]+$")) {
            new Alert(Alert.AlertType.WARNING, "Author name can have only letters, spaces").show();
            return false;
        }

        if (authorName.length() > 100) {
            new Alert(Alert.AlertType.WARNING, "Author name cannot be more than 50 characters").show();
            return false;
        }

        return true;
    }


}
