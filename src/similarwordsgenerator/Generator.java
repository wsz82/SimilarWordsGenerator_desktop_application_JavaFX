package similarwordsgenerator;

import java.util.*;

class Generator {

    Generator() {
    }

    Set<String> generate(Analyser analyser, int numberOfWords) {

        Set<String> result = new TreeSet<>();

        while ( result.size() < numberOfWords ) {

            StringBuilder output = new StringBuilder();
            Random r = new Random();

            int wordLength = analyser.getWordLengths().get(r.nextInt(analyser.getWordLengths().toArray().length));

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

            result.add(output.toString());
        }

        return result;
    }

    void writeToConsole(Set<String> result) { //to another class

        for ( String word : result ) {

            System.out.println(word);
        }
    }
}
