package similarwordsgenerator.model;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoaderSeedTest {
    private LoaderSeed loaderSeed = new LoaderSeed();
    private String testDir = System.getProperty("dir.test.files");

    @Test
    void analyserIsReturnedFromBINfile() {
        assertNotNull(loaderSeed.load(testDir + File.separator + "input.bin"));
    }
}