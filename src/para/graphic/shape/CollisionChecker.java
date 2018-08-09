package para.graphic.shape;

public class CollisionChecker{
  private TimeNumberVec2 timenumbervec2;

  public CollisionChecker(){
    timenumbervec2 = new TimeNumberVec2();
  };

  public Shape check(ShapeManager sm, Vec2 p0, Vec2 v, float[] resttime){
    timenumbervec2.reset();
    Vec2 vnew = new Vec2(v);
    Shape[] data = sm.getData();
    for(int i=0;i<data.length;i++){
      Shape s = data[i];
      Vec2 v0 = new Vec2(s.left,  s.top);    //   v0--->v1
      Vec2 v1 = new Vec2(s.right, s.top);    //    ^    |
      Vec2 v2 = new Vec2(s.right, s.bottom); //    |    v
      Vec2 v3 = new Vec2(s.left,  s.bottom); //   v3<---v2
      Vec2 d0 = MathUtil.minus(v1,v0);
      Vec2 d1 = MathUtil.minus(v2,v1);
      Vec2 d2 = MathUtil.minus(v3,v2);
      Vec2 d3 = MathUtil.minus(v0,v3);
      float hittime;
      if(MathUtil.crossprod(d0,v)<0){
        hittime = isIntersect(v2,v3,p0,v,resttime[0]);
      }else{
        hittime = isIntersect(v0,v1,p0,v,resttime[0]);
      }
      if(0<=hittime){
        vnew = new Vec2(v.data[0],-v.data[1]);
      }else{
        if(MathUtil.crossprod(d1,v)<0){
          hittime = isIntersect(v3,v0,p0,v,resttime[0]);
        }else{
          hittime = isIntersect(v1,v2,p0,v,resttime[0]);
        }
        if(0<=hittime){
          vnew = new Vec2(-v.data[0],v.data[1]);
        }
      }
      if(0<=hittime && hittime<=resttime[0]){
        timenumbervec2.compareAndReplace(hittime, i, vnew);
      }
    }
    if(timenumbervec2.getTime() != Float.MAX_VALUE){ // hit
      p0.data[0] = p0.data[0]+v.data[0]*timenumbervec2.getTime();
      p0.data[1] = p0.data[1]+v.data[1]*timenumbervec2.getTime();
      v.data[0] = timenumbervec2.getVNew().data[0];
      v.data[1] = timenumbervec2.getVNew().data[1];
      resttime[0] = resttime[0]-timenumbervec2.getTime();
      return data[timenumbervec2.getNumber()];
    }else{
      return null;
    }
  }

  protected float isIntersect(Vec2 v0, Vec2 v1, Vec2 p0, Vec2 v,
                                   float resttime){
    Vec2 vec = new Vec2(v0.data[0]-p0.data[0],v0.data[1]-p0.data[1]);
    Mat22 mat = new Mat22(v.data[0],-(v1.data[0]-v0.data[0]),
                          v.data[1],-(v1.data[1]-v0.data[1]));
    Mat22 matinv;
    try{
      matinv = mat.inv();
    }catch(ArithmeticException ex){// in case of parallel
      return -1.0f;
    }
    Vec2 st = MathUtil.times(matinv, vec);
    final float s = st.data[0];
    final float t = st.data[1];
    float ret;
    if(0<= t && t<=1 && 0 <= s && s <=resttime){
      ret =s; 
    }else{
      ret = -1.0f;
    }
    return ret;
  }

  private class TimeNumberVec2{
    volatile private float time;
    volatile private int number;
    volatile private Vec2 vnew;

    private TimeNumberVec2(){
      time =Float.MAX_VALUE;
      number = -10;
      vnew = new Vec2();
    }
    
    synchronized private void reset(){
      time =Float.MAX_VALUE;
      number = -10;
    }

    synchronized private void compareAndReplace(float newtime, int number,
                                                Vec2 vnew){
      if(newtime<time){
        time = newtime;
        this.number = number;
        this.vnew = new Vec2(vnew);
      }
    }

    public float getTime(){
      return time;
    }
    public int getNumber(){
      return number;
    }
    public Vec2 getVNew(){
      return vnew;
    }
  }
}
