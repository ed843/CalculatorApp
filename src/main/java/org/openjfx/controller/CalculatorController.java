package org.openjfx.controller;

import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openjfx.enums.Operation;
import org.openjfx.model.CalculatorModel;

import java.text.ParseException;

public class CalculatorController {
    protected static final Logger logger = LogManager.getLogger();
    private final CalculatorModel model;
    private final Label displayLabel;

    public CalculatorController(CalculatorModel model, Label displayLabel) {
        logger.info("Initializing CalculatorController");
        this.model = model;
        this.displayLabel = displayLabel;
    }

    public void handleNumber(int number) {
        logger.debug("Handling number input: {}", number);
        if (!model.isResetFlag()) {
            model.setDisplayText(model.getDisplayText() + model.getFormatter().format(number));
        } else {
            model.setDisplayText(model.getFormatter().format(number));
            model.setResetFlag(false);
        }
        updateDisplay();
    }

    public void handleOperation(int operationIndex) {
        logger.debug("Handling operation with index: {}", operationIndex);
        if (!model.isResetFlag()) {
            try {
                model.setOperand(model.getDisplayText());
                if (model.getCurrentOperation() != Operation.NONE) {
                    String result = model.calculate();
                    model.setDisplayText(result);
                }
                model.setCurrentOperation(Operation.values()[operationIndex]);
                model.setResetFlag(true);
                updateDisplay();
            } catch (ParseException e) {
                logger.error("Error handling operation: {}", operationIndex, e);
                handleError(e);
            }
        } else {
            model.setCurrentOperation(Operation.values()[operationIndex]);
        }
    }

    public void handleEquals() {
        logger.debug("Handling equals operation");
        if (!model.isResetFlag()) {
            try {
                model.setOperand(model.getDisplayText());
                String result = model.calculate();
                model.setDisplayText(result);
                updateDisplay();
                model.setResetFlag(true);
                logger.info("Equals operation completed. Result: {}", result);
            } catch (ParseException e) {
                logger.error("Error handling equals operation", e);
                handleError(e);
            }
        }
    }

    public void handleClear() {
        model.clear();
        updateDisplay();
    }

    public void handleDecimal() {
        logger.debug("Handling decimal point input");
        if (model.getDisplayText().contains(".") && !model.isResetFlag()) {
            model.setDisplayText(model.getDisplayText().substring(0,
                    model.getDisplayText().indexOf(".") + 1));
        } else if (!model.isResetFlag()) {
            model.setDisplayText(model.getDisplayText() + ".");
        } else {
            model.setDisplayText("0.");
            model.setResetFlag(false);
        }
        updateDisplay();
    }

    public void handlePlusMinus() {
        try {
            model.toggleSign();
            updateDisplay();
        } catch (ParseException e) {
            handleError(e);
        }
    }

    public void handleSquared() {
        try {
            model.squareValue(model.getDisplayText());
            updateDisplay();
        } catch (ParseException e) {
            handleError(e);
        }
    }

    public void handleSquareRoot() {
        try {
            model.squareRootValue(model.getDisplayText());
            updateDisplay();
        } catch (ParseException e) {
            handleError(e);
        }
    }

    private void updateDisplay() {
        try {
            String formattedValue = model.getFormatter().format(
                    model.getFormatter().parse(model.getDisplayText()));
            displayLabel.setText(formattedValue);
            logger.trace("Display updated to: {}", formattedValue);
        } catch (ParseException e) {
            logger.error("Error updating display", e);
            handleError(e);
        }
    }

    private void handleError(Exception e) {
        logger.error("Calculator error occurred", e);
        displayLabel.setText("Error");
        model.clear();
    }
}