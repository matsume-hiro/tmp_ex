package para.graphic.shape;
import para.graphic.target.Target;

/** 
 * 矩形
 */
public class Rectangle extends Shape{
  /** 属性 */
  private Attribute attr;
  /** 左上隅のx座標 */
  private int x;
  /** 左上隅のy座標 */
  private int y;
  /** 幅 */
  private int w;
  /** 高さ */
  private int h;

  /** 矩形を生成する．
   *  @param id 識別子
   *  @param x  左上隅のx座標
   *  @param y  左上隅のy座標
   *  @param w  幅
   *  @param h  高さ
   */
  public Rectangle(int id, int x, int y, int w, int h){
    this(id, x, y, w, h, null);
  }

  /** 矩形を生成する．
   *  @param id 識別子
   *  @param x  左上隅のx座標
   *  @param y  左上隅のy座標
   *  @param w  幅
   *  @param h  高さ
   *  @param attr  属性
   */
  public Rectangle(int id, int x, int y, int w, int h, Attribute attr){
    super(id, x,x+w,y,y+h);
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.attr = attr; 
  }
  
  /**
   * 属性を取得する
   */
  @Override
  public Attribute getAttribute(){
    return attr;
  }

  /** この矩形を出力する．
   *  @param target 出力先
   */
  @Override
  public void draw(Target target){
    target.drawRectangle(id, x, y, w, h, attr);
  }
}
