package similarwordsgenerator.model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

class WordsExport {
    void export(List<String> listOfWords, String path) {
        try (
                BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))
                ) {
            for (String s: listOfWords) {
                fw.write(s);
                fw.newLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
