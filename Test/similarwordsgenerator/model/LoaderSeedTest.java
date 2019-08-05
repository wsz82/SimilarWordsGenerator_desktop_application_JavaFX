package similarwordsgenerator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoaderSeedTest {

    private LoaderSeed loaderSeed = new LoaderSeed();
    private String testDir = "C://Users//Wojciech//IdeaProjects//Similar Words Generator//Test//Files//";

    @Test
    void analyserIsReturnedFromBINfile() {
        assertNotNull(loaderSeed.load(testDir + "input.bin"));
    }

}