package similarwordsgenerator.model;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Controller implements IController {

    private Generator generator = new Generator();

    @Override
    public Set<String> generate(ProgramParameters programParameters) throws IOException {
        return generator.generate(programParameters);
    }

    @Override
    public void compress(int compressionLevel) {
        generator.getAnalyser().compress(compressionLevel);
    }

    @Override
    public void save(Analyser analyser, String path) {
        SaverBIN saver = new SaverBIN();
        saver.save(analyser, path);
    }

    @Override
    public void export(List<String> listOfWords, String path) {
        WordsExport wordsExport = new WordsExport();
        wordsExport.export(listOfWords, path);
    }

    @Override
    public Analyser getAnalyser() {
        return generator.getAnalyser();
    }
}
