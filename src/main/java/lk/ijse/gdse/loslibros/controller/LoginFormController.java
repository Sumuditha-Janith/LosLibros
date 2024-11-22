package lk.ijse.gdse.loslibros.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoginFormController {

    @FXML
    private AnchorPane ancLoginPage;

    @FXML
    private PasswordField pwdPassword;

    @FXML
    private TextField txtUsername;

    public static String loggedInUser;

    @FXML
    void signInOnAction(ActionEvent event) throws IOException {
        String username = txtUsername.getText();
        String password = pwdPassword.getText();

        if ((username.equals("sumuditha") && password.equals("2001")) ||
                (username.equals("employee") && password.equals("1234"))) {
            loggedInUser = username;
            AnchorPane load = FXMLLoader.load(getClass().getResource("/view/DashBoard.fxml"));
            ancLoginPage.getChildren().clear();
            ancLoginPage.getChildren().add(load);
        } else {
            txtUsername.clear();
            pwdPassword.clear();
            new Alert(Alert.AlertType.ERROR, "Wrong Username or Password").show();
        }
    }

}
