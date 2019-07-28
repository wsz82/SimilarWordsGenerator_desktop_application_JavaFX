package similarwordsgenerator.application;

import javafx.application.Application;
import javafx.stage.Stage;
import similarwordsgenerator.model.Controller;
import similarwordsgenerator.model.IController;
import similarwordsgenerator.model.ProgramParameters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        IController controller = new Controller();
        View view = new View();

        List<String> output = new ArrayList<>();
        File userHomeProgram;
        String path = System.getProperty("user.home") + File.separator + ".similarwordsgenerator";
        new File(path).mkdir();
        userHomeProgram = new File(path);

        String mementoName = "memento";

        ProgramParameters parameters;
        boolean mementoExists = new File(userHomeProgram + File.separator + mementoName).exists();

        if (mementoExists) {

            Memento memento = Memento.loadMemento(userHomeProgram, mementoName);
            parameters = memento.getProgramParameters();
            output = memento.getOutput();

        } else {
            parameters = new ProgramParameters.Builder().build();
        }

        view.init(controller, primaryStage, parameters, userHomeProgram, mementoName, output);
    }
}
