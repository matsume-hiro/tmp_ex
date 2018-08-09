package para.graphic.parser;
import java.util.Scanner;

import para.graphic.shape.Attribute;
import para.graphic.shape.Circle;

class CircleParser implements ShapeParser{
  CircleParser(){
  }
  @Override
  public Circle parse(Scanner s, int id){
    int x = s.nextInt();
    int y = s.nextInt();
    int r = s.nextInt();
    Circle ret;
    Attribute attr=null;
    if(s.hasNext("Attribute")){
      attr = AttributeParser.parse(s);
    }
    ret = new Circle(id,x,y,r,attr);
    return ret;
  }

}
