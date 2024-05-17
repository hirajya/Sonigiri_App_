package controller.order.utils;

import controller.order.table_ordersController.OrderView;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class StatusTableCell extends TableCell<OrderView, String> {
    private final Circle circle;
    private final Text text;

    public StatusTableCell() {
        this.circle = new Circle(5);
        this.text = new Text();
        setGraphic(new HBox(5, circle, text)); // Spacing of 5 between circle and text
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else {
            text.setText(item);
            switch (item.toLowerCase()) {
                case "pending":
                    circle.setFill(Color.GRAY);
                    break;
                case "making":
                    circle.setFill(Color.BLUE);
                    break;
                case "ready":
                    circle.setFill(Color.GREEN);
                    break;
                case "done":
                    circle.setFill(Color.RED);
                    break;
                default:
                    circle.setFill(Color.TRANSPARENT);
                    break;
            }
            setGraphic(new HBox(5, circle, text)); // Update graphic with spacing
        }
    }
}
