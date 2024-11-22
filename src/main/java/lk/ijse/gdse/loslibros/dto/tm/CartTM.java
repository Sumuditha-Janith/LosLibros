package lk.ijse.gdse.loslibros.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class CartTM {

    private String bookId;
    private String bookName;
    private int cartQuantity;
    private double unitPrice;
    private double total;
    private Button removeBtn;

}
