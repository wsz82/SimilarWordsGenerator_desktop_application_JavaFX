package similarwordsgenerator.model;

import java.util.List;
import java.util.Set;

public class Controller {

    public enum GenerateSource {
        NEW_ANALYSER, CURRENT_ANALYSER
    }

    private Generator generator = new Generator();

    public Set<String> generate(ProgramParameters programParameters, Controller.GenerateSource generateSource) {
        return generator.generate(programParameters, generateSource);
    }

    public void compress(int compressionLevel, ProgramParameters programParameters) {
        if (generator.getAnalyser() != null) {
            generator.getAnalyser().compress(compressionLevel);
        } else {
            generator.createAnalyser(programParameters);
            generator.getAnalyser().compress(compressionLevel);
        }
    }

    public void save(Analyser analyser, String path) {
        SaverBIN saver = new SaverBIN();
        saver.save(analyser, path);
    }

    public void export(List<String> listOfWords, String path) {
        WordsExport wordsExport = new WordsExport();
        wordsExport.export(listOfWords, path);
    }

    public Analyser getAnalyser() {
        return generator.getAnalyser();
    }

    public void setAnalyser(Analyser analyser) {
        generator.setAnalyser(analyser);
    }
}
