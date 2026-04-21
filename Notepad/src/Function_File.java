import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;


public class Function_File {

    Gui gu;
    String fileName;
    String fileAddress;

    public Function_File(Gui gu){
        this.gu = gu;
    }

    public void newFile() {

        gu.textArea.setText("");
        gu.window.setTitle("Untitled");
        fileName = null;
        fileAddress = null;
    }

    public void open(){

        FileDialog fd = new FileDialog(gu.window, "Open", FileDialog.LOAD);
        fd.setVisible(true);

        if(fd.getFile() != null){
            fileName = fd.getFile();
            fileAddress = fd.getDirectory();
            gu.window.setTitle(fileName);
        }

        try{
            BufferedReader br = new BufferedReader(new FileReader(fileAddress + fileName));

            gu.textArea.setText("");

            String line = null;

            while((line = br.readLine()) != null){

                gu.textArea.append(line + "\n");
            }
            br.close();

        }catch(Exception e){
            System.out.println("File Not Opened!");
        }
    }

    public void save(){

        if(fileName == null){
            saveAs();
        }else{
            try{
                FileWriter fw = new FileWriter(fileAddress + fileName);
                fw.write(gu.textArea.getText());
                gu.window.setTitle(fileName);
                fw.close();
            } catch (Exception e) {
                System.out.println("Something Wrong!");
            }
        }
    }

    public void saveAs(){
        FileDialog fd = new FileDialog(gu.window, "Save", FileDialog.SAVE);
        fd.setVisible(true);

        if(fd.getFile()!=null){
            fileName = fd.getFile();
            fileAddress = fd.getDirectory();
            gu.window.setTitle(fileName);
        }

        try{
            FileWriter fw = new FileWriter(fileAddress + fileName);
            fw.write(gu.textArea.getText());
            fw.close();
        } catch (Exception e) {
            System.out.println("Something Wrong!");
        }
    }

    public void exit(){
        System.exit(0);
    }
}
