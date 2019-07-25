package similarwordsgenerator;

import java.io.*;
import java.util.List;

public class Memento implements Serializable {

    private Parameters parameters;
    private List<String> wordsToSave;

    public Memento() {
    }

    public Memento(Parameters parameters, List<String> wordsToSave, File userHomeProgram, String mementoName) {

        this.parameters = parameters;
        this.wordsToSave = wordsToSave;

        try (

                ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(userHomeProgram + File.separator + mementoName))

        ) {

            os.writeObject(this);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Memento loadMemento (File userHomeProgram, String mementoName) {

        Memento memento = new Memento();

        try (
                ObjectInputStream os = new ObjectInputStream(new FileInputStream(userHomeProgram + File.separator + mementoName))
        ) {

            memento = (Memento) os.readObject();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return memento;
    }


    public Parameters getParameters() {
        return parameters;
    }

    public List<String> getWordsToSave() {
        return wordsToSave;
    }
}
