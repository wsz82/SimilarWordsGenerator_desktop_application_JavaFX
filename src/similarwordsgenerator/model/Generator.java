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

            makeWord();
            if (output == null) {
                if (System.currentTimeMillis() - time > checkTime) {
                    break;
                }
                continue;
            }
            String tempWord = output.toString();

            if (System.currentTimeMillis() - time < checkTime) {
                result.add(tempWord);
            } else if (System.currentTimeMillis() - time < checkTime*2) {
                resultCheck.add(tempWord);
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
        output = new StringBuilder();
        setWordLength();
        addFirstChar();
        for (int i = 1; i <= wordLength - 1; i++) {
            if (!isLastCharInCharsCount()) {
                output = null;
                return;
            }
            ArrayList<Character> charsCountList = analyser.getCharsCount().get(getLastChar());
            Character[] charsCountArr = charsCountList.toArray(new Character[0]);
            output.append(charsCountArr[random.nextInt(charsCountArr.length)]);
        }
        boolean isLongerThanMaxWordLength = (maxWordLength != 0 && output.length() > maxWordLength);
        boolean isShorterThanMinWordLength = (minWordLength != 0 && output.length() < minWordLength);
        if ((lastCharAsInInput && isLastCharNotInLastCharsList()) || isLongerThanMaxWordLength  || isShorterThanMinWordLength) {
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
            Character[] firstCharsArr = analyser.getFirstChars().toArray(new Character[0]);
            output.append(firstCharsArr[random.nextInt(firstCharsArr.length)]);
        } else {
            Character[] charsCountKeySetArr = analyser.getCharsCount().keySet().toArray(new Character[0]);
            output.append(charsCountKeySetArr[random.nextInt(charsCountKeySetArr.length)]);
        }
    }

    private void setWordLength() {
        if (minWordLength != 0 && maxWordLength != 0) {
            chooseFromManualInputs();
        } else if (minWordLength != 0) {
            checkIfMinWordLengthIsHigherThanMaxDefault();
        } else if (maxWordLength != 0) {
            checkIfMaxWordLengthIsShorterThanMinDefault();
        } else {
            chooseDefaultWordLength();
        }
    }

    private void checkIfMaxWordLengthIsShorterThanMinDefault() {
        TreeSet<Integer> sortedWordLengths = new TreeSet<>(analyser.getWordsLengths());
        int smallestNumber = sortedWordLengths.first();

        if (maxWordLength < smallestNumber) {
            wordLength = random.nextInt(maxWordLength) + 1;
        } else {
            chooseDefaultWordLength();
        }
    }

    private void checkIfMinWordLengthIsHigherThanMaxDefault() {
        TreeSet<Integer> sortedWordLengths = new TreeSet<>(analyser.getWordsLengths());
        int highestNumber = sortedWordLengths.last();

        if (minWordLength > highestNumber) {
            wordLength = minWordLength;
        } else {
            chooseDefaultWordLength();
        }
    }

    private void chooseFromManualInputs() {
        wordLength = random.nextInt((maxWordLength - minWordLength) + 1) + minWordLength;
    }

    private void chooseDefaultWordLength() {
        wordLength = analyser.getWordsLengths().get(random.nextInt(analyser.getWordsLengths().toArray().length));
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
