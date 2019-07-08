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
        }

        for (Character key : charsCount.keySet() ) {

            ArrayList<Character> charsCountList = charsCount.get(key);

            if (charsCountList.size() > 100) {

                Set<Character> uniChars = new HashSet<>(charsCountList);
                ArrayList<Character> tempList = new ArrayList<>();

                for (Character ch : uniChars) {

                    long sum = charsCountList.stream()
                            .filter(g -> g == ch)
                            .count();

                    int count = (int) sum * 100 / charsCountList.size();   //I need range 1-100 only for probability

                    for (int j = 0; j < count; j++) {

                        tempList.add(ch);
                    }
                }
                charsCount.get(key).clear();
                charsCount.get(key).addAll(tempList);
            }
        }

        this.charsCount = charsCount;

        if (firstChars.size() > 100) {

            Set<Character> uniFirstChars = new HashSet<>(firstChars);
            List<Character> tempFirstChars = new ArrayList<>();

            for (Character ch : uniFirstChars) {

                long sum = firstChars.stream()
                        .filter(g -> g.equals(ch))
                        .count();

                int count = (int) sum * 100 / firstChars.size();   //I need range 1-100 only for probability

                for (int j = 0; j < count; j++) {

                    tempFirstChars.add(ch);
                }
            }
            this.firstChars = tempFirstChars;
        } else this.firstChars = firstChars;

        if (wordLenghts.size() > 100) {

            Set<Integer> uniWordLenghts = new HashSet<>(wordLenghts);
            List<Integer> tempWordLengths = new ArrayList<>();

            for (Integer i : uniWordLenghts) {

                long sum = wordLenghts.stream()
                        .filter(g -> g.equals(i))
                        .count();

                int count = (int) sum * 100 / wordLenghts.size();   //I need range 1-100 only for probability

                for (int j = 0; j < count; j++) {

                    tempWordLengths.add(i);
                }
            }
            this.wordLengths = tempWordLengths;
        } else this.wordLengths = wordLenghts;
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
                    break;
                } else {
                    while (true) {
                        int tempCount = br.read();

                        if (tempCount == '\n' || tempCount == '\r' ) {
                            break outerloop;
                        }

                        for (int i = 0; i < tempCount; i++) {
                            wordLenghts.add(temp);

                        }
                        continue outerloop;
                    }
                }
            }

            this.wordLengths = wordLenghts;

            br.skip(1);

            outerloop: while (true) {
                if ((temp = br.read()) == '\n' || temp == '\r' ) {
                    break;
                } else {
                    while (true) {
                        int tempCount = br.read();

                        if (tempCount == '\n' || tempCount == '\r' ) {
                            break outerloop;
                        }

                        for (int i = 0; i < tempCount; i++) {
                            firstChars.add((char)temp);

                        }
                        continue outerloop;
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
