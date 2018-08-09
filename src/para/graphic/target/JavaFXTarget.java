package para.graphic.target;
import para.graphic.shape.Attribute;
import para.graphic.shape.ShapeManager;
import para.graphic.shape.Camera;

import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.lang.IllegalStateException;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Paint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
//import javafx.scene.paint.Color; // not Color in javafx module
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.Graphics;

import javax.imageio.*;
import java.io.*;

public class JavaFXTarget implements Target{
  int WIDTH;
  int HEIGHT;
  static AtomicInteger counter;
  JavaFXCanvasTarget canvas;
  BufferedImage buffer;
  Graphics graphics;
  String title;
  Group root;
  Scene scene;
  Stage stage;
  WritableImage image;

  static{
    try{
      //  Platform.startup​(()->{}); //<- do not use this (trouble on Mac OS)
      Platform.startup​(new Runnable(){public void run(){}});
    }catch(IllegalStateException e){
    }
  }
  
  public JavaFXTarget(){
    this("");
  }

  public JavaFXTarget(String title){
    this(title, 320, 240);
  }
  
  public JavaFXTarget(String title, int width, int height){
    WIDTH = width;
    HEIGHT = height;
    this.title = title;
    counter = new AtomicInteger();
    canvas = new JavaFXCanvasTarget(width, height);
    image = new WritableImage(width, height);
    root = new Group();
    scene = new Scene(root, width, height);//, Color.BLACK);
    root.getChildren().add(canvas);
    buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    graphics = buffer.getGraphics();
    graphics.setColor(Color.BLACK);
  }
  
  public void clear(){
    canvas.clear();
  }

  public void draw(final ShapeManager sm){
    sm.draw(this);
  }

  public void draw(final Target t, final ShapeManager sm){
    t.drawCore(sm);
  }

  public void drawCore(final ShapeManager sm){
    canvas.draw(sm);
  }

  // public BufferedImage getBufferedImage(){
  //   return canvas.getBufferedImage();
  // }

  public BufferedImage copyBufferedImage(){
    return canvas.copyBufferedImage();
  }

  @Override
  public BufferedImage copyBufferedImage3Channel(){
    return canvas.copyBufferedImage3Channel();
  }

  public Image copyImage(){
    return canvas.copyImage();
  }
  
  public void init(){
    Platform.runLater(new Runnable(){
        public void run(){
          counter.incrementAndGet();
          stage = new Stage();
          stage.setTitle(title);
          stage.setOnCloseRequest(ev->{//ev.consume();
              if(counter.decrementAndGet()==0){
                System.exit(0);
              }
            });
          stage.setScene(scene);
          scene.setOnKeyPressed(ev->{
              if(ev.getCode()== KeyCode.ESCAPE){
                System.exit(0);
              };
            });
          stage.show();
        }
      });
  }

  public void addKeyPressHandler(EventHandler<? super KeyEvent> hander){
    scene.addEventHandler(KeyEvent.KEY_PRESSED, hander);
  }
  
  public void addKeyReleasedHandler(EventHandler<? super KeyEvent> hander){
    scene.addEventHandler(KeyEvent.KEY_RELEASED, hander);
  }
  
  public void finish(){
    Platform.runLater(new Runnable(){
        public void run(){
          stage.hide();
        };
      });
    if(counter.decrementAndGet()==0){
      System.exit(0);
    }
  }

  public void flush(){
    canvas.flush();
  }

  /** 
   *  円を描画する
   */
  @Override
  public void drawCircle(int id,int x, int y, int r, Attribute attr){
    canvas.drawCircle(id, x, y, r, attr);
  }
  /** 
   *  矩形を描画する
   */
  @Override
  public void drawRectangle(int id,int x,int y,int w,int h, 
      Attribute attr){
    canvas.drawRectangle(id, x, y, w, h, attr);
  }

  /** 
   *  画像図形を描画する
   */
  @Override
  public void drawImage(int id, int x, int y, BufferedImage img, 
                        Attribute attr){
    canvas.drawImage(id, x, y, img, attr);
  }

  /** 
   *  カメラの画像図形を描画する
   */
  public void drawCamera(int id, int x, int y, Attribute attr){
    canvas.drawCamera(id, x, y, attr);
  }

  /** 
   *  数字を描画する
   */
  public void drawDigit(int id, int x, int y, int r, int number,Attribute attr){
    canvas.drawDigit(id ,x, y, r, number, attr);
  }
}
