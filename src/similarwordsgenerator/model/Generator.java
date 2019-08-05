package similarwordsgenerator.model;

import java.io.File;
import java.util.*;

class Generator {
    private Analyser analyser;
    private Random random = new Random();
    private int wordLength;
    private int minWordLength;
    private int maxWordLength;
    private boolean firstCharAsInInput;
    private boolean lastCharAsInInput;
    private StringBuilder output;

    Generator () {
    }

    Set<String> generate(ProgramParameters programParameters, Controller.GenerateSource generateSource) {

        Set<String> result;
        Set<String> resultCheck = new HashSet<>();
        int checkTime = 200;
        long time = System.currentTimeMillis();

        if (programParameters.isSorted()) {
            result = new TreeSet<>();
        } else {
            result = new HashSet<>();
        }
        if (generateSource == Controller.GenerateSource.NEW_ANALYSER) {
            createAnalyser(programParameters);
        }
        while (result.size() < programParameters.getNumberOfWords()) {
            firstCharAsInInput = programParameters.isFirstCharAsInInput();
            lastCharAsInInput = programParameters.isLastCharAsInInput();
            maxWordLength = programParameters.getMaxWordLength();
            minWordLength = programParameters.getMinWordLength();
            output = new StringBuilder();
            wordLength = getWordLength(minWordLength, maxWordLength);

            makeWord();
            if (output == null) {
                continue;
            }
            String tempWord = output.toString();

            if (skipThisWordWhenShorterThanMin(minWordLength, tempWord)) {
                continue;
            }
            if (System.currentTimeMillis() - time < checkTime) {
                if (maxWordLength != 0 && tempWord.length() > maxWordLength) {
                    result.add(tempWord.substring(0, maxWordLength - 1));
                } else {
                    result.add(tempWord);
                }
            } else if (System.currentTimeMillis() - time < checkTime*2) {
                if (maxWordLength != 0 && tempWord.length() > maxWordLength) {
                    resultCheck.add(tempWord.substring(0, maxWordLength - 1));
                } else {
                    resultCheck.add(tempWord);
                }
            } else if (!result.containsAll(resultCheck)) {
                result.addAll(resultCheck);
                time = System.currentTimeMillis();
                checkTime *= 2;
            } else {
                break;
            }
        }
        return result;
    }

    private void makeWord() {
        addFirstChar();
        for (int i = 1; i <= wordLength - 1; i++) {
            if (!isLastCharInCharsCount()) {
                output = null;
                return;
            }
            ArrayList<Character> charsCountList = analyser.getCharsCount().get(getLastChar());  //TODO extract array
            output.append(charsCountList.toArray()[random.nextInt(charsCountList.toArray().length)]);
        }
        if (lastCharAsInInput && isLastCharNotInLastCharsList()) {
            output = null;
        }
    }

    private boolean isLastCharNotInLastCharsList() {
        return !analyser.getLastChars().contains(getLastChar());
    }

    private boolean isLastCharInCharsCount() {
        return analyser.getCharsCount().containsKey(getLastChar());
    }

    private char getLastChar() {
        return output.toString().toCharArray()[output.length() - 1];
    }

    private void addFirstChar() {
        if (firstCharAsInInput) {
            output.append(analyser.getFirstChars().toArray()[random.nextInt(analyser.getFirstChars().toArray().length)]);
        } else {
            output.append(analyser.getCharsCount().keySet().toArray()[random.nextInt(analyser.getCharsCount().keySet().toArray().length)]);
        }
    }

    private int getWordLength(int minWordLength, int maxWordLength) {
        int wordLength;
        if (minWordLength != 0 && maxWordLength != 0) {
            wordLength = random.nextInt((maxWordLength - minWordLength) + 1) + minWordLength;
        } else {
            wordLength = analyser.getWordsLengths().get(random.nextInt(analyser.getWordsLengths().toArray().length));
        }
        return wordLength;
    }

    private boolean skipThisWordWhenShorterThanMin(int minWordLength, String tempWord) {
        return minWordLength != 0 && tempWord.length() < minWordLength;
    }

    void createAnalyser(ProgramParameters programParameters) {
        if (programParameters.getAnalyser() != null) {
            boolean analysersAreTheSame = programParameters.getAnalyser().getHashOfInput() == this.analyser.getHashOfInput();
            boolean inputsAreTheSame = (programParameters.getInput().hashCode() == this.analyser.getHashOfInput());

            if (analysersAreTheSame || inputsAreTheSame) {
                return;
            }
        } else if (programParameters.getInput().isEmpty()) {
            checkFileFormat(programParameters);
        } else {
            this.analyser = new Analyser();
            analyser.analyze(programParameters.getInput());
        }
    }

    private void checkFileFormat(ProgramParameters programParameters) {
        String path = programParameters.getPath();
        File file = new File(path);

        if (file.getName().endsWith(".txt")) {
            this.analyser = new LoaderWords().load(path);
        } else if (file.getName().endsWith(".bin")) {
            this.analyser = new LoaderSeed().load(path);
        }
    }

    Analyser getAnalyser() {
        return analyser;
    }

    void setAnalyser(Analyser analyser) {
        this.analyser = analyser;
    }
}
