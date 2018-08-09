package para.graphic.parser;
import java.util.Scanner;

import para.graphic.shape.Attribute;
import para.graphic.shape.Rectangle;

class RectangleParser implements ShapeParser{
  RectangleParser(){
  }
  @Override
  public Rectangle parse(Scanner s, int id){
    int x = s.nextInt();
    int y = s.nextInt();
    int w = s.nextInt();
    int h = s.nextInt();
    Rectangle ret;
    Attribute attr=null;
    if(s.hasNext("Attribute")){
      attr = AttributeParser.parse(s);
    }
    ret = new Rectangle(id,x,y,w,h,attr);
    return ret;
  }

}
