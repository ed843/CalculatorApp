package org.openjfx.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openjfx.controller.CalculatorController;
import org.openjfx.model.CalculatorModel;

public class CalculatorView {
    private final Logger logger = LogManager.getLogger();
    private final GridPane gridPane;
    private final Label displayLabel;
    private final CalculatorController controller;

    public CalculatorView() {
        logger.info("Initializing CalculatorView");
        gridPane = new GridPane();
        displayLabel = createDisplayLabel();
        controller = new CalculatorController(new CalculatorModel(), displayLabel);

        setupGridPane();
        addButtons();
        setupKeyboardHandling();
        logger.info("CalculatorView initialization completed");
    }


    private boolean shiftPressed = false;  // Add this field to the class

    private void setupKeyboardHandling() {
        logger.debug("Setting up keyboard event handling");
        gridPane.setFocusTraversable(true);

        gridPane.setOnKeyPressed(event -> {
            logger.debug("Key pressed: {}", event.getCode());

            if (event.getCode() == KeyCode.SHIFT) {
                shiftPressed = true;
                event.consume();
                return;
            }

            if (shiftPressed && event.getCode() == KeyCode.EQUALS) {
                controller.handleOperation(0);  // Addition
                event.consume();
                return;
            }

            switch (event.getCode()) {
                case DIGIT0, NUMPAD0 -> controller.handleNumber(0);
                case DIGIT1, NUMPAD1 -> controller.handleNumber(1);
                case DIGIT2, NUMPAD2 -> {
                    if (!shiftPressed) {
                        controller.handleNumber(2);
                    } else {
                        controller.handleSquareRoot();
                    }
                }
                case DIGIT3, NUMPAD3 -> controller.handleNumber(3);
                case DIGIT4, NUMPAD4 -> controller.handleNumber(4);
                case DIGIT5, NUMPAD5 -> controller.handleNumber(5);
                case DIGIT6, NUMPAD6 -> controller.handleNumber(6);
                case DIGIT7, NUMPAD7 -> controller.handleNumber(7);
                case DIGIT8, NUMPAD8 -> controller.handleNumber(8);
                case DIGIT9, NUMPAD9 -> controller.handleNumber(9);
                case ADD -> controller.handleOperation(0);
                case SUBTRACT, MINUS -> controller.handleOperation(1);
                case MULTIPLY -> controller.handleOperation(2);
                case DIVIDE, SLASH -> controller.handleOperation(3);
                case ENTER -> controller.handleEquals();
                case EQUALS -> {
                    if (!shiftPressed) controller.handleEquals();
                }
                case PERIOD, DECIMAL -> controller.handleDecimal();
                case ESCAPE -> controller.handleClear();
                case Q -> controller.handleSquared();
                case R -> controller.handleOneOverX();
                case DELETE -> controller.handleClearEntry();
            }
            event.consume();
        });

        gridPane.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SHIFT) {
                shiftPressed = false;
                event.consume();
            }
        });
    }

    private void setupGridPane() {
        logger.debug("Setting up GridPane layout");
        setupGridConstraints();
        setupBackground();
        gridPane.add(displayLabel, 0, 0, 4, 1);
    }

    private void setupGridConstraints() {
        for (int i = 0; i < 4; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < 7; i++) {
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
        logger.debug("Adding calculator buttons");
        try {
            addNumberButtons();
            addOperationButtons();
            addSpecialButtons();
            logger.debug("All buttons added successfully");
        } catch (Exception e) {
            logger.error("Error adding calculator buttons", e);
            throw e;
        }
    }

    private void addNumberButtons() {
        for (int i = 1; i <= 9; i++) {
            Button button = createButton(String.valueOf(i));
            int finalI = i;
            button.setOnAction(e -> {
                controller.handleNumber(finalI);
                gridPane.requestFocus();
            });
            gridPane.add(button, (i - 1) % 3, ((i - 1) / 3) + 3);
        }
    }

    private void addOperationButtons() {
        String[] operations = {"+", "-", "*", "/"};
        for (int i = 0; i < operations.length; i++) {
            Button button = createButton(operations[i]);
            int finalI = i;
            button.setOnAction(e -> {
                controller.handleOperation(finalI);
                gridPane.requestFocus();
            });
            gridPane.add(button, 3, i + 2);
        }
    }

    private void addSpecialButtons() {
        Button zeroButton = createButton("0");
        zeroButton.setOnAction(e -> {
            controller.handleNumber(0);
            gridPane.requestFocus();
        });
        gridPane.add(zeroButton, 1, 6);

        Button clearButton = createButton("C");
        clearButton.setOnAction(e -> {
            controller.handleClear();
            gridPane.requestFocus();
        });
        gridPane.add(clearButton, 2, 1);

        Button equalButton = createButton("=");
        equalButton.setOnAction(e -> {
            controller.handleEquals();
            gridPane.requestFocus();
        });
        gridPane.add(equalButton, 3, 6);

        Button decimalButton = createButton(".");
        decimalButton.setOnAction(e -> {
            controller.handleDecimal();
            gridPane.requestFocus();
        });
        gridPane.add(decimalButton, 2, 6);

        Button plusMinusButton = createButton("+/-");
        plusMinusButton.setOnAction(e -> {
            controller.handlePlusMinus();
            gridPane.requestFocus();
        });
        gridPane.add(plusMinusButton, 0, 6);

        Button oneOverXButton = createButton("⅟x");
        oneOverXButton.setOnAction(e -> {
            controller.handleOneOverX();
            gridPane.requestFocus();
        });
        gridPane.add(oneOverXButton, 0, 2);

        // create squared button text
        Button squaredButton = createButton("x²");
        squaredButton.setOnAction(e -> {
            controller.handleSquared();
            gridPane.requestFocus();
        });
        gridPane.add(squaredButton, 1, 2);

        Button squareRootButton = createButton("√x");
        squareRootButton.setOnAction(e -> {
            controller.handleSquareRoot();
            gridPane.requestFocus();
        });
        gridPane.add(squareRootButton, 2, 2);

        Button clearEntryButton = createButton("CE");
        clearEntryButton.setOnAction(e -> {
            controller.handleClearEntry();
            gridPane.requestFocus();
        });
        gridPane.add(clearEntryButton, 1, 1);

        Button backSpaceButton = createButton("⌫");
        backSpaceButton.setOnAction(e -> {
            controller.handleBackSpace();
            gridPane.requestFocus();
        });
        gridPane.add(backSpaceButton, 3, 1);
    }

    private Button createButton(String text) {
        logger.trace("Creating button with text: {}", text);
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);

        // Disable focus traversable to prevent keyboard focus
        button.setFocusTraversable(false);

        return button;
    }

    public GridPane getView() {
        return gridPane;
    }
}

