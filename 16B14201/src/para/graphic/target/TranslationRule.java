package para.graphic.target;

import para.graphic.shape.Vec2;

public class TranslationRule{
  final int idoffset;
  final Vec2 xyoffset;

  public TranslationRule(int idoffset, Vec2 xyoffset){
    this.idoffset = idoffset;
    this.xyoffset = new Vec2(xyoffset);
  }

  public int calcTranslateID(int id){
    return idoffset+id;
  }

  public Vec2 calcTranslateXY(float x, float y){
    return new Vec2(x+xyoffset.data[0], y+xyoffset.data[1]);
  }
}
