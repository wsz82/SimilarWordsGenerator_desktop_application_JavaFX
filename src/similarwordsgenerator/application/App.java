package similarwordsgenerator.application;

import io.github.wsz82.ProgramParameters;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private File programDir;
    private ProgramParameters parameters;
    private List<String> output = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        View view = new View();
        String mementoName = "memento";

        createLocationForFiles();
        loadParametersFromMemento(mementoName);
        view.init(primaryStage, parameters, programDir, mementoName, output);
    }

    private void loadParametersFromMemento(String mementoName) {
        boolean mementoExists = new File(programDir + File.separator + mementoName).exists();

        if (mementoExists) {
            Memento memento = Memento.loadMemento(programDir, mementoName);
            parameters = memento.getProgramParameters();
            output = memento.getOutput();
        } else {
            parameters = new ProgramParameters.Builder().build();
        }
    }

    private void createLocationForFiles() {
        String path = System.getProperty("user.home");
        try {
            path = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        new File(path).mkdir();
        programDir = new File(path);
    }
}
