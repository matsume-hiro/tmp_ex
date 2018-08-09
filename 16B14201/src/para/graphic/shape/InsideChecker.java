package para.graphic.shape;

public class InsideChecker{

  private InsideChecker(){
  };

  public static Shape check(ShapeManager sm, Vec2 pos){
    Shape[] data = sm.getData();
    for(int i=0;i<data.length;i++){
      Shape shape = data[i];
      if(shape.left<= pos.data[0] && pos.data[0] <= shape.right &&
         shape.top<= pos.data[1] && pos.data[1] <= shape.bottom){
        return shape;
      }
    }
    return null;
  }
}
