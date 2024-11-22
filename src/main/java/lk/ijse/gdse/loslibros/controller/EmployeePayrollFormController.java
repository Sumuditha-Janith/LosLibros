package lk.ijse.gdse.loslibros.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import lk.ijse.gdse.loslibros.dto.EmployeeDTO;
import lk.ijse.gdse.loslibros.dto.EmployeePayrollDTO;
import lk.ijse.gdse.loslibros.dto.tm.EmployeePayrollTM;
import lk.ijse.gdse.loslibros.model.EmployeeModel;
import lk.ijse.gdse.loslibros.model.EmployeePayrollModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EmployeePayrollFormController implements Initializable {

    @FXML
    private Button btnEmpPayDelete;

    @FXML
    private Button btnEmpPaySave;

    @FXML
    private Button btnEmpPayUpdate;

    @FXML
    private ComboBox<String> cmbEmployeeIdPf;

    @FXML
    private TableColumn<?, ?> colBasicSalary;

    @FXML
    private TableColumn<?, ?> colDeductions;

    @FXML
    private TableColumn<?, ?> colNetSalary;

    @FXML
    private TableColumn<?, ?> colPayDate;

    @FXML
    private TableColumn<?, ?> colPayId;

    @FXML
    private TableColumn<?, ?> colPtEmpId;

    @FXML
    private TableColumn<?, ?> coldBonuses;

    @FXML
    private Label lblBasicSalary;

    @FXML
    private Label lblEmployeeNamePf;

    @FXML
    private Label lblPayId;

    @FXML
    private Label lblPayDate;

    @FXML
    private TableView<EmployeePayrollTM> tableEmployeePayroll;

    @FXML
    private TextField txtDeductions;

    @FXML
    private TextField txtBonus;

    @FXML
    void btnEmpPayDeleteOnAction(ActionEvent event) throws Exception {
        String payrollId = lblPayId.getText();

        if (payrollId != null && !payrollId.isEmpty()) {
            boolean isDeleted = employeePayrollModel.deleteEmployeePayroll(payrollId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Payroll record deleted successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete the payroll record!").show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a valid Payroll ID!").show();
        }
    }

    @FXML
    void btnEmpPayResetOnAction(ActionEvent event) throws Exception {
        refreshPage();
    }

    @FXML
    void btnEmpPaySaveOnAction(ActionEvent event) throws Exception {

        if (!validateFields()) {
            return;
        }

        String payrollId = lblPayId.getText();
        String payrollEmpId = cmbEmployeeIdPf.getSelectionModel().getSelectedItem();
        Date payrollDate = Date.valueOf(lblPayDate.getText());
        String baseSalary = lblBasicSalary.getText();
        String payrollDeductions = txtDeductions.getText();
        String bonuses = txtBonus.getText();

        EmployeePayrollDTO employeePayrollDTO = new EmployeePayrollDTO(
                payrollId,
                payrollEmpId,
                payrollDate,
                baseSalary,
                payrollDeductions,
                bonuses,
                null
        );

        boolean isSaved = employeePayrollModel.saveEmployeePayroll(employeePayrollDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Details saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save details...!").show();
        }
    }

    @FXML
    void btnEmpPayUpdateOnAction(ActionEvent event) {

        if (!validateFields()) {
            return;
        }

        String payrollId = lblPayId.getText();

        if (payrollId == null || payrollId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a valid Payroll ID!").show();
            return;
        }

        try {
            String deductions = txtDeductions.getText();
            String bonuses = txtBonus.getText();

            boolean isUpdated = employeePayrollModel.updateEmployeePayroll(payrollId, deductions, bonuses);

            if (isUpdated) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid input! Please enter valid numbers for Deductions and Bonuses.").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database error" + e.getMessage()).show();
        }
    }

    @FXML
    void cmbEmployeeOnAction(ActionEvent event) throws SQLException {

        String selectedPayFormEmpId = cmbEmployeeIdPf.getSelectionModel().getSelectedItem();
        EmployeeDTO employeeDTO = employeeModel.findEmpById(selectedPayFormEmpId);

        if (employeeDTO != null) {
            lblEmployeeNamePf.setText(employeeDTO.getEmpName());
            lblBasicSalary.setText(employeeDTO.getEmpSalary());
        }
    }


    @FXML
    void onClickPayTable(MouseEvent event) {
        EmployeePayrollTM employeePayrollTM = tableEmployeePayroll.getSelectionModel().getSelectedItem();
        if (employeePayrollTM != null) {

            lblPayId.setText(employeePayrollTM.getPayrollId());
            cmbEmployeeIdPf.setValue(employeePayrollTM.getPayrollEmpId());
            lblBasicSalary.setText(employeePayrollTM.getBaseSalary());
            lblPayDate.setText(String.valueOf(employeePayrollTM.getPayrollDate()));
            txtDeductions.setText(employeePayrollTM.getDeductions());
            txtBonus.setText(employeePayrollTM.getBonuses());

            cmbEmployeeIdPf.setDisable(true);
            btnEmpPaySave.setDisable(true);
            btnEmpPayUpdate.setDisable(false);
            btnEmpPayDelete.setDisable(false);
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colPayId.setCellValueFactory(new PropertyValueFactory<>("payrollId"));
        colPtEmpId.setCellValueFactory(new PropertyValueFactory<>("payrollEmpId"));
        colPayDate.setCellValueFactory(new PropertyValueFactory<>("payrollDate"));
        colBasicSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        colDeductions.setCellValueFactory(new PropertyValueFactory<>("deductions"));
        coldBonuses.setCellValueFactory(new PropertyValueFactory<>("bonuses"));
        colNetSalary.setCellValueFactory(new PropertyValueFactory<>("netSalary"));


        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load").show();
        }
    }

    private void refreshPage() throws Exception {

        lblPayDate.setText(LocalDate.now().toString());


        loadNextEmpPayId();
        loadEmpPayTableData();

        loadEmpIds();

        btnEmpPaySave.setDisable(false);
        btnEmpPayUpdate.setDisable(true);
        btnEmpPayDelete.setDisable(true);

        cmbEmployeeIdPf.setDisable(false);
        cmbEmployeeIdPf.setValue(null);
        lblEmployeeNamePf.setText(" ");
        lblBasicSalary.setText(" ");
        txtDeductions.setText(" ");
        txtBonus.setText(" ");

    }

    EmployeePayrollModel employeePayrollModel = new EmployeePayrollModel();

    private void loadEmpPayTableData() throws SQLException {
        ArrayList<EmployeePayrollDTO> employeePayrollDTOS = employeePayrollModel.getAllEmployeePayrolls();

        ObservableList<EmployeePayrollTM> employeePayrollTMS = FXCollections.observableArrayList();


        for (EmployeePayrollDTO employeePayrollDTO : employeePayrollDTOS) {
            EmployeePayrollTM employeePayrollTM = new EmployeePayrollTM(
                    employeePayrollDTO.getPayrollId(),
                    employeePayrollDTO.getPayrollEmpId(),
                    employeePayrollDTO.getPayrollDate(),
                    employeePayrollDTO.getBaseSalary(),
                    employeePayrollDTO.getDeductions(),
                    employeePayrollDTO.getBonuses(),
                    employeePayrollDTO.getNetSalary()
            );
            employeePayrollTMS.add(employeePayrollTM);
        }

        tableEmployeePayroll.setItems(employeePayrollTMS);
    }

    public void loadNextEmpPayId() throws SQLException {
        String nextEmpPayId = employeePayrollModel.getNextEmployeePayrollId();

        lblPayId.setText(nextEmpPayId);

    }

    private final EmployeeModel employeeModel = new EmployeeModel();

    private void loadEmpIds() throws SQLException {

        ArrayList<String> employeeIds = employeeModel.getAllEmployIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(employeeIds);
        cmbEmployeeIdPf.setItems(observableList);


    }

    private boolean validateFields() {
        if (cmbEmployeeIdPf.getValue() == null || cmbEmployeeIdPf.getValue().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an employee ID.").show();
            return false;
        }

        if (txtDeductions.getText() == null || txtDeductions.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter deductions.").show();
            return false;
        }

        try {
            double deductions = Double.parseDouble(txtDeductions.getText());
            if (deductions < 0) {
                new Alert(Alert.AlertType.WARNING, "Deductions cannot be negative.").show();
                return false;
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid number for deductions.").show();
            return false;
        }

        if (txtBonus.getText() == null || txtBonus.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter bonuses.").show();
            return false;
        }
        try {
            double bonuses = Double.parseDouble(txtBonus.getText());
            if (bonuses < 0) {
                new Alert(Alert.AlertType.WARNING, "Bonuses cannot be negative.").show();
                return false;
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid number for bonuses.").show();
            return false;
        }

        return true;
    }


}
