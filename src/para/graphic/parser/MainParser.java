package para.graphic.parser;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.HashMap;

import para.graphic.shape.ShapeManager;
import para.graphic.target.Target;

public class MainParser implements MetaParser{
  private Target target;
  private ShapeManager shapemanager;
  protected HashMap<String,MetaParser> map;
  public MainParser(Target tgt, ShapeManager sm){
    target = tgt;
    shapemanager = sm; 
    map = new HashMap<String,MetaParser>();
    map.put("shape",new ShapeManagerParser(shapemanager));
    map.put("target",new TargetParser(target,shapemanager));
    map.put("reset",new ResetParser());
  }

  @Override
  public void parse(Scanner s){
    MetaParser mp;
    try{
      while(s.hasNext()){
        mp = map.get(s.next());
        mp.parse(s);
      }
    }catch(NoSuchElementException ex){
      return;
    }
  }

  class ResetParser implements MetaParser{
    public void parse(Scanner s){
      shapemanager.clear();
    }
  }
}
