package similarwordsgenerator.model;

import java.io.Serializable;
import java.util.*;

class Analyser implements Serializable{

    private int hashOfInput;

    private List<Integer> wordsLengths;     //random length is weight average distribution
    private List<Character> firstChars;
    private List<Character> lastChars;
    private Map<Character, ArrayList<Character>> charsCount;

    Analyser () {
    }

    void analyze (List<String> input) {
        this.hashOfInput = input.hashCode();

        List<Integer> wordsLengths = new ArrayList<>();
        List<Character> firstChars = new ArrayList<>();
        List<Character> lastChars = new ArrayList<>();
        Map<Character, ArrayList<Character>> charsCount = new HashMap<>();

        for (String word : input) {

            if (word.equals("")) {
                continue;
            }

            char[] tempWord = word.toCharArray();

            wordsLengths.add(word.length());
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
        this.wordsLengths = wordsLengths;
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

        if (this.wordsLengths.size() > compressionLevel) {

            Set<Integer> uniWordLengths = new HashSet<>(this.wordsLengths);
            List<Integer> tempWordLengths = new ArrayList<>();

            for (Integer i : uniWordLengths) {

                long sum = this.wordsLengths.stream()
                        .filter(g -> g.equals(i))
                        .count();

                int count = (int) sum * compressionLevel / this.wordsLengths.size();

                for (int j = 0; j < count; j++) {

                    tempWordLengths.add(i);
                }
            }

            while (tempWordLengths.size() < compressionLevel) {

                int tempInteger = 0;
                int temp = 0;

                List<Integer> tempStream = new ArrayList<>(getWordsLengths());
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

            this.wordsLengths = tempWordLengths;
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

            this.charsCount = charsCount;
        }
    }

    int getHashOfInput() {
        return hashOfInput;
    }

    List<Integer> getWordsLengths() {
        return wordsLengths;
    }

    List<Character> getFirstChars() {
        return firstChars;
    }

    List<Character> getLastChars() {
        return lastChars;
    }

    Map<Character, ArrayList<Character>> getCharsCount() {
        return charsCount;
    }
}
