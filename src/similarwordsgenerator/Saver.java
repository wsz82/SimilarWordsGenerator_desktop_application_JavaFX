package similarwordsgenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Saver {

    Saver () {
    }

    void save (Analyser analyser, String path) {

        try (
                FileWriter fw = new FileWriter(path)
                ) {

            fw.write(analyser.getMinLength());
            fw.write("\n");
            fw.write(analyser.getMaxLength());
            fw.write("\n");

            for ( char ch : analyser.getFirstChars() ) {

                fw.write(ch);
            }

            for ( char ch : analyser.getCharsCount().keySet() ) {

                fw.write("\n");
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
