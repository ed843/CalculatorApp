package org.openjfx;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openjfx.ui.CalculatorView;

public class App extends Application {
    private final Logger logger = LogManager.getLogger();

    @Override
    public void start(Stage stage) {
        logger.info("Starting calculator application");
        try {
            CalculatorView calculator = new CalculatorView();
            Scene scene = new Scene(calculator.getView(), 400, 600);
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("Calculator");
            stage.setMinWidth(400);
            stage.setMinHeight(600);
            stage.setScene(scene);
            stage.show();
            calculator.getView().requestFocus();
            logger.info("Calculator UI initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize calculator application", e);
            throw e;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}