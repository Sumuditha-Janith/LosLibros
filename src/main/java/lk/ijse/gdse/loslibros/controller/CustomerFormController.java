package lk.ijse.gdse.loslibros.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import lk.ijse.gdse.loslibros.db.DBConnection;
import lk.ijse.gdse.loslibros.dto.CustomerDTO;
import lk.ijse.gdse.loslibros.dto.tm.CustomerTM;
import lk.ijse.gdse.loslibros.model.CustomerModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomerFormController {

    @FXML
    private Button btnCusDelete;

    @FXML
    private Button btnCusSave;

    @FXML
    private Button btnCusUpdate;

    @FXML
    private TableColumn<?, ?> colCusAddress;

    @FXML
    private TableColumn<?, ?> colCusId;

    @FXML
    private TableColumn<?, ?> colCusName;

    @FXML
    private TableColumn<?, ?> colCusNum;

    @FXML
    private Label lblCustomerId;

    @FXML
    private TableView<CustomerTM> tableCustomer;

    @FXML
    private TextField txtCusAddress;

    @FXML
    private TextField txtCusName;

    @FXML
    private TextField txtCusNum;

    @FXML
    void btnCusDeleteOnAction(ActionEvent event) throws SQLException  {
        String customerId = lblCustomerId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = customerModel.deleteCustomer(customerId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Customer deleted").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete customer!").show();
            }
        }

    }

    @FXML
    void btnCusGenReportOnAction(ActionEvent event) {
        CustomerTM customerTM = tableCustomer.getSelectionModel().getSelectedItem();

        if (customerTM == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a customer to generate the report.").show();
            return;
        }

        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/report/CustomerOrderDetailsReport.jrxml"));

            Connection connection = DBConnection.getInstance().getConnection();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_Customer_Id", customerTM.getCustomerId());
            parameters.put("P_Date", LocalDate.now().toString());

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException | SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate the report. Please try again.").show();
            e.printStackTrace();
        }
    }


    @FXML
    void btnCusResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void btnCusSaveOnAction(ActionEvent event) throws SQLException {
        String customerId = lblCustomerId.getText();
        String customerName = txtCusName.getText();
        String customerAddress = txtCusAddress.getText();
        String customerNum = txtCusNum.getText();

        if (validateCustomerDetails(customerName, customerAddress, customerNum)) {
            return;
        }

        CustomerDTO customerDTO = new CustomerDTO(
                customerId,
                customerName,
                customerAddress,
                customerNum
        );

        boolean isSaved = customerModel.saveCustomer(customerDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Customer saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save customer...!").show();
        }
    }

    @FXML
    void btnCusUpdateOnAction(ActionEvent event) throws SQLException {
        String customerId = lblCustomerId.getText();
        String customerName = txtCusName.getText();
        String customerAddress = txtCusAddress.getText();
        String customerNum = txtCusNum.getText();

        if (validateCustomerDetails(customerName, customerAddress, customerNum)) {
            return;
        }

        CustomerDTO customerDTO = new CustomerDTO(
                customerId,
                customerName,
                customerAddress,
                customerNum
        );

        boolean isUpdated = customerModel.updateCustomer(customerDTO);
        if (isUpdated) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Customer updated!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update customer!").show();
        }

    }

    @FXML
    void onClickCusTable(MouseEvent event) {
        CustomerTM customerTM = (CustomerTM) tableCustomer.getSelectionModel().getSelectedItem();
        if (customerTM != null) {
            lblCustomerId.setText(customerTM.getCustomerId());
            txtCusName.setText(customerTM.getCustomerName());
            txtCusAddress.setText(customerTM.getCustomerAddress());
            txtCusNum.setText(customerTM.getCustomerPhone());
        }

        btnCusSave.setDisable(true);

        btnCusDelete.setDisable(false);
        btnCusUpdate.setDisable(false);

    }

    public void initialize() {
        colCusId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCusAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colCusNum.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load customer id").show();
        }
    }

    private void refreshPage() throws SQLException {
        loadNextCustomerId();
        loadTableData();

        btnCusSave.setDisable(false);

        btnCusUpdate.setDisable(true);
        btnCusDelete.setDisable(true);

        txtCusName.setText("");
        txtCusAddress.setText("");
        txtCusNum.setText("");
    }

    CustomerModel customerModel = new CustomerModel();

    private void loadTableData() throws SQLException {
        ArrayList<CustomerDTO> customerDTOS = customerModel.getAllCustomers();

        ObservableList<CustomerTM> customerTMS = FXCollections.observableArrayList();

        for (CustomerDTO customerDTO : customerDTOS) {
            CustomerTM customerTM = new CustomerTM(
                    customerDTO.getCustomerId(),
                    customerDTO.getCustomerName(),
                    customerDTO.getCustomerAddress(),
                    customerDTO.getCustomerPhone()
            );
            customerTMS.add(customerTM);
        }
        tableCustomer.setItems(customerTMS);
    }

    private void loadNextCustomerId() throws SQLException {
        String nextCustomerId = customerModel.getNextCustomerId();
        lblCustomerId.setText(nextCustomerId);
    }

    private boolean validateCustomerDetails(String name, String address, String phone) {
        if (name == null || name.trim().isEmpty() || name.length() < 3) {
            new Alert(Alert.AlertType.WARNING, "Customer name must be at least 3 characters long.").show();
            return true;
        }

        if (!name.matches("^[a-zA-Z\\s\\.]+$")) {
            new Alert(Alert.AlertType.WARNING, "Customer name can only contain letters and spaces").show();
            return true;
        }

        if (address == null || address.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Customer address cannot be empty.").show();
            return true;
        }

        if (phone == null || !phone.matches("\\d{10}")) {
            new Alert(Alert.AlertType.WARNING, "Customer phone number must be exactly 10 digits.").show();
            return true;
        }

        return false;
    }
}
