package para.graphic.target;

import java.awt.image.BufferedImage;
import para.graphic.shape.*;

public class TranslateTarget extends DummyTarget{
  final TranslationRule rule;
  final ShapeManager translatedsm;

  private TranslateTarget(){
    rule=null;
    translatedsm=null;
  }

  public TranslateTarget(ShapeManager translatedsm, TranslationRule rule){
    this.translatedsm = translatedsm;
    this.rule = rule;
  }


  @Override
  /**
   * 引数で与えられた図形の集合のidを変更し、並行移動処理を行う.
   * smを使ったsynchronizedブロックが内包されている
   * @param sm 処理対象の図形の集合
   */
  public void draw(ShapeManager sm){
    synchronized(sm){
      translatedsm.clear();
      sm.draw(this);
    }
  }

  public void drawCircle(int id, int x, int y, int r, Attribute attr){
    int newid  = rule.calcTranslateID(id);
    Vec2 xy = rule.calcTranslateXY(x,y);
    translatedsm.put(new Circle(newid, (int)xy.data[0],(int)xy.data[1], r,attr));
  }

  public void drawRectangle(int id, int x, int y, int w, int h, 
                            Attribute attr){
    int newid  = rule.calcTranslateID(id);
    Vec2 xy = rule.calcTranslateXY(x,y);
    translatedsm.put(new Rectangle(newid, (int)xy.data[0], (int)xy.data[1],
                                  w, h, attr));
  }

  public void drawImage(int id, int x, int y, BufferedImage img, 
                        Attribute attr){
    int newid =  rule.calcTranslateID(id);
    Vec2 xy = rule.calcTranslateXY(x,y);
    translatedsm.put(new Image(newid,(int)xy.data[0],(int)xy.data[1], img,attr));
  }

  public void drawCamera(int id, int x, int y, Attribute attr){
    int newid =  rule.calcTranslateID(id);
    Vec2 xy = rule.calcTranslateXY(x,y);
    translatedsm.put(new Camera(newid,(int)xy.data[0],(int)xy.data[1], attr));
  }

  public void drawDigit(int id, int x, int y, int r, int number,Attribute attr){
    int newid =  rule.calcTranslateID(id);
    Vec2 xy = rule.calcTranslateXY(x,y);
    translatedsm.put(new Digit(newid,(int)xy.data[0], (int)xy.data[1],
                               r, number, attr));
  }
}
