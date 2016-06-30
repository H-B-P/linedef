package com.hbp.linedef;

import java.util.Iterator;
import java.text.DecimalFormat;
//import java.math.*;
//import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
//import com.badlogic.gdx.Input.Keys;
//import com.badlogic.gdx.Input.Buttons;
//import com.badlogic.gdx.audio.Music;
//import com.badlogic.gdx.audio.Sound;
//import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Preferences;


public class GameScreen_2 implements Screen {
	
	final LineDef game;
	
	private Texture mineImage;
   private Texture dotImage;
   private Texture shipImage;
   private Texture[] shipImages;
   private Texture gridImage;
   private Texture statusbarImage;
   private Texture explosionImage;
   private Texture shieldImage;
   private Texture shieldImage_unhit;
   private Texture shieldImage_flicker;
   
   private Texture i_shield_t;
   private TextureRegion i_shield_tr;
   
   private Texture infobubble_1;
   private Texture[] circles_t;
   
   private SpriteBatch batch;
   private OrthographicCamera camera;
   
   private int radius;
   private double xd;
   private double yd;
   private Rectangle dot;
   private Rectangle actual_dot;
   
   private Rectangle ship;
   
   private Array<Rectangle> mines;
   private Array<Kaboom> explosions;
   private Array<Kaboom> horizontal_i_shields;
   private Array<Kaboom> vertical_i_shields;
   private Array<PolyKaboom> general_i_shields;
   private Array<CircleKaboom> circle_i_shields;
   
   private Rectangle grid;
   private Array<Rectangle> shields;
   
   private double posn_x;
   private double posn_y;
   private double rounded_posn_x;
   private double rounded_posn_y;
   
   private double prev_rounded_posn_x;
   private double prev_rounded_posn_y;
   
   private double rotdeg;
   
   private Rectangle menu_button_r;
   private Texture menu_button_t;
   
   private int score;
   
   private int prefs_score;
   
   private BitmapFont font;
   private BitmapFont dotfunction_font;
   
   private Preferences prefs;
   
   private int seconds;
   
   private int charges;
   
   private int maxcharges;
   
   
   private boolean wastouched;
   
   private boolean IS_TIME_HAPPENING;
   
   private float total_time;
   
   private double UNIT_LENGTH_IN_PIXELS;
   private String CURRENT_LINE;
   
   public String MODE;
   public String GENRE;
   private int MINESPEED;
   
 //---Do all the initial stuff that happens on rendering---
   
