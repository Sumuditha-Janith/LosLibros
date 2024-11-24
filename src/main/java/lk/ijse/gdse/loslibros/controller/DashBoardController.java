package lk.ijse.gdse.loslibros.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import static lk.ijse.gdse.loslibros.controller.LoginFormController.loggedInUser;


import java.io.IOException;

public class DashBoardController {

    @FXML
    private AnchorPane anchorPaneMain;

    @FXML
    private AnchorPane nextPage;

    @FXML
    private Button buttonManageEmp;

    @FXML
    private Button buttonEmpLeave;

    @FXML
    private Button buttonEmpPayroll;

    @FXML
    public void initialize() {
        if (!"sumuditha".equals(loggedInUser)) {
            buttonManageEmp.setDisable(true);
            buttonEmpLeave.setDisable(true);
            buttonEmpPayroll.setDisable(true);
        }
    }

    @FXML
    void buttonManageEmployee(ActionEvent event) {
        navigateTo("/view/EmployeeForm.fxml");
    }

    @FXML
    void buttonManageBook(ActionEvent event) {
        navigateTo("/view/BookForm.fxml");
    }

    @FXML
    void buttonManageCustomer(ActionEvent event) {
        navigateTo("/view/CustomerForm.fxml");
    }

    @FXML
    void buttonAddAuthor(ActionEvent event) {
        navigateTo("/view/AuthorForm.fxml");
    }

    @FXML
    void buttonAddCategory(ActionEvent event) {
        navigateTo("/view/CategoryForm.fxml");
    }

    @FXML
    void buttonAddPublisher(ActionEvent event) {
        navigateTo("/view/PublisherForm.fxml");
    }

    @FXML
    void buttonAddSupplier(ActionEvent event) {
        navigateTo("/view/SupplierForm.fxml");
    }


    @FXML
    void buttonOrders(ActionEvent event) {
        navigateTo("/view/OrderForm.fxml");
    }

    @FXML
    void buttonEmployeeLeave(ActionEvent event) {
        navigateTo("/view/EmployeeLeaveForm.fxml");
    }

    @FXML
    void buttonEmployeePayroll(ActionEvent event) {
        navigateTo("/view/EmployeePayrollForm.fxml");
    }

    @FXML
    void signOutOnAction(ActionEvent event) {
        try {
            loggedInUser = null;

            AnchorPane loginPage = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));

            nextPage.getScene().setRoot(loginPage);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load login page!").show();
        }
    }


    public void navigateTo(String fxmlPath) {
        try {
            nextPage.getChildren().clear();
            AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));


            load.prefWidthProperty().bind(nextPage.widthProperty());
            load.prefHeightProperty().bind(nextPage.heightProperty());


            nextPage.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load page!").show();
        }
    }

}
