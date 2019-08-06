package similarwordsgenerator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoaderWordsTest {
    private LoaderWords loaderWords = new LoaderWords();
    private String testDir = System.getProperty("user.home") + "//IdeaProjects//Similar Words Generator//Test//Files//";

    @Test
    void analyserIsReturnedFromTXTfile() {
        assertNotNull(loaderWords.load(testDir + "input.txt"));
    }
}