   public GameScreen_2(final LineDef gam, int minespeed, String genre, String mode) {
	  
	   //--Perform tautological actions--
	   this.game = gam;
      
      MODE=mode;
      GENRE=genre;
      MINESPEED=minespeed;
      
      //--Set up highscores--
      
      prefs = Gdx.app.getPreferences("galen_preferences");
      prefs_score=prefs.getInteger("score_"+GENRE+"_"+MODE);
	  
	  //--Load images--
      mineImage = new Texture(Gdx.files.internal("a_mine_2.png"));
      dotImage = new Texture(Gdx.files.internal("sniperdot.png"));
      shipImages = new Texture[10];
      
      statusbarImage = new Texture(Gdx.files.internal("statusbar_blank.png"));
      gridImage = new Texture(Gdx.files.internal("grid_t.png"));
      
      explosionImage = new Texture(Gdx.files.internal("explosion.png"));
      shieldImage_unhit = new Texture(Gdx.files.internal("shield.png"));
      shieldImage_flicker = new Texture(Gdx.files.internal("shield_flicker.png"));
      shieldImage=shieldImage_unhit;
      new Texture(Gdx.files.internal("Head.png"));
      
      i_shield_t=new Texture(Gdx.files.internal("intercept_shield.png"));
      i_shield_tr=new TextureRegion(i_shield_t);
      
      infobubble_1=new Texture(Gdx.files.internal("infobubble_1.png"));
      
      shipImages[0] = new Texture(Gdx.files.internal("Head.png"));
      shipImages[1] = new Texture(Gdx.files.internal("Head_1_1.png"));
      shipImages[2] = new Texture(Gdx.files.internal("Head_1_2.png"));
      shipImages[3] = new Texture(Gdx.files.internal("Head_1_3.png"));
      shipImages[4] = new Texture(Gdx.files.internal("Head_2_1.png"));
      shipImages[5] = new Texture(Gdx.files.internal("Head_2_2.png"));
      shipImages[6] = new Texture(Gdx.files.internal("Head_2_3.png"));
      shipImages[7] = new Texture(Gdx.files.internal("Head_3_1.png"));
      shipImages[8] = new Texture(Gdx.files.internal("Head_3_2.png"));
      shipImages[9] = new Texture(Gdx.files.internal("Head_3_3.png"));
      
      shipImage=shipImages[0];
      
      menu_button_t=new Texture(Gdx.files.internal("button_menu_smol.png"));
      
      //--Set zeroes to zero--
      score=MINESPEED/5;
      
      posn_x=0;
      posn_y=0;
      rounded_posn_x=0;
      rounded_posn_y=0;
      
      prev_rounded_posn_x=0;
      prev_rounded_posn_y=0;
      
      total_time=0;
      seconds=0;
      wastouched=false;
      //(Whether the game is not-exactly-paused.)
      IS_TIME_HAPPENING=true;
      
      //--Create rectangles--
      
      menu_button_r=new Rectangle(240,450,100,40);
      
      dot = new Rectangle();
      dot.x = 0;
      dot.y = 0;
      
      actual_dot = new Rectangle();
      actual_dot.x = 0;
      actual_dot.y = 0;
      
      ship = new Rectangle(0,0, 320, 60);
      grid = new Rectangle();
      
      shields= new Array<Rectangle>();
      
      mines = new Array<Rectangle>();
      explosions = new Array<Kaboom>();
      horizontal_i_shields = new Array<Kaboom>();
      vertical_i_shields = new Array<Kaboom>();
      general_i_shields = new Array<PolyKaboom>();
      circle_i_shields = new Array<CircleKaboom>();
      
      //--Set up basics--
      spawnShield(1);
      //spawnShield(2);
      //spawnShield(3);
      font = new BitmapFont();
      font.setColor(Color.CYAN);
      dotfunction_font = new BitmapFont();
      dotfunction_font.setColor(Color.BLACK);
      maxcharges=6;
      
      //--Batch, Camera, Action--
      camera = new OrthographicCamera();
      camera.setToOrtho(false, 320, 480);
      batch = new SpriteBatch();
      
      UNIT_LENGTH_IN_PIXELS=40;
      
      CURRENT_LINE="Horizontal";
      
      circles_t=new Texture[8];
      
      for(int i=0; i<7; i++){
			if (Gdx.files.internal("circle_r_"+i+".png").exists()){
				circles_t[i]=new Texture(Gdx.files.internal("circle_r_"+i+".png"));
			}
      }
   }
   
   //---Set up functions to be called during Render---
   
   //(These two are self-explanatory.)
   
   private void DO_ABSOLUTELY_NOTHING(){
	   
   }
   
   private int plusorminus(){
	   int coin=MathUtils.random(0,1);
	   return coin*2-1;
   }
   
   //--Collisions between geometric shapes. Why is this not used in libgdx?
   
   private boolean Rectangle_collides_with_Polygon(Rectangle rec, Polygon pol) {
	   float[] rp_input=new float[]{rec.x, rec.y, rec.x+rec.width, rec.y, rec.x+rec.width, rec.y+rec.height, rec.x, rec.y+rec.height};
	   Polygon recpol=new Polygon(rp_input);
	   return Intersector.overlapConvexPolygons(pol, recpol);
   }
   
   private boolean Circle_contains_Rectangle(Circle cir, Rectangle rec) {
	   float[] verts= new float[]{rec.x, rec.y, rec.x+rec.width, rec.y, rec.x+rec.width, rec.y+rec.height, rec.x, rec.y+rec.height};
	   if (cir.contains(verts[0], verts[1]) && cir.contains(verts[2], verts[3]) && cir.contains(verts[4], verts[5]) && cir.contains(verts[6], verts[7])){
		   return true;
	   }
	   else{
		   return false;
		   
	   }
   }
   
   private boolean Circle_intersects_Rectangle(Circle cir, Rectangle rec){
	   if (Intersector.overlaps(cir, rec) && !Circle_contains_Rectangle(cir, rec)){
		   return true;
	   }
	   else{
		   return false;
	   }
   }
  
   //--Game functions--
   
   //(The below functions are those which are more on the 'game' side of the 'math game')
   
