package similarwordsgenerator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Generator gn = new Generator();
    private String fileName;
    private ISaver saver = new SaverBIN();
    private SaverWords saverWords = new SaverWords();

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Similar Words Generator");

        final FileChooser fcLoad = new FileChooser();
        final FileChooser fcSaveRatios = new FileChooser();
        final FileChooser fcSaveWords = new FileChooser();

        final Button loadButton = new Button("Load");
        final Button generateButton = new Button("Generate");
        final Button saveRatiosButton = new Button("Save ratios");
        saveRatiosButton.setDisable(true);
        final Button saveWordsButton = new Button("Save words");
        saveWordsButton.setDisable(true);
        final Button compressButton = new Button("Compress");
        compressButton.setDisable(true);

        final CheckBox sorted = new CheckBox("Sort words");
        sorted.setSelected(true);
        final CheckBox firstChar = new CheckBox("First char as in input");
        firstChar.setSelected(true);
        final CheckBox lastChar = new CheckBox("Last char as in input");
        lastChar.setSelected(true);

        final TextField numberOfWords = new TextField("1");
        final Label numberOfWordsLabel = new Label("Number of words:");
        final TextField minWordLength = new TextField();
        final Label minWordLengthLabel = new Label("Min. word length:");
        final TextField maxWordLength = new TextField();
        final Label maxWordLengthLabel = new Label("Max. word length:");
        final TextField levelOfCompression = new TextField();
        levelOfCompression.setDisable(true);
        final Label levelOfCompressionLabel = new Label("Level of compression:");
        levelOfCompressionLabel.setDisable(true);

        final Label optionsLabel = new Label("Options");


        TextArea inputManual = new TextArea();
        inputManual.setPrefSize(150,500);
        final Label inputManualLabel = new Label("Input");

        TextArea output = new TextArea();
        output.setPrefSize(150,500);
        output.setEditable(false);
        final Label outputLabel = new Label("Output");

        List<String> wordsToSave = new ArrayList<>();


        numberOfWords.setMaxWidth(50);
        numberOfWords.setTextFormatter(new TextFormatter<>(this::filterForNumbersOfWords));

        minWordLength.setMaxWidth(50);
        minWordLength.setTextFormatter(new TextFormatter<>(f -> filterForMinWordLength(maxWordLength, f)));

        maxWordLength.setMaxWidth(50);
        maxWordLength.setTextFormatter(new TextFormatter<>(this::filterForMaxWordLength));
        maxWordLength.focusedProperty().addListener((ov, old_val, new_val) -> {
                    try {
                        if (!new_val && !minWordLength.getText().isEmpty() && (Integer.parseInt(maxWordLength.getText()) < Integer.parseInt(minWordLength.getText()))) {
                            maxWordLength.setText("");
                        }
                    } catch (NumberFormatException e) {
                        maxWordLength.setText("");
                    }
                }
        );

        levelOfCompression.setMaxWidth(50);
        levelOfCompression.setTextFormatter(new TextFormatter<>(this::filterForLevelOfCompression));

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

        loadButton.setOnAction(f -> {
            File file = fcLoad.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    fileName = file.getName();
                    gn = new Generator(file.getPath());

                    inputManual.setText(file.getName());
                    inputManual.setEditable(false);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        inputManual.setTextFormatter(new TextFormatter<>(f -> {
            if (inputManual.getText().endsWith("\n\n")) {

                f.setText("");
            }
            return f;
        }));

        inputManual.setOnMouseClicked(mouseEvent -> {
            if (inputManual.getText().equals(fileName) && mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {

                inputManual.setText("");
                inputManual.setEditable(true);

                gn.setAnalyser(null);
            }
        });

        inputManual.textProperty().addListener((ov, s, t) -> {
            if (!t.isEmpty()) {

                saveRatiosButton.setDisable(false);
                levelOfCompression.setDisable(false);
                levelOfCompressionLabel.setDisable(false);
                compressButton.setDisable(false);

                if (!t.equals(fileName)) {

                    List<String> wordsToAnalyse = Arrays.asList(t.split("\n"));

                    gn = new Generator(wordsToAnalyse);
                }
            } else {

                gn.setAnalyser(null);

                saveRatiosButton.setDisable(true);
                compressButton.setDisable(true);
                levelOfCompression.setDisable(true);
                levelOfCompressionLabel.setDisable(true);
            }

        });

        output.textProperty().addListener((observableValue, s, t1) -> {
            if (!output.getText().isEmpty()) {
                saveWordsButton.setDisable(false);
            } else {
                saveWordsButton.setDisable(true);
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

        generateButton.setOnAction(f -> {
            try {
                output.setText("");

                gn.setNumberOfWords(Integer.parseInt(numberOfWords.getText()));
                try {
                    gn.setMinWordLength(Integer.parseInt(minWordLength.getText()));
                } catch (NumberFormatException e) {
                    gn.setMinWordLength(0);
                }
                try {
                    gn.setMaxWordLength(Integer.parseInt(maxWordLength.getText()));
                } catch (NumberFormatException e) {
                    gn.setMaxWordLength(0);
                }

                wordsToSave.removeAll(wordsToSave);
                wordsToSave.addAll(gn.generate());

                for (String word : wordsToSave) {
                    output.setText(output.getText() + (word + "\n"));
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

        sorted.selectedProperty().addListener((ov, old_val, new_val) ->
                gn.setSorted(new_val)
        );

        firstChar.selectedProperty().addListener((ov, old_val, new_val) ->
                gn.setFirstCharAsInInput(new_val)
        );

        lastChar.selectedProperty().addListener((ov, old_val, new_val) ->
                gn.setLastCharAsInInput(new_val)
        );

        final GridPane options = new GridPane();
        GridPane.setConstraints(loadButton, 0,0);
        GridPane.setConstraints(generateButton, 0,1);
        GridPane.setConstraints(sorted, 0,2);
        GridPane.setConstraints(firstChar, 0,3);
        GridPane.setConstraints(lastChar, 0,4);
        GridPane.setConstraints(numberOfWordsLabel, 0,5);
        GridPane.setConstraints(numberOfWords, 1,5);
        GridPane.setConstraints(minWordLengthLabel, 0,6);
        GridPane.setConstraints(minWordLength, 1,6);
        GridPane.setConstraints(maxWordLengthLabel, 0,7);
        GridPane.setConstraints(maxWordLength, 1,7);
        GridPane.setConstraints(levelOfCompressionLabel, 0,8);
        GridPane.setConstraints(levelOfCompression, 1,8);
        GridPane.setConstraints(compressButton, 0,9);
        GridPane.setConstraints(saveRatiosButton, 0,10);
        GridPane.setConstraints(saveWordsButton, 0,11);
        options.setHgap(6);
        options.setVgap(6);
        options.getChildren().addAll(loadButton, generateButton, saveRatiosButton, sorted, firstChar, lastChar, numberOfWords, numberOfWordsLabel, minWordLength, minWordLengthLabel, maxWordLength, maxWordLengthLabel, saveWordsButton, levelOfCompressionLabel, levelOfCompression, compressButton);

        final VBox optionsPane = new VBox(12);
        optionsPane.getChildren().addAll(optionsLabel, options);
        optionsPane.setAlignment(Pos.TOP_CENTER);
        optionsPane.setPadding(new Insets(12, 12, 12, 12));

        final VBox inputManualPane = new VBox(12);
        inputManualPane.getChildren().addAll(inputManualLabel, inputManual);
        inputManualPane.setAlignment(Pos.TOP_CENTER);
        inputManualPane.setPadding(new Insets(12, 12, 12, 12));

        final VBox outputPane = new VBox(12);
        outputPane.getChildren().addAll(outputLabel, output);
        outputPane.setAlignment(Pos.TOP_CENTER);
        outputPane.setPadding(new Insets(12, 12, 12, 12));

        final Pane hp = new HBox(12);
        hp.getChildren().addAll(optionsPane, inputManualPane, outputPane);
        hp.setPadding(new Insets(12, 12, 12, 12));

        root.getChildren().addAll(hp);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private TextFormatter.Change filterForLevelOfCompression(TextFormatter.Change f) {
        try {
            int input = Integer.parseInt(f.getControlNewText());
            if ( input < 1 ) {
                f.setText("");
            }
        } catch (NumberFormatException e) {
            f.setText("");
        }
        if (f.getControlNewText().isEmpty()) {
            f.setText("");
        }
        return f;
    }

    private TextFormatter.Change filterForMaxWordLength(TextFormatter.Change f) {
        try {
            int input = Integer.parseInt(f.getControlNewText());
            if ( input < 1 ) {
                f.setText("");
            }
        } catch (NumberFormatException e) {
            f.setText("");
        }
        return f;
    }

    private TextFormatter.Change filterForMinWordLength(TextField maxWordLength, TextFormatter.Change f) {
        try {
            int input = Integer.parseInt(f.getControlNewText());
            if ( input < 1 ) {
                f.setText("");
            }
            if ( !maxWordLength.getText().isEmpty() && input > Integer.parseInt(maxWordLength.getText()) ) {
                f.setText("");
            }

        } catch (NumberFormatException e) {
            f.setText("");
        }
        return f;
    }

    private TextFormatter.Change filterForNumbersOfWords(TextFormatter.Change f) {
        try {
            int input = Integer.parseInt(f.getControlNewText());
            if ( input < 1 ) {
                f.setText("1");
            }
        } catch (NumberFormatException e) {
            f.setText("");
        }
        if (f.getControlNewText().isEmpty()) {
            f.setText("1");
        }
        return f;
    }
}
