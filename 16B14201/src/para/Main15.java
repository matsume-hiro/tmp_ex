package para;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.beans.value.ObservableValue;
import para.graphic.target.*;
import para.graphic.shape.*;
import para.graphic.parser.*;
import para.graphic.opencl.*;

/** スライダにより画像処理効果が変わるプログラム
 */
public class Main15 extends Application{
  final Thread thread;
  final JavaFXCanvasTarget jfc;
  final TargetImageFilter target;
  final ShapeManager sm;
  final String filename;
  final String kernelname;

  public Main15(){
    this("imagefilter.cl", "Filter1");
  }
  
  public Main15(String filename, String kernelname){
    this.filename = filename;
    this.kernelname = kernelname;
    sm = new OrderedShapeManager();
    jfc = new JavaFXCanvasTarget(320,240);
    //target = new TargetImageFilter(jfc);
    target = new TargetImageFilter(jfc, this, filename, kernelname);
    target.init();
    target.clear();
    sm.add(new Camera(0,0,0));
    thread = new Thread(new Runnable(){
        public void run(){
          while(true){
            target.draw(sm);
            target.flush();
            //System.out.println(Thread.currentThread().getName());
            try{
              Thread.sleep(33);
            }catch(InterruptedException e){
            }
          }
        }
      }, "CycleDrawer");
  }

  public void start(Stage stage){
    BorderPane pane = new BorderPane();
    pane.setCenter(jfc);
    Slider slider = new Slider(0, 255.0, 128);
    pane.setBottom(slider);
    slider.valueProperty().addListener(
      (ObservableValue<? extends Number> ov,
       Number oldval, Number newval)->{
        target.setParameter(newval.floatValue());
        //System.out.println(Thread.currentThread().getName());
      });
    Scene scene = new Scene(pane);
    stage.setTitle("ImageFilterWithSlider");
    stage.setScene(scene);
    stage.setOnCloseRequest(ev->{System.exit(0);});
    stage.sizeToScene();
    stage.show();
    thread.start();
  }
}
