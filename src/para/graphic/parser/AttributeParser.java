package para.graphic.parser;
import java.util.*;

import para.graphic.shape.Attribute;

class AttributeParser{
  /*
    private final HashMap<String,ShapeParser> map;
    map = new HashMap<String,ShapeParser>();
    map.put("Circle", new CircleParser());
    map.put("Image", new ImageParser());
    map.put("Camera", new CameraParser());
  */
  private static final String[] words = {"Color","Fill","Iaux0",
                                         "Faux0","Faux1"};
  
  public static Attribute parse(Scanner s){
    s.next(); //read "Attribute"
    int r=0;
    int g=0;
    int b=0;
    boolean fill=true;
    int iaux0=0;
    float faux0=0;
    float faux1=0;
    int num;
    finish:
    while(true){
      for(num=0;num<words.length;num++){
        if(s.hasNext(words[num])){
          s.next();
          break;
        }
      }
      switch(num){
      case 0:
        r = s.nextInt();
        g = s.nextInt();
        b = s.nextInt();
        break;
      case 1:
        fill = s.nextBoolean();
        break;
      case 2:
        iaux0 = s.nextInt();
        break;
      case 3:
        faux0 = s.nextFloat();
        break;
      case 4:
        faux1 = s.nextFloat();
        break;
      default:
        break finish;
      }
    }
    Attribute ret = new Attribute(r, g, b, fill, iaux0, faux0, faux1);
    return ret;
  }
}
