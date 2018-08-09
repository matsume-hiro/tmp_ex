package para.graphic.camera;

import org.bytedeco.javacv.*;
import org.bytedeco.javacpp.opencv_core.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

/** 撮影装置としてOpenCVライブラリ経由でweb cameraを使うクラス */
public class CameraJavaCV implements CameraDevice{
  private FrameGrabber framegrabber;
  private Java2DFrameConverter converter;
  private OpenCVFrameConverter.ToIplImage converterimpi;
  private BufferedImage bimg;
  private volatile boolean captureready;
  private final int WIDTH;
  private final int HEIGHT;

  /** 撮影画像の大きさは標準の640x480 としてdevice id=0 のカメラに対応するインスタンスを作成
   */
  public CameraJavaCV(){
    this(0);
  }

  /** 撮影画像の大きさは標準の640x480 としてdevice のカメラに対応するインスタンスを作成
   * @param device カメラのデバイスID
   */
  public CameraJavaCV(int device){
    framegrabber = new OpenCVFrameGrabber(device);
    converter = new Java2DFrameConverter();
    converterimpi = new OpenCVFrameConverter.ToIplImage();
    setSize(640,480);
    WIDTH = 640;
    HEIGHT = 480;
    //setShutdownHookThread();
  }

  /** 撮影画像の大きさを指定してdevice id=0のカメラに対応するインスタンスを作成
   * @param width 画像の幅
   * @param height 画像の高さ
   */
  public CameraJavaCV(int width, int height){
    this(0,width,height);
  }

  /** 撮影画像の大きさを指定してdevice のカメラに対応するインスタンスを作成
   *  @param device カメラのデバイスID
   *  @param width  撮影画像の幅
   *  @param height 撮影画像の高さ
   */
  public CameraJavaCV(int device, int width, int height){
    framegrabber = new OpenCVFrameGrabber(device);
    setSize(width, height);
    WIDTH = width;
    HEIGHT = height;
    //setShutdownHookThread();
  }

  private void setShutdownHookThread(){
    Runtime.getRuntime().addShutdownHook(
      new Thread(new Runnable(){ public void run(){
        if(framegrabber!=null){
          stop();
        }
      }
    }));
  }

  /** 撮影装置を使用可能にする
   *  @return 真ならば使用可能、偽ならば使用不可能
   *  カメラが未接続な場合、カメラデバイスへのアクセス許可がない場合などは使用可能にはならない
   */
  synchronized public boolean start(){
    try {
      framegrabber.start();
    }catch(org.bytedeco.javacv.FrameGrabber.Exception e){
      return false;
    }
    captureready = true;
    return true;
  }

  /** 撮影装置を使用を止める dispose()と実質同じ
   * @return 正常に使用を止められた場合は真、内部で例外が発生した場合は偽
   */
  synchronized public boolean stop(){
    captureready = false;
    try {
      framegrabber.stop();
    }catch(org.bytedeco.javacv.FrameGrabber.Exception e){
      return false;
    }
    return true;
  }

  /** 撮影装置のリソースを解放する 
   * @return 正常に解放できた場合は真、内部で例外が発生した場合は偽
   */
  synchronized public boolean dispose(){
    captureready = false;
    try {
      framegrabber.release();
    }catch(org.bytedeco.javacv.FrameGrabber.Exception e){
      return false;
    }
    return true;
  }

  /** 撮影画像の幅を返す
   * @return 幅の値
   */
  public int getWidth(){
    return framegrabber.getImageWidth();
  }

  /** 撮影画像の高さを返す
   * @return 高さの値
   */
  public int getHeight(){
    return framegrabber.getImageHeight();
  }

  /** 撮影画像の幅、高さを設定する
   * @param width 撮影画像の幅
   * @param height 撮影画像の高さ
   */
  public void setSize(int width, int height){
    framegrabber.setImageWidth(width); 
    framegrabber.setImageHeight(height); 
    bimg = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
  }

