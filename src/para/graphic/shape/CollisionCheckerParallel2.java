package para.graphic.shape;

import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class CollisionCheckerParallel2 extends CollisionChecker{
  private TimeNumberVec2 timenumbervec2;
  private boolean isparallel;

  public CollisionCheckerParallel2(boolean isparallel){
    this.isparallel = isparallel;
    timenumbervec2 = new TimeNumberVec2();
  };

  public Shape check(ShapeManager sm, Vec2 p0, Vec2 v, float[] resttime){
    timenumbervec2.reset();
    //Shape[] data = sm.getData();
    //Optional<TimeNumberVec2> res = Arrays.stream(data).parallel().map(s->{
    //Optional<TimeNumberVec2> res = Arrays.stream(data).map(s->{
    //Optional<TimeNumberVec2> res = sm.getStream().map(s->{
    Stream<Shape> stream;
    if(isparallel){
      stream = sm.getParallelStream().unordered();
    }else{
      stream = sm.getStream();
    }
    Optional<TimeNumberVec2> res = stream.map(s->{
      Vec2 vnew = null;//new Vec2();
      Vec2 v0 = new Vec2(s.left,  s.top);    //   v0--->v1
      Vec2 v1 = new Vec2(s.right, s.top);    //    ^    |
      Vec2 v2 = new Vec2(s.right, s.bottom); //    |    v
      Vec2 v3 = new Vec2(s.left,  s.bottom); //   v3<---v2
      Vec2 d0 = MathUtil.minus(v1,v0);
      Vec2 d1 = MathUtil.minus(v2,v1);
      //      Vec2 d2 = MathUtil.minus(v3,v2);
      //      Vec2 d3 = MathUtil.minus(v0,v3);
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
        return new TimeNumberVec2(hittime, s, vnew);
      }else{
        return new TimeNumberVec2(-10);
      }
      }).
      filter(tnv->{
          if(tnv.time<0){
            return false;
          }else{
            return true;
          }
        }).
      //      min(CollisionCheckerParallel2::compare);
      min((l,r)
          ->{if(l.time<r.time){
              return -1;
            }else{
              return 1;
            }
          });

    if(res.isPresent() && res.get().getTime() != Float.MAX_VALUE){ // hit
      TimeNumberVec2 timenumbervec2 = res.get();
      p0.data[0] = p0.data[0]+v.data[0]*timenumbervec2.getTime();
      p0.data[1] = p0.data[1]+v.data[1]*timenumbervec2.getTime();
      v.data[0] = timenumbervec2.getVNew().data[0];
      v.data[1] = timenumbervec2.getVNew().data[1];
      resttime[0] = resttime[0]-timenumbervec2.getTime();
      return timenumbervec2.getShape();
    }else{
      return null;
    }
  }

  public static int compare(TimeNumberVec2 a, TimeNumberVec2 b){
    if (a.time<b.time){
      return -1;
    }else{
      return 1;
    }
  }
  
  private class TimeNumberVec2{
    volatile private float time;
    volatile private Shape shape;
    volatile private Vec2 vnew;

    private TimeNumberVec2(){
    }

    private TimeNumberVec2(float time){
      this.time = time;
    }

    private TimeNumberVec2(float time, Shape shape, Vec2 vnew){
      this.time = time;
      this.shape = shape;
      this.vnew = new Vec2(vnew);
    }
    
    synchronized private void reset(){
      time = Float.MAX_VALUE;
      shape = null;
    }

    synchronized private void compareAndReplace(float newtime, Shape shape,
                                                Vec2 vnew){
      if(newtime<time){
        time = newtime;
        this.shape = shape;
        this.vnew = new Vec2(vnew);
      }
    }

    public float getTime(){
      return time;
    }
    public Shape getShape(){
      return shape;
    }
    public Vec2 getVNew(){
      return vnew;
    }
  }
}
