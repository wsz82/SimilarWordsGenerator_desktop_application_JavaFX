package similarwordsgenerator.model;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IController {
    Set<String> generate(ProgramParameters programParameters) throws IOException;

    void compress(int compressionLevel);

    void save(Analyser analyser, String path);

    void export(List<String> listOfWords, String path);

    Analyser getAnalyser();
}
