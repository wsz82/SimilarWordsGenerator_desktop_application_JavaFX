package similarwordsgenerator;

import java.io.*;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class Loader {

    private List<String> load;

    Loader () {
    }

    List<String> load (String path) {

        List<String> loadList = new ArrayList<>();

        try (
                BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))
                ){

            String temp;

            while ( (temp = fr.readLine()) != null ) {

                loadList.add(temp);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.load = loadList;

    }
}
