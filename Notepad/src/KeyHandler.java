import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    Gui gu;

    public KeyHandler(Gui gu){
        this.gu = gu;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S){
            gu.file.save();
        }else if(e.isShiftDown() && e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
            gu.file.saveAs();
        }else if(e.isAltDown() && e.getKeyCode() == KeyEvent.VK_F){
            gu.menuFile.doClick();
        }else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_U){
            gu.edit.undo();
        }else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Y){
            gu.edit.redo();
        }else if(e.isAltDown() && e.getKeyCode() == KeyEvent.VK_F){
            gu.menuFile.doClick();
        }else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O){
            gu.file.open();
        }else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_N){
            gu.file.newFile();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
