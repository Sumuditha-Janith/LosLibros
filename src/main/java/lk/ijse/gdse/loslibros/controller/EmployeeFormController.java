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
import lk.ijse.gdse.loslibros.dto.tm.EmployeeTM;
import lk.ijse.gdse.loslibros.model.EmployeeModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeFormController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<EmployeeTM, String> colEmpAddress;

    @FXML
    private TableColumn<EmployeeTM, String> colEmpId;

    @FXML
    private TableColumn<EmployeeTM, String> colEmpMail;

    @FXML
    private TableColumn<EmployeeTM, String> colEmpName;

    @FXML
    private TableColumn<EmployeeTM, Integer> colEmpNum;

    @FXML
    private TableColumn<EmployeeTM, String> colEmpRole;

    @FXML
    private TableColumn<EmployeeTM, Double> colEmpSalary;

    @FXML
    private TableView<EmployeeTM> tableEmployee;

    @FXML
    private Label lblEmployeeId;

    @FXML
    private TextField txtEmpAddress;

    @FXML
    private TextField txtEmpEmail;

    @FXML
    private TextField txtEmpName;

    @FXML
    private TextField txtEmpNum;

    @FXML
    private TextField txtEmpRole;

    @FXML
    private TextField txtEmpSalary;

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        String employeeId = lblEmployeeId.getText();
        String employeeName = txtEmpName.getText();
        String employeeRole = txtEmpRole.getText();
        String employeeSalary = txtEmpSalary.getText();
        String employeeAddress = txtEmpAddress.getText();
        String employeeNum = txtEmpNum.getText();
        String employeeEmail = txtEmpEmail.getText();

        if (employeeName.isEmpty() || employeeRole.isEmpty() || employeeSalary.isEmpty() ||
                employeeAddress.isEmpty() || employeeNum.isEmpty() || employeeEmail.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill in all fields").show();
            return;
        }

        if (!employeeSalary.matches("\\d+(\\.\\d{1,2})?")) {
            new Alert(Alert.AlertType.WARNING, "Salary must be a positive number with up to two decimal places").show();
            return;
        }

        if (!employeeNum.matches("\\d{10}")) {
            new Alert(Alert.AlertType.WARNING, "Contact number must be exactly 10 digits").show();
            return;
        }

        if (!employeeEmail.matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$")) {
            new Alert(Alert.AlertType.WARNING, "Invalid email format").show();
            return;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO(
                employeeId,
                employeeName,
                employeeRole,
                employeeSalary,
                employeeAddress,
                employeeNum,
                employeeEmail
        );

        boolean isSaved =employeeModel.saveEmployee(employeeDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Employee saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save employee...!").show();
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String employeeId = lblEmployeeId.getText();
        String employeeName = txtEmpName.getText();
        String employeeRole = txtEmpRole.getText();
        String employeeSalary = txtEmpSalary.getText();
        String employeeAddress = txtEmpAddress.getText();
        String employeeNum = txtEmpNum.getText();
        String employeeEmail = txtEmpEmail.getText();

        if (employeeName.isEmpty() || employeeRole.isEmpty() || employeeSalary.isEmpty() ||
                employeeAddress.isEmpty() || employeeNum.isEmpty() || employeeEmail.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill in all fields").show();
            return;
        }

        if (!employeeSalary.matches("\\d+(\\.\\d{1,2})?")) {
            new Alert(Alert.AlertType.WARNING, "Salary must be a positive number with up to two decimal places").show();
            return;
        }

        if (!employeeNum.matches("\\d{10}")) {
            new Alert(Alert.AlertType.WARNING, "Contact number must be exactly 10 digits").show();
            return;
        }

        if (!employeeEmail.matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$")) {
            new Alert(Alert.AlertType.WARNING, "Invalid email format").show();
            return;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO(
                employeeId,
                employeeName,
                employeeRole,
                employeeSalary,
                employeeAddress,
                employeeNum,
                employeeEmail
        );



        boolean isUpdate = employeeModel.updateEmployee(employeeDTO);
        if (isUpdate) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Employee updated...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update employee!").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String employeeId = lblEmployeeId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = employeeModel.deleteEmployee(employeeId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Employee deleted").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete employee!").show();
            }
        }

    }


    @FXML
    void onClickTable(MouseEvent event) {
        EmployeeTM employeeTM = tableEmployee.getSelectionModel().getSelectedItem();
        if (employeeTM != null) {
            lblEmployeeId.setText(employeeTM.getEmpId());
            txtEmpName.setText(employeeTM.getEmpName());
            txtEmpAddress.setText(employeeTM.getEmpAddress());
            txtEmpRole.setText(employeeTM.getEmpRole());
            txtEmpSalary.setText(employeeTM.getEmpSalary());
            txtEmpEmail.setText(employeeTM.getEmpMail());
            txtEmpNum.setText(employeeTM.getEmpNum());

            btnSave.setDisable(true);

            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colEmpName.setCellValueFactory(new PropertyValueFactory<>("empName"));
        colEmpRole.setCellValueFactory(new PropertyValueFactory<>("empRole"));
        colEmpSalary.setCellValueFactory(new PropertyValueFactory<>("empSalary"));
        colEmpAddress.setCellValueFactory(new PropertyValueFactory<>("empAddress"));
        colEmpNum.setCellValueFactory(new PropertyValueFactory<>("empNum"));
        colEmpMail.setCellValueFactory(new PropertyValueFactory<>("empMail"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load customer id").show();
        }
    }

    private void loadNextEmployeeId() throws SQLException {
        String nextEmployeeId = employeeModel.getNextEmployeeId();
        lblEmployeeId.setText(nextEmployeeId);
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextEmployeeId();
        loadTableData();

        btnSave.setDisable(false);

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        txtEmpName.setText("");
        txtEmpAddress.setText("");
        txtEmpRole.setText("");
        txtEmpSalary.setText("");
        txtEmpNum.setText("");
        txtEmpEmail.setText("");

    }

    EmployeeModel employeeModel = new EmployeeModel();

    private void loadTableData() throws SQLException, ClassNotFoundException {
        ArrayList<EmployeeDTO> employeeDTOS = employeeModel.getAllEmployees();

        ObservableList<EmployeeTM> employeeTMS = FXCollections.observableArrayList();


        for (EmployeeDTO employeeDTO : employeeDTOS) {
            EmployeeTM employeeTM = new EmployeeTM(
                    employeeDTO.getEmpId(),
                    employeeDTO.getEmpName(),
                    employeeDTO.getEmpRole(),
                    employeeDTO.getEmpSalary(),
                    employeeDTO.getEmpAddress(),
                    employeeDTO.getEmpNum(),
                    employeeDTO.getEmpMail()
                    );
            employeeTMS.add(employeeTM);
        }

        tableEmployee.setItems(employeeTMS);
    }



}



