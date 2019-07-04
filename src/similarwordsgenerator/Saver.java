package similarwordsgenerator;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class Saver {

    Saver () {
    }

    void save (Analyser analyser, String path) {

        try (
                BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))
                ) {

            fw.write(analyser.getMinLength());
            fw.newLine();
            fw.write(analyser.getMaxLength());
            fw.newLine();

            for ( char ch : analyser.getFirstChars() ) {

                fw.write(ch);
            }

            for ( char ch : analyser.getCharsCount().keySet() ) {

                fw.newLine();
                fw.write(ch);

                List<Character> tempChars = new ArrayList<>(analyser.getCharsCount().get(ch));
                Set<Character> tempUniChars = new HashSet<>(tempChars);

                for ( char uniChar : tempUniChars ) {

                    long sum = tempChars.stream()
                            .filter(g -> g.equals(uniChar))
                            .count();

                    if ( sum > 0 ) {

                        if ( tempChars.size() > 100 ) {             //I need range 1-100 only for probability, maybe should be moved to Analyser

                        fw.write(uniChar);
                        fw.write((int)( sum * 100 / tempChars.size()));
                        } else {

                            fw.write(uniChar);
                            fw.write((int) sum);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
