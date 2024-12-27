package org.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
            Scene scene = new Scene(calculator.getView(), 640, 480);
            stage.setScene(scene);
            stage.show();
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