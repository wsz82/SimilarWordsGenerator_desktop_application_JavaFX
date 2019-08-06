package similarwordsgenerator.application;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import similarwordsgenerator.model.Controller;
import similarwordsgenerator.model.ProgramParameters;

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

class View {
    private Controller controller = new Controller();

    private TextArea inputArea = new TextArea();
    private TextArea outputArea = new TextArea();
    private CheckBox sorted = new CheckBox("Sort words");
    private CheckBox firstChar = new CheckBox("First sign as in input");
    private CheckBox lastChar = new CheckBox("Last sign as in input");
    private TextField numberOfWords;
    private TextField minWordLength;
    private TextField maxWordLength;
    private TextField levelOfCompression = new TextField();
    private Label levelOfCompressionLabel = new Label("Level of compression:");
    private Button loadButton = new Button("Load");
    private Button generateButton = new Button("Generate");
    private Button saveSeedButton = new Button("Save seed");
    private Button exportWordsButton = new Button("Export words");
    private Button compressButton = new Button("Compress");
    private ChoiceBox<String> loadChoiceBox = new ChoiceBox<>();

    private String path = null;
    private List<String> output = new ArrayList<>();
    private List<String> input = new ArrayList<>();
    private boolean compressed;

    void init (Stage primaryStage, ProgramParameters initProgramParameters, File userHomeProgram, String mementoName, List<String> output) {
        Group root = new Group();
        Scene scene = new Scene(root, 650, 600);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Similar Words Generator");

        inputArea.setPromptText("Please choose a file with words in .txt or .csv format separated by newline. Eventually choose a seed (.bin created in this program)");
        inputArea.setPrefSize(150,500);
        Label inputManualLabel = new Label("Input");
        outputArea.setPrefSize(150,500);
        outputArea.setEditable(false);
        Label outputLabel = new Label("Output");

        Label optionsLabel = new Label("Options");
        sorted.setSelected(initProgramParameters.isSorted());
        firstChar.setSelected(initProgramParameters.isFirstCharAsInInput());
        lastChar.setSelected(initProgramParameters.isLastCharAsInInput());
        numberOfWords = new TextField(Integer.toString(initProgramParameters.getNumberOfWords()));
        Label numberOfWordsLabel = new Label("Number of words:");
        minWordLength = getMinMaxTextFields(initProgramParameters.getMinWordLength());
        Label minWordLengthLabel = new Label("Min. word length:");
        maxWordLength = getMinMaxTextFields(initProgramParameters.getMaxWordLength());
        Label maxWordLengthLabel = new Label("Max. word length:");

        setPrimarySettings();

        loadChoiceBox.setMinWidth(100);
        loadChoiceBox.setMaxWidth(100);
        FileChooser fcLoad = new FileChooser();
        fcLoad.setInitialDirectory(userHomeProgram);
        FileChooser fcSaveSeed = new FileChooser();
        fcSaveSeed.setInitialDirectory(userHomeProgram);
        FileChooser fcExportWords = new FileChooser();
        fcExportWords.setInitialDirectory(userHomeProgram);

        numberOfWords.setMaxWidth(50);
        numberOfWords.setTextFormatter(new TextFormatter<>(this::filterForNumbersOfWords));
        minWordLength.setMaxWidth(50);
        minWordLength.setTextFormatter(new TextFormatter<>(change -> filterForMinWordLength(maxWordLength, change)));
        maxWordLength.setMaxWidth(50);
        maxWordLength.setTextFormatter(new TextFormatter<>(this::filterForMaxWordLength));
        levelOfCompression.setMaxWidth(50);
        levelOfCompression.setTextFormatter(new TextFormatter<>(this::filterForLevelOfCompression));

        fcSaveSeed.setTitle("Save seed to a file");
        fcSaveSeed.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Seed", "*.bin"));
        fcExportWords.setTitle("Save words to a file");
        fcExportWords.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text", "*.txt", "*.csv"));
        fcLoad.setTitle("Load a text or seed file");
        fcLoad.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text", "*.txt", "*.csv"),
                new FileChooser.ExtensionFilter("Seed", "*.bin"));

