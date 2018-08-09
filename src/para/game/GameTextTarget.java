package para.game;

import java.io.PrintStream;
import java.io.OutputStream;
import java.io.InputStream;
import para.graphic.target.*;

public class GameTextTarget extends TextTarget{
  public GameTextTarget(OutputStream stream){
    super(stream);
  }

  public GameTextTarget(PrintStream stream){
    super(stream);
  }

  public GameTextTarget(int width, int height, OutputStream stream){
    super(width, height, stream);
  }

  public GameTextTarget(int width, int height, PrintStream stream){
    super(width, height, stream);
  }

  public void state(int state){
    pstream.println("game state "+state);
  }

  public void gamerstate(int state){
    pstream.println("game gamer "+state);
  }

}
