package para.game;

import para.graphic.parser.MainParser;
import para.graphic.parser.MetaParser;
import para.graphic.target.Target;
import para.graphic.shape.ShapeManager;
import java.util.Scanner;

public class GameMainParser extends MainParser{
  public GameMainParser(GameFrame gf, Target tgt, ShapeManager sm){
    super(tgt, sm);
    map.put("game", new GameParser(gf));
  }

  class GameParser implements MetaParser{
    GameFrame gf;
    GameParser(GameFrame gf){
      this.gf = gf;
    }

    public void parse(Scanner s){
      switch(s.next()){
      case "state":
        gf.setState(s.nextInt());
        break;
      case "gamer":
        gf.setGamerState(s.nextInt());
        break;
      }
    }
  }
}
