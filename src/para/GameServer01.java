package para;

import java.io.IOException;
import java.util.Random;
import java.util.stream.IntStream;

import para.graphic.shape.Rectangle;
import para.graphic.shape.Attribute;
import para.graphic.shape.ShapeManager;
import para.graphic.shape.OrderedShapeManager;
import para.graphic.shape.Vec2;
import para.graphic.shape.MathUtil;
import para.graphic.shape.Shape;
import para.graphic.shape.Circle;
import para.graphic.shape.Digit;
import para.graphic.shape.CollisionCheckerParallel3;;
import para.graphic.target.TranslateTarget;
import para.graphic.target.TranslationRule;
import para.game.GameServerFrame;
import para.game.GameInputThread;
import para.game.GameTextTarget;

public class GameServer01{
  final Attribute wallattr = new Attribute(250,230,200,true,0,0,0);
  final Attribute ballattr = new Attribute(250,120,120,true,0,0,0);
  final Attribute scoreattr = new Attribute(60,60,60,true,0,0,0);
  final int MAXCONNECTION=2;
  final GameServerFrame gsf;
  final ShapeManager[] userinput;
  final ShapeManager[] wall;
  final ShapeManager[] blocks;
  final ShapeManager[] ballandscore;
  final Vec2[] pos;
  final Vec2[] vel;
  final int[] score;
  final int[] life;
  final int[] barLen;
  final int[] barHeight;
  final int[] chain;
  final int[][] item;
  final int[] loser;
  final CollisionCheckerParallel3 checker;

  private GameServer01(){
    checker = new CollisionCheckerParallel3(5);
    gsf = new GameServerFrame(MAXCONNECTION);
    userinput = new ShapeManager[MAXCONNECTION];
    wall = new OrderedShapeManager[MAXCONNECTION];
    blocks = new OrderedShapeManager[MAXCONNECTION];
    ballandscore = new ShapeManager[MAXCONNECTION];
    pos = new Vec2[MAXCONNECTION];
    vel = new Vec2[MAXCONNECTION];
    score = new int[MAXCONNECTION];
    life = new int[MAXCONNECTION];
    barLen = new int[MAXCONNECTION];
    barHeight = new int[MAXCONNECTION];
    chain = new int[MAXCONNECTION];
    item = new int[MAXCONNECTION][33*20];
    loser = new int[MAXCONNECTION];
    for(int i=0;i<userinput.length;i++){
      userinput[i] = new ShapeManager();
      ballandscore[i] = new ShapeManager();
      wall[i] = new OrderedShapeManager();
      blocks[i] = new OrderedShapeManager();
      pos[i] = new Vec2(i*350+150,200);
      vel[i] = new Vec2(0,0);
    }
  }

  public void start(){
    try{
      gsf.init();
    }catch(IOException ex){
      System.err.println(ex);
    }
    gsf.welcome();

    int gs=0;
    while(true){
      gs = (gs+1)%350;
      if(gsf.array.size() == 2){
        GameInputThread git = gsf.queue.poll();
        while(git != null) {
          int id = git.getUserID();
          init(id);
          startReceiver(git);
          git = gsf.queue.poll();
        }
      }
      try{
        Thread.sleep(100);
      }catch(InterruptedException ex){
      }
      boolean fin = false;
      for(int i=0;i<MAXCONNECTION;i++){
        GameTextTarget out = gsf.getUserOutput(i);
        if(out != null){
          fin |= calcForOneUser(i);
          ballandscore[i].put(new Circle(i*10000+1, (int)pos[i].data[0],
                                 (int)pos[i].data[1], 5, ballattr));
          putScore(i,score[i]);
          out.state(((int)vel[i].length())*100000 + barHeight[i]*100 + barLen[i]);
          out.gamerstate(0);
          //out.gamerstate(gs); //Gamerの状態をクライアントに伝える
          distributeOutput(out);
        }
      }
      if (fin) {
        gameOver();
      }
    }
  }
    
  private void startReceiver(GameInputThread git){
    int id = git.getUserID();
    git.init(new TranslateTarget(userinput[id],
                    new TranslationRule(id*10000,new Vec2(id*350,0))),
             new ShapeManager[]{userinput[id],wall[id],
                                blocks[id],ballandscore[id]}
             );
    git.start();
  }

  private void init(int id){
    wall[id].add(new Rectangle(id*10000+5, id*350+0, 0, 320, 20, wallattr));
    wall[id].add(new Rectangle(id*10000+6, id*350+0, 0, 20, 300, wallattr));
    wall[id].add(new Rectangle(id*10000+7, id*350+300,0, 20, 300, wallattr));
    wall[id].add(new Rectangle(id*10000+8, id*350+0,281, 320, 20, wallattr));
    Random rd = new Random();
    IntStream.range(0,33*20).forEach(n->{
        item[id][n] = rd.nextInt(20);
        item[id][n] = item[id][n] < 16 ? 0 : item[id][n]-16;
        int x = n%33;
        int y = n/33;
        Attribute attr = item[id][n] == 0 ? new Attribute(250,100,250,true,0,0,0)
                       : item[id][n] == 1 ? new Attribute(100,250,250,true,0,0,0)
                       : item[id][n] == 2 ? new Attribute(250,250,100,true,0,0,0)
                       :                    new Attribute(100,250,100,true,0,0,0);
        blocks[id].add(new Rectangle(id*10000+n,id*350+30+x*8,30+y*8,6,6,
                             attr));
      });
    pos[id] = new Vec2(id*350+150,200);
    vel[id] = new Vec2(4,-12);
    score[id] = 0;
    life[id] = 4;
    barLen[id] = 60;
    barHeight[id] = 225;
    loser[id] = 0;
    for(int i=0;i<life[id];i++) {
      ballandscore[id].put(new Circle(id*10000+6+i, id*350+i*15+50, 330, 5, ballattr));
    }
  }
  
