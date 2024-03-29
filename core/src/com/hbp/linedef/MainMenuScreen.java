package com.hbp.linedef;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Preferences;

public class MainMenuScreen implements Screen {
    final LineDef game;
	OrthographicCamera camera;
	
	private Rectangle nxt_r;
	private Rectangle prv_r;
	
	private Texture nxt_t;
	private Texture prv_t;
	
	private Rectangle but_instructions_r;
	private Texture but_instructions_t;
	
	private Texture instructions_t;
	private Rectangle instructions_r;
	
	private Rectangle cancel_instructions_r;
	
	private Rectangle one_r;
	private Texture one_t;
	
	private Rectangle two_r;
	private Texture two_t;
	
	private String GENRE;
	
	private Rectangle three_r;
	private Texture three_t;
	
	private Rectangle four_r;
	private Texture four_t;
	
	private Preferences prefs;
	
	private Rectangle LIBRARY_r;
	private Texture LIBRARY_t;
	
	private int score_one;
	private int score_two;
	private int score_three;
	private int score_four;
	
	private BitmapFont font;
	
	private int MINESPEED;
	
	private Rectangle selector_r;
	private Rectangle selector_prv_r;
	private Rectangle selector_nxt_r;
	private Texture selector_t;
	
	boolean are_instructions_visible;
	
	public MainMenuScreen(final LineDef gam, String genre, int minespeed) {
		
		nxt_t = new Texture(Gdx.files.internal("fwd_but.png"));
		prv_t = new Texture(Gdx.files.internal("bak_but.png"));
		
		GENRE=genre;
		
		MINESPEED=minespeed;
		
		are_instructions_visible=false;
		
		prefs = Gdx.app.getPreferences("galen_preferences");
		if (GENRE=="LINE"){
			score_one=prefs.getInteger("score_LINE_1");
			score_two=prefs.getInteger("score_LINE_2");
			score_three=prefs.getInteger("score_LINE_3");
			score_four=prefs.getInteger("score_LINE_4");
			
			one_t = new Texture(Gdx.files.internal("button_lines_ortho.png"));
			two_t = new Texture(Gdx.files.internal("button_lines_grad.png"));
			three_t = new Texture(Gdx.files.internal("button_lines_add.png"));
			four_t = new Texture(Gdx.files.internal("button_lines_general.png"));
		}
		
		if (GENRE=="CIRCLE"){
			score_one=prefs.getInteger("score_CIRCLE_5");
			score_two=prefs.getInteger("score_CIRCLE_6");
			score_three=prefs.getInteger("score_CIRCLE_7");
			score_four=prefs.getInteger("score_CIRCLE_8");
			
			one_t = new Texture(Gdx.files.internal("button_circles_ori.png"));
			two_t = new Texture(Gdx.files.internal("button_circles_arb.png"));
			three_t = new Texture(Gdx.files.internal("button_circles_dof.png"));
			four_t = new Texture(Gdx.files.internal("button_circles_all.png"));
		}
		
		but_instructions_r = new Rectangle();
		but_instructions_r.x=180;
		but_instructions_r.y=440;
		but_instructions_r.height=20;
		but_instructions_r.width=120;
		but_instructions_t = new Texture(Gdx.files.internal("button_instructions_smol.png"));
		
		instructions_r = new Rectangle();
		instructions_r.x=20;
		instructions_r.y=20;
		instructions_r.height=440;
		instructions_r.width=280;
		instructions_t = new Texture(Gdx.files.internal("Instructions.png"));
		
		cancel_instructions_r = new Rectangle();
		cancel_instructions_r.x=320-20-40;
		cancel_instructions_r.y=420;
		cancel_instructions_r.height=40;
		cancel_instructions_r.width=40;
		
		selector_r = new Rectangle();
		selector_r.x=10;
		selector_r.y=390;
		selector_r.height=80;
		selector_r.width=140;
		
		selector_prv_r = new Rectangle();
		selector_prv_r.x=selector_r.x;
		selector_prv_r.y=selector_r.y;
		selector_prv_r.height=40;
		selector_prv_r.width=40;
		
		selector_nxt_r = new Rectangle();
		selector_nxt_r.x=selector_r.x+100;
		selector_nxt_r.y=selector_r.y;
		selector_nxt_r.height=40;
		selector_nxt_r.width=40;
		
		selector_r = new Rectangle();
		selector_r.x=10;
		selector_r.y=390;
		selector_r.height=80;
		selector_r.width=140;
		
		selector_t = new Texture(Gdx.files.internal("selector_minespeed.png"));
		
		one_r = new Rectangle();
		one_r.x=10;
		one_r.y=480-180;
		one_r.height=60;
		one_r.width=140;
		
		
		two_r = new Rectangle();
		two_r.x=10;
		two_r.y=480-250;
		two_r.height=60;
		two_r.width=140;
		
		
		three_r = new Rectangle();
		three_r.x=10;
		three_r.y=480-320;
		three_r.height=60;
		three_r.width=140;
		
		
		four_r = new Rectangle();
		four_r.x=10;
		four_r.y=480-390;
		four_r.height=60;
		four_r.width=140;
		
		
		LIBRARY_r = new Rectangle();
		LIBRARY_r.x=90;
		LIBRARY_r.y=10;
		LIBRARY_r.height=60;
		LIBRARY_r.width=140;
		LIBRARY_t = new Texture(Gdx.files.internal("button_library.png"));
		
		game = gam;
		
		
		
		font = new BitmapFont();
		
		
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 320, 480);
		
