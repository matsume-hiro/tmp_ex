package para.graphic.shape;
import java.util.*;
import java.util.stream.*;
import para.graphic.target.Target;

/**
 * 図形の集合
 */
public class ShapeManager{
  /** 図形のリスト */
  final AbstractCollection<Shape> data;

  /** 図形の集合を生成する（空集合）
   */
  public ShapeManager(){
    data = new ArrayList<Shape>(); 
  }

  /** 図形の集合を生成する（空集合）.
   * 継承クラスが初期化に利用する
   * @param data 集合
   */
  ShapeManager(AbstractCollection<Shape> data){
    this.data = data;
  }
  
  /** 図形の集合を空集合にする
   */
  public synchronized void clear(){
    data.clear();
  }

  /** 図形の集合に図形を追加する
   *  もし既に同じ識別子を持つ図形があれば置き換える
   *  @param in 追加する図形
   */
  public synchronized void put(Shape in){
    if(!replace(in)){
      add(in);
    }
  }

  /** 図形の集合に図形を追加する
   *  @param in 追加する図形
   */
  public synchronized void add(Shape in){
    data.add(in);
  }

  /** 図形の集合中の図形を置き換える
   *  @param in 置き換え先の図形
   *  @return もともと図形がリスト中にあったら true, なければ false
   */
  public synchronized boolean replace(Shape in){
    for(Shape s:data){
      if(s.id == in.id){
        data.remove(s);
        data.add(in);
        return true;
      }
    }
    return false;
  }

  /** 図形の集合から図形を削除する
   *  @param id 削除する図形の識別子
   *  @return もともと図形がリスト中にあったら true, なければ false
   */
  public synchronized boolean remove(int id){
    for(Shape s:data){
      if(s.id == id){
        data.remove(s);
        return true;
      }
    }
    return false;
  }

  /** 図形の集合から図形を削除する
   *  @param shape 削除する図形
   *  @return 図形のidと一致する要素がリスト中にあれば true, なければ false
   */
  public synchronized boolean remove(Shape shape){
    int id = shape.id;
    for(Shape s:data){
      if(s.id == id){
        data.remove(s);
        return true;
      }
    }
    return false;
  }

  public synchronized void copy(ShapeManager in){
    synchronized(in){
      data.clear();
      for(Shape s: in.data){
        data.add(s);
      }
    }
  }

  public synchronized void merge(ShapeManager in){
    synchronized(in){
      for(Shape si:in.data){
        put(si);
      }
    }
  }

  /** 図形の集合の複製をつくる
   * @return 新たな{@link para.graphic.shape.ShapeManager ShapeManager}
   */
  public synchronized ShapeManager duplicate(){
    ShapeManager ret = new ShapeManager();
    for(Shape s:data){
      ret.data.add(s);
    }
    return ret;
  }
  
  public synchronized Shape[] getData(){
    return data.toArray(new Shape[0]);
  }

  public synchronized Stream<Shape> getStream(){
    return data.stream();
  } 
  
  public synchronized Stream<Shape> getParallelStream(){
    return data.parallelStream();
  } 
  
  /** 集合内の図形を出力する．
   *  @param target 出力装置
   */
  public synchronized void draw(Target target){
    for(Shape s:data){
      s.draw(target); 
    }
  }
}
