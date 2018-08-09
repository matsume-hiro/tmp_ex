package para.graphic.shape;

/**
 * 2x2正方行列のクラス
 */
public class Mat22{
  public float[] data;

  public static final Mat22 ZERO = new Mat22(0,0,0,0);
  public static final Mat22 I = new Mat22(1,0,0,1);

  public Mat22(){
    data = new float[4];
  }
  
  public Mat22(float a, float b, float c, float d){
    data = new float[]{a,b,c,d};
  }

  public Mat22(Mat22 m){
    data = new float[4];
    System.arraycopy(m.data, 0, data, 0, 4);  
  }
  
  /** 逆行式を計算する
   * @return 行列式の値
   */
  public float det(){
    return data[0]*data[3]-data[1]*data[2];
  }

  /** 逆行列を生成する。
   * @return このインスタンスの逆行列
   */
  public Mat22 inv(){
    Mat22 ret = new Mat22();
    float d = det();
    if(Math.abs(d) <0.00001){
      throw new ArithmeticException("det == 0");
    }
    ret.data[0] =  data[3]/d;
    ret.data[1] = -data[1]/d;
    ret.data[2] = -data[2]/d;
    ret.data[3] =  data[0]/d;
    return ret;
  }

  public String toString(){
    return new String("("+data[0]+","+data[1]+":"+data[2]+","+data[3]+")");
  }
}
