package similarwordsgenerator;

public class Main {

    public static void main(String[] args) {

//        String[] names = new String[]{"Stefan", "Renata", "Zbyszek", "Wojtek", "Antek"};
//        String[] names = new String[]{"Józef", "Grażyna"};
//        String[] names = new String[]{"Józef"};
//        String[] names = new String[]{"ab", "babac", "cd"};
        WordsLoader wordsLoader = new WordsLoader();
        wordsLoader.load("D:\\curses.txt");
//        List<String> names = wordsLoader.load("D:\\TR\\Nazwy\\Dwemerowe ruiny Morrowind.txt");

        Analyser analyser = new Analyser(wordsLoader.getLoad());

//        RatioLoader rl = new RatioLoader();
//        Analyser analyser = rl.loadRatio("D:\\cursesParam.txt");

//        analyser.compress(100);

//        RatioSaver rs = new RatioSaver();
//        rs.save(analyser,"D:\\namesParam.txt");

        GeneratorParameters gp = new GeneratorParameters();
        gp.setSorted(false);
        gp.setFirstCharAsInInput(true);
        gp.setLastCharAsInInput(false);
        gp.setNumberOfWords(50);
        gp.setMinWordLength(0);
        gp.setMaxWordLength(0);

        Generator generator = new Generator();
        generator.writeToConsole(generator.generate(analyser, gp));

    }
}
