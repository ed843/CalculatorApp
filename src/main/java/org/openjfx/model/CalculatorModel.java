package org.openjfx.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openjfx.enums.Operation;
import java.text.DecimalFormat;
import java.text.ParseException;

public class CalculatorModel {
    private final Logger logger = LogManager.getLogger();
    private final DecimalFormat formatter = new DecimalFormat("#,###.#######");
    private double leftOperand;
    private double rightOperand;
    private String displayText = "0";
    private boolean resetFlag = true;
    private Operation currentOperation = Operation.NONE;

    public String calculate() {
        logger.debug("Calculating with operation: {}, left operand: {}, right operand: {}",
                currentOperation, leftOperand, rightOperand);

        double result = switch (currentOperation) {
            case PLUS -> leftOperand + rightOperand;
            case MINUS -> leftOperand - rightOperand;
            case MULTIPLY -> leftOperand * rightOperand;
            case DIVIDE -> leftOperand / rightOperand;
            case NONE -> leftOperand;
        };

        leftOperand = result;
        currentOperation = Operation.NONE;
        String formattedResult = formatter.format(result);
        logger.info("Calculation result: {}", formattedResult);
        return formattedResult;
    }

    public void setOperand(String value) throws ParseException {
        logger.debug("Attempting to set operand with value: {}", value);
        try {
            double parsedValue = formatter.parse(value).doubleValue();
            if (currentOperation == Operation.NONE) {
                leftOperand = parsedValue;
                logger.debug("Set left operand to: {}", leftOperand);
            } else {
                rightOperand = parsedValue;
                logger.debug("Set right operand to: {}", rightOperand);
            }
        } catch (ParseException e) {
            logger.error("Failed to parse operand value: {}", value, e);
            throw e;
        }
    }

    public double getLeftOperand() {
        return leftOperand;
    }

    public void clear() {
        logger.debug("Clearing calculator state");
        leftOperand = 0;
        rightOperand = 0;
        displayText = "0";
        resetFlag = true;
        currentOperation = Operation.NONE;
        logger.info("Calculator state reset to initial values");
    }

    public void clearEntry() {
        logger.debug("Clearing calculator entry");
        displayText = "0";
        resetFlag = true;
        logger.info("Calculator entry reset to initial values");
    }

    public void toggleSign() throws ParseException {
        logger.debug("Toggling sign for value: {}", displayText);
        displayText = displayText.startsWith("-") ?
                displayText.substring(1) :
                "-" + displayText;
        setOperand(displayText);
        logger.debug("Sign toggled, new value: {}", displayText);
    }

    public void reciprocalValue(String value) throws ParseException {
        logger.debug("calculating reciprocal for value: {}", displayText);
        try {
            double parsedValue = formatter.parse(value).doubleValue();
            String result = formatter.format(1 / parsedValue);
            setDisplayText(result);
            setOperand(result);
            logger.debug("Reciprocal value result: {}", result);
        } catch (ParseException e) {
            logger.error("Failed to parse reciprocal value: {}", value, e);
            throw e;
        }
    }

    public void squareValue(String value) throws ParseException {
        logger.debug("Calculating square of: {}", value);
        try {
            double parsedValue = formatter.parse(value).doubleValue();
            String result = formatter.format(parsedValue * parsedValue);
            setDisplayText(result);
            setOperand(result);
            logger.debug("Squared value result: {}", result);
        } catch (ParseException e) {
            logger.error("Failed to square value: {}", value, e);
            throw e;
        }
    }

    public void squareRootValue(String value) throws ParseException {
        logger.debug("Calculating square root of: {}", value);
        try {
            double parsedValue = formatter.parse(value).doubleValue();
            String result = formatter.format(Math.sqrt(parsedValue));
            setDisplayText(result);
            setOperand(result);
            logger.debug("Square root value result: {}", result);
        } catch (ParseException e) {
            logger.error("Failed to square root value: {}", value, e);
            throw e;
        }
    }
    // Getters and setters
    public String getDisplayText() { return displayText; }
    public void setDisplayText(String text) {
        logger.trace("Setting display text to: {}", text);
        this.displayText = text;
    }
    public boolean isResetFlag() { return resetFlag; }
    public void setResetFlag(boolean flag) { this.resetFlag = flag; }
    public Operation getCurrentOperation() { return currentOperation; }
    public void setCurrentOperation(Operation operation) {
        logger.debug("Setting operation to: {}", operation);
        this.currentOperation = operation;
    }
    public DecimalFormat getFormatter() { return formatter; }
}
