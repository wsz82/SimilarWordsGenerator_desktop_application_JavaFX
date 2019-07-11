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

class RatioSaver {

    RatioSaver() {
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

                    fw.write(i);
                    fw.write((int) sum);
                }
            }

            fw.newLine();

            Set<Character> uniFirstChars = new HashSet<>(analyser.getFirstChars());

            for ( char ch : uniFirstChars ) {

                long sum = analyser.getFirstChars().stream()
                        .filter(g -> g.equals(ch))
                        .count();

                if ( sum > 0 ) {

                    fw.write(ch);
                    fw.write((int) sum);
                }
            }

            fw.newLine();

            Set<Character> uniLastChars = new HashSet<>(analyser.getLastChars());

            for ( char ch : uniLastChars ) {

                long sum = analyser.getLastChars().stream()
                        .filter(g -> g.equals(ch))
                        .count();

                if ( sum > 0 ) {

                    fw.write(ch);
                    fw.write((int) sum);
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

                        fw.write(uniChar);
                        fw.write((int) sum);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
