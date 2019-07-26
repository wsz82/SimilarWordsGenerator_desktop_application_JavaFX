package similarwordsgenerator;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class AppMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private ISaver saver = new SaverBIN();
    private SaverWords saverWords = new SaverWords();

    private List<String> wordsToSave;

    @Override
    public void start(Stage primaryStage) {

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
            wordsToSave = memento.getWordsToSave();

        } else {
            parameters = new ProgramParameters.Builder().build();
        }

        Generator gn = new Generator();
        AppView view = new AppView(gn);
        view.init(primaryStage, parameters, userHomeProgram, saver, saverWords, mementoName, wordsToSave);
    }
}
