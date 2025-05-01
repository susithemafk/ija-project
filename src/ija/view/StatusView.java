/**
 * Vizuální reprezentace stavu hry.
 */
package ija.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class StatusView {
    private HBox wrapper;
    private Label stepsLabel;
    private Label messageLabel;

    public StatusView() {
        // vytvoření wrapperu
        wrapper = new HBox();
        wrapper.setAlignment(Pos.TOP_LEFT);
        wrapper.setSpacing(10);
        wrapper.setPadding(new Insets(10));

        // text - počet kroků
        stepsLabel = new Label("Počet kroků: 0");
        stepsLabel.setFont(Font.font(14));

        // text - zpráva
        messageLabel = new Label("");
        messageLabel.setFont(Font.font(14));
        messageLabel.setStyle("-fx-text-fill: green;");

        // přidání textů do wrapperu
        wrapper.getChildren().addAll(stepsLabel, messageLabel);
    }

    public HBox getWrapper() {
        return wrapper;
    }

    public void updateSteps(int count) {
        stepsLabel.setText("Počet kroků: " + count);
    }

    public void showMessage(String text, boolean error) {
        messageLabel.setText(text);
        messageLabel.setStyle(error ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void clearMessage() {
        messageLabel.setText("");
    }
}