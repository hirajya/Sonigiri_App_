package controller.order;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class payment_infoController {
    
    @FXML
    Pane GCashPane, CashPane;

    @FXML
    RadioButton GCash_RButton, Cash_RButton;

    public void initialize() {
        GCashPane.setVisible(false);
        CashPane.setVisible(false);
    }

    public void handleGCashRadioButton() {
        GCashPane.setVisible(true);
        CashPane.setVisible(false);
    }

    public void handleCashRadioButton() {
        GCashPane.setVisible(false);
        CashPane.setVisible(true);
    }


}