   private void spawnShield(int no){
	   	Rectangle shield = new Rectangle();
	    shield.x=20;
	    shield.y = 50+no*20;
	    shield.width = 280;
	    shield.height = 3;
	    shields.add(shield);
   }
   
   private void spawnMine_II(int xposn) {
	      Rectangle mine = new Rectangle();
	      Double xposn_II = (xposn*40.0+160.0)-20.0;
	      mine.x = xposn_II.floatValue();
	      mine.y = 440;
	      mine.width = 40;
	      mine.height = 40;
	      mines.add(mine);
	   }
   
   private void spawnRandomMine(){
	   int k=MathUtils.random(-3,3);
	   spawnMine_II(k);
	   
   }
   
   private void spawnRandomMine_r(){
	   int k=MathUtils.random(-3,-1);
	   spawnMine_II(k);
	   
   }
   private void spawnRandomMine_l(){
	   int k=MathUtils.random(1,3);
	   spawnMine_II(k);
	   
   }
   
   private void spawn_horizontal_i_shield(float x,float y) {
	   Kaboom horizontal_i_shield = new Kaboom();
	   horizontal_i_shield.birthtime=total_time;
	   horizontal_i_shield.rect= new Rectangle();
	   horizontal_i_shield.rect.width = 1000;
	   horizontal_i_shield.rect.height = 5;
	   horizontal_i_shield.rect.x=x;
	   horizontal_i_shield.rect.y=y;
	   horizontal_i_shields.add(horizontal_i_shield);
	   CURRENT_LINE="Vertical";
   }
   
   private void spawn_vertical_i_shield(float x,float y) {
	   Kaboom vertical_i_shield = new Kaboom();
	   vertical_i_shield.birthtime=total_time;
	   vertical_i_shield.rect= new Rectangle();
	   vertical_i_shield.rect.width = 5;
	   vertical_i_shield.rect.height = 1000;
	   vertical_i_shield.rect.x=x;
	   vertical_i_shield.rect.y=y;
	   vertical_i_shields.add(vertical_i_shield);
	   CURRENT_LINE="General_yinterc";
   }
   
   private void spawn_general_i_shield(float y_intercept,float rot) {
	   PolyKaboom general_i_shield = new PolyKaboom();
	   general_i_shield.birthtime=total_time;
	   general_i_shield.poly= new Polygon();
	   general_i_shield.poly.setOrigin(160, y_intercept);
	   float[] vertices=new float[]{-500, y_intercept-2, -500, y_intercept+2, 500, y_intercept+2, 500, y_intercept-2};
	   general_i_shield.poly.setVertices(vertices);
	   general_i_shield.poly.setRotation(rot);
	   general_i_shields.add(general_i_shield);
	   CURRENT_LINE="OriC_centre";
   }
   
   private void spawn_circle_i_shield(float x_centre, float y_centre, float radius) {
	   CircleKaboom circle_i_shield = new CircleKaboom();
	   circle_i_shield.birthtime=total_time;
	   circle_i_shield.circ= new Circle(x_centre, y_centre, radius);
	   circle_i_shields.add(circle_i_shield);
   }
   
   private void spawnExplosion(float X, float Y){
	   Kaboom boom = new Kaboom();
	   boom.rect= new Rectangle();
	   boom.birthtime=total_time;
	   boom.rect.x= X;
	   boom.rect.y= Y;
	   explosions.add(boom);
   }
   
   private void wave_without_pause(int ss){
		  int ts=ss+5;
		   if (seconds>=ts && seconds<ts+20){
	 		  if((seconds-ts)%2 == 0) spawnRandomMine();
	 		 dotfunction_font.setColor(Color.BLACK);
	 	   }
		   if (seconds>=ts+20 && seconds<ts+40){
			   if((seconds-ts)%4 == 0) spawnRandomMine();
		 	   if((seconds-ts)%4 == 2){
		 			spawnRandomMine_r();
					spawnRandomMine_l();
		 	   } 
		   }
	   }
   
