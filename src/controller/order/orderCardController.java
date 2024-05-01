package controller.order;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.ordered_items;

public class orderCardController {
    
    @FXML
    Text ifSpicyText, productNameText, qtyText, totalPriceText;

    public void initialize() {

    }

    public void setData(ordered_items order_sample) throws SQLException {
        if (order_sample.isSpicy().equals("Spicy")) {
            ifSpicyText.setText("Spicy");
        } else {
            ifSpicyText.setText("");
        }
        
        productNameText.setText(ordered_items.getProduct_name(order_sample.getProduct_id()));
        qtyText.setText(String.valueOf(order_sample.getQuantity()) + "x");
        totalPriceText.setText(String.valueOf(ordered_items.findProductPriceSimple(order_sample.getProduct_id()) * order_sample.getQuantity()) + "0 PHP");

    }
}
