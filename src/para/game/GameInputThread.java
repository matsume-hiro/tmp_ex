package para.game;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

import para.graphic.parser.MainParser;
import para.graphic.shape.ShapeManager;
import para.graphic.target.TranslateTarget;

public class GameInputThread extends Thread{
  final GameServerFrame gameserverframe;
  final InputStream istream;
  final int userID;
  private TranslateTarget translator;
  private ShapeManager[] data;

  public GameInputThread(InputStream is, int num, GameServerFrame gsf){
    userID = num;
    gameserverframe = gsf;
    istream = is;
  }

  public void init(TranslateTarget translator, ShapeManager[] data){
    this.data = data;
    this.translator = translator;
  }

  public int getUserID(){
    return userID;
  }
  
  public void run(){
    ShapeManager tmp = new ShapeManager();
    MainParser parser = new MainParser(translator, tmp);
    parser.parse(new Scanner(istream)); //loop
    for(int i=0; i<data.length;i++){
      data[i].clear();
    }
    gameserverframe.removeUser(userID);
  }
}
