package controller.order;

import java.io.IOException;

import controller.mainController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class customer_infoController {
    
    @FXML
    Button go_paymentInfo_btn, pow;

    @FXML
    public void initialize() {
        
    }

    public void handleGoPaymentInfoButtonMet() {
        try {
            // Get reference to the main controller
            mainController mainController = (mainController) go_paymentInfo_btn.getScene().getRoot().getUserData();

            // Call loadView method from mainController to switch views
            mainController.loadView("/view/orders/payment_info.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("tangina");
    }

    public void powtry() {
        System.out.println("pow");
    }

    public static void main(String[] args) {
        // System.out.println(go_paymentInfo_btn);
    }
    
}
