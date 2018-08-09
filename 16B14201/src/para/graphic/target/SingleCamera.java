package para.graphic.target;

import para.graphic.camera.*;
import java.awt.image.*;
import java.nio.*;

/** 撮影装置のインスタンスを複数のターゲットで共有するためのクラス */
class SingleCamera{
  static private CameraDevice cameradev=null;
  private SingleCamera(){};
  static private boolean isStarted=false;

  /** {@link SingleCamera}のインスタンスを作る 
   * @param width 撮影画像の幅
   * @param height 撮影画像の高さ
   * @return 作成されたインスタンス
   */
  static synchronized SingleCamera create(int width, int height){
    if(cameradev == null){
      cameradev = new CameraJavaCV(width, height);
    }
    return new SingleCamera();
  }

  /** 撮影装置を撮影可能状態にする
   * @return 撮影可能状態になれば true を返す
   */
  public boolean start(){
    synchronized(this){
      if(isStarted){
        return true;
      }
      isStarted = true;
    }
    return cameradev.start();
  }

  /** 撮影装置を撮影停止状態にする
   * @return 撮影装置の状態を撮影状態から停止状態に変更できれば true もともと止まっていたり、
   * 停止できなければ false
   */
  public boolean stop(){
    synchronized(this){
      if(!isStarted){
        return false;
      }
      isStarted =false;
    }
    return cameradev.stop();
  }

  /** 撮影画像の幅を返す
   * @return 幅
   */
  public int getWidth(){
    return cameradev.getWidth();
  }

  /** 撮影画像の高さを返す
   * @return 高さ
   */
  public int getHeight(){
    return cameradev.getHeight();
  }

  /** 撮影画像の1フレーム分のデータをコピーするために適した{@link BufferedImage}
   * を用意して返す
   * @return 新たに用意された{@link BufferedImage}
   */
  public BufferedImage createBufferedImage(){
    return cameradev.createBufferedImage();
  }

  /** 撮影画像の1フレーム分のデータを返す 
   * @param img 1フレーム分のデータがコピーされる {@link BufferedImage}
   * getメソッドの中で一番使用を推奨。引数に渡す{@link BufferedImage}の作成には
   * {@link createBufferedImage}が便利。
   */
  public void get(BufferedImage img){
    start();
    cameradev.getBufferedImage(img);
  } 

  /** 撮影画像の1フレーム分のデータを返す
   * @param buffer 1フレーム分のデータがコピーされる {@link ByteBuffer}
   */
  public void get(ByteBuffer buffer){
    start();
    cameradev.getByteBuffer(buffer);
  } 

  /** 撮影画像の1フレーム分のデータを返す 
   * OS依存のデータ形式なため、使用は非推奨
   * @return 1フレーム分のデータ
   */
  public ByteBuffer get(){
    start();
    return cameradev.getRawByteBuffer();
  } 
}
