package similarwordsgenerator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Loader {

    private List<String> load;

    Loader () {
    }

    List<String> load (String path) {

        List<String> loadList = new ArrayList<>();

        try (
                BufferedReader fileReader = new BufferedReader(new FileReader(path))
                ){

            while ( true ) {

                String line = fileReader.readLine();
                loadList.add(line);

                if ( line == null ) {

                    loadList.remove(loadList.remove(null));
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.load = loadList;

    }
}
