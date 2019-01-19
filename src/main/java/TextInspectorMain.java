import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TextInspectorMain {
    public static void main(String[] args) {
        File directory;
        List<String> allFiles=new ArrayList<String>();
        directory=selectDir();

        searchAllFiles(directory,allFiles);
        for (int i = 0; i < allFiles.size(); i++) {
            System.out.println(allFiles.get(i));
        }

        directory.getParentFile().mkdirs();
        //directory.createNewFile();

        System.out.println(allFiles.size());
    }

    private static File selectDir(){
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleziona la cartella contenente i file");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else {
            JOptionPane.showMessageDialog(null,"Errore, riprova.");
            selectDir();
        }
        return null;
    }

    private static void searchAllFiles(File dir,List<String> allFiles){
        File[] files=dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                searchAllFiles(files[i],allFiles);
            }
            else{
                allFiles.add(files[i].getPath());
                System.out.println(files[i]);
            }
        }
    }
}