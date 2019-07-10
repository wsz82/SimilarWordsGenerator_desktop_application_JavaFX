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
        this.firstChars = firstChars;
        this.wordLengths = wordLenghts;
        this.charsCount = charsCount;
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

    void compress (int compressionLevel) {

        Map<Character, ArrayList<Character>> charsCount = this.charsCount;

        if (this.firstChars.size() > compressionLevel) {

            Set<Character> uniFirstChars = new HashSet<>(this.firstChars);
            List<Character> tempFirstChars = new ArrayList<>();

            for (Character ch : uniFirstChars) {

                long sum = this.firstChars.stream()
                        .filter(g -> g.equals(ch))
                        .count();

                int count = (int) sum * compressionLevel / this.firstChars.size();

                for (int j = 0; j < count; j++) {

                    tempFirstChars.add(ch);
                }
            }

            while (tempFirstChars.size() < compressionLevel) {

                char tempChar = 0;
                int temp = 0;

                List<Character> tempStream = new ArrayList<>(getFirstChars());
                tempStream.removeAll(tempFirstChars);

                for (Character ch : tempStream) {


                    long sum = tempStream.stream()
                            .filter(g -> g.equals(ch))
                            .count();

                    if (sum > temp) {
                        temp = (int) sum;
                        tempChar = ch;
                    }

                }
                tempFirstChars.add(tempChar);
            }

            this.firstChars = tempFirstChars;
        }

        if (this.wordLengths.size() > compressionLevel) {

            Set<Integer> uniWordLenghts = new HashSet<>(this.wordLengths);
            List<Integer> tempWordLengths = new ArrayList<>();

            for (Integer i : uniWordLenghts) {

                long sum = this.wordLengths.stream()
                        .filter(g -> g.equals(i))
                        .count();

                int count = (int) sum * compressionLevel / this.wordLengths.size();

                for (int j = 0; j < count; j++) {

                    tempWordLengths.add(i);
                }
            }

            while (tempWordLengths.size() < compressionLevel) {

                int tempInteger = 0;
                int temp = 0;

                List<Integer> tempStream = new ArrayList<>(getWordLengths());
                tempStream.removeAll(tempWordLengths);

                for (Integer i : tempStream) {


                    long sum = tempStream.stream()
                            .filter(g -> g.equals(i))
                            .count();

                    if (sum > temp) {
                        temp = (int) sum;
                        tempInteger = i;
                    }

                }
                tempWordLengths.add(tempInteger);
            }

            this.wordLengths = tempWordLengths;
        }



        for (Character key : this.charsCount.keySet() ) {

            ArrayList<Character> charsCountList = this.charsCount.get(key);

            if (charsCountList.size() > compressionLevel) {

                Set<Character> uniChars = new HashSet<>(charsCountList);
                ArrayList<Character> tempList = new ArrayList<>();

                for (Character ch : uniChars) {

                    long sum = charsCountList.stream()
                            .filter(g -> g == ch)
                            .count();

                    int count = (int) sum * compressionLevel / charsCountList.size();

                    for (int j = 0; j < count; j++) {

                        tempList.add(ch);
                    }
                }

                while (tempList.size() < compressionLevel) {

                    char tempChar = 0;
                    int temp = 0;

                    List<Character> tempStream = new ArrayList<>(charsCountList);
                    tempStream.removeAll(tempList);

                    for (Character ch : tempStream) {

                        long sum = tempStream.stream()
                                .filter(g -> g.equals(ch))
                                .count();

                        if (sum > temp) {
                            temp = (int) sum;
                            tempChar = ch;
                        }

                    }
                    tempList.add(tempChar);
                }

                charsCount.get(key).clear();
                charsCount.get(key).addAll(tempList);
            }


        }

        this.charsCount = charsCount;
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
