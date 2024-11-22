package lk.ijse.gdse.loslibros.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import lk.ijse.gdse.loslibros.dto.CategoryDTO;
import lk.ijse.gdse.loslibros.dto.tm.CategoryTM;
import lk.ijse.gdse.loslibros.model.CategoryModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class CategoryFormController {

    @FXML
    private Button btnCategoryDelete;

    @FXML
    private Button btnCategorySave;

    @FXML
    private Button btnCategoryUpdate;

    @FXML
    private TableColumn<?, ?> colCategoryId;

    @FXML
    private TableColumn<?, ?> colCategoryName;

    @FXML
    private Label lblCategoryId;

    @FXML
    private TableView<CategoryTM> tableCategory;

    @FXML
    private TextField txtCategoryName;

    @FXML
    void btnCategoryDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String categoryId = lblCategoryId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = categoryModel.deleteCategory(categoryId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Category deleted").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete category!").show();
            }
        }

    }

    @FXML
    void btnCategoryResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
    }

    @FXML
    void btnCategorySaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (!validateFields()) {
            return;
        }

        String categoryId = lblCategoryId.getText();
        String categoryName = txtCategoryName.getText();

        CategoryDTO categoryDTO = new CategoryDTO(
                categoryId,
                categoryName
        );

        boolean isSaved = categoryModel.saveCategory(categoryDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Category saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save category...!").show();
        }

    }

    @FXML
    void btnCategoryUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (!validateFields()) {
            return;
        }
        String categoryId = lblCategoryId.getText();
        String categoryName = txtCategoryName.getText();

        CategoryDTO categoryDTO = new CategoryDTO(
                categoryId,
                categoryName
        );

        boolean isUpdated = categoryModel.updateCategory(categoryDTO);
        if (isUpdated) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Category updated...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update category...!").show();
        }

    }


    @FXML
    void onClickCategoryTable(MouseEvent event) {
        CategoryTM categoryTM = tableCategory.getSelectionModel().getSelectedItem();
        if (categoryTM != null) {
            lblCategoryId.setText(categoryTM.getCategoryId());
            txtCategoryName.setText(categoryTM.getCategoryName());

            btnCategorySave.setDisable(true);

            btnCategoryDelete.setDisable(false);
            btnCategoryUpdate.setDisable(false);
        }
    }

    public void initialize() {
        colCategoryId.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colCategoryName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load category id").show();
        }
    }

    private void loadNextCategoryId() throws SQLException {
        String nextCategoryId = categoryModel.getNextCategoryId();
        lblCategoryId.setText(nextCategoryId);
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextCategoryId();
        loadTableData();

        btnCategorySave.setDisable(false);

        btnCategoryUpdate.setDisable(true);
        btnCategoryDelete.setDisable(true);

        txtCategoryName.setText("");


    }

    CategoryModel categoryModel = new CategoryModel();

    private void loadTableData() throws SQLException, ClassNotFoundException {
        ArrayList<CategoryDTO> categoryDTOS = categoryModel.getAllCategories();

        ObservableList<CategoryTM> categoryTMS = FXCollections.observableArrayList();


        for (CategoryDTO categoryDTO : categoryDTOS) {
            CategoryTM categoryTM = new CategoryTM(
                    categoryDTO.getCategoryId(),
                    categoryDTO.getCategoryName()
            );
            categoryTMS.add(categoryTM);
        }

        tableCategory.setItems(categoryTMS);
    }
    private boolean validateFields() {
        String categoryName = txtCategoryName.getText();

        if (categoryName == null || categoryName.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a category name.").show();
            return false;
        }

        return true;
    }

}
