package lk.ijse.gdse.loslibros.dto;

import lombok.*;

import java.sql.Date;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class OrderDTO {

    private String orderId;
    private String customerId;
    private Date orderDate;

    private ArrayList<OrderDetailsDTO> orderDetailsDTOS;

}
