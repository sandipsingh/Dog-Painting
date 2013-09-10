package com.app.lighter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.entity.util.ScreenCapture;
import org.anddev.andengine.entity.util.ScreenCapture.IScreenCaptureCallback;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.anddev.andengine.entity.particle.emitter.IParticleEmitter;
import org.anddev.andengine.entity.particle.initializer.AlphaInitializer;
import org.anddev.andengine.entity.particle.initializer.ColorInitializer;
import org.anddev.andengine.entity.particle.initializer.VelocityInitializer;
import org.anddev.andengine.entity.particle.modifier.AlphaModifier;
import org.anddev.andengine.entity.particle.modifier.ColorModifier;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.particle.modifier.ScaleModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.FileBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;



import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.view.RenderSurfaceView;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.FileUtils;
import org.anddev.andengine.util.StreamUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LighterWithImage extends BaseGameActivity implements
		SensorEventListener {

	private SensorManager mSensorManager;
	public ScreenCapture screenCapture;
	private ShakeEventListener mSensorListener;
	public Scene scene;

	public int CAMERA_WIDTH = 480;
	public int CAMERA_HEIGHT = 800;           

	private static final float RATE_MIN = 20; // 10
	private static final float RATE_MAX = 20; // 20
	private static final int PARTICLES_MAX = 100; // 100
	private static final int SELECT_PICTURE = 1;

	String mPath = null;
	String myImage;
	Button bb;
	File directory, imageFile;
    protected FileBitmapTextureAtlasSource mFile;
    String picturePath;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mParticleTextureRegion;
	Texture flameTexture, lighterTexture, lighterOnBtnTexture1,
			lighterOnBtnTexture, lighterCapTexture, homeTexture, soundTexture,
			soundOffTexture, arrowTexture, shareTexture, uploadTexture;
	TextureRegion lighterTextureRegion, lighterOnBtnTextureRegion,
			lighterCapRegion, homeTextureRegion, soundTextureRegion,
			soundOffTextureRegion, arrowTextureRegion, shareTextureRegion,
			uploadTextureRegion;
	TiledTextureRegion flameTextureRegion;
	public static Sprite lighterSprite, lighterOnBtnSprite, lighterCapSprite,
			homeSprite, soundSprite, arrowSprite, soundOffSprite, shareSprite,
			uploadSprite;
	LinearLayout loadingPageLinearLayout;
	private SensorManager sensorManager;

	Sound lighter_on, lighter_cap_open1, lighter_cap_close, lidopenfull;
	boolean is_stop = false;
	int loadCounter = 3;
	private Camera mCamera;
	double pressedY, draggedY;
	private ParticleSystemFactory factory;

	private int lightercolor;
	public int idOpenCap = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		lightercolor = getIntent().getIntExtra("lighter color", 0);
		System.out.println("lighter color->\t" + lightercolor);
		loadingPageLinearLayout = (LinearLayout) findViewById(R.id.loadingPagelinearLayout);

		// setting onShakeListener here for the
		// lighter.................................

		final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensorListener = new ShakeEventListener();

		mSensorListener
				.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

					public void onShake() {
						// Toast.makeText(ParticleViewActivity.this, "Shake!",
						// Toast.LENGTH_SHORT).show();
						idOpenCap++;
						if ((idOpenCap % 2) == 0) {
							isLighterLidOpen = true;
							lighter_cap_open1.play();
							arrowSprite.setVisible(false);

						} else {
							isLighterLidClosed = true;
							lighter_cap_close.play();
						}
					}
				});

	}

	@Override
	protected void onStart() {
		super.onResume();
		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);

	}

	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(mSensorListener);
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume(); // for loading the activity
		loadCounter = 3;
		is_stop = true;

		// mSensorManager.unregisterListener(mSensorListener); //for
		// sensorManager
		// super.onStop();

	}

	public void onLoadComplete() {
		scene.registerUpdateHandler(new IUpdateHandler() {
			public void reset() {

			}

			public void onUpdate(float pSecondsElapsed) {
				if (is_stop == true) {
					loadCounter--;
				}
				if (loadCounter == 0) {
					loadCounter = 0;
					handlernew.sendEmptyMessage(0);
					scene.unregisterUpdateHandler(this);
				}

			}
		});
	}

	Handler handlernew = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {                
			case 0:
				loadingPageLinearLayout.setVisibility(View.GONE);
				break;
		
				
			}
		}
	};

	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera)
				.setNeedsSound(true);
		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		engineOptions.getRenderOptions().disableExtensionVertexBufferObjects();
		// return new LimitedFPSEngine(engineOptions, 60);
		return new Engine(engineOptions);

	}

	public void onLoadResources() {

		mBitmapTextureAtlas = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mParticleTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "particle/f4."
						+ "png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);

		
		
		
		
		
		
		
		
		// texture for uploaded image
		uploadTexture = new BitmapTextureAtlas(512, 512, // displaying the
				// lighter after
				// loading
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		uploadTextureRegion = upload(lightercolor);
		this.mEngine.getTextureManager().loadTexture(uploadTexture);

		// lighter texture
		lighterTexture = new BitmapTextureAtlas(512, 512, // displaying the
															// lighter after
															// loading
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		lighterTextureRegion = loadLighter(lightercolor);
		this.mEngine.getTextureManager().loadTexture(lighterTexture);

		// displaying the share button
		shareTexture = new BitmapTextureAtlas(128, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		shareTextureRegion = loadShare(lightercolor);
		this.mEngine.getTextureManager().loadTexture(shareTexture);

		// displaying home icon on screen

		homeTexture = new BitmapTextureAtlas(128, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		homeTextureRegion = loadHome(lightercolor);
		this.mEngine.getTextureManager().loadTexture(homeTexture);

		// displaying the sound icon on screen

		soundTexture = new BitmapTextureAtlas(128, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		soundTextureRegion = loadSound(lightercolor);
		this.mEngine.getTextureManager().loadTexture(soundTexture);

		// displaying the soundOff icon on screen

		soundOffTexture = new BitmapTextureAtlas(128, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		soundOffTextureRegion = loadSoundOff(lightercolor);
		this.mEngine.getTextureManager().loadTexture(soundOffTexture);

		// displaying the arrow
		arrowTexture = new BitmapTextureAtlas(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		arrowTextureRegion = loadArrow(lightercolor);
		this.mEngine.getTextureManager().loadTexture(arrowTexture);

		lighterOnBtnTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		lighterOnBtnTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset((BitmapTextureAtlas) this.lighterOnBtnTexture,
						this, "gfx/wheel.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(lighterOnBtnTexture);

		lighterOnBtnTexture1 = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.mEngine.getTextureManager().loadTexture(lighterOnBtnTexture1);

		lighterCapTexture = new BitmapTextureAtlas(512, 512, // displaying the
																// lighter cap
																// on loading
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		lighterCapRegion = loadLighterCap(lightercolor);
		this.mEngine.getTextureManager().loadTexture(lighterCapTexture);

		sensorManager = (SensorManager) this
				.getSystemService(this.SENSOR_SERVICE);
		Sensor accelerometer = sensorManager.getSensorList(
				Sensor.TYPE_ACCELEROMETER).get(0);
		sensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_FASTEST);

	}

	// loadSound method for sound icon
	private TextureRegion loadSoundOff(int lightercolor) {

		TextureRegion soundOffTextureRegion = null;
		switch (lightercolor) {

		case 8:
			soundOffTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundOffTexture,
							this, "gfx/soundOff.png", 0, 0);
			break;

		}
		return soundOffTextureRegion;
	}

	// uploadd method for uploaded image
	private TextureRegion upload(int lightercolor) {
		TextureRegion uploadTextureRegion = null;
		switch (lightercolor) {

		case 8:
			uploadTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.uploadTexture,
							this,  "gfx/share_icon.png", 0, 0);
			
			
			Toast.makeText(
					LighterWithImage.this,
					"Screenshot: ",
					Toast.LENGTH_SHORT)
					.show();
			
			break;

		}
		return uploadTextureRegion;
	}

	// loadArrow method for arrow
	private TextureRegion loadArrow(int lightercolor) {
		TextureRegion arrowTextureRegion = null;
		switch (lightercolor) {

		case 8:
			arrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.arrowTexture,
							this, "gfx/arrow.png", 0, 0);
			break;

		}
		return arrowTextureRegion;
	}

	// loadSound method for sound icon
	private TextureRegion loadSound(int lightercolor) {
		TextureRegion soundTextureRegion = null;
		switch (lightercolor) {

		case 8:
			soundTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundTexture,
							this, "gfx/mute.png", 0, 0);
			break;

		}
		return soundTextureRegion;
	}

	// loadShare method for share texture
	private TextureRegion loadShare(int lightercolor) {
		TextureRegion shareTextureRegion = null;
		switch (lightercolor) {

		case 8:
			shareTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.shareTexture,
							this, "gfx/share_icon.png", 0, 0);
			break;

		}
		return shareTextureRegion;
	}

	// loadhome method for home texture
	private TextureRegion loadHome(int lightercolor) {
		TextureRegion homeTextureRegion = null;
		switch (lightercolor) {
		case 8:
			homeTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.homeTexture,
							this, "gfx/home.png", 0, 0);
			break;

		}
		return homeTextureRegion;
	}

	private TextureRegion loadLighterCap(int lightercolor) {
		switch (lightercolor) {

		case 8:
			lighterCapRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.lighterCapTexture, this,
							"gfx/lightercapa_new.png", 0, 0);
			break;

		}
		return lighterCapRegion;
	}

	private TextureRegion loadLighter(int lightercolor) {
		TextureRegion lighterTextureRegion1 = null;
		switch (lightercolor) {

		case 8:
			lighterTextureRegion1 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.lighterTexture,
							this, "gfx/lightera_new.png", 0, 0);
			break;

		}
		return lighterTextureRegion1;

	}

	public Scene onLoadScene() {

		System.out.println("Sound is creationg begining ");
		try {
			System.out.println("Sound is creationg 1111111111");

			this.lighter_cap_open1 = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "sfx/lidopenfull.ogg");
			this.lighter_cap_close = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "sfx/lidopenfull.ogg");
			this.lighter_on = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "sfx/sparker.ogg");
			System.out.println("Sound is creationg 222222222222");
		} catch (Exception e) {
			System.out.println("exception is " + e);
		}

		this.mEngine.registerUpdateHandler(new FPSLogger());

		// sprite for share icon
		shareSprite = new Sprite((CAMERA_WIDTH / 4) + 95

		- (shareTextureRegion.getWidth() / 4) + 21, 75 // adjusting the
														// horizontal movment of
														// share icon

				- shareTextureRegion.getHeight(), shareTextureRegion) {

			// creating functioning for share icon
			public boolean onAreaTouched(
					org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {

				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					pressedX = pSceneTouchEvent.getX();

					final int viewWidth = LighterWithImage.this.mRenderSurfaceView
							.getWidth();
					final int viewHeight = LighterWithImage.this.mRenderSurfaceView
							.getHeight();

					// making a file for storing image
					File direct = new File(
							Environment.getExternalStorageDirectory()
									+ "/Virtual Ligher/images");
					// making a directoryif doesnt exists
					if (!direct.exists()) {
						if (direct.mkdir())
							; // directory is created;

					}

					// screenCapture method of andengine
					screenCapture.capture(viewWidth, viewHeight,
							direct.getAbsolutePath() + "/shot.png",
							new IScreenCaptureCallback() {

								public void onScreenCaptured(
										final String pFilePath) {
									LighterWithImage.this
											.runOnUiThread(new Runnable() {

												public void run() {
													Toast.makeText(
															LighterWithImage.this,
															"Screenshot: "
																	+ pFilePath
																	+ " taken!",
															Toast.LENGTH_SHORT)
															.show();

													// for sharing the file
													// after savin it
													// String entry=
													// "Hey, I found a good app which might be useful to you.";

													String entry = "Hey, I found a good app which might be useful to you.  Link: https:"
															+ "//"
															+ "play.google.com/store/apps/details?id=com.app.lighter";

													System.out
															.println("string is....."
																	+ entry);

													Intent share = new Intent(
															Intent.ACTION_SEND);
													share.setType("image/*");

													share.putExtra(
															android.content.Intent.EXTRA_SUBJECT,
															"Virtual Lighter");
													share.putExtra(
															android.content.Intent.EXTRA_TEXT,
															entry);
													share.putExtra(
															Intent.EXTRA_STREAM,
															Uri.parse("file://"
																	+ pFilePath));
													startActivity(Intent
															.createChooser(
																	share,
																	"Share via"));

												}
											});
								}

								public void onScreenCaptureFailed(
										final String pFilePath,
										final Exception pException) {
									LighterWithImage.this
											.runOnUiThread(new Runnable() {

												public void run() {

													Toast.makeText(
															LighterWithImage.this,
															"FAILED capturing Screenshot: "
																	+ pFilePath
																	+ " !",
															Toast.LENGTH_SHORT)
															.show();
												}
											});
								}
							});

				}
				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					releasedX = pSceneTouchEvent.getX();

					if ((releasedX - pressedX) < 0) {
						// takeShotAndShare();

					} else if ((releasedX - pressedX) > 0) {

					}
				}

				return true;

			};
		};

		// sprite for arrow icon
		arrowSprite = new Sprite((CAMERA_WIDTH / 2) - 70

		- (arrowTextureRegion.getWidth() / 2) + 130, 400 // adjusting the
															// horizontal
															// movment of
															// arroow

				- arrowTextureRegion.getHeight(), arrowTextureRegion);
		// sprite for lighter
		lighterSprite = new Sprite((CAMERA_WIDTH / 2) + 2 // adjusting the
															// horizontal
															// position of
															// lighter body
				- (lighterTextureRegion.getWidth() / 2), CAMERA_HEIGHT // adjusting
																		// the
																		// horizontal
																		// movement
																		// of
																		// lighter
																		// body
				- lighterTextureRegion.getHeight(), lighterTextureRegion);

		lighterOnBtnSprite = new Sprite((CAMERA_WIDTH / 2)
				+ (lighterOnBtnTextureRegion.getWidth() / 4) + 18, // adjusting
																	// the
																	// horizontal
																	// orientation
																	// of wheel
				lighterSprite.getY() + 19, lighterOnBtnTextureRegion) { // adjusting
																		// the
																		// vertical
																		// orientation
																		// of
																		// wheel

			public boolean onAreaTouched(
					org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					lighterOnBtnSprite.setRotationCenterX(lighterOnBtnSprite
							.getWidth() / 2);
					System.out.println("ACTION_DOWN");
					pressedY = pSceneTouchEvent.getY();
				}
				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					System.out.println("ACTION_UP");
				}

				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_MOVE) { // on
																				// movement
																				// of
																				// wheel
																				// touch
					draggedY = pSceneTouchEvent.getY();
					if (draggedY - pressedY > 25) {
						lighterBtnAngle = lighterBtnAngle + 4;
						lighterOnBtnSprite.setRotationCenter(
								lighterOnBtnSprite.getWidth() / 2, // making the
																	// wheel
																	// spin
																	// along its
																	// center
																	// only
								lighterOnBtnSprite.getHeight() / 2); // making
																		// the
																		// wheel
																		// spin
																		// along
																		// its
																		// center
																		// only
						lighterOnBtnSprite.setRotation(lighterBtnAngle);
						lighter_on.play();
						particleSystem.setVisible(true);
					}
				}
				System.out.println("ACTION2222222222222222");
				return true;
			};
		};

		
		
		// sprite for image to be uploaded
		uploadSprite = new Sprite((CAMERA_WIDTH /2) + 2
				- (uploadTextureRegion.getWidth() ), CAMERA_HEIGHT

		- uploadTextureRegion.getHeight()-150, uploadTextureRegion) { 
			

			public boolean onAreaTouched(
					org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				System.out.println("lighterCapSprite  sdgfgsfg");
										   		

				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					//uploadSprite.setVisible(false); // making the arrow invisible on
                     
					final int viewWidth = LighterWithImage.this.mRenderSurfaceView
							.getWidth();
					final int viewHeight = LighterWithImage.this.mRenderSurfaceView
							.getHeight();

					// making a file for storing image
					File direct = new File(
							Environment.getExternalStorageDirectory()
									+ "/Virtual Ligher/pankaj");
					// making a directoryif doesnt exists
					if (!direct.exists()) {
						if (direct.mkdir())
							; // directory is created;

					}
					
					  Intent i = new Intent(
							     Intent.ACTION_PICK,
							     android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

							   startActivityForResult(i, 2);
					
				}
				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					
					if ((releasedX - pressedX) < 0) {
						
					} else if ((releasedX - pressedX) > 0) {
					

					}
				}

				return true;
			}

				 
				   		
			
		};

		lighterCapSprite = new Sprite(CAMERA_WIDTH / 2
				- (lighterCapRegion.getWidth() / 2) + 2, lighterSprite.getY() // adjusting
																				// the
																				// horizontal
																				// orientation
				// of
				// cap
				- (lighterCapRegion.getHeight() / 2) + 24, lighterCapRegion) { // adjusting
																				// the
																				// vertical
																				// orientation
																				// of
																				// cap

			public boolean onAreaTouched(
					org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				System.out.println("lighterCapSprite  sdgfgsfg");
				arrowSprite.setVisible(false); // making the arrow invisible on
												// swipe

				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					pressedX = pSceneTouchEvent.getX();
					System.out.println("lighterCapSprite  ACTION_DOWN");
				}
				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					System.out.println("lighterCapSprite  ACTION_UP");
					releasedX = pSceneTouchEvent.getX();

					System.out.println("( releasedX - pressedX)--->\t"
							+ (releasedX - pressedX));
					if ((releasedX - pressedX) < 0) {
						isLighterLidOpen = true;
						lighter_cap_open1.play();

					} else if ((releasedX - pressedX) > 0) {
						isLighterLidClosed = true;
						lighter_cap_close.play();

					}
				}

				return true;
			};
		};

		// adjusting the vertical movment of home icon
		homeSprite = new Sprite((CAMERA_WIDTH / 4) - 295

		- (homeTextureRegion.getWidth() / 4) + 210, 75 // adjusting the
														// horizontal movment of
														// home icon

				- homeTextureRegion.getHeight(), homeTextureRegion) {

			// creating functioning for home icon
			public boolean onAreaTouched(
					org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				finish();

				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					pressedX = pSceneTouchEvent.getX();

				}
				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					releasedX = pSceneTouchEvent.getX();

					if ((releasedX - pressedX) < 0) {
						isLighterLidOpen = true;
					} else if ((releasedX - pressedX) > 0) {
						isLighterLidClosed = true;
					}
				}

				return true;
			};
		};

		// sprite for sound icon
		soundSprite = new Sprite((CAMERA_WIDTH / 4) - 295 // adjusting the
				// vertical
				// position of
				// home icon body
				- (soundTextureRegion.getWidth() / 4) + 590, 75 // adjusting
				// the
				// horizontal
				// movement
				// of
				// home
				// icon
				- soundTextureRegion.getHeight(), soundTextureRegion) {

			// creating functioning for sound icon
			public boolean onAreaTouched(
					org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					pressedX = pSceneTouchEvent.getX();

				}
				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					if (soundSprite.isVisible()) {
						soundSprite.setVisible(false);
						soundOffSprite.setVisible(true);
						lighter_on.setVolume(0);
						lighter_cap_close.setVolume(0);
						lighter_cap_open1.setVolume(0);           
					} else {
						soundOffSprite.setVisible(false);
						soundSprite.setVisible(true);

						lighter_on.setVolume(1);
						lighter_cap_close.setVolume(1);
						lighter_cap_open1.setVolume(1);
					}

				}

				return true;
			};

		};

		// sprite for sound off icon
		soundOffSprite = new Sprite((CAMERA_WIDTH / 4) - 295 // adjusting the
				// vertical
				// position of
				// home icon body
				- (soundOffTextureRegion.getWidth() / 4) + 590, 75 // adjusting
				// the
				// horizontal
				// movement
				// of
				// home
				// icon
				- soundOffTextureRegion.getHeight(), soundOffTextureRegion) {

		};

		final Entity rectangleGroup = new Entity(CAMERA_WIDTH / 40 - 20,
				CAMERA_HEIGHT / 40 - 20);
		/*
		 * rectangleGroup.registerEntityModifier(new LoopEntityModifier(new
		 * ParallelEntityModifier( new SequenceEntityModifier( new
		 * ScaleModifier(10, 1, 0.5f), new ScaleModifier(10, 0.5f, 1) ), new
		 * RotationModifier(20, 0, 360)) ));
		 */

		particleSystem = build(mEngine);
		rectangleGroup.attachChild(particleSystem);
		particleSystem.setVisible(false);
		rectangleGroup.attachChild(shareSprite);
		rectangleGroup.attachChild(lighterOnBtnSprite);

		rectangleGroup.attachChild(lighterSprite);
		rectangleGroup.attachChild(homeSprite);
		rectangleGroup.attachChild(soundSprite);
		rectangleGroup.attachChild(soundOffSprite);
		rectangleGroup.attachChild(uploadSprite);

		soundOffSprite.setVisible(false);

		rectangleGroup.attachChild(lighterCapSprite);
		rectangleGroup.attachChild(arrowSprite);

		// scene.setBackground(new ColorBackground(0f, 0f, 0f));

		scene = new Scene();
		screenCapture = new ScreenCapture();

		scene.setBackground(new ColorBackground(0f, 0f, 0f));

		/*
		 * Initially make particle system invisible
		 * 
		 * particleSystem = build(mEngine); scene.attachChild(particleSystem);
		 * particleSystem.setVisible(false);
		 * 
		 * // scene.detachChild(particleSystem);
		 * scene.attachChild(lighterOnBtnSprite);
		 * 
		 * // scene. scene.attachChild(shareSprite);
		 * scene.attachChild(lighterSprite); scene.attachChild(homeSprite);
		 * scene.attachChild(soundSprite); scene.attachChild(soundOffSprite);
		 * soundOffSprite.setVisible(false);
		 * 
		 * scene.attachChild(lighterCapSprite); scene.attachChild(arrowSprite);
		 */
		scene.setTouchAreaBindingEnabled(true);

		scene.attachChild(rectangleGroup);
		scene.attachChild(screenCapture);

		// scene.registerTouchArea(lighterOnBtnSprite);
		scene.registerTouchArea(lighterCapSprite);
		scene.registerTouchArea(homeSprite);
		scene.registerTouchArea(soundSprite);
		scene.registerTouchArea(shareSprite);
		scene.registerTouchArea(uploadSprite);

		// scene.registerTouchArea(soundOffSprite);

		scene.registerUpdateHandler(new org.anddev.andengine.engine.handler.IUpdateHandler() {

			public void reset() {
				// TODO Auto-generated method stub

			}

			public void onUpdate(float pSecondsElapsed) {

				System.out.println("onUpdate ---------------onUpdate-");
				rotateLighterCap();

			}
		});
		return scene;

	}


	
	// onActivity result for going to gallery
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);

	  if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
	   Uri selectedImage = data.getData();
	   String[] filePathColumn = { MediaStore.Images.Media.DATA };

	   Cursor cursor = getContentResolver().query(selectedImage,
	     filePathColumn, null, null, null);
	   cursor.moveToFirst();

	   int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	     picturePath = cursor.getString(columnIndex);
	    
	    Toast.makeText(LighterWithImage.this, picturePath,
				 Toast.LENGTH_SHORT).show();
	   cursor.close();

	  // File f = new File(picturePath + File.separator + "image.jpg");
	                                                                                                                            
	   File imageFile = new File(picturePath);
	   BitmapTextureAtlas texture = new BitmapTextureAtlas(128 , 128 , TextureOptions.DEFAULT);
      FileBitmapTextureAtlasSource fileTextureSource = new FileBitmapTextureAtlasSource(imageFile);
  	  uploadTextureRegion = TextureRegionFactory.createFromSource(texture,fileTextureSource ,0,0, true);
  	   
  	                System.out.println("texture is...."+texture);          
  	                              
  	   /*
 		TextureRegion uploadTextureRegion = null;
 	
 			uploadTextureRegion = BitmapTextureAtlasTextureRegionFactory
 					.createFromAsset((BitmapTextureAtlas) this.uploadTexture,
 							this,  "gfx/share_icon.png", 0, 0);
 			*/

 	

  	   
  	   
	//  mFile=FileBitmapTextureAtlasSource.c
	  }
	 }

	double pressedX, releasedX;
	boolean isLighterLidOpen = false, isLighterLidClosed = false;

	public ParticleSystemFactory getFactoryByIndex(int index) {
		return FightParticle.PARTICLE_SYSTEMS[index];
	}

	ParticleSystem particleSystem;
	VelocityInitializer velocityInitializer;

	public ParticleSystem build(Engine engine)// engine.getCamera().getHeight()/2//engine.getCamera().getWidth()/2-lighterSprite.getWidth()/
	{
		IParticleEmitter emitter = new CircleParticleEmitter(
				lighterSprite.getX() + lighterSprite.getWidth() / 2 - 25,
				lighterSprite.getY() - 20, 10);

		particleSystem = new ParticleSystem(emitter, RATE_MIN, RATE_MAX,
				PARTICLES_MAX, this.mParticleTextureRegion);

		particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

		// particleSystem.addParticleInitializer(new VelocityInitializer(35, 45,
		// 0, -10));
		// particleSystem.addParticleInitializer(new AccelerationInitializer(5,
		// -11));
		// particleSystem.addParticleInitializer(new RotationInitializer(0.0f,
		// 360.0f));
		// particleSystem.addParticleInitializer(new ColorInitializer(1.0f,
		// 1.0f, 0.0f));
		//
		// particleSystem.addParticleModifier(new ScaleModifier(0.5f, 2.0f, 0,
		// 5));
		// particleSystem.addParticleModifier(new ExpireModifier(6.5f));
		// particleSystem.addParticleModifier(new ColorModifier(1.0f, 1.0f,
		// 1.0f, 1.0f, 0.0f, 1.0f, 2.5f, 5.5f));
		// particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f,
		// 2.5f, 6.5f));
		//

		// //
		// particleSystem.addParticleInitializer(new VelocityInitializer(-35,
		// -45, 0, 10));
		// particleSystem.addParticleInitializer(new AccelerationInitializer(-5,
		// -11));
		// particleSystem.addParticleInitializer(new RotationInitializer(0.0f,
		// 360.0f));
		// particleSystem.addParticleInitializer(new ColorInitializer(0.0f,
		// 1.0f, 0.0f));
		//
		// particleSystem.addParticleModifier(new ScaleModifier(0.5f, 2.0f, 0,
		// 5));
		// particleSystem.addParticleModifier(new ExpireModifier(6.5f));
		// particleSystem.addParticleModifier(new ColorModifier(0.0f, 1.0f,
		// 1.0f, 1.0f, 0.0f, 1.0f, 2.5f, 5.5f));
		// particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f,
		// 2.5f, 6.5f));

		//
		velocityInitializer = new VelocityInitializer(-30, 30, -120, -100);

		// velocityInitializer.setVelocity(-30,30, -120, -80 );
		// particleSystem.addParticleInitializer(new ColorInitializer(1, 0, 0));
		// particleSystem.addParticleInitializer(new AlphaInitializer(0));
		// // particleSystem.addParticleInitializer(velocityInitializer);
		// particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		// particleSystem.addParticleInitializer(new VelocityInitializer(-5, 5,
		// -80, -60));
		// particleSystem.addParticleInitializer(new GravityInitializer());
		// particleSystem.addParticleModifier(new ScaleModifier(2f, 1.7f, 0f,
		// 2f));
		// particleSystem.addParticleModifier(new ColorModifier(1, 1, 0, 0.5f,
		// 0, 0, 0, 3));
		// particleSystem.addParticleModifier(new ColorModifier(1, 1, 0.5f, 1,
		// 0, 1, 4, 6));

		//
		// particleSystem.addParticleModifier(new ColorModifier(0,0,0, 1, 2, 3,
		// 4, 6));
		// particleSystem.addParticleModifier(new AlphaModifier(0, 1, 0, 0.5f));
		// particleSystem.addParticleModifier(new AlphaModifier(1, 0, 2.5f,
		// 3.5f));
		// particleSystem.addParticleModifier(new ExpireModifier(6, 6f));

		//
		// particleSystem.addParticleInitializer(new ColorInitializer(1, 0, 0));
		// particleSystem.addParticleInitializer(new AlphaInitializer(0));
		// particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		// // velocityInitializer.setVelocity(-30,30, -120, -80 );
		// particleSystem.addParticleInitializer(new GravityInitializer());
		// particleSystem.addParticleModifier(new ColorModifier(1, 1, 0, 0.5f,
		// 0, 0, 0, 3));
		// particleSystem.addParticleModifier(new ColorModifier(1, 1, 0.5f, 1,
		// 0, 1, 4, 6));
		// particleSystem.addParticleModifier(new AlphaModifier(0, 1, 0, 0.5f));
		// particleSystem.addParticleModifier(new AlphaModifier(1, 0, 2.5f,
		// 3.5f));
		// particleSystem.addParticleModifier(new ExpireModifier(6, 6f));

		velocityInitializer.setVelocity(-30, 30, -120, -80);
		particleSystem.addParticleInitializer(velocityInitializer);
		particleSystem
				.addParticleInitializer(new ColorInitializer(1f, 1f, 0.8f));
		particleSystem.addParticleInitializer(new AlphaInitializer(0.7f));
		particleSystem.addParticleModifier(new ScaleModifier(2f, 1.7f, 0f, 2f));
		particleSystem
				.addParticleModifier(new AlphaModifier(0.7f, 0.2f, 1f, 2f));
		particleSystem.addParticleModifier(new ExpireModifier(2f));
		particleSystem.addParticleModifier(new ColorModifier(1f, 0.98f, 1f,
				0.5f, 0.8f, 0.2f, 0f, 0.5f));

		/*
		 * original
		 */

		// particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		//
		// particleSystem.addParticleInitializer(new VelocityInitializer(-75,
		// 75, -80, -60));
		// particleSystem.addParticleInitializer(new ColorInitializer(1f, 1f,
		// 0.8f));
		// particleSystem.addParticleInitializer(new AlphaInitializer(0.7f));
		//
		// particleSystem.addParticleModifier(new ScaleModifier(2f, 1.7f, 0f,
		// 2f));
		// particleSystem.addParticleModifier(new AlphaModifier(0.7f, 0.2f, 1f,
		// 2f));
		// particleSystem.addParticleModifier(new ExpireModifier(2f));
		// particleSystem.addParticleModifier(new ColorModifier(1f, 0.98f, 1f,
		// 0.5f, 0.8f, 0.2f, 0f, 0.5f));

		return particleSystem;
	}

	float angle = 0, chaneInAngle = 5, angleOfLighterCap = 0,
			lighterBtnAngle = 0;
	boolean canRightRotation = false;

	private void rotateLighterCap() {
		if (isLighterLidOpen) {
			lighterCapSprite.setRotationCenter(5, lighterCapSprite.getHeight());
			lighterCapSprite.setRotation(angleOfLighterCap);

			angleOfLighterCap = angleOfLighterCap - chaneInAngle;
			if (angleOfLighterCap <= -145) {
				angleOfLighterCap = -145;// ///////////////////////////////////////////////////angle
											// liter
				isLighterLidOpen = false;
				canRightRotation = true;
				scene.registerTouchArea(lighterOnBtnSprite);

				// lighter_cap_open.play();
			}
		} else if (isLighterLidClosed && canRightRotation
				&& isLighterLidOpen == false) {
			lighterCapSprite.setRotationCenter(5, lighterCapSprite.getHeight());
			lighterCapSprite.setRotation(angleOfLighterCap);

			angleOfLighterCap = angleOfLighterCap + chaneInAngle;
			if (angleOfLighterCap >= 5) {
				angleOfLighterCap = 0;
				isLighterLidClosed = false;
				canRightRotation = false;
				scene.unregisterTouchArea(lighterOnBtnSprite);
				particleSystem.setVisible(false);
				// lighter_cap_close.play();

			}

		}

	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	private float flameRotation;

	void updateFlameAnglePosition(float changeInX) {
		if (changeInX == 0 || changeInX == -0) // no tilt here..........
		{

			velocityInitializer.setVelocity(-30, 30, -120, -80);
		} else if (changeInX > 1) {

			if (changeInX > 1 && changeInX < 2) {
				velocityInitializer.setVelocityX(80, 60); // to make flame go
															// right
			} else if (changeInX > 2 && changeInX < 3) {
				velocityInitializer.setVelocityX(90, 80);
			} else if (changeInX > 3) {
				velocityInitializer.setVelocityX(200, 180);
			}

		}

		else if (changeInX <= -1) {

			if (changeInX < -1 && changeInX > -2) {
				velocityInitializer.setVelocityX(-80, -60); // to make flame go
															// left...........
			} else if (changeInX < -2 && changeInX > -3) {
				velocityInitializer.setVelocityX(-90, -80);
			} else if (changeInX < -3) {
				velocityInitializer.setVelocityX(-200, -180);
			}
		}
	}

	private float changeInX;

	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			switch (event.sensor.getType()) {

			case Sensor.TYPE_ACCELEROMETER:
				changeInX = event.values[0];
				System.out.println("changeInX---------->\t" + changeInX);
				updateFlameAnglePosition(changeInX);
				break;

			}
		}

	}

	LinearLayout lifeLinearLayout;
	Display display1;

	protected void onSetContentView() {
		// TODO Auto-generated method stub

		Display display = this.getWindowManager().getDefaultDisplay();
		final RelativeLayout relativeLayout = new RelativeLayout(this);

		final FrameLayout.LayoutParams relativeLayoutLayoutParams = new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.mRenderSurfaceView = new RenderSurfaceView(this);
		this.mRenderSurfaceView.setRenderer(this.mEngine);
		final LayoutParams surfaceViewLayoutParams = new RelativeLayout.LayoutParams(
				super.createSurfaceViewLayoutParams());
		((android.widget.RelativeLayout.LayoutParams) surfaceViewLayoutParams)
				.addRule(RelativeLayout.CENTER_IN_PARENT);
		// ADD MY NEW VIEW ABOVE
		LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View vv = vi.inflate(R.layout.play_game, null);
		// THIS IS MY CUSTOM VIEW
		vv.setDrawingCacheEnabled(true); // /////////////////////////////////////////////////////////////////////////////////

		relativeLayout
				.addView(this.mRenderSurfaceView, surfaceViewLayoutParams); // ANDENGINE
																			// VIEW
		relativeLayout.addView(vv, createAdViewLayoutParams()); // MYVIEW
		this.setContentView(relativeLayout, relativeLayoutLayoutParams);

	}

	private LayoutParams createAdViewLayoutParams() {
		final LayoutParams adViewLayoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		return adViewLayoutParams;
	}

}
