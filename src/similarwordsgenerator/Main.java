package similarwordsgenerator;

public class Main {

    public static void main(String[] args) {

//        String[] names = new String[]{"Stefan", "Renata", "Zbyszek", "Wojtek", "Antek"};
//        String[] names = new String[]{"Józef", "Grażyna"};
//        String[] names = new String[]{"Józef"};
//        String[] names = new String[]{"ab", "babac", "cd"};
        Loader loader = new Loader();
        loader.load("D:\\curses.txt");
//        List<String> names = loader.load("D:\\TR\\Nazwy\\Dwemerowe ruiny Morrowind.txt");

        Analyser analyser = new Analyser(loader.getLoad());
//        analyser.compress(100);
//        Analyser analyser = new Analyser("D:\\names param.txt");

//        Saver saver = new Saver();
//        saver.save(analyser,"D:\\names param.txt");

        GeneratorParameters gp = new GeneratorParameters();
        gp.setSorted(true);
        gp.setNumberOfWords(50);
        gp.setMinWordLength(0);
        gp.setMaxWordLength(0);

        Generator generator = new Generator();
        generator.writeToConsole(generator.generate(analyser, gp));

    }
}
