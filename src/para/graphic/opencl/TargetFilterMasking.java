package para.graphic.opencl;
import para.graphic.target.*;
import para.graphic.shape.Camera;
import java.nio.*;
import java.awt.image.*;
import com.jogamp.opencl.CLBuffer;
import com.jogamp.opencl.CLCommandQueue;
import com.jogamp.opencl.CLKernel;
import com.jogamp.opencl.CLProgram;
import static com.jogamp.opencl.CLMemory.Mem.*;
import easycl.*;

  /** {@link Camera}の像に指定した画像で被った修飾を加える。
   */
public class TargetFilterMasking extends TargetFilter{
  private CLBuffer<ByteBuffer> InBuffer, OutBuffer, MaskBuffer;
  private ByteBuffer outbuffer;
  private CLCommandQueue queue;
  private CLKernel kernel;

  /** {@link Camera}の像にmaskimageで被った修飾を加える。その他の出力はtargetと同じ。
   * @param target 出力装置
   * @param maskimage カメラで撮影した画像を部分的に隠す画像
   */
  public TargetFilterMasking(Target target, BufferedImage maskimage){
    super(target);
    CLSetup cl;
    cl = CLSetupCreator.createCLSetup();
    cl.initContext(); //OpenCL初期化
    InBuffer = cl.createByteBuffer(Camera.WIDTH*Camera.HEIGHT*3,READ_ONLY);
    OutBuffer = cl.createByteBuffer(Camera.WIDTH*Camera.HEIGHT*4,WRITE_ONLY);
    outbuffer = OutBuffer.getBuffer();
    MaskBuffer = cl.createByteBuffer(maskimage.getWidth()*
                                     maskimage.getHeight()*4,READ_ONLY);
    IntBuffer maskbufferint = MaskBuffer.getBuffer().asIntBuffer();
                                    //byte型のバッファをint型に見せかける
    MaskBuffer.getBuffer().order(ByteOrder.LITTLE_ENDIAN);//int型の中の4バイトの
                                                          //並び順を明示的に指定
    for(int y=0;y<maskimage.getHeight();y++){
      for(int x=0;x<maskimage.getWidth();x++){
        int pixel = maskimage.getRGB(x,y); //int型の変数一つでARGBの情報を表現
        maskbufferint.put(pixel);//4byteをまとめて書き込み
      }
    }
    maskbufferint.rewind();
    queue = cl.createQueue();
    queue.putWriteBuffer(MaskBuffer,false);//maskimageのデータをカーネル
                                           //が動くデバイス側へ転送する指令
    CLProgram program = cl.createProgramFromResource(this,"masking.cl");
    kernel = program.createCLKernel("Masking");
    kernel.setArg(0,Camera.WIDTH); //カーネルプログラムの引数設定
    kernel.setArg(1,Camera.HEIGHT); 
    kernel.setArg(2,InBuffer);
    kernel.setArg(3,OutBuffer);
    kernel.setArg(4,MaskBuffer);
  }

  @Override
  protected ByteBuffer process(ByteBuffer oneshot){
    InBuffer = InBuffer.cloneWith(oneshot);//ホスト側グローバルメモリへコピー
    InBuffer.getBuffer().rewind();
    outbuffer.rewind();
    queue.putWriteBuffer(InBuffer,false)//InBufferのデータをカーネル側へ転送指令
         .putBarrier()
         .put1DRangeKernel(kernel, 0, Camera.WIDTH*Camera.HEIGHT,0)//演算指令
         .putBarrier()
         .putReadBuffer(OutBuffer, true);//OutBufferのデータをホスト側へ転送指令
                                         //転送完了まで待つ
    outbuffer.rewind();
    return outbuffer;
  }
}
