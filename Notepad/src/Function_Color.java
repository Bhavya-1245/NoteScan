import java.awt.*;

public class Function_Color {
    Gui gu;

    public Function_Color(Gui gu){
        this.gu = gu;
    }

    public void changeColor(String color){

        switch(color){
            case "White" :
                gu.window.getContentPane().setBackground(Color.white);
                gu.textArea.setBackground(Color.white);
                gu.textArea.setForeground(Color.black);
                break;

            case "Black" :
                gu.window.getContentPane().setBackground(Color.black);
                gu.textArea.setBackground(Color.black);
                gu.textArea.setForeground(Color.white);
                break;

            case "Red" :
                gu.window.getContentPane().setBackground(Color.red);
                gu.textArea.setBackground(Color.red);
                gu.textArea.setForeground(Color.white);
                break;
        }
    }
}
