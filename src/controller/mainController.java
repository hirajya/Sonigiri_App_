package controller;

import java.io.IOError;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class mainController {
    

    @FXML
    Button order_btn, analytics_btn, pending_orders_btn, done_orders_btn;

    @FXML
    BorderPane mainPane;

    @FXML
    AnchorPane subPane;


    @FXML
    public void initialize() throws IOException {
        mainPane.setUserData(this);
        loadView("/view/order.fxml");
    }

    public void handleOrderButton() throws IOException{
        System.out.println("Order Button Clicked");
        try {
            loadView("/view/order.fxml");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void handleAnalyticsButton() throws IOException{
        System.out.println("Analytics Button Clicked");
        try {
            loadView("/view/analytics.fxml");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void handleDoneOrdersButton() throws IOException{
        System.out.println("Done Orders Button Clicked");
        try {
            loadView("/view/done_orders.fxml");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void handlePendingOrdersButton() throws IOException{
        System.out.println("Pending Orders Button Clicked");
        try {
            loadView("/view/pending_orders.fxml");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void handleOrderOutButton() throws IOException{
        System.out.println("Pending Orders Button Clicked");
        try {
            loadView("/view/confirm_receipt.fxml");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }



    void loadView(String fxmlFile) throws IOException {
    AnchorPane newView = FXMLLoader.load(getClass().getResource(fxmlFile));
    ((AnchorPane) mainPane.getCenter()).getChildren().clear();  // Clear existing content
    ((AnchorPane) mainPane.getCenter()).getChildren().add(newView);  // Add new view to center AnchorPane
}

}
