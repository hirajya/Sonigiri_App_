package controller.analytics;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.order;
import model.ordered_items;

import java.sql.SQLException;

public class analyticsController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Text totalSoldLabel;

    @FXML
    private Text bulgogiSoldLabel;

    @FXML
    private Text tunaMayoSoldLabel;

    @FXML
    private Text adoboSoldLabel;

    @FXML
    private Text totalSalesLabel;

    public void initialize() {
        try {
            loadAnalyticsData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAnalyticsData() throws SQLException {
        int totalSold = getTotalSoldCount();
        totalSoldLabel.setText(" " + totalSold);

        int bulgogiSold = getSoldCountForProduct("Bulgogi");
        bulgogiSoldLabel.setText(" " + bulgogiSold);

        int tunaMayoSold = getSoldCountForProduct("Tuna-Mayo");
        tunaMayoSoldLabel.setText(" " + tunaMayoSold);

        int adoboSold = getSoldCountForProduct("Chicken Adb");
        adoboSoldLabel.setText(" " + adoboSold);

        double totalSales = getTotalSales();
        totalSalesLabel.setText(" " + totalSales);
    }

    private int getTotalSoldCount() throws SQLException {
        // Fetch total sold count from database (sum of all quantities)
        // Assuming you have a method in your model to fetch this data
        return order.getTotalSoldCount();
    }

    private double getTotalSales() throws SQLException {
        // Fetch total sold count from database (sum of all quantities)
        // Assuming you have a method in your model to fetch this data
        return order.getTotalEarnings();
    }

    private int getSoldCountForProduct(String productName) throws SQLException {
        // Fetch sold count for a specific product from database
        // Assuming you have a method in your model to fetch this data
        return ordered_items.getSoldCountForProduct(productName);
    }
}