        checkMemento(initProgramParameters, output, controller);

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
        GridPane.setConstraints(saveSeedButton, 0,10);
        GridPane.setConstraints(exportWordsButton, 0,11);
        options.setHgap(6);
        options.setVgap(6);
        options.getChildren().addAll(loadBox, generateButton, saveSeedButton, sorted, firstChar, lastChar, numberOfWords, numberOfWordsLabel, minWordLength, minWordLengthLabel, maxWordLength, maxWordLengthLabel, exportWordsButton, levelOfCompressionLabel, levelOfCompression, compressButton);

        final VBox optionsPane = new VBox(12);
        optionsPane.getChildren().addAll(optionsLabel, options);
        optionsPane.setAlignment(Pos.TOP_CENTER);
        optionsPane.setPadding(new Insets(12, 12, 12, 12));

        final VBox inputAreaPane = new VBox(12);
        inputAreaPane.getChildren().addAll(inputManualLabel, inputArea);
        inputAreaPane.setAlignment(Pos.TOP_CENTER);
        inputAreaPane.setPadding(new Insets(12, 12, 12, 12));

        final VBox outputAreaPane = new VBox(12);
        outputAreaPane.getChildren().addAll(outputLabel, outputArea);
        outputAreaPane.setAlignment(Pos.TOP_CENTER);
        outputAreaPane.setPadding(new Insets(12, 12, 12, 12));

        final Pane mainPane = new HBox(12);
        mainPane.getChildren().addAll(optionsPane, inputAreaPane, outputAreaPane);
        mainPane.setPadding(new Insets(12, 12, 12, 12));

