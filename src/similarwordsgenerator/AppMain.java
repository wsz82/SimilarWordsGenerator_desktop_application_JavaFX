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

        Directory dir = Directory.getInstance();
        File userHomeProgram = dir.getUserHomeProgram();

        similarwordsgenerator.Parameters parameters;
        boolean mementoExists = new File(userHomeProgram + File.separator + "memento.bin").exists();

        if (mementoExists) {

            Memento memento = Memento.loadMemento(userHomeProgram);
            parameters = memento.getParameters();
            wordsToSave = memento.getWordsToSave();

        } else {
            parameters = new similarwordsgenerator.Parameters.Builder().build();
        }

        Generator gn = new Generator();
        AppView view = new AppView(gn);
        view.init(primaryStage, parameters, userHomeProgram, saver, saverWords);
    }
}
