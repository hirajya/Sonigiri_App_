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
    
        // Calculate the total price with the discount logic
        double price = ordered_items.findProductPriceSimple(order_sample.getProduct_id());
        int quantity = order_sample.getQuantity();
        double totalPrice = 0.0;
        int discountedOnigiriCount = quantity / 4; // Count of discounted onigiri
    
        // Calculate total price considering the discount
        totalPrice = (quantity - discountedOnigiriCount) * price + discountedOnigiriCount * (price * 0.5);
    
        totalPriceText.setText(String.format("%.2f PHP", totalPrice));
    }
    
    
    

    public void updateData(ordered_items updatedOrder) throws SQLException {
        setData(updatedOrder);
    }

    
}
