package org.openjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.openjfx.enums.Operation;

import java.text.DecimalFormat;
import java.text.ParseException;

import static org.openjfx.enums.Operation.NONE;

/**
 * JavaFX App
 */
public class App extends Application {
    DecimalFormat formatter = new DecimalFormat("#,###.#######");
    double leftOperand;
    double rightOperand;
    String calculatorDisplayText = "0";
    boolean resetFlag = true; // calculator starts with reset flag true
    Operation curOperation = NONE;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        // create a blank grid pane
        var gridPane = new GridPane();

        // Set column constraints to make each column take up available space
        for (int i = 0; i < 4; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS);  // Allow the column to expand horizontally
            gridPane.getColumnConstraints().add(colConstraints);
        }


        // Set row constraints to make each row take up available space
        for (int i = 0; i < 6; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.ALWAYS);  // Allow the row to expand vertically
            gridPane.getRowConstraints().add(rowConstraints);
        }


        // add black background color to grid pane
        var backgroundFill = new BackgroundFill(Paint.valueOf("black"), CornerRadii.EMPTY, Insets.EMPTY);
        var background = new Background(backgroundFill);
        gridPane.setBackground(background);

        // add calculator display text to grid pane
        Font font = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 25);
        var label1 = new Label(calculatorDisplayText);  // This label will show the display text
        label1.setMaxWidth(Double.MAX_VALUE);
        label1.setMaxHeight(Double.MAX_VALUE);
        label1.setTextFill(Paint.valueOf("white"));
        label1.setAlignment(Pos.CENTER);
        label1.setFont(font);
        gridPane.add(label1, 0, 0, 3, 1);


        // add numpad with event handlers to grid pane
        for (int i = 1; i <= 9; i++) {
            var button = new Button(Integer.toString(i));
            button.setMaxWidth(Double.MAX_VALUE);  // Ensure the button expands to fill the cell
            button.setMaxHeight(Double.MAX_VALUE);
            int finalI = i;
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                if (!resetFlag) {
                    calculatorDisplayText += formatter.format(finalI);
                    try {
                        label1.setText(formatter.format(formatter.parse(calculatorDisplayText)));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    calculatorDisplayText = formatter.format(finalI);
                    try {
                        label1.setText(formatter.format(formatter.parse(calculatorDisplayText)));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    resetFlag = false;
                }
            });
            gridPane.add(button, (i - 1) % 3, ((i - 1) / 3) + 2); // Add button to the grid
        }

        // add zero button to the grid pane with event handler
        var zeroButton = new Button("0");
        zeroButton.setMaxWidth(Double.MAX_VALUE);  // Ensure the button expands to fill the cell
        zeroButton.setMaxHeight(Double.MAX_VALUE);
        zeroButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (!resetFlag) {
                calculatorDisplayText += "0";
                try {
                    label1.setText(formatter.format(formatter.parse(calculatorDisplayText)));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            } // if reset flag is enabled, do nothing
        });
        gridPane.add(zeroButton, 1, 5);

        // add decimal button
        var decimalButton = new Button(".");
        decimalButton.setMaxWidth(Double.MAX_VALUE);
        decimalButton.setMaxHeight(Double.MAX_VALUE);
        decimalButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (!resetFlag) {
                calculatorDisplayText += ".";
                label1.setText(calculatorDisplayText);
            } else {
                calculatorDisplayText = "0.";
                label1.setText(calculatorDisplayText);
                resetFlag = false;
            }
        });
        gridPane.add(decimalButton, 2, 5);

        // create clear button
        var clearButton = new Button("C");
        clearButton.setMaxWidth(Double.MAX_VALUE);
        clearButton.setMaxHeight(Double.MAX_VALUE);
        clearButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            calculatorDisplayText = "0";
            label1.setText(formatter.format(0));
            curOperation = NONE;
            leftOperand = 0;
            rightOperand = 0;
            resetFlag = true;
        });
        gridPane.add(clearButton, 2, 1);

        // create operation buttons
        String[] operations = new String[]{"+", "-", "*", "/"};
        for (int i = 0; i < operations.length; i++) {
            var button = new Button(operations[i]);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setMaxHeight(Double.MAX_VALUE);
            int finalI = i;
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                if (!resetFlag) {
                    if (curOperation != NONE) {
                        switch (curOperation) {
                            case PLUS:
                                leftOperand = leftOperand + Double.parseDouble(calculatorDisplayText);
                                calculatorDisplayText = formatter.format(leftOperand);
                                try {
                                    label1.setText(formatter.format(formatter.parse(calculatorDisplayText)));
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                curOperation = Operation.values()[finalI];
                                resetFlag = true;
                                break;
                            case MINUS:
                                leftOperand = leftOperand - Integer.parseInt(calculatorDisplayText);
                                calculatorDisplayText = formatter.format(leftOperand);
                                try {
                                    label1.setText(formatter.format(formatter.parse(calculatorDisplayText)));
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                curOperation = Operation.values()[finalI];
                                resetFlag = true;
                                break;
                            case MULTIPLY:
                                leftOperand = leftOperand * Double.parseDouble(calculatorDisplayText);
                                calculatorDisplayText = formatter.format(leftOperand);
                                try {
                                    label1.setText(formatter.format(formatter.parse(calculatorDisplayText)));
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                curOperation = Operation.values()[finalI];
                                resetFlag = true;
                                break;
                            case DIVIDE:
                                leftOperand = leftOperand / Double.parseDouble(calculatorDisplayText);
                                calculatorDisplayText = formatter.format(leftOperand);
                                try {
                                    label1.setText(formatter.format(formatter.parse(calculatorDisplayText)));
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                curOperation = Operation.values()[finalI];
                                resetFlag = true;
                                break;
                        }

                    } else {
                        curOperation = Operation.values()[finalI];
                        leftOperand = Double.parseDouble(calculatorDisplayText);
                        calculatorDisplayText = "0";
                        label1.setText(formatter.format(Double.parseDouble(calculatorDisplayText)));
                        resetFlag = true;
                    }
                }
            });
            gridPane.add(button, 3, i + 1);
        }
        // add equal button to grid pane
        var equalSign = new Button("=");
        equalSign.setMaxWidth(Double.MAX_VALUE);
        equalSign.setMaxHeight(Double.MAX_VALUE);
        equalSign.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (!resetFlag) {
                rightOperand = Double.parseDouble(calculatorDisplayText);
                switch (curOperation) {
                    case PLUS:
                        calculatorDisplayText = formatter.format(leftOperand + rightOperand);
                        try {
                            label1.setText(formatter.format(formatter.parse(calculatorDisplayText)));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        curOperation = NONE;
                        break;
                    case MINUS:
                        calculatorDisplayText = formatter.format(leftOperand - rightOperand);
                        try {
                            label1.setText(formatter.format(formatter.parse(calculatorDisplayText)));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        curOperation = NONE;
                        break;
                    case MULTIPLY:
                        calculatorDisplayText = formatter.format(leftOperand * rightOperand);
                        try {
                            label1.setText(formatter.format(formatter.parse(calculatorDisplayText)));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        curOperation = NONE;
                        break;
                    case DIVIDE:
                        calculatorDisplayText = formatter.format(leftOperand / rightOperand);
                        try {
                            label1.setText(formatter.format(formatter.parse(calculatorDisplayText)));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        curOperation = NONE;
                        break;
                    case NONE:
                        try {
                            label1.setText(formatter.format(formatter.parse(calculatorDisplayText)));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        resetFlag = true;
                        break;

                }
            }
        });
        gridPane.add(equalSign, 3, 5);


        // prepare the scene to show
        var scene = new Scene(new StackPane(gridPane), 640, 480);
        stage.setScene(scene);

        // show scene
        stage.show();
    }

}