        root.getChildren().addAll(mainPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        inputArea.setTextFormatter(new TextFormatter<>(change -> {
            if (inputArea.getText().endsWith("\n\n")) {
                change.setText("");
            }
            return change;
        }));

        inputArea.setOnMouseClicked(mouseEvent -> {
            if (path != null) {
                clearPathFromInputArea(mouseEvent);
            }
        });

        inputArea.textProperty().addListener((observableValue, oldVal, newVal) -> {
            if (!newVal.isEmpty()) {
                if (path == null) {
                    provideInputManually(newVal);
                }
                setOptionsDisability(false);
            } else {
                clearInputPathAnalyser();
                setOptionsDisability(true);
            }
            checkIfToClearCompressedFlag(oldVal, newVal);
        });

        outputArea.textProperty().addListener((observableValue) -> {
            if (!outputArea.getText().isEmpty()) {
                exportWordsButton.setDisable(false);
            } else {
                exportWordsButton.setDisable(true);
            }
        });

        sorted.selectedProperty().addListener((observableValue, oldVal, newVal) -> {
            Set<String> words;
            List<String> wordsFromOutput = Arrays.asList(outputArea.getText().split("\n"));

            outputArea.setText("");
            if (newVal) {
                words = new TreeSet<>(wordsFromOutput);
            } else {
                words = new HashSet<>(wordsFromOutput);
            }
            this.output = new ArrayList<>(words);
            for (String word : words) {
                outputArea.setText(outputArea.getText() + (word + "\n"));
            }
        });

        maxWordLength.focusedProperty().addListener((ov) -> {
            try {
                clearMaxWordLengthFieldIfProvidedNumberIsBiggerThanInMinWordLength();
            } catch (NumberFormatException e) {
                maxWordLength.setText("");
            }
        });

        loadButton.setOnAction(actionEvent -> {
            File file = fcLoad.showOpenDialog(primaryStage);

            if (file != null) {
                clearInputPathAnalyser();
                path = file.getPath();
                inputArea.setText(file.getName());
                inputArea.setEditable(false);
                loadChoiceBox.setValue(null);
            }
        });

        generateButton.setOnAction(actionEvent -> {
            ProgramParameters programParameters = setParameters(controller);

            try {
                generateWords(programParameters);
            } catch (NullPointerException en) {
                showErrorInputMessage();
            }
            clearOutputArea();
            for (String word : this.output) {
                outputArea.setText(outputArea.getText() + (word + "\n"));
            }
        });

        saveSeedButton.setOnAction(actionEvent -> {
            ProgramParameters programParameters = setParameters(controller);
            File file = fcSaveSeed.showSaveDialog(primaryStage);
            if (file != null) {
                controller.save(file.getPath(), programParameters);
            }
        });

        exportWordsButton.setOnAction(actionEvent -> {
            File file = fcExportWords.showSaveDialog(primaryStage);
            if (file != null) {
                controller.export(this.output, file.getPath());
            }
        });

        compressButton.setOnAction(actionEvent -> {
            ProgramParameters programParameters = setParameters(controller);
            int levelOfCompressionValue = 0;

            try {
                levelOfCompressionValue = Integer.parseInt(levelOfCompression.getText());
            } catch (NumberFormatException en) {
                levelOfCompression.requestFocus();
            }
            if (levelOfCompressionValue > 0) {
                controller.compress(levelOfCompressionValue, programParameters);
                compressed = true;
                compressButton.setDisable(true);
            }
        });

        loadChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                clearInputPathAnalyser();
                path = userHomeProgram + File.separator + newValue;
                inputArea.setText(newValue);
                inputArea.setEditable(false);
            }
        });

        loadChoiceBox.setOnMouseEntered(mouseEvent -> {
            List<String> seedFiles = new ArrayList<>();

            try (
                    Stream<Path> walk = Files.walk(Paths.get(userHomeProgram.getPath()))
            ) {
                seedFiles = walk
                        .map(e -> e.getFileName().toString())
                        .filter(e -> e.endsWith(".bin"))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ObservableList<String> seedFilesOb = FXCollections.observableArrayList(seedFiles);

            if (loadChoiceBox.getItems().isEmpty()) {
                loadChoiceBox.setItems(seedFilesOb);
            }
            else {
                loadChoiceBox.setOnMouseClicked(nextEvent -> loadChoiceBox.setItems(seedFilesOb));
            }
        });

        primaryStage.setOnCloseRequest(windowEvent -> {
            ProgramParameters programParametersToSave = setParameters(controller);
            new Memento(programParametersToSave, this.output, userHomeProgram, mementoName);
        });
    }

    private void provideInputManually(String newVal) {
        input = Arrays.asList(newVal.split("\n"));
    }

    private void checkIfToClearCompressedFlag(String oldVal, String newVal) {
        if (!newVal.equals(oldVal)) {
            compressed = false;
            controller.setAnalyser(null);
        }
    }

    private void clearPathFromInputArea(MouseEvent mouseEvent) {
        boolean pathIsWrittenToInputArea = inputArea.getText().equals(new File(path).getName());
        boolean primaryMouseButtonIsClicked = mouseEvent.getButton().equals(MouseButton.PRIMARY);
        boolean buttonIsClickedTwice = mouseEvent.getClickCount() == 2;

        if (pathIsWrittenToInputArea && primaryMouseButtonIsClicked && buttonIsClickedTwice) {
            clearInputPathAnalyser();
            inputArea.setText("");
            inputArea.setEditable(true);
            loadChoiceBox.setValue(null);
        }
    }

    private void clearMaxWordLengthFieldIfProvidedNumberIsBiggerThanInMinWordLength() {
        if (!minWordLength.getText().isEmpty() && (Integer.parseInt(maxWordLength.getText()) < Integer.parseInt(minWordLength.getText()))) {
            maxWordLength.setText("");
        }
    }

    private void clearOutputArea() {
        if (path != null || (input != null && !input.isEmpty()) || controller.getAnalyser() != null) {
            outputArea.setText("");
        }
    }

    private void generateWords(ProgramParameters programParameters) {
        this.output.removeAll(this.output);
        if (compressed) {
            this.output.addAll(controller.generate(programParameters, Controller.GenerateSource.CURRENT_ANALYSER));
        } else {
            this.output.addAll(controller.generate(programParameters, Controller.GenerateSource.NEW_ANALYSER));
        }
    }

    private void clearInputPathAnalyser() {
        input = Collections.emptyList();
        path = null;
        controller.setAnalyser(null);
        compressed = false;
        compressButton.setDisable(false);
    }

    private void setPrimarySettings () {
        saveSeedButton.setDisable(true);
        exportWordsButton.setDisable(true);
        compressButton.setDisable(true);
        levelOfCompression.setDisable(true);
        levelOfCompressionLabel.setDisable(true);
    }

    private void checkMemento(ProgramParameters initProgramParameters, List<String> output, Controller controller) {
        if (initProgramParameters.getLevelOfCompression() != 0) {
            levelOfCompression.setText(Integer.toString(initProgramParameters.getLevelOfCompression()));
        }
        if (initProgramParameters.getAnalyser() != null) {
            controller.setAnalyser(initProgramParameters.getAnalyser());
            compressed = initProgramParameters.isCompressed();
        }
        if (initProgramParameters.getInput() != null && !initProgramParameters.getInput().isEmpty()) {
            for (String word : initProgramParameters.getInput()) {
                inputArea.setText(inputArea.getText() + (word + "\n"));
            }
            input = initProgramParameters.getInput();
            saveSeedButton.setDisable(false);
            levelOfCompression.setDisable(false);
            levelOfCompressionLabel.setDisable(false);
        }

        if (output != null && !output.isEmpty()) {
            for (String word : output) {
                outputArea.setText(outputArea.getText() + (word + "\n"));
                this.output.add(word);
            }
            exportWordsButton.setDisable(false);
        }

        if (initProgramParameters.getPath() != null) {
            boolean fileExists = new File(initProgramParameters.getPath()).exists();

            if (fileExists) {
                inputArea.setText(new File(initProgramParameters.getPath()).getName());
                inputArea.setEditable(false);
                path = initProgramParameters.getPath();
                setOptionsDisability(false);
            }
        }
    }

    private void showErrorInputMessage() {
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

    private void setOptionsDisability(boolean boo) {
        saveSeedButton.setDisable(boo);
        levelOfCompression.setDisable(boo);
        levelOfCompressionLabel.setDisable(boo);
        compressButton.setDisable(boo);
    }

    private ProgramParameters setParameters(Controller controller) {
        ProgramParameters.Builder parametersBuilder = new ProgramParameters.Builder();

        parametersBuilder.setAnalyser(controller.getAnalyser());
        parametersBuilder.setInput(input);
        parametersBuilder.setPath(path);
        parametersBuilder.setFirstCharAsInInput(firstChar.isSelected());
        parametersBuilder.setLastCharAsInInput(lastChar.isSelected());
        parametersBuilder.setSorted(sorted.isSelected());
        parametersBuilder.setCompressed(compressed);
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
        try {
            parametersBuilder.setLevelOfCompression(Integer.parseInt(levelOfCompression.getText()));
        } catch (NumberFormatException e) {
            parametersBuilder.setLevelOfCompression(0);
        }
        return parametersBuilder.build();
    }

    private TextField getMinMaxTextFields(int number) {
        if (number == 0) {
            return new TextField();
        } else {
            return new TextField(Integer.toString(number));
        }
    }

    private TextFormatter.Change filterForLevelOfCompression(TextFormatter.Change change) {
        try {
            int input = Integer.parseInt(change.getControlNewText());
            if ( input < 1 ) {
                change.setText("");
            }
        } catch (NumberFormatException e) {
            change.setText("");
        }
        if (change.getControlNewText().isEmpty()) {
            change.setText("");
        }

        if (change.isContentChange()) {
            compressButton.setDisable(false);
        }

        return change;
    }

    private TextFormatter.Change filterForMaxWordLength(TextFormatter.Change change) {
        try {
            int input = Integer.parseInt(change.getControlNewText());
            if ( input < 1 ) {
                change.setText("");
            }
        } catch (NumberFormatException e) {
            change.setText("");
        }
        return change;
    }

    private TextFormatter.Change filterForMinWordLength(TextField maxWordLength, TextFormatter.Change change) {
        try {
            int input = Integer.parseInt(change.getControlNewText());
            if ( input < 1 ) {
                change.setText("");
            }
            if ( !maxWordLength.getText().isEmpty() && input > Integer.parseInt(maxWordLength.getText()) ) {
                change.setText("");
            }

        } catch (NumberFormatException e) {
            change.setText("");
        }
        return change;
    }

    private TextFormatter.Change filterForNumbersOfWords(TextFormatter.Change change) {
        try {
            int input = Integer.parseInt(change.getControlNewText());
            if ( input < 1 ) {
                change.setText("1");
            }
        } catch (NumberFormatException e) {
            change.setText("");
        }
        if (change.getControlNewText().isEmpty()) {
            change.setText("1");
        }
        return change;
    }
}
