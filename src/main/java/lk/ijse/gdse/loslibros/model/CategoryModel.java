package lk.ijse.gdse.loslibros.model;

import lk.ijse.gdse.loslibros.dto.CategoryDTO;
import lk.ijse.gdse.loslibros.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryModel {

    public ArrayList<CategoryDTO> getAllCategories() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM category");

        ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();

        while (rst.next()) {
            CategoryDTO categoryDTO = new CategoryDTO(
                    rst.getString(1),
                    rst.getString(2)

            );
            categoryDTOS.add(categoryDTO);
        }
        return categoryDTOS;

    }

    public String getNextCategoryId() throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT cat_id FROM category ORDER BY cat_id DESC LIMIT 1");

        if (rst.next()) {
            String lastId = rst.getString(1);

            if (!lastId.startsWith("CT")) {
                throw new IllegalArgumentException("Unexpected ID format: " + lastId);
            }

            String substring = lastId.substring(2);

            try {
                int i = Integer.parseInt(substring);
                int newIdIndex = i + 1;

                return String.format("CT%02d", newIdIndex);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid numeric part in ID: " + lastId, e);
            }
        }

        return "CT01";
    }


    public boolean saveCategory(CategoryDTO categoryDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into category VALUES (?,?)",
                categoryDTO.getCategoryId(),
                categoryDTO.getCategoryName()
        );
    }

    public boolean updateCategory(CategoryDTO categoryDTO) throws SQLException {
        return CrudUtil.execute(
                "update category set cat_name=? where cat_id=?",
                categoryDTO.getCategoryName(),
                categoryDTO.getCategoryId()
        );

    }

    public boolean deleteCategory(String categoryId) throws SQLException {
        return CrudUtil.execute("delete from category where cat_id=?", categoryId);
    }

    public ArrayList<String> getAllCategoryIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select cat_id from category");

        ArrayList<String> categoryIds = new ArrayList<>();

        while (rst.next()) {
            categoryIds.add(rst.getString(1));
        }

        return categoryIds;
    }

    public CategoryDTO findCategoryById(String selectedCategoryId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from category where cat_id=?", selectedCategoryId);

        if (rst.next()) {
            return new CategoryDTO(
                    rst.getString(1),
                    rst.getString(2)

            );
        }
        return null;
    }

}