  private boolean calcForOneUser(int id){
    float[] btime = new float[]{1.0f};
    float[] stime = new float[]{1.0f};
    float[] wtime = new float[]{1.0f};
    float time = 1.0f;
    boolean ret = false;
    int enemyId = (id+1) % 2;
    while(0<time){
      btime[0] = time;
      stime[0] = time;
      wtime[0] = time;
      Vec2 tmpbpos = new Vec2(pos[id]);
      Vec2 tmpbvel = new Vec2(vel[id]);
      Vec2 tmpspos = new Vec2(pos[id]);
      Vec2 tmpsvel = new Vec2(vel[id]);
      Vec2 tmpwpos = new Vec2(pos[id]);
      Vec2 tmpwvel = new Vec2(vel[id]);
      Shape b=checker.check(userinput[id], tmpbpos, tmpbvel, btime);
      Shape s=checker.check(blocks[id], tmpspos, tmpsvel, stime);
      Shape w=checker.check(wall[id], tmpwpos, tmpwvel, wtime);
      if( b != null && 
          (s == null || stime[0]<btime[0]) &&
          (w == null || wtime[0]<btime[0])){
        pos[id] = tmpbpos;
        vel[id] = tmpbvel;
        float spd = vel[id].length();
        vel[id].add(new Vec2((pos[id].data[0]-b.getCenter().data[0])*10/barLen[id], 0));
        vel[id].normalize();
        vel[id].mul(spd);
        time = btime[0];
      }else if(s != null){
        int ef = item[id][s.getID()%1000];
        blocks[id].remove(s); // block hit!
        chain[id] += 1;
        pos[id] = tmpspos;
        vel[id] = tmpsvel;
        if (ef == 1) {
          //barLen[id] = barLen[id] < 60 ? barLen[id]+1 : 60;
          barLen[enemyId] = barLen[enemyId] > 20 ? barLen[enemyId]-10 : 20;
        } else if (ef == 2) {
          barHeight[enemyId] = barHeight[enemyId] > 180 ? barHeight[enemyId]-10 : 180;
        } else if (ef == 3) {
          vel[enemyId].mul((float)1.05);
        } else {
          score[id] += 1;
        }
        barLen[id] = barLen[id]+chain[id]/50 < 60 ? barLen[id]+1+chain[id]/50 : 60;
        barHeight[id] = barHeight[id]+chain[id]/50 < 225 ? barHeight[id]+1+chain[id]/50 : 225;
        score[id] += 1 + chain[id]/10;
        if (vel[id].length() > 12) vel[id].mul((float)0.99);
        time = stime[0];
        ret = (blocks.length == 0);
      }else if(w != null){
        pos[id] = tmpwpos;
        vel[id] = tmpwvel;
        time = wtime[0];
        if (w.getID()%10000 == 8) {
          damage(id);
          life[id] -= 1;
          chain[id] = 0;
          ret = (life[id] < 0);
        }
      }else{
        pos[id] = MathUtil.plus(pos[id], MathUtil.times(vel[id],time));
        time = 0;
      }
    }
    return ret;
  }

  private void putScore(int id, int score){
    int one = score%10;
    int ten = (score/10)%10;
    int hun = (score/100)%10;
    int tho = (score/1000)%10;
    ballandscore[id].put(new Digit(id*10000+2,id*350+300,330,20,one,scoreattr));
    ballandscore[id].put(new Digit(id*10000+3,id*350+250,330,20,ten,scoreattr));
    ballandscore[id].put(new Digit(id*10000+4,id*350+200,330,20,hun,scoreattr));
    ballandscore[id].put(new Digit(id*10000+5,id*350+150,330,20,tho,scoreattr));
  }

  private void damage(int id) {
    ballandscore[id].remove(id*10000+5+life[id]);
  }

  private void gameOver() {
    for(int i=0;i<MAXCONNECTION;i++){
      GameTextTarget out = gsf.getUserOutput(i);
      if (out != null) {
        score[i] += life[i]< 1 ? 0 : life[i]*100;
        putScore(i,score[i]);
      }
    }
    for(int i=0;i<MAXCONNECTION*3;i++){
      GameTextTarget out = gsf.getUserOutput(i%2);
      if (out != null) {
        loser[i%2] = score[i%2] < score[(i+1)%2] ? 1 : 0;
        out.gamerstate(loser[i%2]);
        distributeOutput(out);
      }
    }
    for(int i=0;i<MAXCONNECTION;i++){
      GameTextTarget out = gsf.getUserOutput(i);
      if (out != null) {
        out.finish();
      }
    }
  }
  
  private void distributeOutput(GameTextTarget out){
    if(out == null){
      return;
    }
    out.clear();
    for(int i=0;i<MAXCONNECTION;i++){
      if(gsf.getUserOutput(i)!=null){
        out.draw(wall[i]);
        out.draw(blocks[i]);
        out.draw(userinput[i]);
        out.draw(ballandscore[i]);
      }
    }
    out.flush();
  }

  public static void main(String[] args){
    GameServer01 gs = new GameServer01();
    gs.start();
  }
}
