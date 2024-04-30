package controller.order;

import java.io.IOException;

import controller.mainController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class table_orders {

    @FXML
    Button add_order_btn;
    

    public void initialize() {
        
    }

    public void handleAddOrderButtonMet() {
        try {
            // Get reference to the main controller
            mainController mainController = (mainController) add_order_btn.getScene().getRoot().getUserData();

            // Call loadView method from mainController to switch views
            mainController.loadView("/view/orders/order.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
