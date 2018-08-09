package para.graphic.shape;
import java.awt.image.*;

import para.graphic.target.Target;

/**
 * 画像図形
 */
public class Image extends Shape{
  public static final int MAXWIDTH=1024;
  public static final int MAXHEIGHT=768;
  /** 左上隅のx座標 */
  private int x;
  /** 左上隅のy座標 */
  private int y;
  /** 画像情報 */
  private BufferedImage img;
  /** 属性 */
  private Attribute attr;

  /** 画像図形を生成する
   *  @param id 識別子
   *  @param x  左上隅のx座標
   *  @param y  左上隅のy座標
   *  @param img 画像情報
   *  @param a 属性
   */
  public Image(int id, int x, int y, BufferedImage img, Attribute a){
    super(id, x,x+img.getWidth(),y,y+img.getHeight());
    this.x = x;
    this.y = y;
    this.img = img;
    attr = a;
  }

  /** 属性を取得する */
  @Override
  public Attribute getAttribute(){
    return attr;
  }

  /** この画像を出力する．
   *  @param target 出力先
   */
  @Override
  public void draw(Target target){
    target.drawImage(id, x, y, img, attr);
  }
}
