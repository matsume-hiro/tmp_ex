package para.graphic.opencl;

public class Position{
  private int x,y;
  public Position(){
  }

  public Position(int x, int y){
    this.x = x;
    this.y = y;
  }

  synchronized public void set(int x, int y){ 
    this.x = x;
    this.y = y;
  }

  synchronized public void get(int[] ret){ 
    ret[0] = x;
    ret[1] = y;
  }
}
