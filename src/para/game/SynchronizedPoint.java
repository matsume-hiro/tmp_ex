package para.game;

public class SynchronizedPoint{
  private final float[] xy;
  private long time;

  public SynchronizedPoint(){
    xy = new float[2];
  }

  public synchronized void set(float x, float y, long time){
    xy[0] = x;
    xy[1] = y;
    this.time = time;
  }

  public synchronized SynchronizedPoint copy(){
    SynchronizedPoint ret = new SynchronizedPoint();
    System.arraycopy(xy, 0, ret.xy, 0, 2);
    ret.time = time;
    return ret;
  }

  public synchronized float[] getXY(){
    float[] ret = new float[2];
    System.arraycopy(xy, 0, ret, 0, 2);
    return ret;
  }

  public synchronized long getTime(){
    return time;
  }
}
