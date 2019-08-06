package similarwordsgenerator.model;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordsExportTest {
    private String testDir = System.getProperty("user.home") + "//IdeaProjects//Similar Words Generator//Test//Files//";
    private List<String> input = Arrays.asList("John", "Nancy", "Stacy");
    private Set<String> output;
    private Generator generator = new Generator();
    private ProgramParameters.Builder parametersBuilder = new ProgramParameters.Builder();
    private ProgramParameters parameters;
    private WordsExport wordsExport = new WordsExport();

    @Test
    void fileContainsOutputAfterExporting() {
        parametersBuilder.setInput(input);
        parameters = parametersBuilder.build();
        output = generator.generate(parameters, Controller.GenerateSource.NEW_ANALYSER);
        wordsExport.export(new ArrayList<>(output), testDir + "exported.txt");

        Set<String> loaded = new HashSet<>();
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(testDir + "exported.txt"), StandardCharsets.UTF_8))
        ){
            String temp;

            br.mark(3);
            if (br.read() != '\ufeff') br.reset();
            while ( (temp = br.readLine()) != null ) {
                if (!temp.equals("")) loaded.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(output, loaded);
    }
}