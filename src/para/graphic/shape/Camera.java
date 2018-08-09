package para.graphic.shape;

import para.graphic.target.Target;

/** カメラで撮影された画像の矩形図形
 */
public class Camera extends Shape{
  public static final int WIDTH=320;
  public static final int HEIGHT=240;
  private int x,y;
  private Attribute attr;

  public Camera(int id, int x, int y){
    this(id, x, y, null);
  }
  public Camera(int id, int x, int y, Attribute a){
    super(id,x,x+WIDTH,y,y+HEIGHT);
    this.x =x; 
    this.y = y;
    attr = a;
  }

  @Override
  public Attribute getAttribute(){
    return attr;
  }

/** カメラ像を出力する
 */
  @Override
  public void draw(Target target){
    target.drawCamera(id, x, y, attr);
  }
}
