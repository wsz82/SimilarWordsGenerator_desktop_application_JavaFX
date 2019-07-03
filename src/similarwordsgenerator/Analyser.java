package similarwordsgenerator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Analyser {

    private int minLength;
    private int maxLength;
    private Set<Character> firstChars;
    private Map<Character, ArrayList<Character>> charsCount;

    Analyser() {
    }

    void analyse (List<String> input) {

        Set<Character> firstChars = new HashSet<>();                     //starting letter maybe should be optional
        Map<Character, ArrayList<Character>> charsCount = new HashMap<>();

        int maxLength = input.get(0).length();
        int minLength = input.get(0).length();

        for (String word : input) {

            char[] tempWord = word.toLowerCase().toCharArray();             //lower case should be optional
            firstChars.add(Character.toUpperCase(tempWord[0]));

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

    void paramLoad (String path) {

        Set<Character> firstChars = new HashSet<>();
        Map<Character, ArrayList<Character>> charsCount = new HashMap<>();

        try (
                BufferedReader br = new BufferedReader(new FileReader(path))
                ) {

            this.minLength = br.read();
            br.read();
            this.maxLength = br.read();
            br.read();

            int temp;

            while ( (temp = br.read()) != 10 ) {    //carriage return

                firstChars.add((char)temp);
            }

            this.firstChars = firstChars;

            outerloop: while (true) {

                int tempKey = br.read();

                if (tempKey == 10) {
                    continue;
                } else if (tempKey == -1) break;

                if (!charsCount.containsKey((char)tempKey)) {

                    charsCount.put((char)tempKey, new ArrayList<>());
                }

                while (true) {

                    int tempNextChar = br.read();

                    if (tempNextChar == 10) {
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
