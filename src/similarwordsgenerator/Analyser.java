package similarwordsgenerator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class Analyser implements Serializable{

    private List<Integer> wordLengths;  //length is weight average
    private List<Character> firstChars;
    private List<Character> lastChars;  //starting letter maybe should be optional
    private Map<Character, ArrayList<Character>> charsCount;

    Analyser () {
    }

    Analyser (List<String> input) {

        List<Integer> wordLengths = new ArrayList<>();
        List<Character> firstChars = new ArrayList<>();
        List<Character> lastChars = new ArrayList<>();
        Map<Character, ArrayList<Character>> charsCount = new HashMap<>();

        for (String word : input) {

            char[] tempWord = word.toCharArray();             //lower case should be optional

            wordLengths.add(word.length());
            firstChars.add(tempWord[0]);
            lastChars.add(tempWord[tempWord.length - 1]);

            for (int index = 0; index < tempWord.length - 1; index++) {

                if (charsCount.containsKey(tempWord[index])) {

                    charsCount.get(tempWord[index]).add(0, tempWord[index + 1]);
                } else {

                    charsCount.put(tempWord[index], new ArrayList<>());
                    charsCount.get(tempWord[index]).add(0, tempWord[index + 1]);
                }
            }
        }
        this.wordLengths = wordLengths;
        this.firstChars = firstChars;
        this.lastChars = lastChars;
        this.charsCount = charsCount;
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

    List<Integer> getWordLengths() {
        return wordLengths;
    }

    void setWordLengths(List<Integer> wordLengths) {
        this.wordLengths = wordLengths;
    }

    List<Character> getFirstChars() {
        return firstChars;
    }

    void setFirstChars(List<Character> firstChars) {
        this.firstChars = firstChars;
    }

    List<Character> getLastChars() {
        return lastChars;
    }

    void setLastChars(List<Character> lastChars) {
        this.lastChars = lastChars;
    }

    Map<Character, ArrayList<Character>> getCharsCount() {
        return charsCount;
    }

    void setCharsCount(Map<Character, ArrayList<Character>> charsCount) {
        this.charsCount = charsCount;
    }
}
