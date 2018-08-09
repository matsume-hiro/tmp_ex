package para.graphic.target;

import java.awt.image.BufferedImage;
import para.graphic.shape.Attribute;
import para.graphic.shape.ShapeManager;
import javafx.scene.image.Image;

/**
 * 何も出力しない出力装置. {@link Target}の実装に便利である
 */
public class DummyTarget implements Target{
  public void draw(ShapeManager sm){
    sm.draw(this);
  }
  public void draw(Target t, ShapeManager sm){
    t.drawCore(sm);
  }
  public void drawCore(ShapeManager sm){
    draw(sm);
  }
  public BufferedImage getBufferedImage(){return null;}
  public BufferedImage copyBufferedImage(){return null;}
  public BufferedImage copyBufferedImage3Channel(){return null;}
  public Image copyImage(){return null;}
  public void init(){}
  public void finish(){}
  public void flush(){}
  public void clear(){}
  public void drawCircle(int id,int x, int y, int r, Attribute attr){}
  public void drawRectangle(int id, int x, int y, int w, int h, Attribute attr){}
  public void drawImage(int id, int x, int y, BufferedImage img,
                        Attribute attr){}
  public void drawCamera(int id, int x, int y, Attribute attr){}
  public void drawDigit(int id, int x, int y, int r, int number,
                         Attribute attr){}
}