		nxt_r = new Rectangle();
		nxt_r.x=260;
		nxt_r.y=20;
		nxt_r.height=40;
		nxt_r.width=40;
		nxt_t = new Texture(Gdx.files.internal("fwd_but.png"));
		
		prv_r = new Rectangle();
		prv_r.x=20;
		prv_r.y=20;
		prv_r.height=40;
		prv_r.width=40;
		prv_t = new Texture(Gdx.files.internal("bak_but.png"));

	}

	@Override
	public void render(float delta) {
		//Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		Gdx.graphics.setWindowedMode(320, 480);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		DecimalFormat df = new DecimalFormat("####");
		//font = new BitmapFont();
	    font.setColor(Color.BLACK);
		game.batch.draw(one_t, one_r.x, one_r.y);
		font.draw(game.batch, "SCORE:", one_r.x+180, one_r.y+35);
		font.draw(game.batch, df.format(score_one), one_r.x+250, one_r.y+35);
		
		game.batch.draw(two_t, two_r.x, two_r.y);
		font.draw(game.batch, "SCORE:", two_r.x+180, two_r.y+35);
		font.draw(game.batch, df.format(score_two), two_r.x+250, two_r.y+35);
		
		game.batch.draw(three_t, three_r.x, three_r.y);
		font.draw(game.batch, "SCORE:", three_r.x+180, three_r.y+35);
		font.draw(game.batch, df.format(score_three), three_r.x+250, three_r.y+35);
		
		game.batch.draw(four_t, four_r.x, four_r.y);
		font.draw(game.batch, "SCORE:", four_r.x+180, four_r.y+35);
		font.draw(game.batch, df.format(score_four), four_r.x+250, four_r.y+35);
		
		//game.batch.draw(LIBRARY_t, LIBRARY_r.x, LIBRARY_r.y);
		font.draw(game.batch, "GENRE:  " + GENRE, 170, 420);
		
		//font.draw(game.batch, "MINE SPEED:  " + "LOW", 10, 460);
		
		game.batch.draw(selector_t, selector_r.x, selector_r.y);
		game.batch.draw(prv_t, selector_prv_r.x, selector_prv_r.y);
		game.batch.draw(nxt_t, selector_nxt_r.x, selector_nxt_r.y);
		font.draw(game.batch, ""+MINESPEED, selector_r.x+60, selector_r.y+25);
		game.batch.draw(but_instructions_t, but_instructions_r.x, but_instructions_r.y);
		
		if (GENRE=="LINE"){
			game.batch.draw(nxt_t, nxt_r.x, nxt_r.y);
		}else{
			game.batch.draw(prv_t, prv_r.x, prv_r.y);
		}
		
		if (are_instructions_visible){
			game.batch.draw(instructions_t, instructions_r.x, instructions_r.y);
		}
		
		//
		
		game.batch.end();

		if (Gdx.input.justTouched()) {
			float tp_x=Gdx.input.getX();
			float tp_y=Gdx.input.getY();
			if (!are_instructions_visible){
				if (selector_prv_r.contains(tp_x, 480-tp_y) && MINESPEED>40){
					MINESPEED-=5;
				}
				if (selector_nxt_r.contains(tp_x, 480-tp_y) && MINESPEED<100){
					MINESPEED+=5;
				}
				
				
				if (GENRE=="LINE"){
					if (one_r.contains(tp_x,480-tp_y)){
			            game.setScreen(new GameScreen_2(game, MINESPEED, "LINE", 1));
			            dispose();
					}
					if (two_r.contains(tp_x,480-tp_y)){
			            game.setScreen(new GameScreen_2(game, MINESPEED, "LINE", 2));
			            dispose();
					}
					if (three_r.contains(tp_x,480-tp_y)){
			            game.setScreen(new GameScreen_2(game, MINESPEED, "LINE", 3));
			            dispose();
					}
					if (four_r.contains(tp_x,480-tp_y)){
			            game.setScreen(new GameScreen_2(game, MINESPEED, "LINE", 4));
			            dispose();
					}
					
					
					
					if (nxt_r.contains(tp_x,480-tp_y)){
			            game.setScreen(new MainMenuScreen(game, "CIRCLE", MINESPEED));
			            dispose();
					}
	
				}
				if (GENRE=="CIRCLE"){
					if (one_r.contains(tp_x,480-tp_y)){
			            game.setScreen(new GameScreen_2(game, MINESPEED, "CIRCLE", 5));
			            dispose();
					}
					if (two_r.contains(tp_x,480-tp_y)){
			            game.setScreen(new GameScreen_2(game, MINESPEED, "CIRCLE", 6));
			            dispose();
					}
					if (three_r.contains(tp_x,480-tp_y)){
			            game.setScreen(new GameScreen_2(game, MINESPEED, "CIRCLE", 7));
			            dispose();
					}
					if (four_r.contains(tp_x,480-tp_y)){
			            game.setScreen(new GameScreen_2(game, MINESPEED, "CIRCLE", 8));
			            dispose();
					}
					
					if (prv_r.contains(tp_x,480-tp_y)){
			            game.setScreen(new MainMenuScreen(game, "LINE", MINESPEED));
			            dispose();
					}
	
				}
				
			}
			
			if(((!instructions_r.contains(tp_x,480-tp_y))||(cancel_instructions_r.contains(tp_x,480-tp_y)))&&are_instructions_visible==true){
				System.out.println(tp_x);
				System.out.println(tp_y);
				are_instructions_visible=false;
			}
			else if (but_instructions_r.contains(tp_x,480-tp_y) && are_instructions_visible==false){
	            are_instructions_visible=true;
			}
			
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}