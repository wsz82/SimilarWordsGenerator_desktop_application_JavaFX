package similarwordsgenerator.application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

class InputErrorMessage {
    static void show() {
        Toolkit.getDefaultToolkit().beep();

        Stage errorStage = new Stage();
        errorStage.setResizable(false);
        errorStage.setAlwaysOnTop(true);
        errorStage.initModality(Modality.APPLICATION_MODAL);
        errorStage.initStyle(StageStyle.DECORATED);
        errorStage.setTitle("Input error");

        VBox errorPane = new VBox();
        Scene errorScene = new Scene(errorPane, 300, 100);
        Text errorText = new Text("Wrong input!");

        errorPane.getChildren().add(errorText);
        errorPane.setAlignment(Pos.CENTER);
        errorPane.setPadding(new Insets(12, 12, 12, 12));

        errorStage.setScene(errorScene);
        errorStage.show();
    }
}
