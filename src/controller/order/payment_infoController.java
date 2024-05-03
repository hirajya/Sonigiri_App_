package controller.order;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.mainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class payment_infoController {

    @FXML
    Button CCalculateChangeButton;

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
    Text changeText;

    @FXML
    RadioButton GCash_RButton, Cash_RButton;

    static ArrayList<ordered_items> ordersList2;
    static int orderNumCurrent = Integer.parseInt(order.getOrderNumCount()) + 1;

    static String custName;
    static String custNote;
    static String paymentM;
    static double amountPaid;
    static double change;
    static double totalAmount;

    @FXML
    Button backButton2;

    public void initialize() throws SQLException {
        ordered_items.printOrderedItems(ordersList2);
        GCashPane.setVisible(false);
        CashPane.setVisible(false);
        noSideRectangles();
        refreshTableOrder();
        setNumOniText();
        setTotalAmountText();
        setOrderNumText();
        setCustInfo();
    }

    public void selectPaymentMethod() {
        if (GCash_RButton.isSelected()) {
            paymentM = "GCash";
            paymentMText.setText("GCash");
        } else if (Cash_RButton.isSelected()) {
            paymentM = "Cash";
            paymentMText.setText("Cash");

        }
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
        for (ordered_items order : ordersList2) {
            totalAmount += ordered_items.findProductPriceSimple(order.getProduct_id()) * order.getQuantity();
        }
        totalAmountText.setText(totalAmount + "0 PHP");
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
    }

    public void computeChange1() {
        amountPaid = Double.parseDouble(CAmountPaidTextField.getText());    
        amountPaidText.setText(String.valueOf(amountPaid) + "0 Php");
        change = amountPaid - totalAmount;
        changeText.setText(String.valueOf(change) + "0 Php");
    }

    public void setCustNameNote() {
        custNameText.setText(custName);
        custNoteText.setText(custNote);
    }

    public void saveDataOrder() {

    }

    public void toTable() {
        
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
