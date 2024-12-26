package org.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openjfx.ui.CalculatorView;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        CalculatorView calculator = new CalculatorView();
        Scene scene = new Scene(calculator.getView(), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}