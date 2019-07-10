package similarwordsgenerator;

import java.util.*;

class Generator {

    Generator() {
    }

    Set<String> generate(Analyser analyser, GeneratorParameters genParam) {

        Set<String> result;

        if (genParam.isSorted()) {

            result = new TreeSet<>();

        } else result = new HashSet<>();

        while ( result.size() < genParam.getNumberOfWords() ) {

            StringBuilder output = new StringBuilder();
            Random r = new Random();

            int wordLength;
            int maxWordLength = genParam.getMaxWordLength();
            int minWordLength = genParam.getMinWordLength();

            if ( minWordLength != 0 && maxWordLength != 0 ) {

                wordLength = r.nextInt((maxWordLength - minWordLength) + 1) + minWordLength;
            } else wordLength = analyser.getWordLengths().get(r.nextInt(analyser.getWordLengths().toArray().length));

            output.append(analyser.getFirstChars().toArray()[r.nextInt(analyser.getFirstChars().toArray().length)]);

            for ( int i = 1; i <= wordLength; i++) {

                for (Character ch : analyser.getCharsCount().keySet()) {

                    if (output.toString().length() == wordLength) {
                        break;
                    }

                    if (Character.toLowerCase(ch) == (Character.toLowerCase(output.toString().toCharArray()[output.toString().length() - 1]))) {

                        output.append(analyser.getCharsCount().get(ch).toArray()[r.nextInt(analyser.getCharsCount().get(ch).toArray().length)]);
                    }
                }
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

    void writeToConsole(Set<String> result) { //to another class

        for ( String word : result ) {

            System.out.println(word);
        }
    }
}
