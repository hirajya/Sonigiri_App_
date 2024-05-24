package controller.order;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.order;
import model.ordered_items;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import controller.mainController;
import java.util.ArrayList;

public class orderController {

    @FXML
    Text orderNumText, numOniText, totalAmountText, UQtyCntText;

    @FXML
    Pane updatePane;

    @FXML
    VBox orderCard;

    @FXML
    ScrollPane orderScrollPane;

    @FXML
    Button backButton, order_out_btn, add_order_btn, qty_plus_btn, qty_minus_btn, UQtyM, UQtyA, updateButtonGo, updateDeleteButtonGo;

    @FXML
    RadioButton tnmyo_RButton, blgi_RButton, chcknadb_RButton, spicyYes_RButton, spicyNo_RButton, UTnm_Rbutton, Ublg_Rbutton, UChck_Rbutton, UNSpicy_Rbutton, USpicy_Rbutton;

    @FXML
    Text qtyText;

    @FXML
    Pane orderPane, orderSumPane;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sonigiri_database";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    private mainController mainController;

    
    static ArrayList<ordered_items> orders = new ArrayList<ordered_items>();
    static String orderSelected;
    static String isSpicySelected;
    static int product_idSelected;
    static int orderNumCurrent;
    static double totalAmount = 0.0;
    static int numberOfOnigiri = 0; 

