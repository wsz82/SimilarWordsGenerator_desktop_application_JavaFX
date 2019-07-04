package similarwordsgenerator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class Analyser {

    private int minLength;
    private int maxLength;
    private Set<Character> firstChars;
    private Map<Character, ArrayList<Character>> charsCount;

    Analyser (List<String> input) {

        Set<Character> firstChars = new HashSet<>();                     //starting letter maybe should be optional
        Map<Character, ArrayList<Character>> charsCount = new HashMap<>();

        int maxLength = input.get(0).length();
        int minLength = input.get(0).length();

        for (String word : input) {

            char[] tempWord = word.toCharArray();             //lower case should be optional
            firstChars.add(tempWord[0]);

            if (tempWord.length < minLength) {

                minLength = tempWord.length;
            }

            if (tempWord.length > maxLength) {

                maxLength = tempWord.length;
            }

            for (int index = 0; index < tempWord.length - 1; index++) {

                if (charsCount.containsKey(tempWord[index])) {

                    charsCount.get(tempWord[index]).add(0, tempWord[index + 1]);
                } else {

                    charsCount.put(tempWord[index], new ArrayList<>());
                    charsCount.get(tempWord[index]).add(0, tempWord[index + 1]);
                }
            }

            this.charsCount = charsCount;
        }

        this.firstChars = firstChars;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    Analyser (String path) {

        Set<Character> firstChars = new HashSet<>();
        Map<Character, ArrayList<Character>> charsCount = new HashMap<>();

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8), 2)
                ) {

            int temp;

            while (true) {
                if ((temp = br.read()) == '\n' || temp == '\r' ) {
                    continue;
                } else {
                    this.minLength = temp;
                    if (this.minLength != 0) {
                        break;
                    }
                }
            }
            while (true) {
                if ((temp = br.read()) == '\n' || temp == '\r' ) {
                    continue;
                } else {
                    this.maxLength = temp;
                    if (this.maxLength != 0) {
                        break;
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

    Set<Character> getFirstChars() {
        return firstChars;
    }

    Map<Character, ArrayList<Character>> getCharsCount() {
        return charsCount;
    }

    int getMaxLength() {
        return maxLength;
    }

    int getMinLength() {
        return minLength;
    }
}
