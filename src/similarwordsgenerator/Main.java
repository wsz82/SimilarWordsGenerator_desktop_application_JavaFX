package similarwordsgenerator;

import java.util.List;

public class Main {

    public static void main(String[] args) {

//        String[] names = new String[]{"Stefan", "Renata", "Zbyszek", "Wojtek", "Antek"};
//        String[] names = new String[]{"Józef", "Grażyna"};
//        String[] names = new String[]{"Józef"};
//        String[] names = new String[]{"ab", "babac", "cd"};
        Loader loader = new Loader();
        loader.load("D:\\names.txt");
//        List<String> names = loader.load("D:\\TR\\Nazwy\\Dwemerowe ruiny Morrowind.txt");

        Analyser analyser = new Analyser(loader.getLoad());
//        Analyser analyser = new Analyser(names);

//        Saver saver = new Saver(analyser,"D:\\TR\\Nazwy\\Dwemerowe ruiny Morrowind_param.txt");

        Generator generator = new Generator();
        generator.writeToConsole(generator.generate(analyser, 5));

//        for ( String word : temp ) {
//
//            System.out.println(word);
//        }

    }
}
