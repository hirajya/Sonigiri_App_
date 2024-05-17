package controller.order;

import java.io.IOException;

import controller.mainController;
import controller.order.utils.StatusTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.util.Callback;




public class table_ordersController {

    @FXML
    Button add_order_btn;

    // Font fontPoppinsBold = Font.loadFont("assets/fonts/Poppins-Bold.ttf", 45);
    // Font fontPoppinsRegular = Font.loadFont("assets/fonts/Poppins-Regular.ttf", 45);

    @FXML
    Button FpendingBtn, FmakingBtn, FreadyBtn, FdoneBtn, FallBtn;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sonigiri_database";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private TableView<OrderView> tableViewOrder;

    @FXML
    Text PendingOrdersCnt, MakingOrdersCnt, ReadyOrdersCnt, OrderClaimedCnt, ordersText;
    

    public void initialize() {
        fetchData();
        setupTableView();
        addTableClickListener();
        updateHeaderCounterOrders();


    }

    private void fetchData() {
        ObservableList<OrderView> orders = FXCollections.observableArrayList();
    
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT o.order_NumOrder, o.order_CusName AS buyerName, o.order_Date, o.order_Time, o.order_Status, " +
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
                String PStatus = resultSet.getString("order_Status");
                int productId = resultSet.getInt("product_id");
                String isSpicy = resultSet.getString("isSpicy");
                int qty = resultSet.getInt("qty");
    
                OrderView orderView;
                if (!orderMap.containsKey(orderNum)) {
                    orderView = new OrderView(orderNum, buyerName, orderDate, orderTime, 0, 0, 0, 0, 0, 0, PStatus);
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

    
    

    private void addTableClickListener() {
        tableViewOrder.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2 && tableViewOrder.getSelectionModel().getSelectedItem() != null) {
                toReceipt();
            }
        });
    }

    public int getPending() {
        // Get the total count of pending orders
        int pending = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(order_NumOrder) AS pending " +
                "FROM order_table " +
                "WHERE order_Status = 'Pending'"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                pending = resultSet.getInt("pending");
            }
        } catch (SQLException e) {
            e.printStackTrace();
    }

    return pending;
    }

    public int getDone() {
        // Get the total count of done orders
        int done = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(order_NumOrder) AS done " +
                "FROM order_table " +
                "WHERE order_Status = 'Done'"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                done = resultSet.getInt("done");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return done;
    }

    public int getMaking() {
        // Get the total count of making orders
        int making = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(order_NumOrder) AS making " +
                "FROM order_table " +
                "WHERE order_Status = 'Making'"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                making = resultSet.getInt("making");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return making;
    }

    public int getReady() {
        // Get the total count of ready orders
        int ready = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(order_NumOrder) AS ready " +
                "FROM order_table " +
                "WHERE order_Status = 'Ready'"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ready = resultSet.getInt("ready");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ready;
    }

    public void updateHeaderCounterOrders() {
        // Update the header counter for orders
        int pending = getPending();
        int done = getDone();
        int making = getMaking();
        int ready = getReady();

        PendingOrdersCnt.setText(String.valueOf(pending));
        MakingOrdersCnt.setText(String.valueOf(making));
        ReadyOrdersCnt.setText(String.valueOf(ready));
        OrderClaimedCnt.setText(String.valueOf(done));
    }

    public void showAll() {
        fetchData();
    }

    public void showPendingOrders() {
        fetchData();
        // Get the current list of orders in the TableView
        ObservableList<OrderView> allOrders = tableViewOrder.getItems();

        // Use a FilteredList to filter orders by status
        FilteredList<OrderView> filteredOrders = new FilteredList<>(allOrders, order -> "Pending".equals(order.getPStatus()));

        // Wrap the FilteredList in a SortedList (if you want to maintain sorting)
        SortedList<OrderView> sortedOrders = new SortedList<>(filteredOrders);
        sortedOrders.comparatorProperty().bind(tableViewOrder.comparatorProperty());

        // Set the filtered and sorted list as the items for the TableView
        tableViewOrder.setItems(sortedOrders);
    }

    public void showMakingOrders() {
        fetchData();
        // Get the current list of orders in the TableView
        ObservableList<OrderView> allOrders = tableViewOrder.getItems();

        // Use a FilteredList to filter orders by status
        FilteredList<OrderView> filteredOrders = new FilteredList<>(allOrders, order -> "Making".equals(order.getPStatus()));

        // Wrap the FilteredList in a SortedList (if you want to maintain sorting)
        SortedList<OrderView> sortedOrders = new SortedList<>(filteredOrders);
        sortedOrders.comparatorProperty().bind(tableViewOrder.comparatorProperty());

        // Set the filtered and sorted list as the items for the TableView
        tableViewOrder.setItems(sortedOrders);
    }

    public void showReadyOrders() {
        fetchData();
        // Get the current list of orders in the TableView
        ObservableList<OrderView> allOrders = tableViewOrder.getItems();

        // Use a FilteredList to filter orders by status
        FilteredList<OrderView> filteredOrders = new FilteredList<>(allOrders, order -> "Ready".equals(order.getPStatus()));

        // Wrap the FilteredList in a SortedList (if you want to maintain sorting)
        SortedList<OrderView> sortedOrders = new SortedList<>(filteredOrders);
        sortedOrders.comparatorProperty().bind(tableViewOrder.comparatorProperty());

        // Set the filtered and sorted list as the items for the TableView
        tableViewOrder.setItems(sortedOrders);
    }

    public void showDoneOrders() {
        fetchData();
        // Get the current list of orders in the TableView
        ObservableList<OrderView> allOrders = tableViewOrder.getItems();

        // Use a FilteredList to filter orders by status
        FilteredList<OrderView> filteredOrders = new FilteredList<>(allOrders, order -> "Done".equals(order.getPStatus()));

        // Wrap the FilteredList in a SortedList (if you want to maintain sorting)
        SortedList<OrderView> sortedOrders = new SortedList<>(filteredOrders);
        sortedOrders.comparatorProperty().bind(tableViewOrder.comparatorProperty());

        // Set the filtered and sorted list as the items for the TableView
        tableViewOrder.setItems(sortedOrders);
    }


    private void toReceipt() {
        try {
            // Get reference to the main controller
            mainController mainController = (mainController) add_order_btn.getScene().getRoot().getUserData();
            
            // Get the selected order
            OrderView selectedOrder = tableViewOrder.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                int selectedOrderNum = selectedOrder.getOrderNum();
                // Pass the selected order number to the receipt controller
                receipt_Controller.orderNum = selectedOrderNum;
                // Call loadView method from mainController to switch views
                mainController.loadView("/view/orders/receipt.fxml");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    


      


    

    private void setupTableView() {
        TableColumn<OrderView, Integer> orderNumColumn = new TableColumn<>("Order #");
        orderNumColumn.setCellValueFactory(new PropertyValueFactory<>("orderNum"));

        TableColumn<OrderView, String> custNameColumn = new TableColumn<>("Cust Name");
        custNameColumn.setCellValueFactory(new PropertyValueFactory<>("custName"));

        TableColumn<OrderView, String> orderDateColumn = new TableColumn<>("Date");
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        TableColumn<OrderView, String> orderTimeColumn = new TableColumn<>("Time");
        orderTimeColumn.setCellValueFactory(new PropertyValueFactory<>("orderTime"));

        TableColumn<OrderView, String> PStatusColumn = new TableColumn<>("Status");
        PStatusColumn.setCellValueFactory(new PropertyValueFactory<>("PStatus"));
        PStatusColumn.setCellFactory(column -> new StatusTableCell());

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

        tableViewOrder.getColumns().addAll(orderNumColumn, custNameColumn, orderDateColumn, orderTimeColumn, PStatusColumn);

        orderNumColumn.setSortType(SortType.DESCENDING);
        tableViewOrder.getSortOrder().add(orderNumColumn);
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
        private String PStatus;
    
        public OrderView(int orderNum, String custName, String orderDate, String orderTime,
                         int spicyTunaMayoCount, int spicyBulgogiCount, int spicyChickenAdoboCount,
                         int notSpicyTunaMayoCount, int notSpicyBulgogiCount, int notSpicyChickenAdoboCount, String PStatus) {
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
            this.PStatus = PStatus;
        }
    
        // Getters

        public String getPStatus() {
            return PStatus;
        }

        public String setPStatus(String PStatus) {
            return PStatus;
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




