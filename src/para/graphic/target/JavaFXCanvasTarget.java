package para.graphic.target;
import para.graphic.shape.Attribute;
import para.graphic.shape.ShapeManager;
import para.graphic.shape.Camera;

import javafx.application.Platform;
import javafx.stage.Stage;
import java.lang.IllegalStateException;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
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

/**
 *  JavaFXの Canvas を出力装置とするTargetの実装
 */
public class JavaFXCanvasTarget extends Canvas implements Target{
  final int WIDTH;
  final int HEIGHT;
  final GraphicsContext gc;
  final BufferedImage buffer;
  final Graphics graphics;
  final WritableImage image;
  SingleCamera camdev;
  BufferedImage camimage;
  
  public JavaFXCanvasTarget(){
    this(320, 240);
  }
  
  /**
   *  JavaFXCanvasTarget のインスタンスを作成
   *  @param width 　幅
   *  @param height　高さ
   */
  public JavaFXCanvasTarget(int width, int height){
    super(width, height);
    WIDTH = width;
    HEIGHT = height;
    camdev = SingleCamera.create(Camera.WIDTH, Camera.HEIGHT);
    camimage = camdev.createBufferedImage();
    image = new WritableImage(width, height);
    gc = getGraphicsContext2D();
    buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    graphics = buffer.getGraphics();
    graphics.setColor(Color.BLACK);
  }
  
  public synchronized void clear(){
    Color c = graphics.getColor();
    graphics.setColor(new Color(255,255,255,255));
    graphics.fillRect(0, 0, WIDTH, HEIGHT);
    graphics.setColor(c);
  }

  public void draw(final ShapeManager sm){
  /* What is happened, when ``synchronized'' is not used */ 
    sm.draw(this);
  }

  public void draw(final Target t, final ShapeManager sm){
    t.drawCore(sm);
  }

  public void drawCore(final ShapeManager sm){
    draw(sm);
  }
  /*
  public BufferedImage getBufferedImage(){
    return buffer;
  }
  */
  public synchronized BufferedImage copyBufferedImage(){
    return new BufferedImage(buffer.getColorModel(),
                             (WritableRaster)buffer.getData(),
                             buffer.isAlphaPremultiplied(),null);
  }

  @Override
  public synchronized BufferedImage copyBufferedImage3Channel(){
    BufferedImage bimage = new BufferedImage(WIDTH, HEIGHT,
                                            BufferedImage.TYPE_3BYTE_BGR);
    Graphics graphics = bimage.getGraphics();
    graphics.drawImage(this.buffer,0,0,null);  
    return bimage;
  }

  public Image copyImage(){
    synchronized(image){
      return new WritableImage(image.getPixelReader(), WIDTH, HEIGHT);
    }
  }
  
  public void init(){
  }

  public void addKeyPressHandler(EventHandler<? super KeyEvent> hander){
    setFocusTraversable(true);
    addEventHandler(KeyEvent.KEY_PRESSED, hander);
  }

  public void addKeyReleasedHandler(EventHandler<? super KeyEvent> hander){
    setFocusTraversable(true);
    addEventHandler(KeyEvent.KEY_RELEASED, hander);
  }
  
  public void finish(){
  }

  public synchronized void flush(){
    synchronized(image){
      SwingFXUtils.toFXImage(buffer, image);
      WritableImage tmp = new WritableImage(image.getPixelReader(),WIDTH,HEIGHT);
      Platform.runLater(()->{gc.drawImage(tmp,0,0);});
    }
  }

  private void setColor(Attribute attr){
    if(attr!=null){
      int c[]=attr.getColor();
      graphics.setColor(new java.awt.Color(c[0],c[1],c[2]));
    }
  }

  /** 
   *  円を描画する
   */
  @Override
  public synchronized void drawCircle(int id,int x, int y, int r,Attribute attr){
    setColor(attr);
    if(attr!=null && attr.getFill()){
      graphics.fillArc(x-r,y-r,r*2,r*2,0,360);
    }else{
      graphics.drawArc(x-r,y-r,r*2,r*2,0,360);
    }
  }
  /** 
   *  矩形を描画する
   */
  @Override
  public synchronized void drawRectangle(int id, int x, int y, int w, int h, 
                            Attribute attr){
    setColor(attr);
    if(attr!=null && attr.getFill()){
      graphics.fillRect(x, y, w, h); 
    }else{
      graphics.drawRect(x, y, w, h);
    }
  }

  /** 
   *  画像図形を描画する
   */
  @Override
  public synchronized void drawImage(int id, int x, int y, BufferedImage img, 
                        Attribute attr){
    setColor(attr);
    if(attr != null){
      if(attr.getFill()){
        graphics.fillRect(x, y, img.getWidth(), img.getHeight()); 
        graphics.drawImage(img,x,y,null);
        graphics.drawRect(x, y, img.getWidth(), img.getHeight()); 
      }else{
        graphics.drawImage(img,x,y,null);
        graphics.drawRect(x, y, img.getWidth(), img.getHeight()); 
      }
    }else{
      graphics.drawImage(img,x,y,null);
    } 
  }

  /** 
   *  カメラの画像図形を描画する
   */
  public synchronized void drawCamera(int id, int x, int y, Attribute attr){
    camdev.get(camimage);
    drawImage(id, x, y, camimage, attr);
  }

  /** 
   *  数字を描画する
   */
  public synchronized void drawDigit(int id, int x, int y, int r,
                                     int number, Attribute attr){
    setColor(attr);
    switch(number){
    case 0:
      drawDigit(x,y,r,true,true,true,false,true,true,true);
      break;
    case 1:
      drawDigit(x,y,r,false,false,true,false,false,true,false);
      break;
    case 2:
      drawDigit(x,y,r,true,false,true,true,true,false,true);
      break;
    case 3:
      drawDigit(x,y,r,true,false,true,true,false,true,true);
      break;
    case 4:
      drawDigit(x,y,r,false,true,true,true,false,true,false);
      break;
    case 5:
      drawDigit(x,y,r,true,true,false,true,false,true,true);
      break;
    case 6:
      drawDigit(x,y,r,true,true,false,true,true,true,true);
      break;
    case 7:
      drawDigit(x,y,r,true,false,true,false,false,true,false);
      break;
    case 8:
      drawDigit(x,y,r,true,true,true,true,true,true,true);
      break;
    case 9:
      drawDigit(x,y,r,true,true,true,true,false,true,false);
      break;
    }
  }

  private void drawDigit(int x, int y, int r, boolean top, boolean upperleft,
                         boolean upperright, boolean center, boolean lowerleft,
                         boolean lowerright, boolean bottom){
    if(top){
      graphics.fillRect(x-r, y-r, 2*r, r/4+1);//top
    }
    if(upperleft){
      graphics.fillRect(x-r, y-r, r/4+1, r);//upper left
    }
    if(upperright){
      graphics.fillRect(x+r-r/3-1, y-r, r/3+1, r);//upper right
    }
    if(center){
      graphics.fillRect(x-r, y-r/8-1, 2*r, r/4+1);//center
    }
    if(lowerleft){
      graphics.fillRect(x-r, y, r/4+1, r);//lower left
    }
    if(lowerright){
      graphics.fillRect(x+r-r/3-1, y, r/3+1, r);//lower right
    }
    if(bottom){
      graphics.fillRect(x-r, y+r-r/3-1, 2*r, r/3+1);//bottom
    }
  }
}
