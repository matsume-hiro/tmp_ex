package para.graphic.shape;
import java.util.*;

/** 描画順序をidの順とする図形集合
 */
public class OrderedShapeManager extends ShapeManager{
  /** 図形の集合を生成する（空集合）
   */
  public OrderedShapeManager(){
    super(new TreeSet<Shape>(new ShapeComparator()));
  }
}
