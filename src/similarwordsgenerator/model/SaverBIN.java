package similarwordsgenerator.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

class SaverBIN {

    void save(Analyser analyser, String path) {

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
