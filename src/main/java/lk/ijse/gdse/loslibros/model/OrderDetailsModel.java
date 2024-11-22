package lk.ijse.gdse.loslibros.model;

import lk.ijse.gdse.loslibros.dto.OrderDetailsDTO;
import lk.ijse.gdse.loslibros.util.CrudUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailsModel {

    public final BookModel bookModel = new BookModel();

    public boolean saveOrderDetailsList(ArrayList<OrderDetailsDTO> orderDetailsDTOS) throws SQLException {
        for (OrderDetailsDTO orderDetailsDTO : orderDetailsDTOS) {
            boolean isOrderDetailsSaved = saveOrderDetail(orderDetailsDTO);
            if (!isOrderDetailsSaved) {
                return false;
            }

            boolean isBookUpdated = bookModel.reduceQty(orderDetailsDTO);
            if (!isBookUpdated) {
                return false;
            }
        }
        return true;
    }

    public boolean saveOrderDetail(OrderDetailsDTO orderDetailsDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into order_details values (?,?,?,?)",
                orderDetailsDTO.getOrderId(),
                orderDetailsDTO.getBookId(),
                orderDetailsDTO.getQuantity(),
                orderDetailsDTO.getPrice()
                );
    }

}
