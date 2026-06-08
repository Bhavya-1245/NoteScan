package com.noteS.dest;

public class Function_Edit {
    Gui gu;
    
    public Function_Edit(Gui gu){
        this.gu = gu;
    }

    public void undo(){
        if(gu.um.canUndo()) gu.um.undo();

    }

    public void redo(){
        if(gu.um.canRedo()) gu.um.redo();

    }
}
