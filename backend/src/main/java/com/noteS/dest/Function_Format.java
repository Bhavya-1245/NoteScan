package com.noteS.dest;

import java.awt.*;

public class Function_Format {

    Gui gu;
    Font arial, comicSans, timesNewRoman;
    String selectedFont;

    public Function_Format(Gui gu){
        this.gu = gu;
    }

    public void wordWrap(){

        if(gu.wordWrapOn == false){
            gu.wordWrapOn = true;
            gu.textArea.setLineWrap(true);
            gu.textArea.setWrapStyleWord(true);
            gu.iWrap.setText("Word Wrap: On");
        }
        else if(gu.wordWrapOn == true){
            gu.wordWrapOn = false;
            gu.textArea.setLineWrap(false);
            gu.textArea.setWrapStyleWord(false);
            gu.iWrap.setText("Word Wrap: Off");
        }
    }

    public void createFont(int fontSize){

        arial = new Font("Arial", Font.PLAIN, fontSize);
        comicSans = new Font("Comic Sans MS", Font.PLAIN, fontSize);
        timesNewRoman = new Font("TImes New Roman", Font.PLAIN, fontSize);

        setFont(selectedFont);
    }

    public void setFont(String font){
        selectedFont = font;

        switch(selectedFont){
            case "Arial": gu.textArea.setFont(arial); break;
            case "Comic Sans MS": gu.textArea.setFont(comicSans); break;
            case "Times New Roman": gu.textArea.setFont(timesNewRoman); break;
        }
    }
}
