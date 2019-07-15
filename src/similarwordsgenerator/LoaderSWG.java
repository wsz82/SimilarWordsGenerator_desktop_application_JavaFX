package similarwordsgenerator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class LoaderSWG implements ILoader {

    @Override
    public Analyser load(String path) {

        Analyser analyser = new Analyser();

        List<Integer> wordLenghts = new ArrayList<>();
        List<Character> firstChars = new ArrayList<>();
        List<Character> lastChars = new ArrayList<>();
        Map<Character, ArrayList<Character>> charsCount = new HashMap<>();

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))
        ) {

            int temp;

            if (br.read() != '\ufeff') br.reset();

            outerloop:
            while (true) {
                if ((temp = br.read()) == '\n' || temp == '\r') {
                    break;
                } else {
                    while (true) {
                        int tempCount = br.read();

                        if (tempCount == '\n' || tempCount == '\r') {
                            break outerloop;
                        }

                        for (int i = 0; i < tempCount; i++) {
                            wordLenghts.add(temp);

                        }
                        continue outerloop;
                    }
                }
            }

            analyser.setWordLengths(wordLenghts);

            br.skip(1);

            outerloop:
            while (true) {
                if ((temp = br.read()) == '\n' || temp == '\r') {
                    break;
                } else {
                    while (true) {
                        int tempCount = br.read();

                        if (tempCount == '\n' || tempCount == '\r') {
                            break outerloop;
                        }

                        for (int i = 0; i < tempCount; i++) {
                            firstChars.add((char) temp);

                        }
                        continue outerloop;
                    }
                }
            }

            analyser.setFirstChars(firstChars);

            br.skip(1);

            outerloop:
            while (true) {
                if ((temp = br.read()) == '\n' || temp == '\r') {
                    break;
                } else {
                    while (true) {
                        int tempCount = br.read();

                        if (tempCount == '\n' || tempCount == '\r') {
                            break outerloop;
                        }

                        for (int i = 0; i < tempCount; i++) {
                            lastChars.add((char) temp);

                        }
                        continue outerloop;
                    }
                }
            }

            analyser.setLastChars(lastChars);

            outerloop:
            while (true) {

                int tempKey = br.read();

                if (tempKey == '\n' || tempKey == '\r') {
                    continue;
                } else if (tempKey == -1) break;

                if (!charsCount.containsKey((char) tempKey)) {

                    charsCount.put((char) tempKey, new ArrayList<>());
                }

                while (true) {

                    int tempNextChar = br.read();

                    if (tempNextChar == '\n' || tempNextChar == '\r') {
                        continue outerloop;
                    } else if (tempNextChar == -1) break outerloop;

                    int tempCount = br.read();

                    for (int index = 0; index < tempCount; index++) {

                        charsCount.get((char) tempKey).add((char) tempNextChar);
                    }
                }

            }

            analyser.setCharsCount(charsCount);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return analyser;
    }
}

