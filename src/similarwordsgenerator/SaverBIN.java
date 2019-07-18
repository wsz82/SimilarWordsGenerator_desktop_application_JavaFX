package similarwordsgenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaverBIN implements ISaver {

    @Override
    public void save(Analyser analyser, String path) {

        try (

                ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(path))

                ) {

            os.writeObject(analyser);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
