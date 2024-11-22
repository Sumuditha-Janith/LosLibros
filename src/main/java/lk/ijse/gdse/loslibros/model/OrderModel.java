package lk.ijse.gdse.loslibros.model;

import lk.ijse.gdse.loslibros.db.DBConnection;
import lk.ijse.gdse.loslibros.util.CrudUtil;
import lk.ijse.gdse.loslibros.dto.OrderDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderModel {

    private final OrderDetailsModel orderDetailsModel = new OrderDetailsModel();

    public String getNextOrderId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select order_id from orders order by order_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("O0%03d", newIdIndex);
        }
        return "O0001";
    }

    public boolean saveOrder(OrderDTO orderDTO) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            boolean isOrderSaved = CrudUtil.execute(
                    "insert into orders values (?,?,?)",
                    orderDTO.getOrderId(),
                    orderDTO.getCustomerId(),
                    orderDTO.getOrderDate()
            );
            if (isOrderSaved) {
                boolean isOrderDetailListSaved = orderDetailsModel.saveOrderDetailsList(orderDTO.getOrderDetailsDTOS());
                if (isOrderDetailListSaved) {
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

}
