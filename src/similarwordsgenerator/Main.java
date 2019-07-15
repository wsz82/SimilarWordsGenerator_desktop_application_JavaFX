package similarwordsgenerator;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        ILoader ws = new WordsLoader();
//        List<String> names = wordsLoader.load("D:\\TR\\Nazwy\\Dwemerowe ruiny Morrowind.txt");

//        Analyser analyser = ws.load("D:\\curses.txt");

//        analyser.compress(100);

//        ISaver rs = new SaverSWG();
//        rs.save(analyser,"D:\\SWGcurses");        //broken

//        ILoader il = new LoaderSWG();
//        il.load("D:\\SWGcurses.swg");             //broken

//        ISaver is = new SaverBIN();
//        is.save(analyser, "D:\\ANcurses.bin");

//        ILoader  il = new LoaderBIN();
//        Analyser analyser = il.load("D:\\ANcurses.bin");

        Generator generator = new Generator("D:\\curses.txt", false, true, false, 50, 0, 0);
        generator.writeToConsole(generator.generate());

    }
}
