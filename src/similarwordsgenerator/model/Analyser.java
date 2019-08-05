package similarwordsgenerator.model;

import java.io.Serializable;
import java.util.*;

class Analyser implements Serializable{

    private int hashOfInput;
    private List<Integer> wordsLengths = new ArrayList<>();     //random length is weight average distribution
    private List<Character> firstChars = new ArrayList<>();
    private List<Character> lastChars = new ArrayList<>();
    private Map<Character, ArrayList<Character>> charsCount = new HashMap<>();

    Analyser () {
    }

    void analyze (List<String> input) {
        this.hashOfInput = input.hashCode();

        for (String word : input) {

            if (word.equals("")) {
                continue;
            }

            char[] tempWord = word.toCharArray();

            wordsLengths.add(word.length());
            firstChars.add(tempWord[0]);
            lastChars.add(tempWord[tempWord.length - 1]);

            addCharToCharsCount(tempWord);
        }
    }

    private void addCharToCharsCount(char[] tempWord) {
        for (int index = 0; index < tempWord.length - 1; index++) {

            if (charsCount.containsKey(tempWord[index])) {

                charsCount.get(tempWord[index]).add(0, tempWord[index + 1]);
            } else {

                charsCount.put(tempWord[index], new ArrayList<>());
                charsCount.get(tempWord[index]).add(0, tempWord[index + 1]);
            }
        }
    }

    void compress (int compressionLevel) {

        if (compressionLevel == 0) {
            return;
        }

        wordsLengths = compressListWithInteger(compressionLevel, wordsLengths);
        firstChars = compressListWithCharacters(compressionLevel, firstChars);
        lastChars = compressListWithCharacters(compressionLevel, lastChars);
        charsCount = compressCharsCount(compressionLevel, charsCount);
    }

    private Map<Character, ArrayList<Character>> compressCharsCount(int compressionLevel, Map<Character, ArrayList<Character>> charsCount) {

        for (Character key : charsCount.keySet() ) {

            List<Character> charsCountList = charsCount.get(key);

            if (charsCountList.size() > compressionLevel) {

                Set<Character> uniChars = new HashSet<>(charsCountList);
                List<Character> tempList = new ArrayList<>();

                for (Character ch : uniChars) {

                    long sum = charsCountList.stream()
                            .filter(g -> g == ch)
                            .count();

                    int count = (int) sum * compressionLevel / charsCountList.size();

                    for (int j = 0; j < count; j++) {

                        tempList.add(ch);
                    }
                }

                tempList = fulfillCharListToCompressionLevel(tempList, charsCountList, compressionLevel);

                charsCount.get(key).clear();
                charsCount.get(key).addAll(tempList);
            }
        }
        return charsCount;
    }

    private List<Character> compressListWithCharacters(int compressionLevel, List<Character> listWithCharacters) {
        if (listWithCharacters.size() > compressionLevel) {

            Set<Character> uniFirstChars = new HashSet<>(listWithCharacters);
            List<Character> tempFirstChars = new ArrayList<>();

            for (Character ch : uniFirstChars) {

                long sum = listWithCharacters.stream()
                        .filter(g -> g.equals(ch))
                        .count();

                int count = (int) sum * compressionLevel / listWithCharacters.size();

                for (int j = 0; j < count; j++) {

                    tempFirstChars.add(ch);
                }
            }

            tempFirstChars = fulfillCharListToCompressionLevel(tempFirstChars, listWithCharacters, compressionLevel);

            return tempFirstChars;
        } else {
            return listWithCharacters;
        }
    }

    private List<Character> fulfillCharListToCompressionLevel(List<Character> tempList, List<Character> listWithCharacters, int compressionLevel) {
        while (tempList.size() < compressionLevel) {

            char tempChar = 0;
            int temp = 0;

            List<Character> tempStream = new ArrayList<>(listWithCharacters);
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
        return tempList;
    }

    private List<Integer> compressListWithInteger(int compressionLevel, List<Integer> listWithIntegers) {
        if (listWithIntegers.size() > compressionLevel) {

            Set<Integer> uniWordLengths = new HashSet<>(listWithIntegers);
            List<Integer> tempWordLengths = new ArrayList<>();

            for (Integer i : uniWordLengths) {

                long sum = listWithIntegers.stream()
                        .filter(g -> g.equals(i))
                        .count();

                int count = (int) sum * compressionLevel / listWithIntegers.size();

                for (int j = 0; j < count; j++) {

                    tempWordLengths.add(i);
                }
            }

            tempWordLengths = fulfillIntegerListToCompressionLevel(tempWordLengths, listWithIntegers, compressionLevel);

            return tempWordLengths;
        } else {
            return listWithIntegers;
        }
    }

    private List<Integer> fulfillIntegerListToCompressionLevel(List<Integer> tempWordLengths, List<Integer> listWithIntegers, int compressionLevel) {
        while (tempWordLengths.size() < compressionLevel) {

            int tempInteger = 0;
            int temp = 0;

            List<Integer> tempStream = new ArrayList<>(listWithIntegers);
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
        return tempWordLengths;
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
