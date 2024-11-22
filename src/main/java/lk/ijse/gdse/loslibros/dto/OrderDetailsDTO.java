package lk.ijse.gdse.loslibros.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class OrderDetailsDTO {
    private String orderId;
    private String bookId;
    private int quantity;
    private double price;
}
