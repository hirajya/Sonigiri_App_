package controller.order;

import java.io.IOException;

import controller.mainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;




public class table_ordersController {

    @FXML
    Button add_order_btn;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sonigiri_database";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private TableView<OrderView> tableViewOrder;
    

    public void initialize() {
        fetchData();
        setupTableView();
    }



    private void fetchData() {
        ObservableList<OrderView> orders = FXCollections.observableArrayList();
    
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT o.order_NumOrder, o.order_CusName AS buyerName, o.order_Date, o.order_Time, " +
                    "i.product_id, i.isSpicy, i.qty " +
                "FROM order_table o " +
                    "LEFT JOIN ordered_items i ON o.order_NumOrder = i.order_NumOrder " +
                    "LEFT JOIN product p ON i.product_id = p.product_id"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            Map<Integer, OrderView> orderMap = new HashMap<>();
            while (resultSet.next()) {
                int orderNum = resultSet.getInt("order_NumOrder");
                String buyerName = resultSet.getString("buyerName");
                String orderDate = resultSet.getString("order_Date");
                String orderTime = resultSet.getString("order_Time");
                int productId = resultSet.getInt("product_id");
                String isSpicy = resultSet.getString("isSpicy");
                int qty = resultSet.getInt("qty");
    
                OrderView orderView;
                if (!orderMap.containsKey(orderNum)) {
                    orderView = new OrderView(orderNum, buyerName, orderDate, orderTime, 0, 0, 0, 0, 0, 0);
                    orderMap.put(orderNum, orderView);
                    orders.add(orderView);
                } else {
                    orderView = orderMap.get(orderNum);
                }
    
                // Adjust quantity based on spiciness
                if (isSpicy.equals("Spicy")) {
                    switch (productId) {
                        case 1: // Product ID for Tuna Mayo
                            orderView.setSpicyTunaMayoCount(qty);
                            break;
                        case 2: // Product ID for Bulgogi
                            orderView.setSpicyBulgogiCount(qty);
                            break;
                        case 3: // Product ID for Chicken Adobo
                            orderView.setSpicyChickenAdoboCount(qty);
                            break;
                        // Add more cases if needed for other products
                    }
                } else if (isSpicy.equals("Not Spicy")) {
                    switch (productId) {
                        case 1: // Product ID for Tuna Mayo
                            orderView.setNotSpicyTunaMayoCount(qty);
                            break;
                        case 2: // Product ID for Bulgogi
                            orderView.setNotSpicyBulgogiCount(qty);
                            break;
                        case 3: // Product ID for Chicken Adobo
                            orderView.setNotSpicyChickenAdoboCount(qty);
                            break;
                        // Add more cases if needed for other products
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        tableViewOrder.setItems(orders);
    }
    
      


    

    private void setupTableView() {
        TableColumn<OrderView, Integer> orderNumColumn = new TableColumn<>("Order Number");
        orderNumColumn.setCellValueFactory(new PropertyValueFactory<>("orderNum"));
    
        TableColumn<OrderView, String> custNameColumn = new TableColumn<>("Cust Name");
        custNameColumn.setCellValueFactory(new PropertyValueFactory<>("custName"));
    
        TableColumn<OrderView, String> orderDateColumn = new TableColumn<>("Order Date");
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
    
        TableColumn<OrderView, String> orderTimeColumn = new TableColumn<>("Order Time");
        orderTimeColumn.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
    
        // Spicy category
        TableColumn<OrderView, Integer> spicyColumn = new TableColumn<>("Spicy");
        tableViewOrder.getColumns().add(spicyColumn);
    
        TableColumn<OrderView, Integer> spicyTunaMayoCountColumn = new TableColumn<>("Tna Myo");
        spicyTunaMayoCountColumn.setCellValueFactory(new PropertyValueFactory<>("spicyTunaMayoCount"));
    
        TableColumn<OrderView, Integer> spicyBulgogiCountColumn = new TableColumn<>("Blg");
        spicyBulgogiCountColumn.setCellValueFactory(new PropertyValueFactory<>("spicyBulgogiCount"));
    
        TableColumn<OrderView, Integer> spicyChickenAdoboCountColumn = new TableColumn<>("Chck Adb");
        spicyChickenAdoboCountColumn.setCellValueFactory(new PropertyValueFactory<>("spicyChickenAdoboCount"));
    
        spicyColumn.getColumns().addAll(spicyTunaMayoCountColumn, spicyBulgogiCountColumn, spicyChickenAdoboCountColumn);
    
        // Not Spicy category
        TableColumn<OrderView, Integer> notSpicyColumn = new TableColumn<>("Not Spicy");
        tableViewOrder.getColumns().add(notSpicyColumn);
    
        TableColumn<OrderView, Integer> notSpicyTunaMayoCountColumn = new TableColumn<>("Tna Myo");
        notSpicyTunaMayoCountColumn.setCellValueFactory(new PropertyValueFactory<>("notSpicyTunaMayoCount"));
    
        TableColumn<OrderView, Integer> notSpicyBulgogiCountColumn = new TableColumn<>("Blg");
        notSpicyBulgogiCountColumn.setCellValueFactory(new PropertyValueFactory<>("notSpicyBulgogiCount"));
    
        TableColumn<OrderView, Integer> notSpicyChickenAdoboCountColumn = new TableColumn<>("Chck Adb");
        notSpicyChickenAdoboCountColumn.setCellValueFactory(new PropertyValueFactory<>("notSpicyChickenAdoboCount"));
    
        notSpicyColumn.getColumns().addAll(notSpicyTunaMayoCountColumn, notSpicyBulgogiCountColumn, notSpicyChickenAdoboCountColumn);
    
        tableViewOrder.getColumns().addAll(orderNumColumn, custNameColumn, orderDateColumn, orderTimeColumn);
    }
    
    

    public void handleAddOrderButtonMet() {
        try {
            // Get reference to the main controller
            mainController mainController = (mainController) add_order_btn.getScene().getRoot().getUserData();

            // Call loadView method from mainController to switch views
            mainController.loadView("/view/orders/order.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class OrderView {
        private final int orderNum;
        private final String custName;
        private final String orderDate;
        private final String orderTime;
        private int spicyTunaMayoCount;
        private int spicyBulgogiCount;
        private int spicyChickenAdoboCount;
        private int notSpicyTunaMayoCount;
        private int notSpicyBulgogiCount;
        private int notSpicyChickenAdoboCount;
    
        public OrderView(int orderNum, String custName, String orderDate, String orderTime,
                         int spicyTunaMayoCount, int spicyBulgogiCount, int spicyChickenAdoboCount,
                         int notSpicyTunaMayoCount, int notSpicyBulgogiCount, int notSpicyChickenAdoboCount) {
            this.orderNum = orderNum;
            this.custName = custName;
            this.orderDate = orderDate;
            this.orderTime = orderTime;
            this.spicyTunaMayoCount = spicyTunaMayoCount;
            this.spicyBulgogiCount = spicyBulgogiCount;
            this.spicyChickenAdoboCount = spicyChickenAdoboCount;
            this.notSpicyTunaMayoCount = notSpicyTunaMayoCount;
            this.notSpicyBulgogiCount = notSpicyBulgogiCount;
            this.notSpicyChickenAdoboCount = notSpicyChickenAdoboCount;
        }
    
        // Getters
        public int getOrderNum() {
            return orderNum;
        }
    
        public String getCustName() {
            return custName;
        }
    
        public String getOrderDate() {
            return orderDate;
        }
    
        public String getOrderTime() {
            return orderTime;
        }
    
        public int getSpicyTunaMayoCount() {
            return spicyTunaMayoCount;
        }
    
        public void setSpicyTunaMayoCount(int spicyTunaMayoCount) {
            this.spicyTunaMayoCount = spicyTunaMayoCount;
        }
    
        public int getSpicyBulgogiCount() {
            return spicyBulgogiCount;
        }
    
        public void setSpicyBulgogiCount(int spicyBulgogiCount) {
            this.spicyBulgogiCount = spicyBulgogiCount;
        }
    
        public int getSpicyChickenAdoboCount() {
            return spicyChickenAdoboCount;
        }
    
        public void setSpicyChickenAdoboCount(int spicyChickenAdoboCount) {
            this.spicyChickenAdoboCount = spicyChickenAdoboCount;
        }
    
        public int getNotSpicyTunaMayoCount() {
            return notSpicyTunaMayoCount;
        }
    
        public void setNotSpicyTunaMayoCount(int notSpicyTunaMayoCount) {
            this.notSpicyTunaMayoCount = notSpicyTunaMayoCount;
        }
    
        public int getNotSpicyBulgogiCount() {
            return notSpicyBulgogiCount;
        }
    
        public void setNotSpicyBulgogiCount(int notSpicyBulgogiCount) {
            this.notSpicyBulgogiCount = notSpicyBulgogiCount;
        }
    
        public int getNotSpicyChickenAdoboCount() {
            return notSpicyChickenAdoboCount;
        }
    
        public void setNotSpicyChickenAdoboCount(int notSpicyChickenAdoboCount) {
            this.notSpicyChickenAdoboCount = notSpicyChickenAdoboCount;
        }
    }
    
    
}


