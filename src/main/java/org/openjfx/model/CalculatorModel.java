package org.openjfx.model;

import org.openjfx.enums.Operation;
import java.text.DecimalFormat;
import java.text.ParseException;

public class CalculatorModel {
    private final DecimalFormat formatter = new DecimalFormat("#,###.#######");
    private double leftOperand;
    private double rightOperand;
    private String displayText = "0";
    private boolean resetFlag = true;
    private Operation currentOperation = Operation.NONE;

    public String calculate() {
        double result = switch (currentOperation) {
            case PLUS -> leftOperand + rightOperand;
            case MINUS -> leftOperand - rightOperand;
            case MULTIPLY -> leftOperand * rightOperand;
            case DIVIDE -> leftOperand / rightOperand;
            case NONE -> leftOperand;
        };
        leftOperand = result;  // Store the result in leftOperand
        currentOperation = Operation.NONE;
        return formatter.format(result);
    }

    public void setOperand(String value) throws ParseException {
        double parsedValue = formatter.parse(value).doubleValue();
        if (currentOperation == Operation.NONE) {
            leftOperand = parsedValue;
        } else {
            rightOperand = parsedValue;
        }
    }

    public double getLeftOperand() {
        return leftOperand;
    }

    public void clear() {
        leftOperand = 0;
        rightOperand = 0;
        displayText = "0";
        resetFlag = true;
        currentOperation = Operation.NONE;
    }

    public void toggleSign() throws ParseException {
        displayText = displayText.startsWith("-") ?
                displayText.substring(1) :
                "-" + displayText;
        // Immediately update the operand when toggling sign
        setOperand(displayText);
    }

    // Getters and setters
    public String getDisplayText() { return displayText; }
    public void setDisplayText(String text) { this.displayText = text; }
    public boolean isResetFlag() { return resetFlag; }
    public void setResetFlag(boolean flag) { this.resetFlag = flag; }
    public Operation getCurrentOperation() { return currentOperation; }
    public void setCurrentOperation(Operation operation) { this.currentOperation = operation; }
    public DecimalFormat getFormatter() { return formatter; }
}
