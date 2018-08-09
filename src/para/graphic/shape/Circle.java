package para.graphic.shape;
import para.graphic.target.Target;

/** 
 * 円
 */
public class Circle extends Shape{
  /** 中心のx座標 */
  private int x;
  /** 中心のy座標 */
  private int y;
  /** 半径 */
  private int r;
  /** 属性 */
  private Attribute attr;

  /** 円を生成する
   *  @param id 識別子
   *  @param x  中心のx座標
   *  @param y  中心のy座標
   *  @param r  半径
   */
  public Circle(int id, int x, int y, int r){
    this(id, x, y, r, null);
  }

  /** 円を生成する
   *  @param id 識別子
   *  @param x  中心のx座標
   *  @param y  中心のy座標
   *  @param r  半径
   *  @param attr  属性
   */
  public Circle(int id, int x, int y, int r, Attribute attr){
    super(id, x-r,x+r,y-r,y+r);
    this.x =x; 
    this.y = y;
    this.r = r;
    this.attr = attr;
  }

  /** 属性を取得する */
  @Override
  public Attribute getAttribute(){
    return attr;
  }

  /** この円を出力する
   *  @param target 出力先
   */
  @Override
  public void draw(Target target){
    target.drawCircle(id, x, y, r, attr);
  }

  public int getX(){
    return x;
  }

  public int getY(){
    return y;
  }
}
