package lk.ijse.gdse.loslibros;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppInitializer extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent load = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
        Scene scene = new Scene(load);

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/shop-icon.png")));
        stage.setTitle("Los Libros");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
