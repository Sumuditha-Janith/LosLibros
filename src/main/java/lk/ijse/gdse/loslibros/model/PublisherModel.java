package lk.ijse.gdse.loslibros.model;

import lk.ijse.gdse.loslibros.dto.PublisherDTO;
import lk.ijse.gdse.loslibros.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PublisherModel {

    public ArrayList<PublisherDTO> getAllPublishers() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("select * from publisher");

        ArrayList<PublisherDTO> publisherDTOS = new ArrayList<>();

        while (rst.next()) {
            PublisherDTO publisherDTO = new PublisherDTO(
                    rst.getString(1),
                    rst.getString(2)

            );
            publisherDTOS.add(publisherDTO);
        }
        return publisherDTOS;

    }


    public String getNextPublisherId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select pub_id from publisher order by pub_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);

            if (!lastId.startsWith("PB")) {
                throw new IllegalArgumentException("Unexpected ID format: " + lastId);
            }

            String substring = lastId.substring(2);

            try {
                int i = Integer.parseInt(substring);
                int newIdIndex = i + 1;

                return String.format("PB%02d", newIdIndex);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid numeric part in ID: " + lastId, e);
            }
        }

        return "PB01";
    }


    public boolean savePublisher(PublisherDTO publisherDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into publisher values (?,?)",
                publisherDTO.getPublisherId(),
                publisherDTO.getPublisherName()
        );
    }

    public boolean updatePublisher(PublisherDTO publisherDTO) throws SQLException {
        return CrudUtil.execute(
                "update publisher set pub_name=? where pub_id=?",
                publisherDTO.getPublisherName(),
                publisherDTO.getPublisherId()
        );

    }

    public boolean deletePublisher(String publisherId) throws SQLException {
        return CrudUtil.execute("delete from publisher where pub_id=?", publisherId);
    }

    public ArrayList<String> getAllPublisherIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select pub_id from publisher");

        ArrayList<String> publisherIds = new ArrayList<>();

        while (rst.next()) {
            publisherIds.add(rst.getString(1));
        }

        return publisherIds;
    }

    public PublisherDTO findPublisherById(String selectedPublisherId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from publisher where pub_id=?", selectedPublisherId);

        if (rst.next()) {
            return new PublisherDTO(
                    rst.getString(1),
                    rst.getString(2)

            );
        }
        return null;
    }

}
