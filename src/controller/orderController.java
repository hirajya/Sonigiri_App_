package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class orderController {
    

    @FXML
    Button order_out_btn, choose_tm_btn, choose_blg_btn;

    @FXML
    public void initialize() {

    }
    
    public void handleConfirmOrderButtonMet() {
        try {
            // Get reference to the main controller
            mainController mainController = (mainController) order_out_btn.getScene().getRoot().getUserData();

            // Call loadView method from mainController to switch views
            mainController.loadView("/view/confirm_receipt.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
