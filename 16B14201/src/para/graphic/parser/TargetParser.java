package para.graphic.parser;

import java.util.Scanner;
import java.util.HashMap;

import para.graphic.shape.ShapeManager;
import para.graphic.target.Target;


class TargetParser implements MetaParser{
  final Target target;
  final ShapeManager shapemanager;
  HashMap<String,MetaParser> map;
  TargetParser(Target tgt,ShapeManager sm){
    target = tgt;
    shapemanager = sm;
    map = new HashMap<String,MetaParser>();
    map.put("draw", new DrawParser());
    map.put("flush", new FlushParser());
    map.put("clear", new ClearParser());
  }

  public void parse(Scanner s){
    String token = s.next();
    MetaParser mp = map.get(token);
    mp.parse(s);
  }

  class DrawParser implements MetaParser{
    public void parse(Scanner s){
      target.draw(shapemanager);
    }
  }

  class FlushParser implements MetaParser{
    public void parse(Scanner s){
      target.flush();
    }
  }

  class ClearParser implements MetaParser{
    public void parse(Scanner s){
      shapemanager.clear();
      target.clear();
    }
  }
  
}
