package similarwordsgenerator;

import java.io.IOException;
import java.util.*;

public class Generator {

    private Analyser analyser;
    private GeneratorParameters gp;

    public Generator () {
    }

    public Generator (List<String> input, GeneratorParameters gp) throws IOException {
        this.analyser = new Analyser(input);
        this.gp = gp;
    }

    public Generator (String path, GeneratorParameters gp) throws IOException {
        fileFormat(path);
        this.gp = gp;
    }

    public Generator (String path, boolean sorted, boolean firstCharAsInInput, boolean lastCharAsInInput, int numberOfWords, int minWordLength, int maxWordLength) throws IOException {

        fileFormat(path);

        this.gp = new GeneratorParameters(sorted, firstCharAsInInput, lastCharAsInInput, numberOfWords, minWordLength, maxWordLength);
    }

    Set<String> generate() {

        Set<String> result;

        if (gp.isSorted()) {

            result = new TreeSet<>();

        } else result = new HashSet<>();

        while ( result.size() < gp.getNumberOfWords() ) {

            StringBuilder output = new StringBuilder();
            Random r = new Random();

            boolean firstCharAsInInput = gp.isFirstCharAsInInput();
            boolean lastCharAsInInput = gp.isLastCharAsInInput();
            int wordLength;
            int maxWordLength = gp.getMaxWordLength();
            int minWordLength = gp.getMinWordLength();

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

            if (tempWord.length() < minWordLength && minWordLength != 0) {

                continue;

            } else if (tempWord.length() > maxWordLength && maxWordLength != 0) {

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
            } else if (path.endsWith(".swg")) {                         //new file format .swg
                this.analyser = new LoaderSWG().load(path);
            } else throw new IOException();
        } catch (IOException e) {
            throw new IOException("Wrong file format.");
        }
    }

    public void setGp(GeneratorParameters gp) {
        this.gp = gp;
    }

    public GeneratorParameters getGp() {
        return gp;
    }

    public void setAnalyser(Analyser analyser) {
        this.analyser = analyser;
    }

    public Analyser getAnalyser() {
        return analyser;
    }
}
