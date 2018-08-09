package para.graphic.shape;
import para.graphic.target.Target;

/**
 * 数字
 */
public class Digit extends Shape{
  final int x; //中心のx座標
  final int y; //中心のy座標
  final int r; //大きさ
  final int number; //数字
  final Attribute attr; //属性
  
  /** 数字を生成する
   *  @param id 識別子
   *  @param x 中心のx座標
   *  @param y 中心のy座標
   *  @param r 大きさ
   *  @param number 数字
   *  @param a 属性
   */
  public Digit(int id, int x, int y, int r, int number, Attribute a){
    super(id, x-r,x+r,y-r,y+r);
    this.x = x; 
    this.y = y;
    this.r = r;
    this.number = number;
    this.attr = a;
  }

  /** 属性を取得する */
  @Override
  public Attribute getAttribute(){
    return attr;
  }

  /** この数字を出力する
   *  @param target 出力先
   */
  @Override
  public void draw(Target target){
    target.drawDigit(id, x, y, r, number, attr);
  }

}
