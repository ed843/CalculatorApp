package org.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.openjfx.ui.CalculatorView;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        CalculatorView calculatorView = new CalculatorView(stage);
        Scene scene = new Scene(calculatorView.getRoot(), 410, 610);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}