package para.graphic.shape;
import para.graphic.target.Target;

/** 
 * 図形の抽象クラス
 */
abstract public class Shape{
  /** 識別子 */
  final protected int id;
  final protected int left,right,top,bottom;
  protected Shape(int id, int l, int r, int t, int b){
    left = l; 
    right = r;
    top = t;
    bottom =b;
    this.id = id;
  }

  /** 
   * ID番号を取得する 
   * @return ID
   */
  public int getID(){
    return id;
  }
  
  /** 
   *  属性を取得する
   * @return 属性
   */
  abstract public Attribute getAttribute();

  /** 図形を出力する
   *  @param target 出力装置
   */
  abstract public void draw(Target target);
}
