package similarwordsgenerator.application;

import io.github.wsz82.ProgramParameters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MementoTest {
    private static Memento savedMemento;
    private static Memento loadedMemento;
    private static List<String> testingList;
    private static String testDir;
    private static File mementoFile;
    private static String mementoName;

    @BeforeAll
    void buildRandomParametersAndOutputAndCreateMemento() throws Exception {
        init();
        Random random = new Random();
        ProgramParameters.Builder parametersBuilder = new ProgramParameters.Builder();

        parametersBuilder.setInput(testingList);
        parametersBuilder.setPath(testDir);
        parametersBuilder.setFirstCharAsInInput(random.nextBoolean());
        parametersBuilder.setLastCharAsInInput(random.nextBoolean());
        parametersBuilder.setSorted(random.nextBoolean());
        parametersBuilder.setCompressed(random.nextBoolean());
        parametersBuilder.setNumberOfWords(random.nextInt());
        parametersBuilder.setMinWordLength(random.nextInt());
        parametersBuilder.setMaxWordLength(random.nextInt());
        parametersBuilder.setLevelOfCompression(random.nextInt());
        ProgramParameters programParameters = parametersBuilder.build();
        savedMemento = new Memento(programParameters, testingList, mementoFile, mementoName);
        loadedMemento = Memento.loadMemento(mementoFile, mementoName);
    }

    static void init() throws Exception {
        mementoName = "memento_test";
        testingList = Arrays.asList("John", "Stacy", "Nancy");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL pathURL = classLoader.getResource("Files");
        testDir = Paths.get(pathURL.toURI()).toAbsolutePath().toString();
        mementoFile = new File(testDir);
    }

    @Test
    void inputsAreTheSame() {
        assertEquals(savedMemento.getProgramParameters().getInput(), loadedMemento.getProgramParameters().getInput());
    }

    @Test
    void pathsAreTheSame() {
        assertEquals(savedMemento.getProgramParameters().getPath(), loadedMemento.getProgramParameters().getPath());
    }

    @Test
    void firstCharBooleansAreTheSame() {
        assertEquals(savedMemento.getProgramParameters().isFirstCharAsInInput(), loadedMemento.getProgramParameters().isFirstCharAsInInput());
    }

    @Test
    void lastCharBooleansAreTheSame() {
        assertEquals(savedMemento.getProgramParameters().isLastCharAsInInput(), loadedMemento.getProgramParameters().isLastCharAsInInput());
    }

    @Test
    void sortedAreTheSame() {
        assertEquals(savedMemento.getProgramParameters().isSorted(), loadedMemento.getProgramParameters().isSorted());
    }

    @Test
    void compressedAreTheSame() {
        assertEquals(savedMemento.getProgramParameters().isCompressed(), loadedMemento.getProgramParameters().isCompressed());
    }

    @Test
    void numbersOfWordsAreTheSame() {
        assertEquals(savedMemento.getProgramParameters().getNumberOfWords(), loadedMemento.getProgramParameters().getNumberOfWords());
    }

    @Test
    void minWordLengthsAreTheSame() {
        assertEquals(savedMemento.getProgramParameters().getMinWordLength(), loadedMemento.getProgramParameters().getMinWordLength());
    }

    @Test
    void maxWordLengthsAreTheSame() {
        assertEquals(savedMemento.getProgramParameters().getMaxWordLength(), loadedMemento.getProgramParameters().getMaxWordLength());
    }

    @Test
    void levelsOfCompressionAreTheSame() {
        assertEquals(savedMemento.getProgramParameters().getLevelOfCompression(), loadedMemento.getProgramParameters().getLevelOfCompression());
    }
}