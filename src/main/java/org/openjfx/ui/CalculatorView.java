package org.openjfx.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openjfx.controller.CalculatorController;
import org.openjfx.enums.Operation;
import org.openjfx.model.CalculatorModel;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class CalculatorView {
    private final Logger logger = LogManager.getLogger();
    private final GridPane gridPane;
    private final Label displayLabel;
    private final CalculatorController controller;
    private final Map<Operation, Button> operationButtons = new HashMap<>();
    private final BorderPane root;
    private boolean shiftPressed = false;
    private double xOffset = 0;
    private double yOffset = 0;

    private static final Color BACKGROUND_COLOR = Color.rgb(28, 28, 30);
    private static final Color DISPLAY_BACKGROUND = Color.rgb(44, 44, 46);
    private static final Color NUMBER_BUTTON_COLOR = Color.rgb(58, 58, 60);
    private static final Color OPERATION_BUTTON_COLOR = Color.rgb(255, 159, 10);
    private static final Color OPERATION_ACTIVE_COLOR = Color.rgb(255, 255, 255);
    private static final Color SPECIAL_BUTTON_COLOR = Color.rgb(72, 72, 74);
    private static final Color TEXT_COLOR = Color.WHITE;

    public CalculatorView(Stage stage) {
        logger.info("Initializing CalculatorView");
        gridPane = new GridPane();
        displayLabel = createDisplayLabel();
        CalculatorModel model = new CalculatorModel();
        controller = new CalculatorController(model, displayLabel);
        root = new BorderPane();

        // Add listener to update operation button states
        model.currentOperationProperty().addListener((obs, oldOp, newOp) -> {
            updateOperationButtonStates(oldOp, newOp);
        });

        setupGridPane();
        addButtons();
        setupKeyboardHandling();
        setupBorderPane(stage);
        addExitButton(stage);
        logger.info("CalculatorView initialization completed");
    }

    public BorderPane getRoot() {
        return root;
    }

    private void updateOperationButtonStates(Operation oldOp, Operation newOp) {
        // Reset old operation button style if it exists
        if (oldOp != null && operationButtons.containsKey(oldOp)) {
            setOperationButtonStyle(operationButtons.get(oldOp), OPERATION_BUTTON_COLOR, false);
        }

        // Set new operation button style if it exists
        if (newOp != null && newOp != Operation.NONE && operationButtons.containsKey(newOp)) {
            setOperationButtonStyle(operationButtons.get(newOp), OPERATION_ACTIVE_COLOR, true);
        }
    }

    private void setupBorderPane(Stage stage) {
        // Set the calculator view in the center
        root.setCenter(this.getView());

        // Style the BorderPane with the same dark theme and rounded corners
        BackgroundFill backgroundFill = new BackgroundFill(
                BACKGROUND_COLOR,
                new CornerRadii(20),  // Matching the corner radius from gridPane
                Insets.EMPTY
        );
        root.setBackground(new Background(backgroundFill));

        // Add padding around the entire calculator
        root.setPadding(new Insets(10));

        // Setup window drag functionality
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        // Add a subtle border effect
        root.setStyle("""
        -fx-border-color: rgba(255, 255, 255, 0.1);
        -fx-border-width: 1px;
        -fx-border-radius: 20px;
        -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 15, 0, 0, 0);
        """);
    }




    private Button createExitButton(Stage stage) {
        Button exitButton = new Button("×");  // Using × instead of X for a more polished look
        exitButton.setStyle("""
        -fx-background-color: transparent;
        -fx-text-fill: rgb(255, 255, 255, 0.8);
        -fx-font-family: 'SF Pro Display';
        -fx-font-size: 18;
        -fx-cursor: hand;
        -fx-padding: 5 10;
        """);

        // Add hover effect
        exitButton.setOnMouseEntered(e -> exitButton.setStyle("""
        -fx-background-color: rgba(255, 59, 48, 0.8);
        -fx-text-fill: white;
        -fx-font-family: 'SF Pro Display';
        -fx-font-size: 18;
        -fx-cursor: hand;
        -fx-padding: 5 10;
        -fx-background-radius: 5;
        """));

        exitButton.setOnMouseExited(e -> exitButton.setStyle("""
        -fx-background-color: transparent;
        -fx-text-fill: rgb(255, 255, 255, 0.8);
        -fx-font-family: 'SF Pro Display';
        -fx-font-size: 18;
        -fx-cursor: hand;
        -fx-padding: 5 10;
        """));

        exitButton.setOnAction(event -> stage.close());
        return exitButton;
    }

    private void addExitButton(Stage stage) {
        Button exitButton = createExitButton(stage);
        HBox topBar = new HBox(exitButton);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(5, 5, 0, 0));
        topBar.setBackground(new Background(new BackgroundFill(
                BACKGROUND_COLOR,
                new CornerRadii(20, 20, 0, 0, false),  // Rounded top corners only
                Insets.EMPTY
        )));
        root.setTop(topBar);
    }




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
                case DIGIT8, NUMPAD8 -> {
                    if(!shiftPressed) {
                        controller.handleNumber(8);
                    } else {
                        controller.handleOperation(2);
                    }
                }
                case DIGIT9, NUMPAD9 -> controller.handleNumber(9);
                case ADD -> controller.handleOperation(0);
                case SUBTRACT, MINUS -> controller.handleOperation(1);
                case MULTIPLY, STAR -> controller.handleOperation(2);
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
                case BACK_SPACE -> controller.handleBackSpace();
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
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Set fixed row heights
        for (int i = 0; i < 7; i++) {
            RowConstraints rc = new RowConstraints();
            if (i == 0) {
                rc.setMinHeight(100); // Display row
                rc.setMaxHeight(100);
            } else {
                rc.setMinHeight(60); // Button rows
                rc.setMaxHeight(60);
            }
            rc.setVgrow(Priority.NEVER);
            gridPane.getRowConstraints().add(rc);
        }

        // Set fixed column widths
        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.NEVER);
            cc.setMinWidth(80);
            cc.setMaxWidth(80);
            gridPane.getColumnConstraints().add(cc);
        }

        setupBackground();
        gridPane.add(displayLabel, 0, 0, 4, 1);
    }


    private void setupBackground() {
        var backgroundFill = new BackgroundFill(BACKGROUND_COLOR, new CornerRadii(20), Insets.EMPTY);
        gridPane.setBackground(new Background(backgroundFill));
    }

    private Label createDisplayLabel() {
        Label label = new Label("0");
        label.setStyle(String.format("""
            -fx-background-color: rgb(%d, %d, %d);
            -fx-background-radius: 10;
            -fx-padding: 20;
            """,
                (int)(DISPLAY_BACKGROUND.getRed() * 255),
                (int)(DISPLAY_BACKGROUND.getGreen() * 255),
                (int)(DISPLAY_BACKGROUND.getBlue() * 255)));

        Font font = Font.font("SF Pro Display", FontWeight.LIGHT, 48);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setPrefHeight(100);
        label.setMinHeight(100);
        label.setTextFill(TEXT_COLOR);
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setFont(font);

        // Add text wrapping for long numbers
        label.setWrapText(true);
        return label;
    }

    private void setOperationButtonStyle(Button button, Color backgroundColor, boolean isActive) {
        String style = String.format("""
            -fx-background-color: rgb(%d, %d, %d);
            -fx-text-fill: %s;
            -fx-font-family: 'SF Pro Display';
            -fx-font-size: 18;
            -fx-background-radius: 30;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);
            """,
                (int)(backgroundColor.getRed() * 255),
                (int)(backgroundColor.getGreen() * 255),
                (int)(backgroundColor.getBlue() * 255),
                isActive ? "rgb(255, 159, 10)" : "white");

        button.setStyle(style);
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
            Button button = createButton(String.valueOf(i), NUMBER_BUTTON_COLOR);
            int finalI = i;
            button.setOnAction(e -> {
                controller.handleNumber(finalI);
                gridPane.requestFocus();
            });
            gridPane.add(button, (i - 1) % 3, ((i - 1) / 3) + 3);
        }
    }

    private Button createOperatorButton(String text) {
        logger.trace("Creating button with text: {}", text);
        Button button = new Button(text);
        button.setPrefSize(80, 60);
        button.setMinSize(80, 60);
        button.setMaxSize(80, 60);
        button.setFocusTraversable(false);

        setOperationButtonStyle(button, CalculatorView.OPERATION_BUTTON_COLOR, false);

        return button;
    }

    private void addOperationButtons() {
        String[] operations = {"+", "-", "×", "÷"};
        Operation[] operationEnums = {Operation.PLUS, Operation.MINUS, Operation.MULTIPLY, Operation.DIVIDE};

        for (int i = 0; i < operations.length; i++) {
            Button button = createOperatorButton(operations[i]);
            int finalI = i;
            button.setOnAction(e -> {
                controller.handleOperation(finalI);
                gridPane.requestFocus();
            });
            gridPane.add(button, 3, i + 2);

            // Store the button reference
            operationButtons.put(operationEnums[i], button);
        }
    }

    private void addSpecialButtons() {
        Button zeroButton = createButton("0", NUMBER_BUTTON_COLOR);
        zeroButton.setOnAction(e -> {
            controller.handleNumber(0);
            gridPane.requestFocus();
        });
        gridPane.add(zeroButton, 1, 6);

        Button clearButton = createButton("C", SPECIAL_BUTTON_COLOR);
        clearButton.setOnAction(e -> {
            controller.handleClear();
            gridPane.requestFocus();
        });
        gridPane.add(clearButton, 2, 1);

        Button equalButton = createButton("=", OPERATION_BUTTON_COLOR);
        equalButton.setOnAction(e -> {
            controller.handleEquals();
            gridPane.requestFocus();
        });
        gridPane.add(equalButton, 3, 6);

        addRemainingSpecialButtons();
    }

    private void addRemainingSpecialButtons() {
        Button decimalButton = createButton(".", NUMBER_BUTTON_COLOR);
        decimalButton.setOnAction(e -> {
            controller.handleDecimal();
            gridPane.requestFocus();
        });
        gridPane.add(decimalButton, 2, 6);

        Button plusMinusButton = createButton("±", SPECIAL_BUTTON_COLOR);
        plusMinusButton.setOnAction(e -> {
            controller.handlePlusMinus();
            gridPane.requestFocus();
        });
        gridPane.add(plusMinusButton, 0, 6);

        Button oneOverXButton = createButton("1/x", SPECIAL_BUTTON_COLOR);
        oneOverXButton.setOnAction(e -> {
            controller.handleOneOverX();
            gridPane.requestFocus();
        });
        gridPane.add(oneOverXButton, 0, 2);

        Button squaredButton = createButton("x²", SPECIAL_BUTTON_COLOR);
        squaredButton.setOnAction(e -> {
            controller.handleSquared();
            gridPane.requestFocus();
        });
        gridPane.add(squaredButton, 1, 2);

        Button squareRootButton = createButton("√", SPECIAL_BUTTON_COLOR);
        squareRootButton.setOnAction(e -> {
            controller.handleSquareRoot();
            gridPane.requestFocus();
        });
        gridPane.add(squareRootButton, 2, 2);

        Button clearEntryButton = createButton("CE", SPECIAL_BUTTON_COLOR);
        clearEntryButton.setOnAction(e -> {
            controller.handleClearEntry();
            gridPane.requestFocus();
        });
        gridPane.add(clearEntryButton, 1, 1);

        Button backSpaceButton = createButton("⌫", SPECIAL_BUTTON_COLOR);
        backSpaceButton.setOnAction(e -> {
            controller.handleBackSpace();
            gridPane.requestFocus();
        });
        gridPane.add(backSpaceButton, 3, 1);
    }

    private Button createButton(String text, Color backgroundColor) {
        logger.trace("Creating button with text: {}", text);
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);
        button.setMinSize(80, 60);
        button.setMaxSize(80, 60);
        button.setFocusTraversable(false);
        setOperationButtonStyle(button, backgroundColor, false);

        // Modern button styling
        button.setStyle(String.format("""
            -fx-background-color: rgb(%d, %d, %d);
            -fx-text-fill: white;
            -fx-font-family: 'SF Pro Display';
            -fx-font-size: 18;
            -fx-background-radius: 30;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);
            """,
                (int)(backgroundColor.getRed() * 255),
                (int)(backgroundColor.getGreen() * 255),
                (int)(backgroundColor.getBlue() * 255)));

        // Hover effect
        button.setOnMouseEntered(e ->
                button.setStyle(String.format("""
                -fx-background-color: rgb(%d, %d, %d);
                -fx-text-fill: white;
                -fx-font-family: 'SF Pro Display';
                -fx-font-size: 18;
                -fx-background-radius: 30;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 15, 0, 0, 0);
                """,
                        (int)(backgroundColor.getBrightness() * 1.2 * 255),
                        (int)(backgroundColor.getBrightness() * 1.2 * 255),
                        (int)(backgroundColor.getBrightness() * 1.2 * 255))));

        button.setOnMouseExited(e ->
                button.setStyle(String.format("""
                -fx-background-color: rgb(%d, %d, %d);
                -fx-text-fill: white;
                -fx-font-family: 'SF Pro Display';
                -fx-font-size: 18;
                -fx-background-radius: 30;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);
                """,
                        (int)(backgroundColor.getRed() * 255),
                        (int)(backgroundColor.getGreen() * 255),
                        (int)(backgroundColor.getBlue() * 255))));

        return button;
    }

    public GridPane getView() {
        return gridPane;
    }
}

