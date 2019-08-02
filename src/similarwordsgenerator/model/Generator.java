package similarwordsgenerator.model;

import java.io.File;
import java.io.IOException;
import java.util.*;

class Generator {

    private Analyser analyser;

    Generator () {
    }

    Set<String> generate(ProgramParameters programParameters, Controller.GenerateSource generateSource) throws IOException {

        if (generateSource == Controller.GenerateSource.NEW_ANALYSER) {
            checkInput(programParameters);
        }

        Set<String> result;
        Set<String> resultCheck = new HashSet<>();
        int checkTime = 200;
        long time = System.currentTimeMillis();

        if (programParameters.isSorted()) {

            result = new TreeSet<>();

        } else result = new HashSet<>();

        while ( result.size() < programParameters.getNumberOfWords() ) {

            StringBuilder output = new StringBuilder();
            Random r = new Random();

            boolean firstCharAsInInput = programParameters.isFirstCharAsInInput();
            boolean lastCharAsInInput = programParameters.isLastCharAsInInput();
            int wordLength;
            int maxWordLength = programParameters.getMaxWordLength();
            int minWordLength = programParameters.getMinWordLength();

            if ( minWordLength != 0 && maxWordLength != 0 ) {

                wordLength = r.nextInt((maxWordLength - minWordLength) + 1) + minWordLength;

            } else wordLength = analyser.getWordsLengths().get(r.nextInt(analyser.getWordsLengths().toArray().length));

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
            if (System.currentTimeMillis() - time < checkTime) {
                if (maxWordLength != 0 && tempWord.length() > maxWordLength) {

                    result.add(tempWord.substring(0, maxWordLength - 1));

                } else result.add(tempWord);
            } else if (System.currentTimeMillis() - time < checkTime*2) {

                if (maxWordLength != 0 && tempWord.length() > maxWordLength) {

                    resultCheck.add(tempWord.substring(0, maxWordLength - 1));

                } else resultCheck.add(tempWord);
            } else if (!result.containsAll(resultCheck)) {
                result.addAll(resultCheck);
                time = System.currentTimeMillis();
                checkTime *= 2;
            } else break;
        }

        return result;
    }

    void checkInput(ProgramParameters programParameters) throws IOException {
        if (this.analyser != null && programParameters.getAnalyser()!= null && ((programParameters.getAnalyser().getHashOfInput() == this.analyser.getHashOfInput()) || (programParameters.getInput().hashCode() == this.analyser.getHashOfInput()))) {

            return;

        } else if (programParameters.getInput().isEmpty()) {

            String path = programParameters.getPath();
            File file = new File(path);

            if (file.getName().endsWith(".txt")) {
                this.analyser = new LoaderWords().load(path);
            } else if (file.getName().endsWith(".bin")) {
                this.analyser = new LoaderBIN().load(path);
            } else {
                throw new IOException();
            }

        } else {

            this.analyser = new Analyser();
            analyser.analyze(programParameters.getInput());
        }
    }

    Analyser getAnalyser() {
        return analyser;
    }

    void setAnalyser(Analyser analyser) {
        this.analyser = analyser;
    }
}
