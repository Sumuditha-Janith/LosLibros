package lk.ijse.gdse.loslibros.model;

import lk.ijse.gdse.loslibros.util.CrudUtil;
import lk.ijse.gdse.loslibros.dto.AuthorDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthorModel {

    public ArrayList<AuthorDTO> getAllAuthors() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("select * from author");

        ArrayList<AuthorDTO> authorDTOS = new ArrayList<>();

        while (rst.next()) {
            AuthorDTO authorDTO = new AuthorDTO(
                    rst.getString(1),
                    rst.getString(2)

            );
            authorDTOS.add(authorDTO);
        }
        return authorDTOS;

    }

    public String getNextAuthorId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select au_id from author order by au_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("A%03d", newIdIndex);
        }
        return "A001";
    }

    public boolean saveAuthor(AuthorDTO authorDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into author VALUES (?,?)",
                authorDTO.getAuthorId(),
                authorDTO.getAuthorName()
        );
    }

    public boolean updateAuthor(AuthorDTO authorDTO) throws SQLException {
        return CrudUtil.execute(
                "update author set au_name=? where au_id=?",
                authorDTO.getAuthorName(),
                authorDTO.getAuthorId()
        );

    }

    public boolean deleteAuthor(String authorId) throws SQLException {
        return CrudUtil.execute("delete from author where au_id=?", authorId);
    }

    public ArrayList<String> getAllAuthorIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select au_id from author");

        ArrayList<String> authorIds = new ArrayList<>();

        while (rst.next()) {
            authorIds.add(rst.getString(1));
        }

        return authorIds;
    }

    public AuthorDTO findAuthById(String selectedAuthorId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from author where au_id=?", selectedAuthorId);

        if (rst.next()) {
            return new AuthorDTO(
                    rst.getString(1),
                    rst.getString(2)

            );
        }
        return null;
    }


}