    @FXML
    public void initialize() throws IOException {
        noSideRectangles();
        orderNumCurrent = getNextAvailableOrderNumber();
        setOrderNumText();
        orders.clear();
        try {
            refreshTable(); // Call this method to populate the orderCard VBox with the updated data
            setNumOniText();
            setTotalAmountText();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
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

    


    public void setMainController(mainController mainController) {
        this.mainController = mainController;
    }

    public void setOrderNumText() {
        orderNumText.setText("Order #" + orderNumCurrent);
    }

    public void setNumOniText() {
        numberOfOnigiri = 0;
        for (ordered_items order : orders) {
            numberOfOnigiri += order.getQuantity();
        }
        numOniText.setText(String.valueOf(numberOfOnigiri));
    }

    public void setTotalAmountText() throws SQLException {
        totalAmount = 0.0;
        int onigiriCount = 0;
    
        // Calculate total amount with discount
        for (ordered_items order : orders) {
            double price = ordered_items.findProductPriceSimple(order.getProduct_id());
            int quantity = order.getQuantity();
            
            for (int i = 1; i <= quantity; i++) {
                onigiriCount++;
                if (onigiriCount % 4 == 0) {
                    // Apply 50% discount for every 4th onigiri
                    totalAmount += price * 0.5;
                } else {
                    totalAmount += price;
                }
            }
        }
        totalAmountText.setText(String.format("%.2f Php", totalAmount));
    }
    

    public void selectFlavor() {
        if (tnmyo_RButton.isSelected()) {
            orderSelected = "Tuna Mayo";
            product_idSelected = 1;
        } else if (blgi_RButton.isSelected()) {
            orderSelected = "Bulgogi";
            product_idSelected = 2;
        } else if (chcknadb_RButton.isSelected()) {
            orderSelected = "Chicken Adobo";
            product_idSelected = 3;
        }
    }

    public void qtyPlus() {
        int qty = Integer.parseInt(qtyText.getText());
        qty++;
        qtyText.setText(String.valueOf(qty));
    }

    public void qtyMinus() {
        int qty = Integer.parseInt(qtyText.getText());
        if (qty > 1) {
            qty--;
            qtyText.setText(String.valueOf(qty));
        }
    }

    public void selectIsSpicy() {
        if (spicyYes_RButton.isSelected()) {
            isSpicySelected = "Spicy";
        } else if (spicyNo_RButton.isSelected()) {
            isSpicySelected = "Not Spicy";
        }
    }

    public void addOrderCard(ordered_items orderItem) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/orders/widgets/add_order_pane.fxml"));
        Pane containerContent = loader.load(); // Load the container FXML
        orderCardController oCardController = loader.getController(); // Assign controller to variable
        oCardController.setData(orderItem);
        int index = orders.indexOf(orderItem);
        containerContent.setUserData(index);
        containerContent.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                int clickedIndex = (int) containerContent.getUserData();
            if (updatePane.isVisible() && updatePane.getUserData() == orderItem) {
                updatePane.setVisible(false);
                containerContent.setStyle(null); // Remove highlight
            } else {
                // Select the update order
                selectUpdateOrder(orders.get(clickedIndex));
                // Highlight the clicked pane
                containerContent.setStyle("-fx-background-color: lightgreen;"); // Change the background color
                // Show the update pane
                updatePane.setVisible(true);
                updatePane.setUserData(orderItem);
                // Remove highlight from other panes
                for (Node node : orderCard.getChildren()) {
                    if (node instanceof Pane && node != containerContent) {
                        node.setStyle(null); // Remove any existing styles
                    }
                }
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        orderCard.getChildren().add(containerContent); // Add the container to the VBox
    }

   
    public void selectUpdateOrder(ordered_items orderSelect) {
        UTnm_Rbutton.setSelected(false);
        Ublg_Rbutton.setSelected(false);
        UChck_Rbutton.setSelected(false);
        USpicy_Rbutton.setSelected(false);
        UNSpicy_Rbutton.setSelected(false);

        updatePane.setVisible(true);
        updatePane.setUserData(orderSelect);
        if (orderSelect.getProduct_id() == 1) {
            UTnm_Rbutton.setSelected(true);
        } else if (orderSelect.getProduct_id() == 2) {
            Ublg_Rbutton.setSelected(true);
        } else if (orderSelect.getProduct_id() == 3) {
            UChck_Rbutton.setSelected(true);
        }

        if (orderSelect.isSpicy().equals("Spicy")) {
            USpicy_Rbutton.setSelected(true);
            UNSpicy_Rbutton.setSelected(false);
        } else {
            UNSpicy_Rbutton.setSelected(true);
            USpicy_Rbutton.setSelected(false);
        }

        UQtyCntText.setText(String.valueOf(orderSelect.getQuantity()));
    }

    public void updateQtyPresent() {
        UQtyCntText.setText(qtyText.getText());
    }

    public void updateQtyMinus() {
        int qty = Integer.parseInt(UQtyCntText.getText());
        if (qty > 1) {
            qty--;
            UQtyCntText.setText(String.valueOf(qty));
        }
    }

    public void updateQtyPlus() {
        int qty = Integer.parseInt(UQtyCntText.getText());
        qty++;
        UQtyCntText.setText(String.valueOf(qty));
    }
    

    public void handleAddToCartButtonMet() throws SQLException {
        selectFlavor();
        selectIsSpicy();
        int qty = Integer.parseInt(qtyText.getText());
        ordered_items order = new ordered_items(orderNumCurrent ,product_idSelected, isSpicySelected, qty);
        orders.add(order);
        
        try {
            setTotalAmountText();
            addOrderCard(order);
            setNumOniText();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleUpdateButtonGo() throws IOException {
        ordered_items order = (ordered_items) updatePane.getUserData();
        int qty = Integer.parseInt(UQtyCntText.getText());
        if (UTnm_Rbutton.isSelected()) {
            order.setProduct_id(1);
        } else if (Ublg_Rbutton.isSelected()) {
            order.setProduct_id(2);
        } else if (UChck_Rbutton.isSelected()) {
            order.setProduct_id(3);
        }

        if (USpicy_Rbutton.isSelected()) {
            order.setSpicy("Spicy");
        } else if (UNSpicy_Rbutton.isSelected()) {
            order.setSpicy("Not Spicy");
        }

        order.setQuantity(qty);
        updateQtyPresent();
        ordered_items.printOrderedItems(orders);
        try {
            setNumOniText();
            setTotalAmountText();

            // Clear all existing containers
            orderCard.getChildren().clear();

            // Re-add all containers with updated data
            for (ordered_items orderItem : orders) {
                addOrderCard(orderItem);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updatePane.setVisible(false);
        for (Node node : orderCard.getChildren()) {
            if (node instanceof Pane) {
                node.setStyle(null); // Remove any existing styles
            }
        }
        
    }

    public void handleUpdateDeleteButtonGo() {
        ordered_items order = (ordered_items) updatePane.getUserData();
        orders.remove(order);
        orderCard.getChildren().clear();
        try {
            for (ordered_items orderItem : orders) {
                addOrderCard(orderItem);
            }
            setNumOniText();
            setTotalAmountText();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        updatePane.setVisible(false);
    }

    public void refreshTable() throws SQLException, IOException {
        for (ordered_items orderItem : orders) {
            addOrderCard(orderItem);
        }
        setNumOniText();
        setTotalAmountText();
    }
    
    public void handleConfirmOrderButtonMet() {
        try {
            // Get reference to the main controller
            customer_infoController.ordersListl = orders;
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

    public void goBackToTable() {
        try {
            // Get reference to the main controller
            mainController mainController = (mainController) order_out_btn.getScene().getRoot().getUserData();

            // Call loadView method from mainController to switch views
            mainController.loadView("/view/orders/table_orders.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
