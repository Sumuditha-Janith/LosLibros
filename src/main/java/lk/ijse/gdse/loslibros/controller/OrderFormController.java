package lk.ijse.gdse.loslibros.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import lk.ijse.gdse.loslibros.dto.BookDTO;
import lk.ijse.gdse.loslibros.dto.CustomerDTO;
import lk.ijse.gdse.loslibros.dto.OrderDTO;
import lk.ijse.gdse.loslibros.dto.OrderDetailsDTO;
import lk.ijse.gdse.loslibros.dto.tm.CartTM;
import lk.ijse.gdse.loslibros.model.BookModel;
import lk.ijse.gdse.loslibros.model.CustomerModel;
import lk.ijse.gdse.loslibros.model.OrderModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderFormController implements Initializable {

    @FXML
    private ComboBox<String> cmbBookId;

    @FXML
    private ComboBox<String> cmbCustomerId;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colBookId;

    @FXML
    private TableColumn<?, ?> colBookName;

    @FXML
    private TableColumn<?, ?> colCartQuantity;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private Label lblBookName;

    @FXML
    private Label lblBookPrice;

    @FXML
    private Label lblBookQty;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Label lblOrderDate;

    @FXML
    private Label lblOrderId;

    @FXML
    private TableView<CartTM> tableCart;

    @FXML
    private TextField txtAddToCartQty;

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {

        String selectedBookId = cmbBookId.getValue();

        if (selectedBookId == null) {
            new Alert(Alert.AlertType.ERROR, "Please select book..!").show();
            return;
        }

        String cartQtyString = txtAddToCartQty.getText();

        String qtyPattern = "^[0-9]+$";

        if (!cartQtyString.matches(qtyPattern)){
            new Alert(Alert.AlertType.ERROR, "Please enter valid quantity..!").show();
            return;
        }

        String bookName = lblBookName.getText();
        int cartQty = Integer.parseInt(cartQtyString);
        int qtyOnHand = Integer.parseInt(lblBookQty.getText());

        if (qtyOnHand < cartQty) {
            new Alert(Alert.AlertType.ERROR, "Not enough books..!").show();
            return;
        }

        txtAddToCartQty.setText("");

        double unitPrice = Double.parseDouble(lblBookPrice.getText());
        double total = unitPrice * cartQty;

        for (CartTM cartTM : cartTMS) {

            if (cartTM.getBookId().equals(selectedBookId)) {
                int newQty = cartTM.getCartQuantity() + cartQty;
                cartTM.setCartQuantity(newQty);
                cartTM.setTotal(unitPrice * newQty);

                tableCart.refresh();
                return;
            }
        }


        Button btn = new Button("Remove");

        CartTM newCartTM = new CartTM(
                selectedBookId,
                bookName,
                cartQty,
                unitPrice,
                total,
                btn
        );

        btn.setOnAction(actionEvent -> {

            cartTMS.remove(newCartTM);

            tableCart.refresh();
        });

        cartTMS.add(newCartTM);

    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) throws SQLException {

        if (tableCart.getItems().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please add books to cart..!").show();
            return;
        }
        if (cmbCustomerId.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select a customer to place a order..!").show();
            return;
        }

        String orderId = lblOrderId.getText();
        Date dateOfOrder = Date.valueOf(lblOrderDate.getText());
        String customerId = cmbCustomerId.getValue();

        ArrayList<OrderDetailsDTO> orderDetailsDTOS = new ArrayList<>();

        for (CartTM cartTM : cartTMS) {

            OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(
                    orderId,
                    cartTM.getBookId(),
                    cartTM.getCartQuantity(),
                    cartTM.getUnitPrice()
            );

            orderDetailsDTOS.add(orderDetailsDTO);
        }

        OrderDTO orderDTO = new OrderDTO(
                orderId,
                customerId,
                dateOfOrder,
                orderDetailsDTOS
        );

        boolean isSaved = orderModel.saveOrder(orderDTO);

        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Order saved..!").show();

            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "Order failed..!").show();
        }


    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void cmbBookOnAction(ActionEvent event) throws SQLException {

        String selectedBookId = cmbBookId.getSelectionModel().getSelectedItem();
        BookDTO bookDTO = bookModel.findBookById(selectedBookId);

        if (bookDTO != null) {

            lblBookName.setText(bookDTO.getBookName());
            lblBookQty.setText(String.valueOf(bookDTO.getBookQuantity()));
            lblBookPrice.setText(String.valueOf(bookDTO.getBookPrice()));
        }

    }

    @FXML
    void cmbCustomerOnAction(ActionEvent event) throws SQLException {
        String selectedCustomerId = cmbCustomerId.getSelectionModel().getSelectedItem();
        CustomerDTO customerDTO = customerModel.findCusById(selectedCustomerId);

        if (customerDTO != null) {

            lblCustomerName.setText(customerDTO.getCustomerName());

            cmbCustomerId.setDisable(true);
        }
    }

    private final OrderModel orderModel = new OrderModel();
    private final CustomerModel customerModel = new CustomerModel();
    private final BookModel bookModel = new BookModel();

    private final ObservableList<CartTM> cartTMS = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();

        try {
            refreshPage();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load data..!").show();
        }
    }


    private void setCellValues() {
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colCartQuantity.setCellValueFactory(new PropertyValueFactory<>("cartQuantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("removeBtn"));

        tableCart.setItems(cartTMS);
    }


    private void refreshPage() throws SQLException {
        lblOrderId.setText(orderModel.getNextOrderId());

        lblOrderDate.setText(LocalDate.now().toString());

        loadCustomerIds();
        loadBookIds();


        cmbCustomerId.getSelectionModel().clearSelection();
        cmbBookId.getSelectionModel().clearSelection();
        lblBookName.setText("");
        lblBookQty.setText("");
        lblBookPrice.setText("");
        txtAddToCartQty.setText("");
        lblCustomerName.setText("");

        cmbCustomerId.setDisable(false);


        cartTMS.clear();

        tableCart.refresh();
    }

    private void loadBookIds() throws SQLException {
        ArrayList<String> bookIds = bookModel.getAllBookIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(bookIds);
        cmbBookId.setItems(observableList);
    }

    private void loadCustomerIds() throws SQLException {
        ArrayList<String> customerIds = customerModel.getAllCustomerIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(customerIds);
        cmbCustomerId.setItems(observableList);
    }


}
