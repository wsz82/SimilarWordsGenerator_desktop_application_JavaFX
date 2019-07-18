package similarwordsgenerator;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Generator gn;
    private GeneratorParameters gp = new GeneratorParameters();

    private ISaver saver = new SaverBIN();
    private SaverWords saverWords = new SaverWords();

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        Scene scene = new Scene(root, 400, 600);
        primaryStage.setResizable(false);

        primaryStage.setTitle("Similar Words Generator");
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(400);

        final FileChooser fcLoad = new FileChooser();
        final FileChooser fcSaveRatios = new FileChooser();
        final FileChooser fcSaveWords = new FileChooser();

        final Button loadButton = new Button("Load");
        final Button generateButton = new Button("Generate");
        final Button saveRatiosButton = new Button("Save ratios");
        final Button saveWordsButton = new Button("Save words");
        saveWordsButton.setDisable(true);
        final Button compressButton = new Button("Compress");
        compressButton.setDisable(true);

        final CheckBox sorted = new CheckBox("Sort words");
        final CheckBox firstChar = new CheckBox("First char as in input");
        final CheckBox lastChar = new CheckBox("Last char as in input");

        final TextField numberOfWords = new TextField("1");
        final Label numberOfWordsLabel = new Label("Number of words:");
        final TextField minWordLength = new TextField("0");
        final Label minWordLengthLabel = new Label("Min. word length:");
        final TextField maxWordLength = new TextField("0");
        final Label maxWordLengthLabel = new Label("Max. word length:");
        final TextField levelOfCompression = new TextField();
        final Label levelOfCompressionLabel = new Label("Level of compression:");

        lastChar.setSelected(true);

        firstChar.setSelected(true);

        sorted.setSelected(true);

        TextArea listOfWords = new TextArea();
        listOfWords.setPrefSize(150,500);
        listOfWords.setEditable(false);

        List<String> wordsToSave = new ArrayList<>();

        numberOfWords.setMaxWidth(50);
        minWordLength.setMaxWidth(50);
        maxWordLength.setMaxWidth(50);
        levelOfCompression.setMaxWidth(50);

        fcSaveRatios.setTitle("Save ratios to a file");
        fcSaveRatios.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Ratios", "*.bin"));

        fcSaveWords.setTitle("Save words to a file");
        fcSaveWords.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text", "*.txt", "*.csv"));

        fcLoad.setTitle("Load a text or ratios file");
        fcLoad.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text", "*.txt", "*.csv"),
                new FileChooser.ExtensionFilter("Ratios", "*.bin"));

        loadButton.setOnAction(e -> {
            File file = fcLoad.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    gn = new Generator(file.getPath(), gp);
                    compressButton.setDisable(false);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        saveRatiosButton.setOnAction(e -> {
            File file = fcSaveRatios.showSaveDialog(primaryStage);
            if (file != null) saver.save(gn.getAnalyser(), file.getPath());
        });

        saveWordsButton.setOnAction(e -> {
            File file = fcSaveWords.showSaveDialog(primaryStage);
            if (file != null) {
                saverWords.save(wordsToSave, file.getPath());
            }
        });

        generateButton.setOnAction(e -> {
            try {
                listOfWords.setText("");

                gp.setNumberOfWords(Integer.parseInt(numberOfWords.getText()));
                gp.setMinWordLength(Integer.parseInt(minWordLength.getText()));
                gp.setMaxWordLength(Integer.parseInt(maxWordLength.getText()));

                wordsToSave.removeAll(wordsToSave);
                wordsToSave.addAll(gn.generate());
                saveWordsButton.setDisable(false);

                for (String word : wordsToSave) {
                    listOfWords.setText(listOfWords.getText() + (word + "\n"));
                }
            } catch (NullPointerException en) {
                loadButton.fire();
            }
        });

        compressButton.setOnAction(e -> {

            int levelOfCompressionValue = 0;

            try {
                levelOfCompressionValue = Integer.parseInt(levelOfCompression.getText());
            } catch (NumberFormatException en) {
                levelOfCompression.requestFocus();
            }

            if (levelOfCompressionValue > 0) {
                try {
                    gn.getAnalyser().compress(levelOfCompressionValue);
                } catch (NullPointerException en) {
                    loadButton.fire();
                }
            }

        });

        sorted.selectedProperty().addListener(
                (ObservableValue<? extends Boolean> ov,
                 Boolean old_val, Boolean new_val) ->
                    gp.setSorted(new_val)
        );

        firstChar.selectedProperty().addListener(
                (ObservableValue<? extends Boolean> ov,
                 Boolean old_val, Boolean new_val) ->
                        gp.setFirstCharAsInInput(new_val)
        );

        lastChar.selectedProperty().addListener(
                (ObservableValue<? extends Boolean> ov,
                 Boolean old_val, Boolean new_val) ->
                        gp.setLastCharAsInInput(new_val)
        );

        final GridPane gp = new GridPane();
        GridPane.setConstraints(loadButton, 0,0);
        GridPane.setConstraints(generateButton, 0,1);
        GridPane.setConstraints(saveRatiosButton, 0,2);
        GridPane.setConstraints(sorted, 0,3);
        GridPane.setConstraints(firstChar, 0,4);
        GridPane.setConstraints(lastChar, 0,5);
        GridPane.setConstraints(numberOfWordsLabel, 0,6);
        GridPane.setConstraints(numberOfWords, 1,6);
        GridPane.setConstraints(minWordLengthLabel, 0,7);
        GridPane.setConstraints(minWordLength, 1,7);
        GridPane.setConstraints(maxWordLengthLabel, 0,8);
        GridPane.setConstraints(maxWordLength, 1,8);
        GridPane.setConstraints(saveWordsButton, 0,9);
        GridPane.setConstraints(levelOfCompressionLabel, 0,10);
        GridPane.setConstraints(levelOfCompression, 1,10);
        GridPane.setConstraints(compressButton, 0,11);
        gp.setHgap(6);
        gp.setVgap(6);
        gp.getChildren().addAll(loadButton, generateButton, saveRatiosButton, sorted, firstChar, lastChar, numberOfWords, numberOfWordsLabel, minWordLength, minWordLengthLabel, maxWordLength, maxWordLengthLabel, saveWordsButton, levelOfCompressionLabel, levelOfCompression, compressButton);

        final Pane rg = new VBox(12);
        rg.getChildren().addAll(gp);
        rg.setPadding(new Insets(12, 12, 12, 12));

        final Pane hp = new HBox(12);
        hp.getChildren().addAll(rg, listOfWords);
        hp.setPadding(new Insets(12, 12, 12, 12));

        root.getChildren().addAll(hp);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
