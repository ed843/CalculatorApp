package org.openjfx.controller;

import javafx.scene.control.Label;
import org.openjfx.enums.Operation;
import org.openjfx.model.CalculatorModel;

import java.text.ParseException;

public class CalculatorController {
    private final CalculatorModel model;
    private final Label displayLabel;

    public CalculatorController(CalculatorModel model, Label displayLabel) {
        this.model = model;
        this.displayLabel = displayLabel;
    }

    public void handleNumber(int number) {
        if (!model.isResetFlag()) {
            model.setDisplayText(model.getDisplayText() + model.getFormatter().format(number));
        } else {
            model.setDisplayText(model.getFormatter().format(number));
            model.setResetFlag(false);
        }
        updateDisplay();
    }

    public void handleOperation(int operationIndex) {
        if (!model.isResetFlag()) {
            try {
                model.setOperand(model.getDisplayText());
                if (model.getCurrentOperation() != Operation.NONE) {
                    model.setDisplayText(model.calculate());
                    model.setOperand(model.getDisplayText()); // Store result as left operand
                }
                model.setCurrentOperation(Operation.values()[operationIndex]);
                model.setResetFlag(true);
                updateDisplay();
            } catch (ParseException e) {
                handleError(e);
            }
        } else {
            // Allow changing operation even when reset flag is true
            model.setCurrentOperation(Operation.values()[operationIndex]);
        }
    }

    public void handleEquals() {
        if (!model.isResetFlag()) {
            try {
                model.setOperand(model.getDisplayText());
                String result = model.calculate();
                model.setDisplayText(result);
                model.setOperand(result); // Store result as left operand
                updateDisplay();
                model.setResetFlag(true);
            } catch (ParseException e) {
                handleError(e);
            }
        }
    }

    public void handleClear() {
        model.clear();
        updateDisplay();
    }

    public void handleDecimal() {
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
        if (model.getDisplayText().charAt(0) != '-') {
            model.setDisplayText("-" + model.getDisplayText());
        } else {
            model.setDisplayText(model.getDisplayText().substring(1));
        }
        updateDisplay();
    }

    private void updateDisplay() {
        try {
            displayLabel.setText(model.getFormatter().format(
                    model.getFormatter().parse(model.getDisplayText())));
        } catch (ParseException e) {
            handleError(e);
        }
    }

    private void handleError(Exception e) {
        displayLabel.setText("Error");
        model.clear();
    }
}