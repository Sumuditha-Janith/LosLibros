package lk.ijse.gdse.loslibros.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import lk.ijse.gdse.loslibros.dto.SupplierDTO;
import lk.ijse.gdse.loslibros.dto.tm.SupplierTM;
import lk.ijse.gdse.loslibros.model.SupplierModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class SupplierFormController {

    @FXML
    private Button btnSupplierDelete;

    @FXML
    private Button btnSupplierSave;

    @FXML
    private Button btnSupplierUpdate;

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private TableColumn<?, ?> colSupplierName;

    @FXML
    private Label lblSupplierId;

    @FXML
    private TableView<SupplierTM> tableSupplier;

    @FXML
    private TextField txtSupplierName;

    @FXML
    void btnSupplierDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        String supplierId = lblSupplierId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = supplierModel.deleteSupplier(supplierId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Supplier deleted").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete supplier!").show();
            }
        }
    }


    @FXML
    void btnSupplierResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
    }

    @FXML
    void btnSupplierSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (!validateFields()) {
            return;
        }

        String supplierId = lblSupplierId.getText();
        String supplierName = txtSupplierName.getText();


        SupplierDTO supplierDTO = new SupplierDTO(
                supplierId,
                supplierName
        );

        boolean isSaved = supplierModel.saveSupplier(supplierDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Supplier saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save supplier...!").show();
        }
    }

    @FXML
    void btnSupplierUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (!validateFields()) {
            return;
        }

        String supplierId = lblSupplierId.getText();
        String supplierName = txtSupplierName.getText();


        SupplierDTO supplierDTO = new SupplierDTO(
                supplierId,
                supplierName
        );

        boolean isUpdate = supplierModel.updateSupplier(supplierDTO);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Supplier updated...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update supplier!").show();
        }
    }

    @FXML
    void onClickSupplierTable(MouseEvent event) {
        SupplierTM supplierTM = tableSupplier.getSelectionModel().getSelectedItem();
        if (supplierTM != null) {
            lblSupplierId.setText(supplierTM.getSupplierId());
            txtSupplierName.setText(supplierTM.getSupplierName());

            btnSupplierSave.setDisable(true);

            btnSupplierDelete.setDisable(false);
            btnSupplierUpdate.setDisable(false);
        }
    }

    public void initialize() {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load supplier id").show();
        }
    }

    private void loadNextSupplierId() throws SQLException {
        String nextSupplierId = supplierModel.getNextSupplierId();
        lblSupplierId.setText(nextSupplierId);
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextSupplierId();
        loadTableData();

        btnSupplierSave.setDisable(false);

        btnSupplierUpdate.setDisable(true);
        btnSupplierDelete.setDisable(true);

        txtSupplierName.setText("");


    }

    SupplierModel supplierModel = new SupplierModel();

    private void loadTableData() throws SQLException, ClassNotFoundException {
        ArrayList<SupplierDTO> supplierDTOS = supplierModel.getAllSuppliers();

        ObservableList<SupplierTM> supplierTMS = FXCollections.observableArrayList();

        for (SupplierDTO supplierDTO : supplierDTOS) {
            SupplierTM supplierTM = new SupplierTM(
                    supplierDTO.getSupplierId(),
                    supplierDTO.getSupplierName()
            );
            supplierTMS.add(supplierTM);
        }
        tableSupplier.setItems(supplierTMS);
    }

    private boolean validateFields() {
        String supplierName = txtSupplierName.getText();

        if (supplierName == null || supplierName.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a supplier name.").show();
            return false;
        }

        return true;
    }

}