   //---RENDER---
   @Override
   public void render(float delta) {
	   
	   //--Adjust time--
      if(IS_TIME_HAPPENING){
	   total_time+=Gdx.graphics.getDeltaTime();
      }
	  //--Update ship image used--
      shipImage = shipImages[charges];
      
	  Gdx.gl.glClearColor(0, 0, 0, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      
      Gdx.graphics.setWindowedMode(320, 480);

      //--Update Camera, update Batch--
      camera.update();
      batch.setProjectionMatrix(camera.combined);
      
      batch.begin();
      batch.draw(gridImage, grid.x, grid.y);
      for(Kaboom boom: explosions) {
          batch.draw(explosionImage, boom.rect.x-20, boom.rect.y-20);
       }
      
      
      
      for(Rectangle mine: mines) {
         batch.draw(mineImage, mine.x-20, mine.y-20);
      }
      
      
//      for(Kaboom horizontal_i_shield: horizontal_i_shields) {
//    	  batch.draw(i_shield_t, horizontal_i_shield.rect.x, horizontal_i_shield.rect.y);
//       }
//      for(Kaboom vertical_i_shield: vertical_i_shields) {
//    	  //batch.draw(i_shield_t, vertical_i_shield.rect.x, vertical_i_shield.rect.y);
//    	  batch.draw(i_shield_tr, vertical_i_shield.rect.x, vertical_i_shield.rect.y, 0, 0, 500f, 5f, 1f, 1f, 90f, true);
//       }

      for(Rectangle shield: shields) {
          batch.draw(shieldImage, shield.x, shield.y-3);
       }
      
      
      if(Gdx.input.getY()>80 && Gdx.input.getY()<480 && Gdx.input.getX()>0 && Gdx.input.getX()<320){
    	  posn_x=(Gdx.input.getX()-160)/UNIT_LENGTH_IN_PIXELS;
    	  posn_y=-(Gdx.input.getY()-240)/UNIT_LENGTH_IN_PIXELS;
    	  rounded_posn_x=Math.round((float)posn_x);
    	  rounded_posn_y=Math.round((float)posn_y);
    	  xd=posn_x-prev_rounded_posn_x;
    	  yd=posn_y-prev_rounded_posn_y;
      }
      
      dot.x=(float)(rounded_posn_x*UNIT_LENGTH_IN_PIXELS+160);
      dot.y=(float)(rounded_posn_y*UNIT_LENGTH_IN_PIXELS+240);
      
      if(CURRENT_LINE=="OriC_centre"){
    	  prev_rounded_posn_x=0;
    	  prev_rounded_posn_y=0;
    	  actual_dot.x=160;
    	  actual_dot.y=240;
    	  CURRENT_LINE="Circle_line";    			  
      }
      
      if(CURRENT_LINE=="Horizontal"){
    	  batch.draw(i_shield_tr, -90, dot.y-3, 500, 0, 500f, 5f, 1f, 1f, 0f, true);
      }
      if(CURRENT_LINE=="Vertical"){
    	  batch.draw(i_shield_tr, dot.x+3, 0, 0, 0, 500f, 5f, 1f, 1f, 90f, true);
      }
      if(CURRENT_LINE=="General_yinterc"){
    	  batch.draw(dotImage, 160-6, dot.y-6);
      }
      if(CURRENT_LINE=="General_line"){
    	  //batch.draw(dotImage, actual_dot.x-5, actual_dot.y-5);
    	  if(rounded_posn_x!=0){
    		  rotdeg=Math.atan((rounded_posn_y-prev_rounded_posn_y)/(rounded_posn_x-prev_rounded_posn_x))*180/Math.PI;
    		  batch.draw(i_shield_tr, actual_dot.x-500, actual_dot.y-3, 500f, 0, 1000f, 5f, 1f, 1f, (float)rotdeg, true);
    	  }
      }
      if(CURRENT_LINE=="Circle_centre"){
    	  batch.draw(dotImage, dot.x-5, dot.y-5);
      }
     
      if(CURRENT_LINE=="Circle_line"){
    	  //batch.draw(dotImage, actual_dot.x-5, actual_dot.y-5);
    	  
    	  radius=(int)Math.round(Math.sqrt(xd*xd+yd*yd));
    	  if (radius<7 && radius>0){
    		  batch.draw(circles_t[radius], (float)(actual_dot.x-radius*UNIT_LENGTH_IN_PIXELS), (float)(actual_dot.y-radius*UNIT_LENGTH_IN_PIXELS));
    	  }
      }
      
      DecimalFormat df = new DecimalFormat("#.#");
      DecimalFormat df_two = new DecimalFormat("#");
      DecimalFormat df_three = new DecimalFormat("#.##");
      
      if(CURRENT_LINE=="Horizontal"){
    	  font.draw(batch, "y = "+(int)rounded_posn_y, Gdx.input.getX()-30, 480-Gdx.input.getY()+40);
      }
      if(CURRENT_LINE=="Vertical"){
    	  font.draw(batch, "x = "+(int)rounded_posn_x, Gdx.input.getX()-50, 480-Gdx.input.getY()+10);
      }
      if(CURRENT_LINE=="General_yinterc"){
    	  if (rounded_posn_y>0){
    		  font.draw(batch, "y = mx + "+(int)rounded_posn_y, Gdx.input.getX()-50, 480-Gdx.input.getY()+10);
    	  }
    	  else if (rounded_posn_y==0){
    		  font.draw(batch, "y = mx", Gdx.input.getX()-50, 480-Gdx.input.getY()+10);
    	  }
    	  else if (rounded_posn_y<0){
    		  font.draw(batch, "y = mx -"+(int)-rounded_posn_y, Gdx.input.getX()-50, 480-Gdx.input.getY()+10);
    	  }
      }
      if(CURRENT_LINE=="General_line"){
    	  if (rounded_posn_y>0){
    		  font.draw(batch, "y = (" +(int)(rounded_posn_y-prev_rounded_posn_y)+"/"+(int)(rounded_posn_x-prev_rounded_posn_x) + ")x + "+(int)prev_rounded_posn_y, Gdx.input.getX()-50, 480-Gdx.input.getY()+10);
    	  }
    	  else if (rounded_posn_y==0){
    		  font.draw(batch, "y = (" +(int)(rounded_posn_y-prev_rounded_posn_y)+"/"+(int)(rounded_posn_x-prev_rounded_posn_x) + ")x", Gdx.input.getX()-50, 480-Gdx.input.getY()+10);
    	  }
    	  else if (rounded_posn_y<0){
    		  font.draw(batch, "y = (" +(int)(rounded_posn_y-prev_rounded_posn_y)+"/"+(int)(rounded_posn_x-prev_rounded_posn_x) + ")x - "+(int)-prev_rounded_posn_y, Gdx.input.getX()-50, 480-Gdx.input.getY()+10);
    	  }
    	  
      }
      if(CURRENT_LINE=="Circle_centre"){
    	  String xpart="";
    	  String ypart="";
    	  if (rounded_posn_x>0){
    		  xpart="(x-"+(int)rounded_posn_x+")^2";
    	  }
    	  else if(rounded_posn_x==0){
    		  xpart="x^2";
    	  }
    	  if (rounded_posn_x<0){
    		  xpart="(x+"+(int)-rounded_posn_x+")^2";
    	  }
    	  if (prev_rounded_posn_y>0){
    		  ypart="(y-"+(int)rounded_posn_y+")^2";
    	  }
    	  else if(rounded_posn_y==0){
    		  ypart="y^2";
    	  }
    	  if (rounded_posn_y<0){
    		  ypart="(y+"+(int)-rounded_posn_y+")^2";
    	  }
    	  font.draw(batch, xpart + " + " + ypart + " = r^2", Gdx.input.getX()-50, 480-Gdx.input.getY()+10);
      }
      if(CURRENT_LINE=="Circle_line"){
    	  String xpart="";
    	  String ypart="";
    	  if (prev_rounded_posn_x>0){
    		  xpart="(x-"+(int)prev_rounded_posn_x+")^2";
    	  }
    	  else if(prev_rounded_posn_x==0){
    		  xpart="x^2";
    	  }
    	  if (prev_rounded_posn_x<0){
    		  xpart="(x+"+(int)-prev_rounded_posn_x+")^2";
    	  }
    	  if (prev_rounded_posn_y>0){
    		  ypart="(y-"+(int)prev_rounded_posn_x+")^2";
    	  }
    	  else if(prev_rounded_posn_y==0){
    		  ypart="y^2";
    	  }
    	  if (prev_rounded_posn_y<0){
    		  ypart="(y+"+(int)-prev_rounded_posn_x+")^2";
    	  }
    	  font.draw(batch, xpart + " + " + ypart + " = "+ radius*radius, Gdx.input.getX()-50, 480-Gdx.input.getY()+10);
      }
      batch.draw(shipImage, ship.x, ship.y);
      //--Draw status bar and menu button--
      //(These have to be drawn last so the dot doesn't go over them.)
      
      batch.draw(statusbarImage, 0, 400);
      batch.draw(menu_button_t,265,455);
      
      //batch.draw(infobubble_1,Gdx.input.getX()-80-5,480-Gdx.input.getY()+5);
      
      batch.end();
      
      
      //--Exit the game when main menu button pressed--
      
      //(Do NOT move this back to inside the batch. Weird, weird bugs crop up if you do.)
      
      if(Gdx.input.isTouched()){
    	  if (menu_button_r.contains(Gdx.input.getX(), 480-Gdx.input.getY())){
    		  game.setScreen(new MainMenuScreen(game, GENRE, MINESPEED));
    		  dispose();
    	  }
      }
      
      //--Do the things which happen every second--
      
      if((seconds+1)<(total_time)){
    	  seconds+=1;
    	  System.out.println(seconds);
    	  
    	  //Now we have the setup out of the way . . .
    	  
    	  //Updates to charges
    	  if(charges<maxcharges){
    		  charges+=1;
    	  }
    	  //Events!
    	  
    	  
    	  if (true){
	    	  wave_without_pause(0);
	    	  wave_without_pause(50);
	    	  wave_without_pause(100);
	    	  wave_without_pause(150);
    	  }
    	  if (seconds==203){
    		  
    		  
    		  if(score>prefs_score){
    			  
    	    	  
    	    	  prefs.putInteger("score_"+GENRE+"_"+MODE, score);
    	    	  prefs.flush();
    		  }
    		  game.setScreen(new MainMenuScreen(game, GENRE, MINESPEED));
    		  dispose();
    	  }
      }
      
      //--Check whether explosions and other such things have timed out--
      Iterator<Kaboom> iterk = explosions.iterator();
      while(iterk.hasNext()) {
    	  Kaboom boom = iterk.next();
    	  if(total_time - boom.birthtime > 0.25) iterk.remove();
      }
      
      Iterator<Kaboom> iterh = horizontal_i_shields.iterator();
      while(iterh.hasNext()) {
    	  Kaboom other_dot = iterh.next();
    	  if(total_time - other_dot.birthtime > 0.1) iterh.remove();
      }
      
      Iterator<Kaboom> iterv = vertical_i_shields.iterator();
      while(iterv.hasNext()) {
    	  Kaboom other_dot = iterv.next();
    	  if(total_time - other_dot.birthtime > 0.1) iterv.remove();
      }
      
      Iterator<PolyKaboom> iterg = general_i_shields.iterator();
      while(iterg.hasNext()) {
    	  PolyKaboom other_dot = iterg.next();
    	  if(total_time - other_dot.birthtime > 0.1) iterg.remove();
      }
      
      Iterator<CircleKaboom> iterc = circle_i_shields.iterator();
      while(iterc.hasNext()) {
    	  CircleKaboom other_dot = iterc.next();
    	  if(total_time - other_dot.birthtime > 0.1) iterc.remove();
      }
      
      //--Check for overlap between mines and mine-detonators; act appropriately if found--
      
      shieldImage=shieldImage_unhit;
      
      Iterator<Rectangle> iter = mines.iterator();
      
      if (IS_TIME_HAPPENING){
      
		  while(iter.hasNext()) {
		     Rectangle mine = iter.next();
		     mine.y -= MINESPEED * Gdx.graphics.getDeltaTime();
		     if(mine.y + 80 < 0) iter.remove();
		 
			 boolean deadyet=false;
			 
			 Iterator<Rectangle> iters = shields.iterator();
			 while(iters.hasNext()) {
			 Rectangle shield = iters.next();
			 if(mine.overlaps(shield) && !deadyet) {
		     	spawnExplosion(mine.x,mine.y);
		        //iters.remove();
		         	iter.remove();
		         	deadyet=true;
		         	shieldImage=shieldImage_flicker;
		             
		          }
		     }
		     
		     Iterator<Kaboom> iterod = horizontal_i_shields.iterator();
		     while(iterod.hasNext()) {
		    	 Kaboom other_dot = iterod.next();
		    	 if(other_dot.rect.overlaps(mine) && !deadyet) {
		         	spawnExplosion(mine.x,mine.y);
		         	iter.remove();
		         	deadyet=true;
		            score+=1;
		          }
		     }
		     
		     Iterator<Kaboom> itervs = vertical_i_shields.iterator();
		     while(itervs.hasNext()) {
		    	 Kaboom other_dot = itervs.next();
		    	 if(other_dot.rect.overlaps(mine) && !deadyet) {
		         	spawnExplosion(mine.x,mine.y);
		         	iter.remove();
		         	deadyet=true;
		            score+=1;
		          }
		     }
		     
		     Iterator<PolyKaboom> itergs = general_i_shields.iterator();
		     while(itergs.hasNext()) {
		    	 PolyKaboom other_dot = itergs.next();
		    	 if(Rectangle_collides_with_Polygon(mine, other_dot.poly) && !deadyet) {
		    		 spawnExplosion(mine.x,mine.y);
		         	iter.remove();
		         	deadyet=true;
		            score+=1;
		          }
		     }
		     
		     Iterator<CircleKaboom> itercs = circle_i_shields.iterator();
		     while(itercs.hasNext()) {
		    	 CircleKaboom other_dot = itercs.next();
		    	 if(Circle_intersects_Rectangle(other_dot.circ, mine) && !deadyet) {
		    		 spawnExplosion(mine.x,mine.y);
		         	iter.remove();
		         	deadyet=true;
		            score+=1;
		          }
		     }
		  }
      }      
      //--Let the player pause/unpause--
      if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
    	  IS_TIME_HAPPENING=!IS_TIME_HAPPENING;
      }
      
      //--If the screen just finished being touched, kill any mines overlapping the dot--
      //(also, pause/unpause if they untouched over the ship)
      if (Gdx.input.isTouched()){
    	  wastouched=true;
      }else{
    	  if(wastouched){
    		  if(ship.contains(Gdx.input.getX(), 480-Gdx.input.getY())){
    			  IS_TIME_HAPPENING=!IS_TIME_HAPPENING;
    		  }
    		  else{
    			  if( charges>0 && IS_TIME_HAPPENING){
    				  if (CURRENT_LINE=="Horizontal"){
    					  spawn_horizontal_i_shield(0, dot.y);
    				  }
    				  else if (CURRENT_LINE=="Vertical"){
    					  spawn_vertical_i_shield(dot.x, 0);
    				  }
    				  else if (CURRENT_LINE=="General_yinterc"){
    					  prev_rounded_posn_x=0;
    					  prev_rounded_posn_y=rounded_posn_y;
    					  actual_dot.x=160;
    					  actual_dot.y=dot.y;
    					  CURRENT_LINE="General_line";
    				  }
    				  else if (CURRENT_LINE=="General_line"){
    					  spawn_general_i_shield(actual_dot.y, (float)rotdeg);
    					  
    				  }
    				  else if (CURRENT_LINE=="Circle_centre"){
    					  prev_rounded_posn_x=rounded_posn_x;
    					  prev_rounded_posn_y=rounded_posn_y;
    					  actual_dot.x=dot.x;
    					  actual_dot.y=dot.y;
    					  CURRENT_LINE="Circle_line";
    				  }
    				  else if (CURRENT_LINE=="Circle_line"){
    					  if (radius<7 && radius>0){
    					  spawn_circle_i_shield(actual_dot.x, actual_dot.y, (float)(radius*UNIT_LENGTH_IN_PIXELS));
    					  }
    					  CURRENT_LINE="Horizontal";
    				  }
    				  //charges-=1;
    			  }
    		  }
    	  }
    	  wastouched=false;
      }
      
   }
   @Override
   
   //---END THE WORLD RESPONSIBLY---
   
   //(Still need to do this properly, but leaving most of the images etc
   //running doesn't appear to be causing any problems yet.)
   public void dispose() {
      // dispose of all the native resources
      mineImage.dispose();
      dotImage.dispose();
      batch.dispose();
   }

@Override
public void show() {
	// TODO Auto-generated method stub
	
}

@Override
public void resize(int width, int height) {
	// TODO Auto-generated method stub
	
}

@Override
public void pause() {
	// TODO Auto-generated method stub
	
}

@Override
public void resume() {
	// TODO Auto-generated method stub
	
}

@Override
public void hide() {
	// TODO Auto-generated method stub
	
}
}