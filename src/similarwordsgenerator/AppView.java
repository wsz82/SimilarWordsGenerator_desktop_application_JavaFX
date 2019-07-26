package similarwordsgenerator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppView {

    private final Generator gn;

    private CheckBox sorted;
    private CheckBox firstChar;
    private CheckBox lastChar;
    private TextField numberOfWords;
    private TextField minWordLength;
    private TextField maxWordLength;
    private TextField levelOfCompression;
    private Label levelOfCompressionLabel;
    private Button saveRatiosButton;
    private Button saveWordsButton;
    private Button compressButton;

    private String path = null;
    private List<String> output = new ArrayList<>();
    private List<String> input = new ArrayList<>();

    public AppView(Generator gn) {
        this.gn = gn;
    }

    public void init (Stage primaryStage, Parameters initParameters, File userHomeProgram, ISaver saver, SaverWords saverWords, String mementoName, List<String> wordsToSave) {

        Group root = new Group();
        Scene scene = new Scene(root, 650, 600);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Similar Words Generator");

        final FileChooser fcLoad = new FileChooser();
        fcLoad.setInitialDirectory(userHomeProgram);
        final FileChooser fcSaveRatios = new FileChooser();
        fcSaveRatios.setInitialDirectory(userHomeProgram);
        final FileChooser fcSaveWords = new FileChooser();
        fcSaveWords.setInitialDirectory(userHomeProgram);

        final Button loadButton = new Button("Load");
        final Button generateButton = new Button("Generate");
        saveRatiosButton = new Button("Save ratios");
        saveRatiosButton.setDisable(true);
        saveWordsButton = new Button("Save words");
        saveWordsButton.setDisable(true);
        compressButton = new Button("Compress");
        compressButton.setDisable(true);

        final ChoiceBox<String> loadChoiceBox = new ChoiceBox<>();
        loadChoiceBox.setMinWidth(100);
        loadChoiceBox.setMaxWidth(100);

        sorted = new CheckBox("Sort words");
        sorted.setSelected(initParameters.isSorted());
        firstChar = new CheckBox("First char as in input");
        firstChar.setSelected(initParameters.isFirstCharAsInInput());
        lastChar = new CheckBox("Last char as in input");
        lastChar.setSelected(initParameters.isLastCharAsInInput());

        numberOfWords = new TextField(Integer.toString(initParameters.getNumberOfWords()));
        final Label numberOfWordsLabel = new Label("Number of words:");
        minWordLength = minMaxTextFields(initParameters.getMinWordLength());
        final Label minWordLengthLabel = new Label("Min. word length:");
        maxWordLength = minMaxTextFields(initParameters.getMaxWordLength());
        final Label maxWordLengthLabel = new Label("Max. word length:");
        levelOfCompression = new TextField();
        levelOfCompression.setDisable(true);
        levelOfCompressionLabel = new Label("Level of compression:");
        levelOfCompressionLabel.setDisable(true);

        final Label optionsLabel = new Label("Options");


        TextArea inputArea = new TextArea();
        inputArea.setPrefSize(150,500);
        final Label inputManualLabel = new Label("Input");

        TextArea outputArea = new TextArea();
        outputArea.setPrefSize(150,500);
        outputArea.setEditable(false);
        final Label outputLabel = new Label("Output");


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

        primaryStage.setOnCloseRequest(event -> {
            Parameters parametersToSave = settingParameters();
            new Memento(parametersToSave, this.output, userHomeProgram, mementoName);
        });

        if (initParameters.getInput() != null && !initParameters.getInput().isEmpty()) {

            for (String word : initParameters.getInput()) {
                inputArea.setText(inputArea.getText() + (word + "\n"));
            }

            input = initParameters.getInput();
        }

        if (wordsToSave != null && !wordsToSave.isEmpty()) {
            for (String word : wordsToSave) {
                outputArea.setText(outputArea.getText() + (word + "\n"));
            }

            saveWordsButton.setDisable(false);
        }

        if (initParameters.getPath() != null) {

            boolean fileExists = new File (initParameters.getPath()).exists();

            if (fileExists) {

                inputArea.setText(new File(initParameters.getPath()).getName());
                path = initParameters.getPath();

                settingOptionsDisibility(false);
            }
        }

        loadChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                path = userHomeProgram + File.separator + newValue;
                input = Collections.emptyList();
                inputArea.setText(newValue);
                inputArea.setEditable(false);

            }
        });

        loadChoiceBox.setOnMouseEntered(event -> {

            List<String> ratiosFiles = new ArrayList<>();

            try (
                    Stream<Path> walk = Files.walk(Paths.get(userHomeProgram.getPath()))

                    ) {

                ratiosFiles = walk
                        .map(e -> e.getFileName().toString())
                        .filter(e -> e.endsWith(".bin"))
                        .collect(Collectors.toList());

            } catch (IOException e) {
                e.printStackTrace();
            }

            ObservableList<String> ratiosFilesOb = FXCollections.observableArrayList(ratiosFiles);

            if (loadChoiceBox.getItems().isEmpty()) {
                loadChoiceBox.setItems(ratiosFilesOb);
            }
            else {
                loadChoiceBox.setOnMouseClicked(nextEvent -> loadChoiceBox.setItems(ratiosFilesOb));
            }
        });

        loadButton.setOnAction(f -> {
            File file = fcLoad.showOpenDialog(primaryStage);

            if (file != null) {
                path = file.getPath();
                inputArea.setText(file.getName());
                inputArea.setEditable(false);
                input = Collections.emptyList();
                loadChoiceBox.setValue(null);
            }
        });

        inputArea.setTextFormatter(new TextFormatter<>(f -> {
            if (inputArea.getText().endsWith("\n\n")) {

                f.setText("");
            }
            return f;
        }));

        inputArea.setOnMouseClicked(mouseEvent -> {
            if ( !inputArea.getText().isEmpty() && path != null && inputArea.getText().equals(new File(path).getName()) && mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {

                path = null;
                input = Collections.emptyList();

                inputArea.setText("");
                inputArea.setEditable(true);
                loadChoiceBox.setValue(null);

            }
        });

        inputArea.textProperty().addListener((ov, s, t) -> {
            if (!t.isEmpty()) {

                if (path == null) {
                    input = Arrays.asList(t.split("\n"));
                }

                settingOptionsDisibility(false);

            } else {

                path = null;
                input = Collections.emptyList();

                settingOptionsDisibility(true);
            }

        });

        outputArea.textProperty().addListener((observableValue, s, t1) -> {
            if (!outputArea.getText().isEmpty()) {
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
                saverWords.save(this.output, file.getPath());
            }
        });

        generateButton.setOnAction(f -> {

            Parameters parameters = settingParameters();

            try {

                //Clearing output for new words
                this.output.removeAll(this.output);

                try {
                    //Generating new words
                    this.output.addAll(gn.generate(parameters));
                } catch (NullPointerException en) {
                    //If no file is loaded so program asks us to load a file
                    loadButton.fire();
                }

                //Emptying output area for new words
                outputArea.setText("");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                //Error message if input is wrong
                errorInput();
            }

            for (String word : this.output) {
                outputArea.setText(outputArea.getText() + (word + "\n"));
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

        sorted.selectedProperty().addListener((ov, old_val, new_val) -> {

            Set<String> words;
            List<String> wordsFromOutput = Arrays.asList(outputArea.getText().split("\n"));
            outputArea.setText("");

            if (new_val) {
                words = new TreeSet<>(wordsFromOutput);
            } else {
                words = new HashSet<>(wordsFromOutput);
            }

            output = new ArrayList<>(words);

            for (String word : words) {
                outputArea.setText(outputArea.getText() + (word + "\n"));
            }
        });

        final HBox loadBox = new HBox();
        loadBox.getChildren().addAll(loadButton, loadChoiceBox);
        loadBox.setSpacing(12);

        final GridPane options = new GridPane();
        GridPane.setConstraints(loadBox, 0,0);
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
        options.getChildren().addAll(loadBox, generateButton, saveRatiosButton, sorted, firstChar, lastChar, numberOfWords, numberOfWordsLabel, minWordLength, minWordLengthLabel, maxWordLength, maxWordLengthLabel, saveWordsButton, levelOfCompressionLabel, levelOfCompression, compressButton);

        final VBox optionsPane = new VBox(12);
        optionsPane.getChildren().addAll(optionsLabel, options);
        optionsPane.setAlignment(Pos.TOP_CENTER);
        optionsPane.setPadding(new Insets(12, 12, 12, 12));

        final VBox inputManualPane = new VBox(12);
        inputManualPane.getChildren().addAll(inputManualLabel, inputArea);
        inputManualPane.setAlignment(Pos.TOP_CENTER);
        inputManualPane.setPadding(new Insets(12, 12, 12, 12));

        final VBox outputPane = new VBox(12);
        outputPane.getChildren().addAll(outputLabel, outputArea);
        outputPane.setAlignment(Pos.TOP_CENTER);
        outputPane.setPadding(new Insets(12, 12, 12, 12));

        final Pane hp = new HBox(12);
        hp.getChildren().addAll(optionsPane, inputManualPane, outputPane);
        hp.setPadding(new Insets(12, 12, 12, 12));

        root.getChildren().addAll(hp);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void errorInput() {
        Toolkit.getDefaultToolkit().beep();

        Stage errorStage = new Stage();
        errorStage.setResizable(false);
        errorStage.setAlwaysOnTop(true);
        errorStage.initModality(Modality.APPLICATION_MODAL);
        errorStage.initStyle(StageStyle.DECORATED);
        errorStage.setTitle("Input error");

        VBox errorPane = new VBox();

        Scene errorScene = new Scene(errorPane, 300, 100);

        Text errorText = new Text("Wrong input data format!");

        errorPane.getChildren().add(errorText);
        errorPane.setAlignment(Pos.CENTER);
        errorPane.setPadding(new Insets(12, 12, 12, 12));

        errorStage.setScene(errorScene);
        errorStage.show();
    }

    private void settingOptionsDisibility(boolean boo) {

        saveRatiosButton.setDisable(boo);
        levelOfCompression.setDisable(boo);
        levelOfCompressionLabel.setDisable(boo);
        compressButton.setDisable(boo);
    }

    private Parameters settingParameters() {

        Parameters.Builder parametersBuilder = new Parameters.Builder();

        parametersBuilder.setNumberOfWords(Integer.parseInt(numberOfWords.getText()));
        try {
            parametersBuilder.setMinWordLength(Integer.parseInt(minWordLength.getText()));
        } catch (NumberFormatException e) {
            parametersBuilder.setMinWordLength(0);
        }
        try {
            parametersBuilder.setMaxWordLength(Integer.parseInt(maxWordLength.getText()));
        } catch (NumberFormatException e) {
            parametersBuilder.setMaxWordLength(0);
        }
        parametersBuilder.setFirstCharAsInInput(firstChar.isSelected());
        parametersBuilder.setLastCharAsInInput(lastChar.isSelected());
        parametersBuilder.setSorted(sorted.isSelected());
        parametersBuilder.setPath(path);
        parametersBuilder.setInput(input);

        return parametersBuilder.build();
    }

    private TextField minMaxTextFields (int number) {
        if (number == 0) {
            return new TextField();
        } else {
            return new TextField(Integer.toString(number));
        }
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
