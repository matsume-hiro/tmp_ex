package para.graphic.target;
import para.graphic.shape.Attribute;
import para.graphic.shape.ShapeManager;
import javafx.scene.image.Image;
import java.awt.image.*;

/**
 *  出力装置のインタフェース
 */
public interface Target{
  /** 出力装置のバッファを無地の初期状態にする
   */
  public void clear();
  /** 出力装置のバッファに出力する
   *  @param sm 図形の集合
   */
  public void draw(ShapeManager sm);
  public void draw(Target t, ShapeManager sm);
  public void drawCore(ShapeManager sm);
  //public BufferedImage getBufferedImage();
  public BufferedImage copyBufferedImage();
  public BufferedImage copyBufferedImage3Channel();
  public Image copyImage();
  /** 出力装置を初期化する */
  public void init();
  /** 出力装置を終了させる */
  public void finish();
  /** 出力装置に実際に描画する */
  public void flush();
  /** 円を出力する．
   *  @param id 円の識別子
   *  @param x  円の中心のx座標
   *  @param y  円の中心のy座標
   *  @param r  円の半径
   *  @param attr 円の属性
   */
  public void drawCircle(int id,int x, int y, int r, Attribute attr);
  /** 矩形を出力する．
   *  @param id 矩形の識別子
   *  @param x  矩形の左上隅のx座標
   *  @param y  矩形の左上隅のy座標
   *  @param w  矩形の幅
   *  @param h  矩形の高さ
   *  @param attr 矩形の属性
   */
  public void drawRectangle(int id, int x, int y, int w, int h, Attribute attr);
  /** 画像図形を出力する．
   *  @param id 画像図形の識別子
   *  @param x  画像図形の左上隅のx座標
   *  @param y  画像図形の左上隅のy座標
   *  @param img  画像情報
   *  @param attr 画像図形の属性
   */
  public void drawImage(int id, int x, int y, BufferedImage img, Attribute attr);
  /** カメラの画像図形を出力する．
   *  @param id 画像図形の識別子
   *  @param x  画像図形の左上隅のx座標
   *  @param y  画像図形の左上隅のy座標
   *  @param attr 画像図形の属性
   */
  public void drawCamera(int id, int x, int y, Attribute attr);
  /** 数字を出力する．
   *  @param id 数字の識別子
   *  @param x  数字の中心のx座標
   *  @param y  数字の中心のy座標
   *  @param r  数字の大きさ
   *  @param number 数字
   *  @param attr 数字の属性
   */
  public void drawDigit(int id, int x, int y, int r, int number,Attribute attr);
}
