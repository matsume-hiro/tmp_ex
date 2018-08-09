package para.graphic.shape;
import java.util.*;

/**
 * Shapeの順序付けをするための比較の方法を定義するクラス.
 */
public class ShapeComparator implements Comparator<Shape>{
  /**
   * ２つのShapeのid番号を比較して、大小関係を定義する
   * @param o1 図形1
   * @param o2 図形2
   * @return o1.id-o2.id
   */
  public int compare(Shape o1, Shape o2){
    return o1.id-o2.id;
    /*
    if(o1.id<o2.id){
      return -1;
    }else if(o2.id<o1.id){
      return 1;
    }else{
      return 0;
    }
    */
  }
}

