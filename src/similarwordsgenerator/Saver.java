package similarwordsgenerator;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Saver {

    Saver () {
    }

    void save (Analyser analyser, String path) {

        try (
                BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))
                ) {

            Set<Integer> uniWordLenghts = new HashSet<>(analyser.getWordLengths());

            for ( Integer i : uniWordLenghts ) {

                long sum = analyser.getWordLengths().stream()
                        .filter(g -> g.equals(i))
                        .count();

                if ( sum > 0 ) {

                    if ( analyser.getWordLengths().size() > 100 ) {             //I need range 1-100 only for probability, maybe should be moved to Analyser

                        fw.write(i);
                        fw.write((int)( sum * 100 / analyser.getWordLengths().size()));
                    } else {

                        fw.write(i);
                        fw.write((int) sum);
                    }
                }
            }

            fw.newLine();

            Set<Character> uniFirstChars = new HashSet<>(analyser.getFirstChars());

            for ( char ch : uniFirstChars ) {

                long sum = analyser.getFirstChars().stream()
                        .filter(g -> g.equals(ch))
                        .count();

                if ( sum > 0 ) {

                    if ( analyser.getFirstChars().size() > 100 ) {             //I need range 1-100 only for probability, maybe should be moved to Analyser

                        fw.write(ch);
                        fw.write((int)( sum * 100 / analyser.getFirstChars().size()));
                    } else {

                        fw.write(ch);
                        fw.write((int) sum);
                    }
                }
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
