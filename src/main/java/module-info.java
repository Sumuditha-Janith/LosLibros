module lk.ijse.gdse.loslibros {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;
    requires net.sf.jasperreports.core;


    opens lk.ijse.gdse.loslibros.controller to javafx.fxml;
    opens lk.ijse.gdse.loslibros.dto.tm to  javafx.base;

    exports lk.ijse.gdse.loslibros;
}