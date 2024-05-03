package controller.order;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.mainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.order;
import model.ordered_items;

public class customer_infoController {
    
    @FXML
    Button go_paymentInfo_btn;

    @FXML
    Text numOniText, orderNumText, totalAmountText;

    @FXML
    VBox orderCard;

    @FXML
    Pane custInfoPane, orderSumPane;

    @FXML
    TextField custNameField;

    @FXML
    TextArea custNoteField;

    @FXML
    Button backButton1;

    static ArrayList<ordered_items> ordersListl;
    static int orderNumCurrent = Integer.parseInt(order.getOrderNumCount()) + 1;
    static String custName;
    static String custNote;



    @FXML
    public void initialize() throws SQLException {
        ordered_items.printOrderedItems(ordersListl);
        noSideRectangles();
        refreshTableOrder();
        setNumOniText();
        setTotalAmountText();
        setOrderNumText();
    }

    public void refreshTableOrder() throws SQLException {
        orderCard.getChildren().clear();
        try {
            for (ordered_items orderItem : ordersListl) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/orders/widgets/add_order_pane.fxml"));
                Pane containerContent = loader.load(); // Load the container FXML
                orderCardController oCardController = loader.getController(); // Assign controller to variable
                oCardController.setData(orderItem);
                containerContent.setUserData(orderItem);
                containerContent.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    try {
                        int clickedIndex = ordersListl.indexOf(orderItem);
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
    

    

    public void handleGoPaymentInfoButtonMet() {
        try {
            // Get reference to the main controller
            mainController mainController = (mainController) go_paymentInfo_btn.getScene().getRoot().getUserData();
            payment_infoController.ordersList2 = ordersListl;
            String custName = custNameField.getText();
            String custNote = custNoteField.getText();
            
            customer_infoController.custName = custName;
            customer_infoController.custNote = custNote;
            payment_infoController.custName = custName;
            payment_infoController.custNote = custNote;


            
            // Call loadView method from mainController to switch views
            mainController.loadView("/view/orders/payment_info.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backToOrder() throws SQLException {
        try {
            // Get reference to the main controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/orders/order.fxml"));
            Pane root = loader.load();
            mainController mainController = (mainController) backButton1.getScene().getRoot().getUserData();
            orderController oc = loader.getController();
            oc.refreshTable();
            
            // Pass the main controller reference to the order controller
            oc.setMainController(mainController);
    
            // Call loadView method from mainController to switch views
            mainController.loadView("/view/orders/order.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void setOrderNumText() {
        orderNumText.setText("Order #" + orderNumCurrent);
    }

    public void setNumOniText() {
        int numberOfOnigiri = 0;
        for (ordered_items order : ordersListl) {
            numberOfOnigiri += order.getQuantity();
        }
        numOniText.setText(String.valueOf(numberOfOnigiri));
    }

    public void setTotalAmountText() throws SQLException {
        double totalAmount = 0.0;
        for (ordered_items order : ordersListl) {
            totalAmount += ordered_items.findProductPriceSimple(order.getProduct_id()) * order.getQuantity();
        }
        totalAmountText.setText(totalAmount + "0 PHP");
    }

    public static void main(String[] args) {
        // System.out.println(go_paymentInfo_btn);
    }
    
}