  /** 撮影画像の1フレームをコピーできる {@link BufferedImage} を用意して返す
   * @return 撮影画像の1フレームの大きさ、ピクセルデータフォーマットの{@link BufferedImage}のインスタンスを作成して返す
   */
  public BufferedImage createBufferedImage(){
    /*
    return new BufferedImage(framegrabber.getImageWidth(),
                             framegrabber.getImageHeight(),
                             BufferedImage.TYPE_3BYTE_BGR);
    */
    return new BufferedImage(WIDTH, HEIGHT,
                             BufferedImage.TYPE_3BYTE_BGR);
  }

  /** 撮影画像の1フレームをコピーできる {@link ByteBuffer} を用意して返す
   * @return 撮影画像の1フレーム分のデータサイズの新規作成された{@link ByteBuffer}のインスタンス
   */
  public ByteBuffer createByteBufferDirect(){
    /*
    return ByteBuffer.allocateDirect(framegrabber.getImageWidth()*
                                     framegrabber.getImageHeight()*3);
    */
    return ByteBuffer.allocateDirect(WIDTH*HEIGHT*3);
  }

  /** 撮影画像を返す 
   *  @param img 撮影画像をコピーする{@link BufferedImage} オブジェクト
   */
  synchronized public void getBufferedImage(BufferedImage img){
    if(!captureready){
      return;
    }
    try {
      Java2DFrameConverter.copy(framegrabber.grab(),img);
    }catch(org.bytedeco.javacv.FrameGrabber.Exception e){
    }
  } 

  /** 撮影画像を返す 
   *  @return 1フレーム分のデータ
   *  OSによりデータの形式が異なるので利用には注意
   */
  synchronized public ByteBuffer getRawByteBuffer(){
    if(!captureready){
      return null;
    }
    /*
    IplImage img = null;
    try {
      img = converterimpi.convert(framegrabber.grab());
      //      bbuffer.rewind();
      //      bbuffer.put(img.getByteBuffer());
    }catch(org.bytedeco.javacv.FrameGrabber.Exception e){
    }
    //    return img.getByteBuffer();
    */
    try {
      Java2DFrameConverter.copy(framegrabber.grab(),bimg);
    }catch(org.bytedeco.javacv.FrameGrabber.Exception e){
    }
    DataBufferByte dbb = (DataBufferByte)bimg.getRaster().getDataBuffer();
    /*
    ByteBuffer ret = ByteBuffer.allocate(framegrabber.getImageWidth()*
                                        framegrabber.getImageHeight()*3);
    */
    ByteBuffer ret = ByteBuffer.allocate(WIDTH*HEIGHT*3);
    ret.put(dbb.getData());
    ret.rewind();
    return ret;
  } 

  /** 撮影画像を返す 
   *  @param output 1フレーム分のデータをコピーする{@link ByteBuffer}オブジェクト
   *  OSに依存せずBGRの順番で1画素を表現する形式でコピーをする
   */
  synchronized public void getByteBuffer(ByteBuffer output){
    if(!captureready){
	return; 
    }
    /*
    BufferedImage bi = new BufferedImage(framegrabber.getImageWidth(),
					 framegrabber.getImageHeight(),
					 BufferedImage.TYPE_3BYTE_BGR);
    */
    BufferedImage bi = new BufferedImage(WIDTH, HEIGHT,
					 BufferedImage.TYPE_3BYTE_BGR);
    try {
      Java2DFrameConverter.copy(framegrabber.grab(),bi);
      //    framegrabber.grab().copyTo(bi);
    }catch(org.bytedeco.javacv.FrameGrabber.Exception e){
    }
    //    return img.getByteBuffer();
    DataBufferByte dbb = (DataBufferByte)bi.getRaster().getDataBuffer();
    output.rewind();
    output.put(dbb.getData());
  }
    
  // /**
  //  * このクラス内にあらかじめ用意してある{@link BufferedImage}に1フレーム分の撮影画像をコピーして、そのオブジェクトを返す
  //  * 呼び出し毎に新規に領域確保するのではないのでデータの操作に注意が必要
  //  * @return 1フレーム分の撮影画像データが格納された{@link BufferedImage}
  //  */
  // synchronized public BufferedImage getShotAsBufferedImage(){
  //   if(!captureready){
  //     return bimg;
  //   }
  //   try {
  //     framegrabber.grab().copyTo(bimg);
  //   }catch(org.bytedeco.javacv.FrameGrabber.Exception e){
  //   }
  //   return bimg;
  // } 
}
