package lk.ijse.gdse.loslibros.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import lk.ijse.gdse.loslibros.dto.*;
import lk.ijse.gdse.loslibros.dto.tm.BookTM;
import lk.ijse.gdse.loslibros.model.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class BookFormController implements Initializable {

    @FXML
    private Button btnBookDelete;

    @FXML
    private Button btnBookSave;

    @FXML
    private Button btnBookUpdate;

    @FXML
    private ComboBox<String> cmbAuthorId;

    @FXML
    private ComboBox<String> cmbCategoryId;

    @FXML
    private ComboBox<String> cmbSupplierId;

    @FXML
    private ComboBox<String> cmbPublisherId;

    @FXML
    private TableColumn<?, ?> colBookAuthor;

    @FXML
    private TableColumn<?, ?> colBookCategory;

    @FXML
    private TableColumn<?, ?> colBookId;

    @FXML
    private TableColumn<?, ?> colBookName;

    @FXML
    private TableColumn<?, ?> colBookPrice;

    @FXML
    private TableColumn<?, ?> colBookQuantity;

    @FXML
    private TableColumn<?, ?> colBookSupplier;

    @FXML
    private TableColumn<?, ?> colBookPublisher;

    @FXML
    private Label lblAuthorName;

    @FXML
    private Label lblBookId;

    @FXML
    private Label lblCategoryName;

    @FXML
    private Label lblPublisherName;

    @FXML
    private Label lblSupplierName;

    @FXML
    private TableView<BookTM> tableBook;

    @FXML
    private TextField txtBookName;

    @FXML
    private TextField txtBookPrice;

    @FXML
    private TextField txtBookQuantity;

    @FXML
    void btnBookDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String bookId = lblBookId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = bookModel.deleteBook(bookId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Book deleted").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete book!").show();
            }
        }
    }


    @FXML
    void btnBookResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
    }

    @FXML
    void btnBookSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (!validateFields()) {
            return;
        }

        String bookId = lblBookId.getText();
        String bookName = txtBookName.getText();
        String authorId = cmbAuthorId.getValue();
        String categoryId = cmbCategoryId.getValue();
        String publisherId = cmbPublisherId.getValue();
        String supplierId = cmbSupplierId.getValue();
        String bookPrice = txtBookPrice.getText();
        String bookQuantity = txtBookQuantity.getText();

        BookDTO bookDTO = new BookDTO(
                bookId,
                bookName,
                authorId,
                categoryId,
                publisherId,
                supplierId,
                bookPrice,
                bookQuantity
        );

        boolean isSaved = bookModel.saveBook(bookDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Book saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save book...!").show();
        }
    }

    @FXML
    void btnBookUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (!validateFields()) {
            return;
        }

        String bookId = lblBookId.getText();
        String bookName = txtBookName.getText();
        String authorId = cmbAuthorId.getValue();
        String categoryId = cmbCategoryId.getValue();
        String publisherId = cmbPublisherId.getValue();
        String supplierId = cmbSupplierId.getValue();
        String bookPrice = txtBookPrice.getText();
        String bookQuantity = txtBookQuantity.getText();

        boolean isUpdated = bookModel.updateBook(bookId, bookName, authorId, categoryId, publisherId, supplierId, bookPrice, bookQuantity);

        if (isUpdated) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Book updated successfully!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update the book!").show();
        }
    }

    @FXML
    void cmbAuthorOnAction(ActionEvent event) throws SQLException {

        String selectedAuthorId = cmbAuthorId.getSelectionModel().getSelectedItem();
        AuthorDTO authorDTO = authorModel.findAuthById(selectedAuthorId);

        if (authorDTO != null) {

            lblAuthorName.setText(authorDTO.getAuthorName());
        }
    }

    @FXML
    void cmbCategoryOnAction(ActionEvent event) throws SQLException {

        String selectedCategoryId = (String) cmbCategoryId.getSelectionModel().getSelectedItem();
        CategoryDTO categoryDTO = categoryModel.findCategoryById(selectedCategoryId);

        if (categoryDTO != null) {

            lblCategoryName.setText(categoryDTO.getCategoryName());
        }

    }

    @FXML
    void cmbPublisherOnAction(ActionEvent event) throws SQLException {
        String selectedPublisherId = (String) cmbPublisherId.getSelectionModel().getSelectedItem();
        PublisherDTO publisherDTO = publisherModel.findPublisherById(selectedPublisherId);

        if (publisherDTO != null) {
            lblPublisherName.setText(publisherDTO.getPublisherName());
        }
    }


    @FXML
    void cmbSupplierOnAction(ActionEvent event) throws SQLException {

        String selectedSupplierId = (String) cmbSupplierId.getSelectionModel().getSelectedItem();
        SupplierDTO supplierDTO = supplierModel.findSupplierById(selectedSupplierId);

        if (supplierDTO != null) {

            lblSupplierName.setText(supplierDTO.getSupplierName());
        }
    }

    @FXML
    void onClickBookTable(MouseEvent event) {
        BookTM bookTM = tableBook.getSelectionModel().getSelectedItem();
        if (bookTM != null) {
            lblBookId.setText(bookTM.getBookId());
            txtBookName.setText(bookTM.getBookName());
            cmbAuthorId.setValue(bookTM.getAuthId());
            cmbCategoryId.setValue(bookTM.getCatId());
            cmbPublisherId.setValue(bookTM.getPubId());
            cmbSupplierId.setValue(bookTM.getSupId());

            txtBookPrice.setText(bookTM.getBookPrice());
            txtBookQuantity.setText(bookTM.getBookQuantity());

            btnBookSave.setDisable(true);

            btnBookDelete.setDisable(false);
            btnBookUpdate.setDisable(false);
        }
    }

    private final AuthorModel authorModel = new AuthorModel();
    private final CategoryModel categoryModel = new CategoryModel();
    private final PublisherModel publisherModel = new PublisherModel();
    private final SupplierModel supplierModel = new SupplierModel();


    public void initialize(URL url, ResourceBundle resourceBundle) {
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colBookAuthor.setCellValueFactory(new PropertyValueFactory<>("authId"));
        colBookCategory.setCellValueFactory(new PropertyValueFactory<>("catId"));
        colBookPublisher.setCellValueFactory(new PropertyValueFactory<>("pubId"));
        colBookSupplier.setCellValueFactory(new PropertyValueFactory<>("supId"));
        colBookPrice.setCellValueFactory(new PropertyValueFactory<>("bookPrice"));
        colBookQuantity.setCellValueFactory(new PropertyValueFactory<>("bookQuantity"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load book id").show();
        }
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextBookId();
        loadTableData();

        loadAuthorIds();
        loadCategoryIds();
        loadPublisherIds();
        loadSupplierIds();

        btnBookSave.setDisable(false);

        btnBookUpdate.setDisable(true);
        btnBookDelete.setDisable(true);

        lblAuthorName.setText("");
        lblCategoryName.setText("");
        lblPublisherName.setText("");
        lblSupplierName.setText("");

        cmbAuthorId.setValue(null);
        cmbCategoryId.setValue(null);
        cmbPublisherId.setValue(null);
        cmbSupplierId.setValue(null);

        txtBookName.setText("");
        txtBookPrice.setText("");
        txtBookQuantity.setText("");

    }

    private void loadNextBookId() throws SQLException {
        String nextBookId = bookModel.getNextBookId();
        lblBookId.setText(nextBookId);
    }


    BookModel bookModel = new BookModel();

    private void loadTableData() throws SQLException, ClassNotFoundException {
        ArrayList<BookDTO> bookDTOS = bookModel.getAllBooks();

        ObservableList<BookTM> bookTMS = FXCollections.observableArrayList();


        for (BookDTO bookDTO : bookDTOS) {
            BookTM bookTM = new BookTM(
                    bookDTO.getBookId(),
                    bookDTO.getBookName(),
                    bookDTO.getAuthId(),
                    bookDTO.getCatId(),
                    bookDTO.getPubId(),
                    bookDTO.getSupId(),
                    bookDTO.getBookPrice(),
                    bookDTO.getBookQuantity()
            );
            bookTMS.add(bookTM);
        }

        tableBook.setItems(bookTMS);
    }


    private void loadAuthorIds() throws SQLException {
        ArrayList<String> authorIds = authorModel.getAllAuthorIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(authorIds);
        cmbAuthorId.setItems(observableList);
    }

    private void loadCategoryIds() throws SQLException {
        ArrayList<String> categoryIds = categoryModel.getAllCategoryIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(categoryIds);
        cmbCategoryId.setItems(observableList);
    }

    private void loadPublisherIds() throws SQLException {
        ArrayList<String> publisherIds = publisherModel.getAllPublisherIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(publisherIds);
        cmbPublisherId.setItems(observableList);
    }

    private void loadSupplierIds() throws SQLException {
        ArrayList<String> supplierIds = supplierModel.getAllSupplierIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(supplierIds);
        cmbSupplierId.setItems(observableList);
    }

    private boolean validateFields() {
        String bookName = txtBookName.getText();
        String bookPrice = txtBookPrice.getText();
        String bookQuantity = txtBookQuantity.getText();
        String authorId = cmbAuthorId.getValue();
        String categoryId = cmbCategoryId.getValue();
        String supplierId = cmbSupplierId.getValue();

        if (bookName == null || bookName.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a book name.").show();
            return false;
        }

        if (bookPrice == null || bookPrice.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a book price.").show();
            return false;
        }

        try {
            double price = Double.parseDouble(bookPrice);
            if (price <= 0) {
                new Alert(Alert.AlertType.WARNING, "Price must be a positive number.").show();
                return false;
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Invalid price. Please enter a valid number.").show();
            return false;
        }

        if (bookQuantity == null || bookQuantity.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a book quantity.").show();
            return false;
        }

        try {
            int quantity = Integer.parseInt(bookQuantity);
            if (quantity < 0) {
                new Alert(Alert.AlertType.WARNING, "Quantity must be a non-negative integer.").show();
                return false;
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Invalid quantity. Please enter a valid integer.").show();
            return false;
        }

        if (authorId == null || authorId.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an author.").show();
            return false;
        }

        if (categoryId == null || categoryId.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a category.").show();
            return false;
        }

        if (supplierId == null || supplierId.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a supplier.").show();
            return false;
        }

        return true;
    }

}
