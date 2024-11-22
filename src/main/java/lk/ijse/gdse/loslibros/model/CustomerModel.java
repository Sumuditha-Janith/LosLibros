package lk.ijse.gdse.loslibros.model;

import lk.ijse.gdse.loslibros.dto.CustomerDTO;
import lk.ijse.gdse.loslibros.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {

    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from customer");

        ArrayList<CustomerDTO> customerDTOS = new ArrayList<>();

        while (rst.next()) {
            CustomerDTO customerDTO = new CustomerDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            );
            customerDTOS.add(customerDTO);
        }
        return customerDTOS;
    }

    public String getNextCustomerId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select cus_id from customer order by cus_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("C0%03d", newIdIndex);
        }
        return "C0001";
    }
    public boolean saveCustomer(CustomerDTO customerDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into customer values(?,?,?,?)",
                customerDTO.getCustomerId(),
                customerDTO.getCustomerName(),
                customerDTO.getCustomerAddress(),
                customerDTO.getCustomerPhone()
        );


    }

    public boolean updateCustomer(CustomerDTO customerDTO) throws SQLException {
        return CrudUtil.execute(
                "update customer set cus_name=?, cus_address=?, p_num=? where cus_id=?",
                customerDTO.getCustomerName(),
                customerDTO.getCustomerAddress(),
                customerDTO.getCustomerPhone(),
                customerDTO.getCustomerId()
                );
    }

    public boolean deleteCustomer(String customerId) throws SQLException {
        return CrudUtil.execute(
                "delete from customer where cus_id=?", customerId
        );
    }


    public ArrayList<String> getAllCustomerIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select cus_id from customer");

        ArrayList<String> customerIds = new ArrayList<>();

        while (rst.next()) {
            customerIds.add(rst.getString(1));
        }

        return customerIds;
    }

    public CustomerDTO findCusById(String selectedCusId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from customer where cus_id=?", selectedCusId);

        if (rst.next()) {
            return new CustomerDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            );
        }
        return null;
    }

}


