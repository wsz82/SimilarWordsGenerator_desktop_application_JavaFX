package similarwordsgenerator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class Analyser {

    private List<Integer> wordLengths;  //length is weight average
    private List<Character> firstChars;  //starting letter maybe should be optional
    private Map<Character, ArrayList<Character>> charsCount;

    Analyser (List<String> input) {

        List<Integer> wordLenghts = new ArrayList<>();
        List<Character> firstChars = new ArrayList<>();
        Map<Character, ArrayList<Character>> charsCount = new HashMap<>();

        for (String word : input) {

            char[] tempWord = word.toCharArray();             //lower case should be optional
            wordLenghts.add(word.length());
            firstChars.add(tempWord[0]);

            for (int index = 0; index < tempWord.length - 1; index++) {

                if (charsCount.containsKey(tempWord[index])) {

                    charsCount.get(tempWord[index]).add(0, tempWord[index + 1]);
                } else {

                    charsCount.put(tempWord[index], new ArrayList<>());
                    charsCount.get(tempWord[index]).add(0, tempWord[index + 1]);
                }
            }

            this.wordLengths = wordLenghts;
            this.charsCount = charsCount;
        }

        this.firstChars = firstChars;
    }

    Analyser (String path) {

        List<Integer> wordLenghts = new ArrayList<>();
        List<Character> firstChars = new ArrayList<>();
        Map<Character, ArrayList<Character>> charsCount = new HashMap<>();

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8), 2)
                ) {

            int temp;

            if (br.read() != '\ufeff') br.reset();

            outerloop: while (true) {
                if ((temp = br.read()) == '\n' || temp == '\r' ) {
                    continue;
                } else {
                    while (true) {

                        if (temp == '\n' || temp == '\r' ) {
                            break outerloop;
                        }
                        wordLenghts.add(temp);
                        temp = br.read();
                    }
                }
            }

            outerloop: while (true) {
                if ((temp = br.read()) == '\n' || temp == '\r' ) {
                    continue;
                } else {
                    while (true) {

                        if (temp == '\n' || temp == '\r' ) {
                            break outerloop;
                        }
                        firstChars.add((char) temp);
                        temp = br.read();
                    }
                }
            }

            this.firstChars = firstChars;

            outerloop: while (true) {

                int tempKey = br.read();

                if (tempKey == '\n' || tempKey == '\r') {
                    continue;
                } else if (tempKey == -1) break;

                if (!charsCount.containsKey((char)tempKey)) {

                    charsCount.put((char)tempKey, new ArrayList<>());
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

            this.charsCount = charsCount;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<Character> getFirstChars() {
        return firstChars;
    }

    Map<Character, ArrayList<Character>> getCharsCount() {
        return charsCount;
    }

    List<Integer> getWordLengths() {
        return wordLengths;
    }
}
