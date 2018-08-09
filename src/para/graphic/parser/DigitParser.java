package para.graphic.parser;
import java.util.Scanner;

import para.graphic.shape.Attribute;
import para.graphic.shape.Digit;

class DigitParser implements ShapeParser{
  DigitParser(){
  }
  @Override
  public Digit parse(Scanner s, int id){
    int x = s.nextInt();
    int y = s.nextInt();
    int r = s.nextInt();
    int num = s.nextInt();
    Digit ret;
    Attribute attr=null;
    if(s.hasNext("Attribute")){
      attr = AttributeParser.parse(s);
    }
    ret = new Digit(id,x,y,r,num,attr);
    return ret;
  }
}
