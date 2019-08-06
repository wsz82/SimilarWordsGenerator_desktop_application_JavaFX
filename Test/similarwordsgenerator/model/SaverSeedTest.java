package similarwordsgenerator.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SaverSeedTest {
    private String testDir = System.getProperty("dir.test.files");
    private List<String> input = Arrays.asList("John", "Nancy", "Stacy");
    private Analyser analyser = new Analyser();
    private SaverSeed saverSeed = new SaverSeed();
    private LoaderSeed loaderSeed = new LoaderSeed();

    @Test
    void analyserAreTheSameBeforeAndAfterSavingSeed() {
        analyser.analyze(input);
        saverSeed.save(analyser, testDir + File.separator + "seed.bin");
        Analyser loadedAnalyser = loaderSeed.load(testDir + File.separator + "seed.bin");
        assertEquals(analyser.getHashOfInput(), loadedAnalyser.getHashOfInput());
    }
}