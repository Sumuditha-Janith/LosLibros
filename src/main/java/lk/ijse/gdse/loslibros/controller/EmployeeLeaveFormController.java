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
import lk.ijse.gdse.loslibros.dto.EmployeeLeaveDTO;
import lk.ijse.gdse.loslibros.dto.tm.EmployeeLeaveTM;
import lk.ijse.gdse.loslibros.model.EmployeeLeaveModel;
import lk.ijse.gdse.loslibros.model.EmployeeModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeLeaveFormController implements Initializable {

    @FXML
    private Button btnEmpLvDelete;

    @FXML
    private Button btnEmpLvSave;

    @FXML
    private Button btnEmpLvUpdate;

    @FXML
    private ComboBox<String> cmbEmployeeId;

    @FXML
    private ComboBox<String> cmbLeaveType;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private TableColumn<?, ?> colEndDate;

    @FXML
    private TableColumn<?, ?> colLeaveId;

    @FXML
    private TableColumn<?, ?> colLeaveType;

    @FXML
    private TableColumn<?, ?> colLtEmpId;

    @FXML
    private TableColumn<?, ?> colStartDate;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private Label lblEmployeeName;

    @FXML
    private Label lblLeaveId;

    @FXML
    private DatePicker dtpEnd;

    @FXML
    private DatePicker dtpStart;

    @FXML
    private TableView<EmployeeLeaveTM> tableEmployeeLeave;

    @FXML
    void btnEmpLvDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String employeeLeaveId = lblLeaveId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = employeeLeaveModel.deleteEmployeeLeave(employeeLeaveId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Employee leave deleted").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete employee leave!").show();
            }
        }
    }

    @FXML
    void btnEmpLvResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();

    }

    @FXML
    void btnEmpLvSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (!validateForm()) {
            return;
        }

        String leaveId = lblLeaveId.getText();
        String leaveEmpId = cmbEmployeeId.getValue();
        String leaveType = cmbLeaveType.getValue();
        Date leaveStartDate = Date.valueOf(String.valueOf(dtpStart.getValue()));
        Date leaveEndDate = Date.valueOf(String.valueOf(dtpEnd.getValue()));
        String leaveStatus = cmbStatus.getValue();

        EmployeeLeaveDTO employeeLeaveDTO = new EmployeeLeaveDTO(
                leaveId,
                leaveEmpId,
                leaveType,
                leaveStartDate,
                leaveEndDate,
                leaveStatus
        );
        boolean isSaved = employeeLeaveModel.saveEmployeeLeave(employeeLeaveDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Employee leave saved!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save employee leave!").show();
        }
    }


    @FXML
    void btnEmpLvUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (!validateForm()) {
            return;
        }

        String leaveId = lblLeaveId.getText();
        String leaveType = cmbLeaveType.getValue();
        Date leaveStartDate = Date.valueOf(dtpStart.getValue());
        Date leaveEndDate = Date.valueOf(dtpEnd.getValue());
        String leaveStatus = cmbStatus.getValue();

        if (leaveType == null || leaveStartDate == null || leaveEndDate == null || leaveStatus == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill all required fields!").show();
            return;
        }

        EmployeeLeaveDTO employeeLeaveDTO = new EmployeeLeaveDTO(
                leaveId,
                null,
                leaveType,
                leaveStartDate,
                leaveEndDate,
                leaveStatus
        );

        boolean isUpdated = employeeLeaveModel.updateEmployeeLeave(employeeLeaveDTO);

        if (isUpdated) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Employee leave updated successfully!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update employee leave!").show();
        }
    }


    @FXML
    void cmbEmployeeOnAction(ActionEvent event) throws SQLException {
        String selectedLeaveFormEmpId = cmbEmployeeId.getSelectionModel().getSelectedItem();
        EmployeeDTO employeeDTO = employeeModel.findEmpById(selectedLeaveFormEmpId);

        if (employeeDTO != null) {
            lblEmployeeName.setText(employeeDTO.getEmpName());
        }
    }


    @FXML
    void onClickLeaveTable(MouseEvent event) {
        EmployeeLeaveTM employeeLeaveTM = tableEmployeeLeave.getSelectionModel().getSelectedItem();
        if (employeeLeaveTM != null) {

            lblLeaveId.setText(employeeLeaveTM.getLeaveId());
            cmbEmployeeId.setValue(employeeLeaveTM.getLeaveEmpId());
            cmbLeaveType.setValue(employeeLeaveTM.getLeaveType());
            dtpStart.setValue(employeeLeaveTM.getLeaveStartDate().toLocalDate());
            dtpEnd.setValue(employeeLeaveTM.getLeaveEndDate().toLocalDate());
            cmbStatus.setValue(employeeLeaveTM.getLeaveStatus());

            btnEmpLvSave.setDisable(true);
            btnEmpLvUpdate.setDisable(false);
            btnEmpLvDelete.setDisable(false);

            cmbEmployeeId.setDisable(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colLeaveId.setCellValueFactory(new PropertyValueFactory<>("leaveId"));
        colLtEmpId.setCellValueFactory(new PropertyValueFactory<>("leaveEmpId"));
        colLeaveType.setCellValueFactory(new PropertyValueFactory<>("leaveType"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("leaveStartDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("leaveEndDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("leaveStatus"));

        cmbLeaveType.setItems(FXCollections.observableArrayList("Sick", "Vacation","Personal"));
        cmbStatus.setItems(FXCollections.observableArrayList("Approved", "Pending","Rejected"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load leave id").show();
        }
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextEmployeeLeaveId();
        loadEmployeeLeaveTableData();
        loadEmpIdsforLeaveTable();

        cmbEmployeeId.setValue(null);
        lblEmployeeName.setText(" ");
        cmbLeaveType.setValue(null);
        dtpStart.setValue(null);
        dtpEnd.setValue(null);
        cmbStatus.setValue("Pending");

        btnEmpLvSave.setDisable(false);
        btnEmpLvUpdate.setDisable(true);
        btnEmpLvDelete.setDisable(true);
        cmbEmployeeId.setDisable(false);

    }

    EmployeeLeaveModel employeeLeaveModel = new EmployeeLeaveModel();

    private void loadEmployeeLeaveTableData() throws SQLException{
        ArrayList<EmployeeLeaveDTO> employeeLeaveDTOS = employeeLeaveModel.getAllEmployeeLeaves();

        ObservableList<EmployeeLeaveTM> employeeLeaveTMS = FXCollections.observableArrayList();

        for (EmployeeLeaveDTO employeeLeaveDTO : employeeLeaveDTOS) {
            EmployeeLeaveTM employeeLeaveTM = new EmployeeLeaveTM(
                    employeeLeaveDTO.getLeaveId(),
                    employeeLeaveDTO.getLeaveEmpId(),
                    employeeLeaveDTO.getLeaveType(),
                    employeeLeaveDTO.getLeaveStartDate(),
                    employeeLeaveDTO.getLeaveEndDate(),
                    employeeLeaveDTO.getLeaveStatus()
            );
            employeeLeaveTMS.add(employeeLeaveTM);
        }
        tableEmployeeLeave.setItems(employeeLeaveTMS);
    }

    private void loadNextEmployeeLeaveId() throws SQLException {
        String nextEmployeeLeaveId = employeeLeaveModel.getNextEmployeeLeaveId();
        lblLeaveId.setText(nextEmployeeLeaveId);
    }

    private final EmployeeModel employeeModel = new EmployeeModel();

    private void loadEmpIdsforLeaveTable() throws SQLException {

        ArrayList<String> employeeIds = employeeModel.getAllEmployIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(employeeIds);
        cmbEmployeeId.setItems(observableList);
    }

    private boolean validateForm() {

        if (cmbEmployeeId.getValue() == null || cmbEmployeeId.getValue().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an employee ID.").show();
            return false;
        }
        if (cmbLeaveType.getValue() == null || cmbLeaveType.getValue().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a leave type.").show();
            return false;
        }
        if (dtpStart.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a start date.").show();
            return false;
        }
        if (dtpEnd.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an end date.").show();
            return false;
        }
        if (cmbStatus.getValue() == null || cmbStatus.getValue().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a leave status.").show();
            return false;
        }

        Date startDate = Date.valueOf(dtpStart.getValue());
        Date endDate = Date.valueOf(dtpEnd.getValue());
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

        if (startDate.before(currentDate)) {
            new Alert(Alert.AlertType.WARNING, "Start date cannot be before the current date.").show();
            return false;
        }

        if (endDate.before(currentDate)) {
            new Alert(Alert.AlertType.WARNING, "End date cannot be before the current date.").show();
            return false;
        }

        if (endDate.equals(startDate)) {
            new Alert(Alert.AlertType.WARNING, "Start date and end date cannot be the same.").show();
            return false;
        }

        if (endDate.before(startDate)) {
            new Alert(Alert.AlertType.WARNING, "End date cannot be before the start date.").show();
            return false;
        }

        return true;
    }


}
