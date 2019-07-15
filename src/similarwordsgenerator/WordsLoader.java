package similarwordsgenerator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordsLoader implements ILoader{

    @Override
    public Analyser load (String path) {

        List<String> loadList = new ArrayList<>();

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))
                ){

            String temp;

            if (br.read() != '\ufeff') br.reset();

            while ( (temp = br.readLine()) != null ) {

                loadList.add(temp);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Analyser(loadList);

    }
}
