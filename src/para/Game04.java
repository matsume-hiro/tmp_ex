package para;

import java.util.Scanner;
import java.util.List;
import java.net.*;
import java.io.*;
import para.graphic.target.*;
import para.graphic.opencl.*;
import para.graphic.shape.*;
import para.graphic.parser.*;
import para.game.*;

public class Game04 extends GameFrame{
  TextTarget inputside;
  TargetImageFilter inputside_loser;
  final Target outputside;
  volatile Thread thread;
  InputStream istream;
  ShapeManager osm;
  ShapeManager ism;
  String serveraddress;
  static final int WIDTH=700;
  static final int HEIGHT=700;
  
  public Game04(){
    super(new JavaFXCanvasTarget(WIDTH, HEIGHT));
    title="Game04";
    outputside = canvas;
    osm = new OrderedShapeManager();
    ism = new ShapeManager();
  }

  public void init(){
    List<String> params = getParameters().getRaw();
    if (params.size()!=0){
      serveraddress = params.get(0);
    }else{
      serveraddress = "localhost";
    }
  }
  
  public void gamestart(int v){
    if(thread != null){
      return;
    }
    try{
      Socket socket;
      socket = new Socket(serveraddress, para.game.GameServerFrame.PORTNO);
      istream = socket.getInputStream();
      OutputStream ostream = socket.getOutputStream();
      inputside = new TextTarget(WIDTH, HEIGHT, ostream);
      inputside_loser = new TargetImageFilter(new TextTarget(WIDTH, HEIGHT, ostream),
      this, "imagefilter.cl", "Filter9" );
    }catch(IOException ex){
      System.err.print("To:"+serveraddress+" ");
      System.err.println(ex);
      System.exit(0);
    }
    
    /* ユーザ入力をサーバに送信するスレッド */
    thread = new Thread(()->{
        state = 1222560;
        int len = 60;
        int y = 225;
        int y_pre = 225;
        int f = 4;
        int x=150;
        Attribute attr = new Attribute(200,128,128);
        ism.put(new Camera(0, 0, 300,attr));
        ism.put(new Rectangle(v+1, x,30*v+y,len,20,attr));
        ism.put(new Rectangle(v+11, x,30*v+y_pre,len,20,attr));
        inputside.draw(ism);
        boolean alive = true;
        while(alive){
          try{
            Thread.sleep(80);
          }catch(InterruptedException ex){
            alive = false;
          }
          len = state%100;
          y_pre = y;
          y = (state%100000)/100;
          f = state/100000;
          if((lefton ==1 || righton ==1)){
            x = x-f*lefton/2+f*righton/2;
          }
          System.out.println(ism.remove(v+11));
          ism.put(new Rectangle(v+1, x,30*v+y,len,20,attr));
          if (y - y_pre != 0) {
            System.out.println(y+" "+y_pre);
            ism.put(new Rectangle(v+11, x,30*v+y_pre,len,20,attr));
          }
          if (gamerstate > 0) {
            inputside.clear();
            inputside_loser.setParameter(100);
            inputside_loser.draw(ism);
          } else {
            //inputside.setParameter(100);
            inputside.clear();
            inputside.draw(ism);
          }
        }
        thread = null;
      },"UserInput");
    thread.start();

    /* 受信したデータを画面に出力するスレッド */
    Thread thread2 = new Thread(()->{
        GameMainParser parser = new GameMainParser(this, outputside, osm);
        BufferedReader br = new BufferedReader(new InputStreamReader(istream));
        parser.parse(new Scanner(istream));//loop
        System.out.println("connection closed");
        thread.interrupt();
      });
    thread2.start();
  }
}
