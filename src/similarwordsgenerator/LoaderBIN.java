package similarwordsgenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoaderBIN implements ILoader {

    @Override
    public Analyser load(String path) {

        Analyser analyser = new Analyser();

        try (
                ObjectInputStream os = new ObjectInputStream(new FileInputStream(path))
                ) {

            analyser = (Analyser) os.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return analyser;
    }
}
