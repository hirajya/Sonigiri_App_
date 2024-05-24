package controller;

import java.io.IOError;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class mainController {
    
    @FXML
    Text dateTextMain, timeTextMain;

    @FXML
    Button order_btn, analytics_btn, earnings_btn;

    @FXML
    BorderPane mainPane;

    @FXML
    AnchorPane subPane;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private int orderNum;

    public int getOrderNum() {
        return orderNum;
    }



    @FXML
    public void initialize() throws IOException {
        mainPane.setUserData(this);
        loadView("/view/orders/table_orders.fxml");
        highlightButton(order_btn);
        dateTextMain.setVisible(false);
        timeTextMain.setVisible(false);
        // updateTime();

    }

    public void handleOrderButton() throws IOException{
        System.out.println("Order Button Clicked");
        try {
            loadView("/view/orders/table_orders.fxml");
            highlightButton(order_btn);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void handleAnalyticsButton() throws IOException{
        System.out.println("Analytics Button Clicked");
        try {
            loadView("/view/dashboard/analytics.fxml");
            highlightButton(analytics_btn);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void handleEarningsButton() throws IOException{
        System.out.println("Done Orders Button Clicked");
        try {
            loadView("/view/earnings/earnings.fxml");
            highlightButton(earnings_btn);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void handleOrderOutButton() throws IOException{
        System.out.println("Pending Orders Button Clicked");
        try {
            loadView("/view/orders/confirm_receipt.fxml");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }



    public void loadView(String fxmlFile) throws IOException {
    AnchorPane newView = FXMLLoader.load(getClass().getResource(fxmlFile));
    ((AnchorPane) mainPane.getCenter()).getChildren().clear();  // Clear existing content
    ((AnchorPane) mainPane.getCenter()).getChildren().add(newView);  // Add new view to center AnchorPane
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    private void highlightButton(Button buttonToHighlight) {
        // Remove highlight from all buttons
        order_btn.getStyleClass().remove("highlighted");
        analytics_btn.getStyleClass().remove("highlighted");
        earnings_btn.getStyleClass().remove("highlighted");

        // Add highlight to the selected button
        buttonToHighlight.getStyleClass().add("highlighted");
    }

    // private void updateTime() {
    //     LocalDateTime now = LocalDateTime.now();
    //     dateTextMain.setText(now.format(dateTimeFormatter));
    //     timeTextMain.setText(now.format(DateTimeFormatter.ofPattern("HH:mm")));

    //     // Schedule an update for every second
    //     new java.util.Timer().schedule(
    //             new java.util.TimerTask() {
    //                 @Override
    //                 public void run() {
    //                     updateTime();
    //                 }
    //             },
    //             (long) Duration.ZERO.toMillis());
    // }

}
