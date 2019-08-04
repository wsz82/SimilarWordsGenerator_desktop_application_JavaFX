package similarwordsgenerator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoaderWordsTest {

    private LoaderWords loaderWords = new LoaderWords();
    private String testDir = "C://Users//Wojciech//IdeaProjects//Similar Words Generator//Test//Files//";

    @Test
    void analyserIsReturnedFromCorrectTXTfile() {
        assertNotNull(loaderWords.load(testDir + "input.txt"));
    }

}