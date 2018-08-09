package para.graphic.parser;
import java.util.Scanner;

import para.graphic.shape.Attribute;
import para.graphic.shape.Camera;

class CameraParser implements ShapeParser{
  CameraParser(){
  }
  @Override
  public Camera parse(Scanner s, int id){
    int x = s.nextInt();
    int y = s.nextInt();
    Attribute attr=null;
    if(s.hasNext("Attribute")){
      attr = AttributeParser.parse(s);
    }
    Camera ret = new Camera(id,x,y,attr);
    return ret;
  }
}
