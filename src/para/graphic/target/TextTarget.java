package para.graphic.target;
import para.graphic.shape.Attribute;
import para.graphic.shape.ShapeManager;
import para.graphic.shape.Camera;

import java.io.*;
import javax.imageio.*;
import javafx.scene.image.WritableImage;
import javafx.scene.image.Image;

import java.awt.image.*;
import java.util.Base64;

/**
 * 文字出力装置
 */
public class TextTarget implements Target{
  final int WIDTH,HEIGHT;
  final WritableImage image;
  final BufferedImage buffer;
  final Base64.Encoder encoder;
  private DirectAccessByteArrayOutputStream baos = 
    new DirectAccessByteArrayOutputStream();
  protected SingleCamera camdev;
  protected BufferedImage camimage;
  protected PrintStream pstream;

  public TextTarget(OutputStream stream){
    this(new PrintStream(stream));
  }

  public TextTarget(PrintStream pstream){
    this(Camera.WIDTH, Camera.HEIGHT, pstream);
  }

  public TextTarget(int width, int height, OutputStream stream){
    this(width, height, new PrintStream(stream));
  }

  public TextTarget(int width, int height, PrintStream pstream){
    this.WIDTH = width;
    this.HEIGHT = height;
    buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    image = new WritableImage(width, height);
    encoder = Base64.getMimeEncoder(76, lineseparator);
    this.pstream = pstream;
    camdev = SingleCamera.create(Camera.WIDTH, Camera.HEIGHT);
    camimage = camdev.createBufferedImage(); 
  }

  @Override
  public synchronized void clear(){
   pstream.println("target clear");
  }
  @Override
  public synchronized void draw(ShapeManager sm){
    predraw();
    sm.draw(this);
    postdraw();
  }

  @Override
  public synchronized void draw(Target t,ShapeManager sm){
    predraw();
    t.drawCore(sm);
    postdraw();
  }

  @Override
  public void drawCore(final ShapeManager sm){
    draw(sm);
  }

  private void predraw(){
  }

  private void postdraw(){
    pstream.println("target draw");
  }
  // @Override
  // /**
  //  描画領域の{@link BufferedImage}を返す。スレッドセーフではなく一般利用は非推奨
  //  @return 描画領域の{@link BufferedImage}
  //  */
  // public BufferedImage getBufferedImage(){
  //   return image;
  // }
  
  @Override
  public synchronized BufferedImage copyBufferedImage(){
    return new BufferedImage(buffer.getColorModel(),
                             (WritableRaster)buffer.getData(),
                             buffer.isAlphaPremultiplied(),null);
  }
  @Override
  public synchronized BufferedImage copyBufferedImage3Channel(){
    return new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
  }

  @Override
  public Image copyImage(){
    synchronized(image){
      return new WritableImage(image.getPixelReader(), WIDTH, HEIGHT);
    }
  }
  
  @Override
  public void init(){
  }
  
  @Override
  public void finish(){
    pstream.close();
  }

  @Override
  public synchronized void flush(){
    pstream.println("target flush");
  }

  /** 属性を表示する．
   *  @param attr 属性
   */
  private void print(Attribute attr){
    if(attr!=null){
      pstream.println(" Attribute "+
                      "Color "+attr.getColor()[0]+" "+
                               attr.getColor()[1]+" "+
                               attr.getColor()[2]+" "+
                      "Fill " +attr.getFill()+" "+
                      "Iaux0 "+attr.getIaux0()+" "+
                      "Faux0 "+attr.getFaux0()+" "+
                      "Faux1 "+attr.getFaux1());
    }else{
      pstream.println();
    }
  }

  static int count=0;
  static final byte[] lineseparator = {'\n'};
  private void print(BufferedImage image){
    try {
      baos.reset();
      OutputStream bos = encoder.wrap(baos);
      //Base64OutputStream bos = new Base64OutputStream(baos,true,76,
      //                                                lineseparator);
      ImageIO.write(image, "png", bos); 
      bos.close();
      pstream.write(baos.getBuffer(),0,baos.getCount());
      pstream.flush();
    }catch(IOException e){
      System.err.print(e);
    }
    pstream.print(" &");
  }

  /**
   *  円の情報を文字出力する
   */
  @Override
  public void drawCircle(int id, int x, int y, int r, 
      Attribute attr){
    pstream.print("shape "+id+" Circle "+x+" "+y+" "+r);
    print(attr);
  }

  /** 
   *  矩形の情報を文字出力する
   */
  @Override
  public void drawRectangle(int id, int x, int y, int w, int h, 
      Attribute attr){
    pstream.print("shape "+id+" Rectangle "+x+" "+y+" "+w+" "+h);
    print(attr);
  }

  /** 
   *  画像の情報を文字出力する
   */
  @Override
  public void drawImage(int id, int x, int y, BufferedImage img, 
                        Attribute attr){
    pstream.println("shape "+id+" Image "+x+" "+y);
    print(img);
    print(attr);
  }

  /** 
   *  カメラ画像の情報を文字出力する
   */
  @Override
  public void drawCamera(int id, int x, int y, Attribute attr){
    camdev.get(camimage);
    drawImage(id, x, y,  camimage, attr);
    //    pstream.print("shape "+id+" Camera "+x+" "+y);
    //    print(attr);
  }

  /** 
   *  数字の情報を文字出力する
   */
  public void drawDigit(int id, int x, int y, int r, int number,Attribute attr){
    pstream.print("shape "+id+" Digit "+x+" "+y+" "+r+" "+number);
    print(attr);
  }
  
  private class DirectAccessByteArrayOutputStream extends java.io.ByteArrayOutputStream{
    DirectAccessByteArrayOutputStream(){
      super();
    } 
    DirectAccessByteArrayOutputStream(int size){
      super(size);
    } 
    byte[] getBuffer(){
      return buf;
    }
    int getCount(){
      return count;
    }
  }

}
