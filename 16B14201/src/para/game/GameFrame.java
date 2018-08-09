package para.game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.util.Scanner;

import para.graphic.target.*;

/**
 * 簡単なゲームを簡便に作成するための基底クラス。
 * ゲームの開始時の選択や、ゲーム中のユーザ入力イベントのハンドラの設定を行う。
 * マウス入力の値は
 * 
 */
public class GameFrame extends Application{
  /** ゲームの表示領域 */
  protected final JavaFXCanvasTarget canvas;
  /** ウィンドウのタイトル文字列 */
  protected volatile String title=null;
  /** 左ボタンに対応するキーボードのFキーの状態. 1 ならば on 0ならば　off */
  protected volatile int lefton=0;
  /** 右ボタンに対応するキーボードのJキーの状態. 1 ならば on 0ならば　off */
  protected volatile int righton=0;
  protected volatile int state;
  protected volatile int gamerstate;
  /** マウスでクリックした最後の値 */
  protected final SynchronizedPoint xy;
  final int WIDTH; 
  final int HEIGHT;
  
  /**
   * ゲーム領域が 400 × 600 のゲームフレームを用意する
   */
  public GameFrame(){
    this(400,600);
  }

  /**
   * ゲーム領域が width × height のゲームフレームを用意する
   * @param width 単位ピクセル
   * @param height 単位ピクセル
   */
  public GameFrame(int width, int height){
    WIDTH = width;
    HEIGHT = height;
    canvas = new JavaFXCanvasTarget(WIDTH, HEIGHT);
    xy = new SynchronizedPoint();
  }

  /**
   * ゲーム領域を与えられた canvasとするゲームフレームを用意する
   * @param canvas ゲーム領域
   */
  public GameFrame(JavaFXCanvasTarget canvas){
    WIDTH = (int)canvas.getWidth();
    HEIGHT = (int)canvas.getHeight();
    this.canvas = canvas;
    xy = new SynchronizedPoint();
  }

  /**
   * スタートボタンを押されると実行されるメソッド。
   * 継承クラスでこのメソッドを上書きしてゲームの処理を開始するとよい。
   * このクラスでは、左右のボタン(F キー と J キー)が押されているかを標準出力に出力する
   * 処理が例として定義されている。
   * @param value ユーザが設定したウィンドウ左下の値が入力される
   */
  public void gamestart(int value){
    Thread th = new Thread(()->{
        int i=0;
        while(true){
          try{
            Thread.sleep(100);
          }catch(InterruptedException ex){
          }
          if((lefton ==1 || righton ==1)){
            i = i-lefton+righton;
            System.out.print(i+" ");
          }
        }
      });
    th.start();
  }

  private void setLeft(){
    lefton = 1;
  }
  private void unsetLeft(){
    lefton = 0;
  }
  private void setRight(){
    righton = 1;
  }
  private void unsetRight(){
    righton = 0;
  }

  void setState(int state){
    this.state = state;
  }

  void setGamerState(int state){
    this.gamerstate = state;
  }
  
  /**
   * JavaFXアプリケーション・スレッドから呼び出されるメソッド.
   * ウィンドウを開き、待機状態とする
   * @param stage JavaFXアプリケーション・スレッドから渡される最上位のJavaFXコンテナ
   */
  public void start(Stage stage){
    stage.setTitle(title);
    VBox root = new VBox();
    root.setAlignment(Pos.TOP_CENTER);
    root.requestFocus();
    Scene scene = new Scene(root);
    Button button = new Button("Start");
    HBox low = new HBox();
    Spinner<Integer> spinner = new Spinner<Integer>(1,4,1);
    stage.setOnCloseRequest(ev->{
        System.exit(0);
      });
    root.addEventHandler(KeyEvent.KEY_RELEASED, ev->{
        switch(ev.getCode()){
        case F: //"F" key
          unsetLeft();
          break;
        case J: //"J" key
          unsetRight();
          break;
        }
      });
    root.addEventHandler(KeyEvent.KEY_PRESSED, ev->{
        switch(ev.getCode()){
        case F: //"F" key
          setLeft();
          break;
        case J: //"J" key
          setRight();
          break;
        }
      });
    canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, ev->{
        xy.set((float)ev.getX(), (float)ev.getY(), System.currentTimeMillis());
      });
    canvas.setFocusTraversable(true);
    button.setPrefHeight(30);
    button.setPrefWidth(100);
    spinner.setPrefHeight(30);
    button.setOnAction(ev->{
        gamestart(spinner.getValue());
      });
    low.getChildren().add(spinner);
    low.getChildren().add(button);
    root.getChildren().add(canvas);
    root.getChildren().add(low);
    canvas.init();
    canvas.clear();
    canvas.flush();
    //stage.setWidth(canvas.getWidth());
    //stage.setHeight(canvas.getHeight()+30);
    stage.setScene(scene);
    stage.show();
  }
}
