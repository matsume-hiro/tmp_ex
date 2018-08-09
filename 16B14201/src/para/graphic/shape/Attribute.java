package para.graphic.shape;

/**
 * 図形の属性
 */
public class Attribute{
  /** 色の赤緑青情報 */
  private int[] color;
  /** 塗りつぶすかどうかのフラグ */
  private boolean fill;
  /** 拡張汎用属性の整数型変数0 */
  private int iaux0;
  /** 拡張汎用属性の浮動小数型変数0 */
  private float faux0;
  /** 拡張汎用属性の浮動小数型変数1 */
  private float faux1;

  /** 図形の属性を生成する
   * @param r 図形の赤成分の強さ属性を[0-255]で設定
   * @param g 図形の緑成分の強さ属性を[0-255]で設定
   * @param b 図形の青成分の強さ属性を[0-255]で設定
   * @param fill 図形の塗りつぶしの描画属性を設定
   * @param iaux0 整数型汎用属性を設定
   * @param faux0 浮動小数点型汎用属性を設定
   * @param faux1 浮動小数点型汎用属性を設定
   */
  public Attribute(int r, int g, int b, boolean fill,
                   int iaux0, float faux0, float faux1){
    color = new int[3];
    color[0]= r<0?0:(255<r?255:r);
    color[1]= g<0?0:(255<g?255:g);
    color[2]= b<0?0:(255<b?255:b);
    this.fill = fill;
    this.iaux0 = iaux0;
    this.faux0 = faux0;
    this.faux1 = faux1;
  }

  /** 図形の属性を生成する
   * @param r 図形の赤成分の強さ属性を[0-255]で設定
   * @param g 図形の緑成分の強さ属性を[0-255]で設定
   * @param b 図形の青成分の強さ属性を[0-255]で設定
   * @param fill 図形の塗りつぶしの描画属性を設定
   */
  public Attribute(int r, int g, int b, boolean fill){
    color = new int[3];
    color[0] = r<0?0:(255<r?255:r);
    color[1] = g<0?0:(255<g?255:g);
    color[2] = b<0?0:(255<b?255:b);
    this.fill= fill; 
  }

  /** 図形の属性を生成する
   * @param r 図形の赤成分の強さ属性を[0-255]で設定
   * @param g 図形の緑成分の強さ属性を[0-255]で設定
   * @param b 図形の青成分の強さ属性を[0-255]で設定
   */
  public Attribute(int r, int g, int b){
    color = new int[3];
    color[0] = r<0?0:(255<r?255:r);
    color[1] = g<0?0:(255<g?255:g);
    color[2] = b<0?0:(255<b?255:b);
    fill= true; 
  }

  /** 属性を生成する
   */
  public Attribute(){
    color = new int[3];
    fill =  true;
  }

  // /** 指定色を範囲内に収めて設定する 
  //  *  @param r 赤成分
  //  *  @param g 緑成分
  //  *  @param b 青成分
  //  *  @return this
  //  */
  // public Attribute setColor(int r, int g, int b){
  //   color[0]= r<0?0:(255<r?255:r);
  //   color[1]= g<0?0:(255<g?255:g);
  //   color[2]= b<0?0:(255<b?255:b);
  //   return this;
  // }

  // /** 塗りつぶしフラグを設定する
  //  *  @param fill 真ならば塗りつぶす
  //  *  @return this
  //  */
  // public Attribute setFill(boolean fill){
  //   this.fill = fill;
  //   return this;
  // }

  // /** 拡張汎用属性の整数型変数0を設定する
  //  *  @param value 設定する値
  //  *  @return this
  //  */
  // public Attribute setIaux0(int value){
  //   this.iaux0 = value;
  //   return this;
  // }
  
  // /** 拡張汎用属性の浮動小数点型変数0を設定する
  //  *  @param value 設定する値
  //  *  @return this
  //  */
  // public Attribute setFaux0(float value){
  //   this.faux0 = value;
  //   return this;
  // }
  
  // /** 拡張汎用属性の浮動小数点型変数1を設定する
  //  *  @param value 設定する値
  //  *  @return this
  //  */
  // public Attribute setFaux1(float value){
  //   this.faux1 = value;
  //   return this;
  // }
  
  /** 色を取得する
   * @return 色(rgb)の格納されている3要素のint型配列
   */
  public int[] getColor(){
    return color.clone();
  }

  /** 塗りつぶしフラグを取得する
   * @return 塗りつぶしフラグの値、真ならば塗りつぶす
   */
  public boolean getFill(){
    return fill;
  }

  /** 拡張汎用属性の整数型変数0の設定値を取得する
   *  @return 変数iaux0の値
   */
  public int getIaux0(){
    return iaux0;
  }
  
  /** 拡張汎用属性の浮動小数点型変数0の設定値を取得する
   *  @return 変数faux0の値
   */
  public float getFaux0(){
    return faux0;
  }
  
  /** 拡張汎用属性の浮動小数点型変数1の設定値を取得する
   *  @return 変数faux1の値
   */
  public float getFaux1(){
    return faux1;
  }
  
}
