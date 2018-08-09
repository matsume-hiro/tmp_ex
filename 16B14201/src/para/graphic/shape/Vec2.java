package para.graphic.shape;

public class Vec2{
  public float[] data;

  public Vec2(){
    data = new float[2];
  }

  public Vec2(float a, float b){
    data = new float[2];
    data[0] = a;
    data[1] = b;
  }

  public Vec2(float[] in){
    data = new float[2];
    data[0] = in[0]; 
    data[1] = in[1]; 
  }

  public Vec2(Vec2 v){
    data = new float[2];
    data[0] = v.data[0];
    data[1] = v.data[1];
  }    

  public float length(){
    return (float)Math.sqrt(data[0]*data[0]+data[1]*data[1]);
  }
  
  public void add(Vec2 a){
    data[0] +=a.data[0];
    data[1] +=a.data[1];
  }

  public void sub(Vec2 a){
    data[0] -=a.data[0];
    data[1] -=a.data[1];
  }

  public void mul(Vec2 a){
    data[0] *=a.data[0];
    data[1] *=a.data[1];
  }

  public void mul(float a){
    data[0] *=a;
    data[1] *=a;
  }

  public void normalize(){
    float t= MathUtil.dot(this,this);
    t = (float)Math.sqrt(t);
    mul(1.0f/t);
  }

  public String toString(){
    return new String("("+data[0]+","+data[1]+")");
  }
}
