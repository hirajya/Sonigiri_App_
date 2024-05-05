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
                  "SUM(CASE WHEN i.isSpicy = 'Spicy' AND p.product_Name = 'Tuna Mayo' THEN i.qty ELSE 0 END) AS spicy_tuna_mayo, " +
                  "SUM(CASE WHEN i.isSpicy = 'Spicy' AND p.product_Name = 'Bulgogi' THEN i.qty ELSE 0 END) AS spicy_bulgogi, " +
                  "SUM(CASE WHEN i.isSpicy = 'Spicy' AND p.product_Name = 'Chicken Adobo' THEN i.qty ELSE 0 END) AS spicy_chicken_adobo, " +
                  "SUM(CASE WHEN i.isSpicy = 'Not Spicy' AND p.product_Name = 'Tuna Mayo' THEN i.qty ELSE 0 END) AS not_spicy_tuna_mayo, " +
                  "SUM(CASE WHEN i.isSpicy = 'Not Spicy' AND p.product_Name = 'Bulgogi' THEN i.qty ELSE 0 END) AS not_spicy_bulgogi, " +
                  "SUM(CASE WHEN i.isSpicy = 'Not Spicy' AND p.product_Name = 'Chicken Adobo' THEN i.qty ELSE 0 END) AS not_spicy_chicken_adobo " +
              "FROM order_table o " +
                  "LEFT JOIN ordered_items i ON o.order_NumOrder = i.order_NumOrder " +
                  "LEFT JOIN product p ON i.product_id = p.product_id " +
              "GROUP BY o.order_NumOrder" // Group by order number
          );
          ResultSet resultSet = preparedStatement.executeQuery();
          while (resultSet.next()) {
            int orderNum = resultSet.getInt("order_NumOrder");
            String buyerName = resultSet.getString("buyerName");
            String orderDate = resultSet.getString("order_Date");
            String orderTime = resultSet.getString("order_Time");
            int spicyTunaMayoCount = resultSet.getInt("spicy_tuna_mayo");
            int spicyBulgogiCount = resultSet.getInt("spicy_bulgogi");
            int spicyChickenAdoboCount = resultSet.getInt("spicy_chicken_adobo");
            int notSpicyTunaMayoCount = resultSet.getInt("not_spicy_tuna_mayo");
            int notSpicyBulgogiCount = resultSet.getInt("not_spicy_bulgogi");
            int notSpicyChickenAdoboCount = resultSet.getInt("not_spicy_chicken_adobo");
      
            orders.add(new OrderView(orderNum, buyerName, orderDate, orderTime,
                spicyTunaMayoCount, spicyBulgogiCount, spicyChickenAdoboCount,
                notSpicyTunaMayoCount, notSpicyBulgogiCount, notSpicyChickenAdoboCount));
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

    public static class OrderView {
        private final int orderNum;
        private final String custName;
        private final String orderDate;
        private final String orderTime;
        private final int spicyTunaMayoCount;
        private final int spicyBulgogiCount;
        private final int spicyChickenAdoboCount;
        private final int notSpicyTunaMayoCount;
        private final int notSpicyBulgogiCount;
        private final int notSpicyChickenAdoboCount;
    
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
    
        public int getSpicyBulgogiCount() {
            return spicyBulgogiCount;
        }
    
        public int getSpicyChickenAdoboCount() {
            return spicyChickenAdoboCount;
        }
    
        public int getNotSpicyTunaMayoCount() {
            return notSpicyTunaMayoCount;
        }
    
        public int getNotSpicyBulgogiCount() {
            return notSpicyBulgogiCount;
        }
    
        public int getNotSpicyChickenAdoboCount() {
            return notSpicyChickenAdoboCount;
        }
    }
    
}


