package similarwordsgenerator.model;

import java.io.File;
import java.util.*;

class Generator {

    private Analyser analyser;

    Generator () {
    }

    Set<String> generate(ProgramParameters programParameters, Controller.GenerateSource generateSource) {

        if (generateSource == Controller.GenerateSource.NEW_ANALYSER) {
            createAnalyser(programParameters);
        }

        Set<String> result;
        Set<String> resultCheck = new HashSet<>();
        int checkTime = 200;
        long time = System.currentTimeMillis();

        if (programParameters.isSorted()) {
            result = new TreeSet<>();
        } else {
            result = new HashSet<>();
        }

        while (result.size() < programParameters.getNumberOfWords()) {

            StringBuilder output = new StringBuilder();
            Random random = new Random();

            int maxWordLength = programParameters.getMaxWordLength();
            int minWordLength = programParameters.getMinWordLength();
            boolean firstCharAsInInput = programParameters.isFirstCharAsInInput();
            boolean lastCharAsInInput = programParameters.isLastCharAsInInput();
            int wordLength;

            wordLength = getWordLength(random, maxWordLength, minWordLength);

            output = addFirstChar(output, random, firstCharAsInInput);

            for (int i = 1; i <= wordLength; i++) {

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

                            output.append(tempLastChars.toArray()[random.nextInt(tempLastChars.toArray().length)]);
                        } else {
                            output.deleteCharAt(wordLength - 2);
                            wordLength += 2;
                        }
                    } else {
                        output.append(charsCountList.toArray()[random.nextInt(charsCountList.toArray().length)]);
                    }
                } else {
                    output.append(charsCountList.toArray()[random.nextInt(charsCountList.toArray().length)]);
                }
            }

            String tempWord = output.toString();

            if (skipThisWordWhenShorterThanMin(minWordLength, tempWord)) continue;

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

    private StringBuilder addFirstChar(StringBuilder output, Random random, boolean firstCharAsInInput) {
        if (firstCharAsInInput) {
            output.append(analyser.getFirstChars().toArray()[random.nextInt(analyser.getFirstChars().toArray().length)]);
        } else {
            output.append(analyser.getCharsCount().keySet().toArray()[random.nextInt(analyser.getCharsCount().keySet().toArray().length)]);
        }
        return output;
    }

    private int getWordLength(Random random, int maxWordLength, int minWordLength) {
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
