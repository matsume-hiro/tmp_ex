package para.graphic.shape;

public class MathUtil{
  private MathUtil(){
  }

  static float dot(Vec2 a, Vec2 b){
    return a.data[0]*b.data[0]+a.data[1]*b.data[1];
  }

  public static Vec2 normal(Vec2 a){
    float t= MathUtil.dot(a,a);
    t = (float)Math.sqrt(t);
    return times(a,1.0f/t);
  }

  public static Vec2 plus(Vec2 a, Vec2 b){
    Vec2 ret = new Vec2();
    ret.data[0] = a.data[0]+ b.data[0]; 
    ret.data[1] = a.data[1]+ b.data[1]; 
    return ret;
  }

  public static Mat22 plus(Mat22 a, Mat22 b){
    Mat22 ret = new Mat22();
    ret.data[0] = a.data[0]+ b.data[0];
    ret.data[1] = a.data[1]+ b.data[1]; 
    ret.data[2] = a.data[2]+ b.data[2];
    ret.data[3] = a.data[3]+ b.data[3]; 
    return ret;
  }

  public static Vec2 minus(Vec2 a, Vec2 b){
    Vec2 ret = new Vec2();
    ret.data[0] = a.data[0]- b.data[0]; 
    ret.data[1] = a.data[1]- b.data[1]; 
    return ret;
  }

  public static Mat22 minus(Mat22 a, Mat22 b){
    Mat22 ret = new Mat22();
    ret.data[0] = a.data[0]- b.data[0];
    ret.data[1] = a.data[1]- b.data[1]; 
    ret.data[2] = a.data[2]- b.data[2];
    ret.data[3] = a.data[3]- b.data[3]; 
    return ret;
  }

  public static Vec2 times(Vec2 a, Vec2 b){
    Vec2 ret = new Vec2();
    ret.data[0] = a.data[0]* b.data[0]; 
    ret.data[1] = a.data[1]* b.data[1]; 
    return ret;
  }

  public static Vec2 times(Vec2 a, float b){
    Vec2 ret = new Vec2();
    ret.data[0] = a.data[0]* b;
    ret.data[1] = a.data[1]* b;
    return ret;
  }

  public static Mat22 times(Mat22 a, float b){
    Mat22 ret = new Mat22();
    ret.data[0] = a.data[0]* b;
    ret.data[1] = a.data[1]* b;
    ret.data[2] = a.data[2]* b;
    ret.data[3] = a.data[3]* b;
    return ret;
  }

  public static Vec2 times(Mat22 m, Vec2 a){
    Vec2 ret = new Vec2();
    ret.data[0] = m.data[0]*a.data[0]+ m.data[1]*a.data[1];
    ret.data[1] = m.data[2]*a.data[0]+ m.data[3]*a.data[1];
    return ret;
  }

  public static Mat22 times(Mat22 a, Mat22 b){
    Mat22 ret = new Mat22();
    ret.data[0] = a.data[0]* b.data[0]+ a.data[1]* b.data[2];
    ret.data[1] = a.data[0]* b.data[1]+ a.data[1]* b.data[3];
    ret.data[2] = a.data[2]* b.data[0]+ a.data[3]* b.data[2];
    ret.data[3] = a.data[2]* b.data[1]+ a.data[3]* b.data[3];
    return ret;
  }

  public static float crossprod(Vec2 a, Vec2 b){
    float ret;
    ret = a.data[0] * b.data[1] - b.data[0] * a.data[1];
    return ret;
  }
}
