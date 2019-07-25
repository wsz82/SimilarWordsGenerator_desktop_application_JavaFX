package similarwordsgenerator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LoaderWords implements ILoader{

    @Override
    public Analyser load (Path path) {

        List<String> loadList = new ArrayList<>();

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path.toString()), StandardCharsets.UTF_8))
                ){

            String temp;
            br.mark(3);

            if (br.read() != '\ufeff') br.reset();

            while ( (temp = br.readLine()) != null ) {

                if (!temp.equals("")) loadList.add(temp);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Analyser(loadList);
    }
}
