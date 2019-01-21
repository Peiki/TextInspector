import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextInspectorMain {
    public static void main(String[] args) {
        File directory;
        List<String> allFiles=new ArrayList<String>();
        directory=selectDir();

        searchAllFiles(directory,allFiles);
        createFile(directory.getPath(),allFiles);
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
            }
        }
    }

    private static void createFile(String path,List<String> allFiles){
        int choice;
        String filename=JOptionPane.showInputDialog(null, "Scegli il nome del file (senza estensione)");
        filename=path+'\\'+filename+".txt";
        File file = new File(filename);
        do{
            if (file.exists()) {
                choice = JOptionPane.showConfirmDialog(null, "Il file esiste già, sovrascriverlo?");
            }
            else{
                choice=0;
            }
            switch (choice){
                case 0:
                    writeOnFile(file,allFiles);
                    break;
                case 1:
                    createFile(path,allFiles);
                    break;
                default:
                    break;
            }
        }while(choice!=1 && choice!=0);

    }

    private static void writeOnFile(File file,List<String> allFiles){
        try{
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            String content;
            int countWords=0,countChars=0,j;

            List<String> words = new ArrayList<String>();
            List<Integer> occurrencies = new ArrayList<Integer>();

            char[] in = new char[70000];
            for (int i = 0; i < allFiles.size(); i++) {
                emptyArray(in);
                File f = new File(allFiles.get(i));
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);

                br.read(in);
                j=0;
                while(in[j]!='£'){
                    countChars++;
                    j++;
                }

                content=new String(in);
                String parts[]=content.split("\\s+");

                for(int h=0;h<parts.length;h++) {
                    addWord(removeSpecialCharacters(parts[h]),words,occurrencies);
                    countWords++;
                }
            }

            orderAll(words,occurrencies);

            //TODO ordine anche per parola

            bw.write("Numero di parole: "+Integer.toString(countWords));
            bw.newLine();
            bw.write("Numero di caratteri: "+Integer.toString(countChars));
            bw.newLine();
            bw.newLine();
            bw.write("Occorrenza delle parole: [num - word]");
            bw.newLine();

            for(int c=0;c<words.size();c++){
                bw.write(occurrencies.get(c)+" - "+words.get(c));
                bw.newLine();
            }

            bw.flush();
            bw.close();
            JOptionPane.showMessageDialog(null,"Processo Completato!");
        }catch(IOException e){
            JOptionPane.showMessageDialog(null,"Errore Fatale");
        }
    }

    private static void addWord(String word, List<String> words, List<Integer> occurrencies){
        int wordPosition=findWord(word,words);
        if(wordPosition==-1){
            if(!word.equals("")){
                words.add(word);
                occurrencies.add(1);
            }
        }
        else{
            occurrencies.set(wordPosition,occurrencies.get(wordPosition)+1);
        }
    }

    private static int findWord(String word, List<String> words){
        for(int i=0;i<words.size();i++) {
            if (words.get(i).equals(word)) {
                return i;
            }
        }
        return -1;
    }

    private static String removeSpecialCharacters(String word){
        String newWord="";
        for(int i=0;i<word.length();i++){
            if(Character.isLetter(word.charAt(i)) || Character.isDigit(word.charAt(i)))
                newWord+=word.charAt(i);
        }
        return newWord;
    }

    private static void emptyArray(char[] array){
        for(int i=0;i<array.length;i++)
            array[i]='£';
    }

    private static void orderAll(List<String> words, List<Integer> occurrencies){
        int key,i;
        String keyString;
        for(int j=1;j<words.size();j++){
            key=occurrencies.get(j);
            keyString=words.get(j);
            i=j-1;
            while(i>=0 && occurrencies.get(i)<key){
                occurrencies.set(i+1,occurrencies.get(i));
                words.set(i+1,words.get(i));
                i=i-1;
            }
            occurrencies.set(i+1,key);
            words.set(i+1,keyString);
        }
    }
}