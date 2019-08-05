package similarwordsgenerator.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SaverSeedTest {

    private String testDir = System.getProperty("user.home") + "//IdeaProjects//Similar Words Generator//Test//Files//";
    private List<String> input = Arrays.asList("John", "Nancy", "Stacy");
    Analyser analyser = new Analyser();
    SaverSeed saverSeed = new SaverSeed();
    LoaderSeed loaderSeed = new LoaderSeed();

    @Test
    void analyserAreTheSameBeforeAndAfterSavingSeed() {
        analyser.analyze(input);
        saverSeed.save(analyser, testDir + "seed.bin");

        Analyser loadedAnalyser = loaderSeed.load(testDir + "seed.bin");
        assertEquals(analyser.getHashOfInput(), loadedAnalyser.getHashOfInput());
    }
}