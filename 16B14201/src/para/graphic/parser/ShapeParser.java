package para.graphic.parser;
import java.util.*;

import para.graphic.shape.Shape;

interface ShapeParser{
  public Shape parse(Scanner s, int id);
}
