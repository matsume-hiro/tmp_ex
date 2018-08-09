package para.graphic.shape;

public class ShapeCollisionChecker{
  private ShapeCollisionChecker(){};

  public static boolean isInside(Shape shape, Vec2 pos){
    if(shape.left<= pos.data[0] && pos.data[0] <= shape.right &&
       shape.top<= pos.data[1] && pos.data[1] <= shape.bottom){
      return true;
    }else{
      return false;
    }
  }

  public static Vec2 calcCenter(Shape shape){
    return new Vec2((shape.left+shape.right)/2.0f,
                    (shape.top+shape.bottom)/2.0f);
  }
  
  public static Shape CollisionCheck(ShapeManager sm, Vec2 p0, Vec2 v,
                                     float[] resttime){
    Vec2 vnew=null;
    Vec2 vnewtmp=null;
    Shape[] data = sm.getData();
    float time=Float.MAX_VALUE;
    int number=-10;
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
        vnewtmp = new Vec2(v.data[0],-v.data[1]);
      }else{
        if(MathUtil.crossprod(d1,v)<0){
          hittime = isIntersect(v3,v0,p0,v,resttime[0]);
        }else{
          hittime = isIntersect(v1,v2,p0,v,resttime[0]);
        }
        if(0<=hittime){
          vnewtmp = new Vec2(-v.data[0],v.data[1]);
        }
      }
      if(0<=hittime && hittime<=resttime[0]){
        if(hittime<time){
          time = hittime;
          number = i;
          vnew = vnewtmp;
        }
      }
    }
    if(time != Float.MAX_VALUE){ // hit
      p0.data[0] = p0.data[0]+v.data[0]*time;//+vnew.data[0]*(1-time);
      p0.data[1] = p0.data[1]+v.data[1]*time;//+vnew.data[1]*(1-time);
      v.data[0] = vnew.data[0];
      v.data[1] = vnew.data[1];
      resttime[0] = resttime[0]-time;
      return data[number];
    }else{
      return null;
    }
  }

  private static float isIntersect(Vec2 v0, Vec2 v1, Vec2 p0, Vec2 v,
                                   float resttime){
    Vec2 vec = new Vec2(v0.data[0]-p0.data[0],v0.data[1]-p0.data[1]);
    Mat22 mat = new Mat22(v.data[0],-(v1.data[0]-v0.data[0]),
                          v.data[1],-(v1.data[1]-v0.data[1]));
    Mat22 matinv;
    try{
      matinv = mat.inv();
    }catch(ArithmeticException ex){
      // in case of parallel
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
}
