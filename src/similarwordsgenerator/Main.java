package similarwordsgenerator;

import java.util.List;

public class Main {

    public static void main(String[] args) {

//        String[] names = new String[]{"Stefan", "Renata", "Zbyszek", "Wojtek", "Antek"};
//        String[] names = new String[]{"Józef", "Grażyna"};
//        String[] names = new String[]{"Józef"};
//        String[] names = new String[]{"ab", "babac", "cd"};
//        Loader loader = new Loader();
//        List<String> names = loader.load("D:\\SWGexampleTwo.txt");

        Analyser analyser = new Analyser();
//        analyser.analyse(names);
//
//        Saver saver = new Saver();
//        saver.save(analyser,"D:\\SWGexampleParamTwo.txt");

        analyser.paramLoad("D:\\SWGexampleParamTwo.txt");

        Generator generator = new Generator();
        generator.writeToConsole(generator.generate(analyser, 10));

//
//        for ( String word : temp ) {
//
//            System.out.println(word);
//        }

    }
}
