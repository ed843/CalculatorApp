package org.openjfx.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.openjfx.controller.CalculatorController;
import org.openjfx.model.CalculatorModel;

public class CalculatorView {
    private final GridPane gridPane;
    private final Label displayLabel;
    private final CalculatorController controller;

    public CalculatorView() {
        gridPane = new GridPane();
        displayLabel = createDisplayLabel();
        controller = new CalculatorController(new CalculatorModel(), displayLabel);

        setupGridPane();
        addButtons();
    }

    private void setupGridPane() {
        setupGridConstraints();
        setupBackground();
        gridPane.add(displayLabel, 0, 0, 3, 1);
    }

    private void setupGridConstraints() {
        for (int i = 0; i < 4; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < 6; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(rowConstraints);
        }
    }

    private void setupBackground() {
        var backgroundFill = new BackgroundFill(Paint.valueOf("black"), CornerRadii.EMPTY, Insets.EMPTY);
        gridPane.setBackground(new Background(backgroundFill));
    }

    private Label createDisplayLabel() {
        Label label = new Label("0");
        Font font = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 36);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.setTextFill(Paint.valueOf("white"));
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setFont(font);
        return label;
    }

    private void addButtons() {
        addNumberButtons();
        addOperationButtons();
        addSpecialButtons();
    }

    private void addNumberButtons() {
        for (int i = 1; i <= 9; i++) {
            Button button = createButton(String.valueOf(i));
            int finalI = i;
            button.setOnMouseClicked(e -> controller.handleNumber(finalI));
            gridPane.add(button, (i - 1) % 3, ((i - 1) / 3) + 2);
        }
    }

    private void addOperationButtons() {
        String[] operations = {"+", "-", "*", "/"};
        for (int i = 0; i < operations.length; i++) {
            Button button = createButton(operations[i]);
            int finalI = i;
            button.setOnMouseClicked(e -> controller.handleOperation(finalI));
            gridPane.add(button, 3, i + 1);
        }
    }

    private void addSpecialButtons() {
        Button zeroButton = createButton("0");
        zeroButton.setOnMouseClicked(e -> controller.handleNumber(0));
        gridPane.add(zeroButton, 1, 5);

        Button clearButton = createButton("C");
        clearButton.setOnMouseClicked(e -> controller.handleClear());
        gridPane.add(clearButton, 2, 1);

        Button equalButton = createButton("=");
        equalButton.setOnMouseClicked(e -> controller.handleEquals());
        gridPane.add(equalButton, 3, 5);

        Button decimalButton = createButton(".");
        decimalButton.setOnMouseClicked(e -> controller.handleDecimal());
        gridPane.add(decimalButton, 2, 5);

        Button plusMinusButton = createButton("+/-");
        plusMinusButton.setOnMouseClicked(e -> controller.handlePlusMinus());
        gridPane.add(plusMinusButton, 0, 5);
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);
        return button;
    }

    public GridPane getView() {
        return gridPane;
    }
}
