package controller.order;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class payment_infoController {
    
    @FXML
    Pane GCashPane, CashPane, paymentInfoPane, orderSumPane;

    @FXML
    RadioButton GCash_RButton, Cash_RButton;

    public void initialize() {
        GCashPane.setVisible(false);
        CashPane.setVisible(false);
        noSideRectangles();
    }

    public void handleGCashRadioButton() {
        GCashPane.setVisible(true);
        CashPane.setVisible(false);
    }

    public void handleCashRadioButton() {
        GCashPane.setVisible(false);
        CashPane.setVisible(true);
    }

    public void noSideRectangles() {
        Rectangle clipRect = new Rectangle();
        clipRect.setWidth(paymentInfoPane.getPrefWidth());
        clipRect.setHeight(paymentInfoPane.getPrefHeight());
        clipRect.setArcWidth(20); // Adjust for desired corner radius
        clipRect.setArcHeight(20); // Adjust for desired corner radius

        paymentInfoPane.setClip(clipRect); // Set the clip to the pane

        // Set background color for the Pane (optional)
        paymentInfoPane.setStyle("-fx-background-color: FFFFFF;");


        Rectangle clipRect2 = new Rectangle();
        clipRect2.setWidth(orderSumPane.getPrefWidth());
        clipRect2.setHeight(orderSumPane.getPrefHeight());
        clipRect2.setArcWidth(20); // Adjust for desired corner radius
        clipRect2.setArcHeight(20); // Adjust for desired corner radius

        orderSumPane.setClip(clipRect2); // Set the clip to the pane

        // Set background color for the Pane (optional)
        orderSumPane.setStyle("-fx-background-color: FFFFFF;");

    }


}
