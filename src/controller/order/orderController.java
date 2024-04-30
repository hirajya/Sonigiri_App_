package controller.order;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import model.order;

import java.io.IOException;

import controller.mainController;

public class orderController {
    

    @FXML
    Button order_out_btn;

    @FXML
    RadioButton tnmyo_RButton, blgi_RButton, chcknadb_RButton;

    @FXML
    Pane orderPane, orderSumPane;

    @FXML
    public void initialize() {
        noSideRectangles();
    }
    
    public void handleConfirmOrderButtonMet() {
        try {
            // Get reference to the main controller
            mainController mainController = (mainController) order_out_btn.getScene().getRoot().getUserData();

            // Call loadView method from mainController to switch views
            mainController.loadView("/view/orders/customer_info.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void noSideRectangles() {
        Rectangle clipRect = new Rectangle();
        clipRect.setWidth(orderPane.getPrefWidth());
        clipRect.setHeight(orderPane.getPrefHeight());
        clipRect.setArcWidth(20); // Adjust for desired corner radius
        clipRect.setArcHeight(20); // Adjust for desired corner radius

        orderPane.setClip(clipRect); // Set the clip to the pane

        // Set background color for the Pane (optional)
        orderPane.setStyle("-fx-background-color: FFFFFF;");


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
