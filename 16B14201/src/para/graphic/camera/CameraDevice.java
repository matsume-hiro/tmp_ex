package para.graphic.camera;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/** カメラ装置を撮影操作するためのインタフェース */
public interface CameraDevice{
  /** 撮影装置を使用可能にする
   *  @return 真ならば使用可能、偽ならば使用不可能
   *  カメラが未接続な場合、カメラデバイスへのアクセス許可がない場合などは使用可能にはならない
   */
  public boolean start();
  /** 撮影装置を使用を止める
   * @return 正常に使用を止められた場合は真、内部で例外が発生した場合は偽
   */
  public boolean stop();
  /** 撮影装置のリソースを開放する
   * @return 正常に使用を止められた場合は真、内部で例外が発生した場合は偽
   */
  public boolean dispose();
  /** 撮影画像の幅を返す
   * @return 幅の値
   */
  public int getWidth();
  /** 撮影画像の高さを返す
   * @return 高さの値
   */
  public int getHeight();
  /** 撮影画像の幅、高さを設定する
   * @param width 撮影画像の幅
   * @param height 撮影画像の高さ
   */
  public void setSize(int width, int height);
  /** 撮影画像の1フレームをコピーできる {@link BufferedImage} を用意して返す
   * @return 撮影画像の1フレームの大きさ、ピクセルデータフォーマットの{@link BufferedImage}のインスタンスを作成して返す
   */
  public BufferedImage createBufferedImage();
    /** 撮影画像の1フレームをコピーできる {@link ByteBuffer} を用意して返す
   * @return 撮影画像の1フレームのデータサイズの{@link ByteBuffer}のインスタンスを作成して返す
   */
  public ByteBuffer createByteBufferDirect();
  /** 撮影画像を返す 
   *  @param image 撮影画像をコピーする{@link BufferedImage} オブジェクト
   */
  public void getBufferedImage(BufferedImage image);
  public void getByteBuffer(ByteBuffer output);
  public ByteBuffer getRawByteBuffer();
}
