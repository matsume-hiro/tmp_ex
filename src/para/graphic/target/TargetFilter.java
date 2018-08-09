package para.graphic.target;
import para.graphic.shape.*;
import java.nio.*;
import java.awt.image.*;
import java.awt.*;
import javafx.scene.image.Image;

abstract public class TargetFilter implements Target{
  protected Target target;
  private SingleCamera camdev;
  private BufferedImage outimage;
  private ByteBuffer captureBuffer; //this is used for different definition 
                                    //between MacOS and other OS
  /** {@link Camera}の像に画像処理を加えるための枠組みを提供する抽象クラス
   * @param target 画像を出力できるTargetクラス
   */
  public TargetFilter(Target target){
    this.target = target;
    camdev = SingleCamera.create(Camera.WIDTH, Camera.HEIGHT);
    outimage = new BufferedImage(Camera.WIDTH, Camera.HEIGHT,
                                 BufferedImage.TYPE_INT_ARGB); 
    captureBuffer = ByteBuffer.allocateDirect(Camera.WIDTH*Camera.HEIGHT*3);
  }

  @Override
  public void clear(){
    target.clear();
  }
  @Override
  public void draw(ShapeManager sm){
    predraw();
    target.draw(this,sm);
    postdraw();
  }

  @Override
  public void draw(Target t,ShapeManager sm){
    predraw();
    t.drawCore(sm);
    postdraw();
  }

  @Override
  public void drawCore(ShapeManager sm){
    sm.draw(this);
  }

  private void predraw(){
  }
  private void postdraw(){
  }

  // @Override
  // /**
  //  フィルタ対象の{@link Target}の描画領域の{@link BufferedImage}を返す。スレッドセーフではなく一般利用は非推奨
  //  @return 描画領域の{@link BufferedImage}
  //  */
  // public BufferedImage getBufferedImage(){
  //   return target.getBufferedImage();
  // }

  // @Override
  // /**
  //  フィルタ対象の{@link Target}の描画領域と同じ大きさの {@link BufferedImage}を作成する。データのコピーは行わない。 
  //  */
  // public BufferedImage createBufferedImage(){
  //   return new BufferedImage(target.getAreaWidth(),
  //                            target.getAreaHeight(),target.image.getType());
  // }
  // @Override
  // /**
  //  フィルタ対象の{@link Target}の描画領域の幅を返す
  //  @return width 領域の幅
  //  */
  // public int getAreaWidth(){
  //   return target.getAreaWidth();
  // }
  // @Override
  // /**
  //  フィルタ対象の{@link Target}の描画領域の高さを返す
  //  @return height 領域の高さ
  //  */
  // public int getAreaHeight(){
  //   return target.getAreaHeight();
  // }
  // @Override
  // /**
  //  描画領域と同じデータ形式の {@link BufferedImage} にデータをコピーする。スレッドセーフ
  //  @param image 描画領域と同じデータ形式の {@link BufferedImage}
  //  */
  // public synchronized void getBufferedImage(BufferedImage image){
  //   image.setData(this.image.getData());
  // }
  @Override
  /**
   描画領域の {@link BufferedImage} をコピーして返す
   @return 描画領域の{@link BufferedImage}のコピー
  */
  public BufferedImage copyBufferedImage(){
    return target.copyBufferedImage();
  }

  @Override
  public BufferedImage copyBufferedImage3Channel(){
    return target.copyBufferedImage3Channel();
  }

  public Image copyImage(){
    return target.copyImage();
  }
  
  public void init(){
    target.init();
  }
  
  public void finish(){
    target.finish();
  }
  public void flush(){
    target.flush();
  }
  public void drawCircle(int id,int x, int y, int r, Attribute attr){
    target.drawCircle(id, x, y, r, attr);
  }
  public void drawRectangle(int id, int x1, int y1, int x2, int y2, Attribute attr){
    target.drawRectangle(id, x1, y1, x2, y2, attr);
  }
  public void drawImage(int id, int x1, int y1, BufferedImage img, Attribute attr){
    target.drawImage(id, x1, y1, img, attr);
  }

  public void drawCamera(int id, int x1, int y1, Attribute attr){
    //the fastest way is the next line, but incompatible between OS
    //  ByteBuffer shot = getShot(); 
    //  if(shot == null){
    //    target.drawImage(id, x1, y1, outimage, attr);
    //    return;
    //  }
    //  ByteBuffer processedbuffer = process(shot); 

    // the following is slower than the above, but compatible for various OS
    getShot(captureBuffer);
    ByteBuffer processedbuffer = process(captureBuffer);

    processedbuffer.rewind();
    IntBuffer ibf = processedbuffer.asIntBuffer();
    DataBufferInt db = (DataBufferInt) outimage.getRaster().getDataBuffer();
    ibf.get(db.getData());
    target.drawImage(id, x1, y1, outimage, attr);    
  }

  public void drawDigit(int id, int x, int y, int r, int number,Attribute attr){
    target.drawDigit(id, x, y, r, number, attr);
  }
  
  public ByteBuffer createCameraBuffer(){
    return ByteBuffer.allocateDirect(Camera.WIDTH*Camera.HEIGHT*3);
  }

  public ByteBuffer createOutputBuffer(){
    return ByteBuffer.allocateDirect(Camera.WIDTH*Camera.HEIGHT*4);
  }

  public ByteBuffer getShot(){
    return camdev.get();
  }

  public void getShot(ByteBuffer data){
    camdev.get(data);
  }

  /** 表示をする度に呼び出され、カメラで撮影した画像に処理を加える。
   * @param input 画像データ
   * @return 処理後の画像データ
   */
  abstract protected ByteBuffer process(ByteBuffer input);
}
