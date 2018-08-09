package para.graphic.shape;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CollisionCheckerParallel3 extends CollisionChecker{
  private TimeNumberVec2 timenumbervec2;
  private TimeNumberVec2[] tnv;
  private List<Future<TimeNumberVec2>> futures;
  ExecutorService executor;
  final int threadcount;
  volatile int unitcount;
  Shape[] data;

  public CollisionCheckerParallel3(int threadcount){
    this.threadcount = threadcount;
    executor = Executors.newFixedThreadPool(threadcount);
    tnv = new TimeNumberVec2[threadcount];
    futures = new ArrayList<Future<TimeNumberVec2>>(threadcount);
    for(int i=0;i<tnv.length;i++){
      tnv[i] = new TimeNumberVec2();
    }
    //    ThreadPoolExecutor pool = new ThreadPoolExecutor()
    timenumbervec2 = new TimeNumberVec2();
  }

  public Shape check(ShapeManager sm, Vec2 p0, Vec2 v, float[] resttime){
    timenumbervec2.reset();
    futures.clear();
    data = sm.getData();
    unitcount = data.length/threadcount;
    for(int i=0;i<threadcount;i++){
      int start = i*unitcount;
      int end;
      if(i!=threadcount-1){
        end = (i+1)*unitcount;
      }else{
        end = data.length;
      }
      futures.add(executor.submit(new CheckInner(start,end,p0,v,resttime[0])));
    }
    for(int i=0;i<threadcount;i++){
      try{
        TimeNumberVec2 tmp = futures.get(i).get();
        timenumbervec2.compareAndReplace(tmp.time, tmp.shape,tmp.vnew);
      }catch(InterruptedException| ExecutionException ex){
        System.err.print(ex);
      }
    }
    if(timenumbervec2.getTime() != Float.MAX_VALUE){ // hit
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

  private class CheckInner implements Callable<TimeNumberVec2>{
    int start;
    int end;
    float resttime;
    Vec2 p0;
    Vec2 v;
    CheckInner(int start, int end, Vec2 p0, Vec2 v, float resttime){
      this.start = start;
      this.end = end;
      this.resttime = resttime;
      this.p0 =p0;
      this.v = v;
    }
    public TimeNumberVec2 call(){
      TimeNumberVec2 timenumbervec2 = new TimeNumberVec2();
      timenumbervec2.reset();
      for(int i=start;i<end;i++){
      Shape s = data[i];
      Vec2 vnew = null;
      Vec2 v0 = new Vec2(s.left,  s.top);    //   v0--->v1
      Vec2 v1 = new Vec2(s.right, s.top);    //    ^    |
      Vec2 v2 = new Vec2(s.right, s.bottom); //    |    v
      Vec2 v3 = new Vec2(s.left,  s.bottom); //   v3<---v2
      Vec2 d0 = MathUtil.minus(v1,v0);
      Vec2 d1 = MathUtil.minus(v2,v1);
      //Vec2 d2 = MathUtil.minus(v3,v2);
      //Vec2 d3 = MathUtil.minus(v0,v3);
      float hittime;
      if(MathUtil.crossprod(d0,v)<0){
        hittime = isIntersect(v2,v3,p0,v,resttime);
      }else{
        hittime = isIntersect(v0,v1,p0,v,resttime);
      }
      if(0<=hittime){
        vnew = new Vec2(v.data[0],-v.data[1]);
      }else{
        if(MathUtil.crossprod(d1,v)<0){
          hittime = isIntersect(v3,v0,p0,v,resttime);
        }else{
          hittime = isIntersect(v1,v2,p0,v,resttime);
        }
        if(0<=hittime){
          vnew = new Vec2(-v.data[0],v.data[1]);
        }
      }
      if(0<=hittime && hittime<=resttime){
        timenumbervec2.compareAndReplace(hittime, s, vnew);
      }
      }
      return timenumbervec2;
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
