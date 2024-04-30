package controller.order;

import java.io.IOException;

import controller.mainController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class customer_infoController {
    
    @FXML
    Button go_paymentInfo_btn;

    @FXML
    Pane custInfoPane, orderSumPane;

    @FXML
    public void initialize() {
        noSideRectangles();
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

    public void noSideRectangles() {
        Rectangle clipRect = new Rectangle();
        clipRect.setWidth(custInfoPane.getPrefWidth());
        clipRect.setHeight(custInfoPane.getPrefHeight());
        clipRect.setArcWidth(20); // Adjust for desired corner radius
        clipRect.setArcHeight(20); // Adjust for desired corner radius

        custInfoPane.setClip(clipRect); // Set the clip to the pane

        // Set background color for the Pane (optional)
        custInfoPane.setStyle("-fx-background-color: FFFFFF;");


        Rectangle clipRect2 = new Rectangle();
        clipRect2.setWidth(orderSumPane.getPrefWidth());
        clipRect2.setHeight(orderSumPane.getPrefHeight());
        clipRect2.setArcWidth(20); // Adjust for desired corner radius
        clipRect2.setArcHeight(20); // Adjust for desired corner radius

        orderSumPane.setClip(clipRect2); // Set the clip to the pane

        // Set background color for the Pane (optional)
        orderSumPane.setStyle("-fx-background-color: FFFFFF;");

    }

    public static void main(String[] args) {
        // System.out.println(go_paymentInfo_btn);
    }
    
}
