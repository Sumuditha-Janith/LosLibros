package lk.ijse.gdse.loslibros.model;

import lk.ijse.gdse.loslibros.dto.BookDTO;
import lk.ijse.gdse.loslibros.dto.OrderDetailsDTO;
import lk.ijse.gdse.loslibros.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookModel {

    public ArrayList<BookDTO> getAllBooks() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("select * from book");

        ArrayList<BookDTO> bookDTOS = new ArrayList<>();

        while (rst.next()) {
            BookDTO bookDTO = new BookDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)

            );
            bookDTOS.add(bookDTO);
        }
        return bookDTOS;

    }

    public String getNextBookId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select b_id from book order by b_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("B%03d", newIdIndex);
        }
        return "B001";
    }

    public boolean saveBook(BookDTO bookDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into book values (?,?,?,?,?,?,?,?)",
                bookDTO.getBookId(),
                bookDTO.getBookName(),
                bookDTO.getAuthId(),
                bookDTO.getCatId(),
                bookDTO.getPubId(),
                bookDTO.getSupId(),
                bookDTO.getBookPrice(),
                bookDTO.getBookQuantity()

        );
    }

    public boolean updateBook(String bookId, String bookName, String authorId, String categoryId, String publisherId, String supplierId, String bookPrice, String bookQuantity) throws SQLException {
        return CrudUtil.execute(
                "UPDATE book SET b_name = ?, au_id = ?, cat_id = ?, pub_id = ?, sup_id = ?, price = ?, qty = ? WHERE b_id = ?",
                bookName,
                authorId,
                categoryId,
                publisherId,
                supplierId,
                bookPrice,
                bookQuantity,
                bookId
        );
    }


    public boolean deleteBook(String bookId) throws SQLException {
        return CrudUtil.execute("delete from book where b_id=?", bookId);
    }

    public boolean reduceQty(OrderDetailsDTO orderDetailsDTO) throws SQLException {
        return CrudUtil.execute(
                "update book set qty = qty - ? where b_id = ?",
                orderDetailsDTO.getQuantity(),
                orderDetailsDTO.getBookId()
        );
    }

    public ArrayList<String> getAllBookIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select b_id from book");

        ArrayList<String> bookIds = new ArrayList<>();

        while (rst.next()) {
            bookIds.add(rst.getString(1));
        }

        return bookIds;
    }

    public BookDTO findBookById(String selectedBookId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from book where b_id=?", selectedBookId);

        if (rst.next()) {
            return new BookDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)
            );
        }
        return null;
    }

}
