package similarwordsgenerator.model;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoaderWordsTest {
    private LoaderWords loaderWords = new LoaderWords();
    private String testDir = System.getProperty("dir.test.files");

    @Test
    void analyserIsReturnedFromTXTfile() {
        assertNotNull(loaderWords.load(testDir + File.separator + "input.txt"));
    }
}