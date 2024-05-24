package controller.order;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.mainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.order;
import model.ordered_items;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class payment_infoController {

    @FXML
    Button done_orders_btn;

    @FXML
    Button CCalculateChangeButton, discount10;

    @FXML
    TextField GCContactNum, GCContactName;

    @FXML
    TextField CAmountPaidTextField;
    
    @FXML
    Pane GCashPane, CashPane, paymentInfoPane, orderSumPane;

    @FXML
    VBox orderCard;

    @FXML
    Text numOniText, orderNumText, totalAmountText, custNameText, paymentMText;

    @FXML
    Text custNoteText;

    @FXML
    Text amountPaidText;

    @FXML
    Text changeText, errorMsg;

    @FXML
    RadioButton GCash_RButton, Cash_RButton;

    static ArrayList<ordered_items> ordersList2;
    static int orderNumCurrent;

    static String custName;
    static String custNote;
    static String paymentM;
    static double amountPaid;
    static double change;
    static double totalAmount;

    @FXML
    Button backButton2;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sonigiri_database";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    private boolean discountApplied = false;

    public void initialize() throws SQLException {
        errorMsg.setVisible(false);
        ordered_items.printOrderedItems(ordersList2);
        orderNumCurrent = getNextAvailableOrderNumber();
        GCashPane.setVisible(false);
        CashPane.setVisible(false);
        noSideRectangles();
        refreshTableOrder();
        setNumOniText();
        setTotalAmountText();
        setOrderNumText();
        setCustInfo();
    }

    private int getNextAvailableOrderNumber() {
        int nextOrderNumber = Integer.parseInt(order.getOrderNumCount()) + 1;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT order_NumOrder FROM order_table ORDER BY order_NumOrder";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            int expectedOrderNumber = 1;

            while (resultSet.next()) {
                int currentOrderNumber = resultSet.getInt("order_NumOrder");

                if (currentOrderNumber != expectedOrderNumber) {
                    nextOrderNumber = expectedOrderNumber;
                    break;
                }
                expectedOrderNumber++;
            }

            if (nextOrderNumber == expectedOrderNumber) {
                nextOrderNumber = expectedOrderNumber;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nextOrderNumber;
    }

    public void applyDiscount10P() {
        if (!discountApplied) {
            totalAmount = totalAmount - (totalAmount * 0.10);  // Apply 10% discount
            // Round up to the nearest whole number
            totalAmount = Math.ceil(totalAmount);
            totalAmountText.setText(totalAmount + "0 Php");
            amountPaidText.setText(totalAmount + "0 Php");
            amountPaid = totalAmount;
            discountApplied = true;
        } else {
            showAlert(AlertType.ERROR, "Error", "Discount Already Applied", "Discount has already been applied.");
        }
    }
    
    
    
    // private void showAlert2(Alert.AlertType type, String title, String header, String content) {
    //     Alert alert = new Alert(type);
    //     alert.setTitle(title);
    //     alert.setHeaderText(header);
    //     alert.setContentText(content);
    //     alert.showAndWait();
    // }
    

    public void selectPaymentMethod() {
        if (GCash_RButton.isSelected()) {
            paymentM = "GCash";
            paymentMText.setText("GCash");
            amountPaidText.setText(String.valueOf(totalAmount) + "0 Php");
            amountPaid = totalAmount;
            // Disable the "Done" button until change is calculated
            done_orders_btn.setDisable(false);
        } else if (Cash_RButton.isSelected()) {
            paymentM = "Cash";
            paymentMText.setText("Cash");
            // Disable the "Done" button until change is calculated
            done_orders_btn.setDisable(true);
        }
    }
    

    public void handleGCashRadioButton1() throws SQLException {
        GCContactName.clear();
        GCContactNum.clear();
        
        CashPane.setVisible(false);
    
        changeText.setText("0.00 Php");
        totalAmountText.setText(totalAmount + "0 PHP");
        // setTotalAmountText();
        amountPaidText.setText(String.valueOf(totalAmount) + "0 Php");
        
        selectPaymentMethod(); // Add this line to update paymentM
    }
    
    public void handleCashRadioButton1() {
        GCashPane.setVisible(false);
        CashPane.setVisible(true);
        changeText.setText("0.00 Php");
        amountPaidText.setText("0.00 Php");
        
        selectPaymentMethod(); // Add this line to update paymentM
    }



    public void handleBackButton() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/orders/customer_info.fxml"));
        AnchorPane containerContent = loader.load(); // Load the container FXML
        customer_infoController cInfoController = loader.getController(); // Assign controller to variable
        cInfoController.ordersListl = ordersList2;
        cInfoController.orderNumCurrent = orderNumCurrent;
        cInfoController.custName = custName;
        cInfoController.custNote = custNote;
        containerContent.setUserData(ordersList2);
        
        // Get reference to the main controller
        mainController mainController = (mainController) backButton2.getScene().getRoot().getUserData();
        mainController.loadView("/view/orders/customer_info.fxml");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }   




    public void setCustInfo() {
        custNameText.setText(custName);
        custNoteText.setText(custNote);
    }

    public void setOrderNumText() {
        orderNumText.setText("Order #" + orderNumCurrent);
    }

    public void setNumOniText() {
        int numberOfOnigiri = 0;
        for (ordered_items order : ordersList2) {
            numberOfOnigiri += order.getQuantity();
        }
        numOniText.setText(String.valueOf(numberOfOnigiri));
    }

    public void setTotalAmountText() throws SQLException {
        totalAmount = 0.0;
        int onigiriCount = 0;
        int discountCount = 0; // Count for every 4th onigiri
    
        // Calculate total amount with discount
        for (ordered_items order : ordersList2) {
            double price = ordered_items.findProductPriceSimple(order.getProduct_id());
            int quantity = order.getQuantity();
    
            for (int i = 1; i <= quantity; i++) {
                onigiriCount++;
                if (onigiriCount % 4 == 0) {
                    // Apply 50% discount for every 4th onigiri
                    totalAmount += price * 0.5;
                    discountCount++; // Increment discount count
                } else {
                    totalAmount += price;
                }
            }
        }
    
        // If there are any discounted onigiri, subtract the total discount amount
        totalAmount -= (discountCount / 4) * (ordered_items.findProductPriceSimple(1) * 0.5);
    
        // Round up the total amount to the nearest integer value
        totalAmount = Math.ceil(totalAmount);
    
        totalAmountText.setText(String.format("%.0f Php", totalAmount)); // Display as integer value
    }

    public void refreshTableOrder() throws SQLException {
        orderCard.getChildren().clear();
        try {
            for (ordered_items orderItem : ordersList2) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/orders/widgets/add_order_pane.fxml"));
                Pane containerContent = loader.load(); // Load the container FXML
                orderCardController oCardController = loader.getController(); // Assign controller to variable
                oCardController.setData(orderItem);
                containerContent.setUserData(orderItem);
                containerContent.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    try {
                        int clickedIndex = ordersList2.indexOf(orderItem);
                        // Handle click event here
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                orderCard.getChildren().add(containerContent); // Add the container to the VBox
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleGCashRadioButton() throws SQLException {
        GCContactName.clear();
        GCContactNum.clear();
        
        GCashPane.setVisible(true);
        CashPane.setVisible(false);

        changeText.setText("0.00 Php");
        setTotalAmountText();
        amountPaidText.setText(String.valueOf(totalAmount) + "0 Php");

    }

    public void handleCashRadioButton() {
        GCashPane.setVisible(false);
        CashPane.setVisible(true);
        changeText.setText("0.00 Php");
        amountPaidText.setText("0.00 Php");
    }

    public void computeChange1() {
        if (!GCash_RButton.isSelected() && !Cash_RButton.isSelected()) {
            // Display error alert for missing payment method selection
            showAlert(AlertType.ERROR, "Error", "Missing Payment Method", "Please select a payment method before calculating change.");
            return;
        }
    
        String amountPaidStr = CAmountPaidTextField.getText();
        if (amountPaidStr.isEmpty()) {
            // Display error alert for missing amount paid
            showAlert(AlertType.ERROR, "Error", "Missing Amount Paid", "Please enter the amount paid.");
            return;
        }
        
        amountPaid = Double.parseDouble(amountPaidStr);
        if (amountPaid < totalAmount) {
            // Display error alert for insufficient payment
            showAlert(AlertType.ERROR, "Error", "Insufficient Payment", "Amount paid must be greater than or equal to the total amount.");
            return;
        }
        
        amountPaidText.setText(String.valueOf(amountPaid) + "0 Php");
        change = amountPaid - totalAmount;
        changeText.setText(String.valueOf(change) + "0 Php");
        
        // Enable the "Done" button only if the payment is sufficient
        done_orders_btn.setDisable(false);
    
    
    amountPaid = Double.parseDouble(amountPaidStr);
    if (amountPaid < totalAmount) {
        // Display error alert for insufficient payment
        showAlert(AlertType.ERROR, "Error", "Insufficient Payment", "Amount paid must be greater than or equal to the total amount.");
        return;
    }
    
    amountPaidText.setText(String.valueOf(amountPaid) + "0 Php");
    change = amountPaid - totalAmount;
    changeText.setText(String.valueOf(change) + "0 Php");
    
    // Enable the "Done" button only if the payment is sufficient
    done_orders_btn.setDisable(false);
}



private void showAlert(AlertType type, String title, String header, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
}

    public void setCustNameNote() {
        custNameText.setText(custName);
        custNoteText.setText(custNote);
    }

    public void saveDataOrder() throws ClassNotFoundException, SQLException {
        for (ordered_items order : ordersList2) {
            ordered_items.addOrder(order);
        }
    }

    public void saveData() throws ClassNotFoundException, SQLException {
        paymentM = paymentMText.getText();
        System.out.println(totalAmount);
        order order1 = new order(orderNumCurrent, custName, paymentM, totalAmount, amountPaid, "Pending", custNote, discountApplied);
        order.addOrder(order1);
    }


    public void toTable() throws ClassNotFoundException, SQLException {

        if (amountPaid < totalAmount) {
            errorMsg.setVisible(true);
            return;
        }

        saveData();
        saveDataOrder();
        try {
            // Get reference to the main controller
            mainController mainController = (mainController) done_orders_btn.getScene().getRoot().getUserData();

            // Call loadView method from mainController to switch views
            mainController.loadView("/view/orders/table_orders.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

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
