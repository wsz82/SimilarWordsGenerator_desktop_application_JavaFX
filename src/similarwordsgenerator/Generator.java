package similarwordsgenerator;

import java.io.IOException;
import java.util.*;

public class Generator {

    private Analyser analyser;
    private boolean sorted = true;
    private boolean firstCharAsInInput = true;
    private boolean lastCharAsInInput = true;
    private int numberOfWords = 1;
    private int minWordLength = 0;          //number 0 is a flag for default word length
    private int maxWordLength = 0;          //number 0 is a flag for default word length

    public Generator () {
    }

    public Generator (List<String> input) {
        this.analyser = new Analyser(input);
    }

    public Generator (String path) throws IOException {
        fileFormat(path);
    }

    Set<String> generate() {

        Set<String> result;

        if (isSorted()) {

            result = new TreeSet<>();

        } else result = new HashSet<>();

        while ( result.size() < getNumberOfWords() ) {

            StringBuilder output = new StringBuilder();
            Random r = new Random();

            boolean firstCharAsInInput = isFirstCharAsInInput();
            boolean lastCharAsInInput = isLastCharAsInInput();
            int wordLength;
            int maxWordLength = getMaxWordLength();
            int minWordLength = getMinWordLength();

            if ( minWordLength != 0 && maxWordLength != 0 ) {

                wordLength = r.nextInt((maxWordLength - minWordLength) + 1) + minWordLength;

            } else wordLength = analyser.getWordLengths().get(r.nextInt(analyser.getWordLengths().toArray().length));

            if ( firstCharAsInInput ) {

                output.append(analyser.getFirstChars().toArray()[r.nextInt(analyser.getFirstChars().toArray().length)]);

            } else output.append(analyser.getCharsCount().keySet().toArray()[r.nextInt(analyser.getCharsCount().keySet().toArray().length)]);

            for ( int i = 1; i <= wordLength; i++) {

                if (output.toString().length() == wordLength) {
                    break;
                }

                char lastChar = output.toString().toCharArray()[output.toString().length() - 1];
                if (!analyser.getCharsCount().containsKey(lastChar)) {
                    break;
                }
                ArrayList<Character> charsCountList = analyser.getCharsCount().get(lastChar);

                if (lastCharAsInInput) {

                    if (output.toString().length() == wordLength - 1) {

                        ArrayList<Character> tempLastChars = new ArrayList<>(charsCountList);
                        tempLastChars.retainAll(analyser.getLastChars());

                        if (!tempLastChars.isEmpty()) {

                            output.append(tempLastChars.toArray()[r.nextInt(tempLastChars.toArray().length)]);
                        } else {
                            output.deleteCharAt(wordLength - 2);
                            wordLength += 2;
                        }
                    } else output.append(charsCountList.toArray()[r.nextInt(charsCountList.toArray().length)]);
                } else output.append(charsCountList.toArray()[r.nextInt(charsCountList.toArray().length)]);
            }

            String tempWord = output.toString();

            if (minWordLength != 0 && tempWord.length() < minWordLength) {
                continue;
            }
            if (maxWordLength != 0 && tempWord.length() > maxWordLength) {

                result.add(tempWord.substring(0, maxWordLength - 1));

            } else result.add(tempWord);
        }

        return result;
    }

    private void fileFormat(String path) throws IOException {
        try {
            if (path.endsWith(".txt")) {
                this.analyser = new LoaderWords().load(path);
            } else if (path.endsWith(".bin")) {
                this.analyser = new LoaderBIN().load(path);
            } else throw new IOException();
        } catch (IOException e) {
            throw new IOException("Wrong file format.");
        }
    }

    public void setAnalyser(Analyser analyser) {
        this.analyser = analyser;
    }

    public Analyser getAnalyser() {
        return analyser;
    }

    public boolean isSorted() {
        return sorted;
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    public boolean isFirstCharAsInInput() {
        return firstCharAsInInput;
    }

    public void setFirstCharAsInInput(boolean firstCharAsInInput) {
        this.firstCharAsInInput = firstCharAsInInput;
    }

    public boolean isLastCharAsInInput() {
        return lastCharAsInInput;
    }

    public void setLastCharAsInInput(boolean lastCharAsInInput) {
        this.lastCharAsInInput = lastCharAsInInput;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }

    public void setNumberOfWords(int numberOfWords) {
        this.numberOfWords = numberOfWords;
    }

    public int getMinWordLength() {
        return minWordLength;
    }

    public void setMinWordLength(int minWordLength) {
        this.minWordLength = minWordLength;
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }

    public void setMaxWordLength(int maxWordLength) {
        this.maxWordLength = maxWordLength;
    }
}
