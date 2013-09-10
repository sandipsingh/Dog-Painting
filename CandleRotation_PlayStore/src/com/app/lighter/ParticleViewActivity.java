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
import org.anddev.andengine.entity.particle.emitter.PointParticleEmitter;
import org.anddev.andengine.entity.particle.initializer.AccelerationInitializer;
import org.anddev.andengine.entity.particle.initializer.AlphaInitializer;
import org.anddev.andengine.entity.particle.initializer.ColorInitializer;
import org.anddev.andengine.entity.particle.initializer.RotationInitializer;
import org.anddev.andengine.entity.particle.initializer.VelocityInitializer;
import org.anddev.andengine.entity.particle.modifier.AlphaModifier;
import org.anddev.andengine.entity.particle.modifier.ColorModifier;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.particle.modifier.ScaleModifier;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.view.RenderSurfaceView;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.FileUtils;
import org.anddev.andengine.util.StreamUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ParticleViewActivity extends BaseGameActivity implements
		SensorEventListener {

	private SensorManager mSensorManager;
	public ScreenCapture screenCapture;
	private ShakeEventListener mSensorListener;
	public Scene scene;
	int value_for_clr = 0;
	public int CAMERA_WIDTH = 480;
	public int CAMERA_HEIGHT = 800;
	ParticleSystem particleSystem, particleSystem_red, particleSystem_blue,
			particleSystem_green;
	private static final float RATE_MIN = 20; // 10
	private static final float RATE_MAX = 20; // 20
	private static final int PARTICLES_MAX = 100; // 100
	String particle_name = "particle/f4_old.png";
	String mPath = null;
	String myImage;
	Button bb;
	File directory, imageFile;

	public TiledTextureRegion characterTiledTextureRegion;
	AnimatedSprite CharacterSprite;
	Rectangle characterRectangle;

	private BitmapTextureAtlas mBitmapTextureAtlas, mBitmapTextureAtlas_red,
			mBitmapTextureAtlas_blue, mBitmapTextureAtlas_green;
	private TextureRegion mParticleTextureRegion, mParticleTextureRegion_red,
			mParticleTextureRegion_blue, mParticleTextureRegion_green;
	Texture flameTexture, lighterTexture, lighterOnBtnTexture1,
			lighterOnBtnTexture, lighterCapTexture, homeTexture, soundTexture,
			soundOffTexture, arrowTexture, shareTexture, sparkTexture,
			FlameColorTexture;
	BitmapTextureAtlas characterTexture;
	TextureRegion lighterTextureRegion, lighterOnBtnTextureRegion,
			lighterCapRegion, homeTextureRegion, soundTextureRegion,
			soundOffTextureRegion, arrowTextureRegion, shareTextureRegion,
			sparkTextureRegion, FlameColorTextureRegion;
	TiledTextureRegion flameTextureRegion;
	public static Sprite lighterSprite, lighterOnBtnSprite, lighterCapSprite,
			homeSprite, soundSprite, arrowSprite, soundOffSprite, shareSprite,
			sparkSprite, FlameColorSprite;
	LinearLayout loadingPageLinearLayout;
	private SensorManager sensorManager;

	Sound lighter_on, lighter_cap_open1, lighter_cap_close, lidopenfull;
	boolean is_stop = false;
	int loadCounter = 3;
	private Camera mCamera;
	double pressedY, draggedY;
	private ParticleSystemFactory factory;
	SharedPreferences myPrefs;
	static File f;
	private int lightercolor;
	public int idOpenCap = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		lightercolor = getIntent().getIntExtra("lighter color", 0);
		System.out.println("lighter color->\t" + lightercolor);
		loadingPageLinearLayout = (LinearLayout) findViewById(R.id.loadingPagelinearLayout);

		// making a shredprrefrence file to check the conition for adds
		myPrefs = this.getSharedPreferences("hello", MODE_WORLD_READABLE);
		f = new File("/data/data/com.app.lighter/shared_prefs/hello.xml");

		if (!f.exists()) {

			SharedPreferences.Editor prefsEditor = myPrefs.edit();
			prefsEditor.putString("level1", "particle/f4_old.png");
			System.out.println("file created..");
			prefsEditor.commit();
		}

		// setting onShakeListener here for the
		// lighter.................................

		// calling async task
		 new CommonSoapTask().execute("url");

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

	public class CommonSoapTask extends AsyncTask<String, Void, Void> {

		protected Void doInBackground(String... params) {

			try {

				for (int i = 0; true; i++) {
					System.out.println("playing infinitely...");

					isBlowing();
				}
				

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;

		}

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

			case 1:
				CharacterSprite.stopAnimation();
				sparkSprite.setVisible(false);
				System.out.println("in handler for stop animation...");

				break;

			case 2:

				/*for (int i = 0; true; i++) {
					System.out.println("playing infinitely...");

					isBlowing();*/

					// finish();

				//}
			}
		};
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

	protected boolean isBlowing() {

		boolean recorder = true;
		int blow_value;

		int minSize = AudioRecord.getMinBufferSize(8000,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		AudioRecord ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, minSize);

		short[] buffer = new short[minSize];

		ar.startRecording();
		while (recorder) {

			ar.read(buffer, 0, minSize);
			for (short s : buffer) {
				if (Math.abs(s) > 12500 && Math.abs(s) < 13000) // DETECT VOLUME
							  					  				// (IF I BLOW IN
						  								  		// THE MIC)
				{       
					blow_value = Math.abs(s);
					System.out.println("Blow Value=  " + blow_value);

					particleSystem.setVisible(false);
					ar.stop();
					ar.release();
					recorder = false;

					return true;

				}

			}
		}
		return false;

	}

	public void onLoadResources() {

		// texture for particle..
		mBitmapTextureAtlas = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mParticleTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this,
						"particle/f4.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);

		// texture for red particle..
		mBitmapTextureAtlas_red = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mParticleTextureRegion_red = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas_red, this,
						"particle/f4.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(
				this.mBitmapTextureAtlas_red);

		// texture for blue particle..
		mBitmapTextureAtlas_blue = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mParticleTextureRegion_blue = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas_blue, this,
						"particle/f4_blue.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(
				this.mBitmapTextureAtlas_blue);

		// texture for green particle..
		mBitmapTextureAtlas_green = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mParticleTextureRegion_green = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas_green, this,
						"particle/f4_green.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(
				this.mBitmapTextureAtlas_green);

		// texture for lighter..
		lighterTexture = new BitmapTextureAtlas(512, 512, // displaying the
															// lighter after
															// loading
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		lighterTextureRegion = loadLighter(lightercolor);
		this.mEngine.getTextureManager().loadTexture(lighterTexture);

		// texturefor character
		characterTexture = new BitmapTextureAtlas(512, 128,
				TextureOptions.BILINEAR);
		characterTiledTextureRegion = loadCharacter(lightercolor);
		this.mEngine.getTextureManager().loadTexture(characterTexture);

		// displaying the share button
		shareTexture = new BitmapTextureAtlas(128, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		shareTextureRegion = loadShare(lightercolor);
		this.mEngine.getTextureManager().loadTexture(shareTexture);

		// displaying the Flame color button
		FlameColorTexture = new BitmapTextureAtlas(128, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		FlameColorTextureRegion = loadFlameColor(lightercolor);
		this.mEngine.getTextureManager().loadTexture(FlameColorTexture);

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

		// displaying the spark icon
		sparkTexture = new BitmapTextureAtlas(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		sparkTextureRegion = loadSpark(lightercolor);
		this.mEngine.getTextureManager().loadTexture(sparkTexture);

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
				SensorManager.SENSOR_DELAY_FASTEST); // fastest tha

	}

	// loadCharacter method for characteer
	private TiledTextureRegion loadCharacter(int lightercolor) {

		TiledTextureRegion characterTiledTextureRegion = null;
		switch (lightercolor) {

		case 0:
			characterTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(characterTexture, this,
							"character.png", 0, 0, 5, 1);
			break;
		case 1:
			characterTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(characterTexture, this,
							"character.png", 0, 0, 5, 1);
			break;
		case 2:
			characterTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(characterTexture, this,
							"character.png", 0, 0, 5, 1);
			break;
		case 3:
			characterTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(characterTexture, this,
							"character.png", 0, 0, 5, 1);
			break;
		case 5:
			characterTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(characterTexture, this,
							"character.png", 0, 0, 5, 1);
			break;
		case 6:
			characterTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(characterTexture, this,
							"character.png", 0, 0, 5, 1);
			break;
		case 7:
			characterTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(characterTexture, this,
							"character.png", 0, 0, 5, 1);
			break;
		}
		return characterTiledTextureRegion;
	}

	// loadMore method for more apps icon
	private TextureRegion loadFlameColor(int lightercolor) {

		TextureRegion FlameColorTextureRegion = null;
		switch (lightercolor) {

		case 0:
			FlameColorTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.FlameColorTexture, this,
							"gfx/flame_change.png", 0, 0);
			break;
		case 1:
			FlameColorTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.FlameColorTexture, this,
							"gfx/flame_change.png", 0, 0);
			break;
		case 2:
			FlameColorTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.FlameColorTexture, this,
							"gfx/flame_change.png", 0, 0);
			break;
		case 3:
			FlameColorTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.FlameColorTexture, this,
							"gfx/flame_change.png", 0, 0);
			break;
		case 5:
			FlameColorTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.FlameColorTexture, this,
							"gfx/flame_change.png", 0, 0);
			break;
		case 6:
			FlameColorTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.FlameColorTexture, this,
							"gfx/flame_change.png", 0, 0);
			break;
		case 7:
			FlameColorTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.FlameColorTexture, this,
							"gfx/flame_change.png", 0, 0);
			break;
		}
		return FlameColorTextureRegion;
	}

	// loadMore method for more apps icon
	private TextureRegion loadSpark(int lightercolor) {

		TextureRegion sparkTextureRegion = null;
		switch (lightercolor) {

		case 0:
			sparkTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.sparkTexture,
							this, "gfx/fire1.png", 0, 0);
			break;
		case 1:
			sparkTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.sparkTexture,
							this, "gfx/fire1.png", 0, 0);
			break;
		case 2:
			sparkTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.sparkTexture,
							this, "gfx/fire1.png", 0, 0);
			break;
		case 3:
			sparkTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.sparkTexture,
							this, "gfx/fire1.png", 0, 0);
			break;
		case 5:
			sparkTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.sparkTexture,
							this, "gfx/fire1.png", 0, 0);
			break;
		case 6:
			sparkTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.sparkTexture,
							this, "gfx/fire1.png", 0, 0);
			break;
		case 7:
			sparkTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.sparkTexture,
							this, "gfx/fire1.png", 0, 0);
			break;
		}
		return sparkTextureRegion;
	}

	// loadSound method for sound icon
	private TextureRegion loadSoundOff(int lightercolor) {

		TextureRegion soundOffTextureRegion = null;
		switch (lightercolor) {

		case 0:
			soundOffTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundOffTexture,
							this, "gfx/soundOff.png", 0, 0);
			break;
		case 1:
			soundOffTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundOffTexture,
							this, "gfx/soundOff.png", 0, 0);
			break;
		case 2:
			soundOffTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundOffTexture,
							this, "gfx/soundOff.png", 0, 0);
			break;
		case 3:
			soundOffTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundOffTexture,
							this, "gfx/soundOff.png", 0, 0);
			break;
		case 5:
			soundOffTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundOffTexture,
							this, "gfx/soundOff.png", 0, 0);
			break;
		case 6:
			soundOffTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundOffTexture,
							this, "gfx/soundOff.png", 0, 0);
			break;
		case 7:
			soundOffTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundOffTexture,
							this, "gfx/soundOff.png", 0, 0);
			break;
		}
		return soundOffTextureRegion;
	}

	// loadArrow method for arrow
	private TextureRegion loadArrow(int lightercolor) {
		TextureRegion arrowTextureRegion = null;
		switch (lightercolor) {

		case 0:
			arrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.arrowTexture,
							this, "gfx/arrow.png", 0, 0);
			break;
		case 1:
			arrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.arrowTexture,
							this, "gfx/arrow.png", 0, 0);
			break;
		case 2:
			arrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.arrowTexture,
							this, "gfx/arrow.png", 0, 0);
			break;
		case 3:
			arrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.arrowTexture,
							this, "gfx/arrow.png", 0, 0);
			break;
		case 5:
			arrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.arrowTexture,
							this, "gfx/arrow.png", 0, 0);
			break;
		case 6:
			arrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.arrowTexture,
							this, "gfx/arrow.png", 0, 0);
			break;
		case 7:
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

		case 0:
			soundTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundTexture,
							this, "gfx/mute.png", 0, 0);
			break;
		case 1:
			soundTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundTexture,
							this, "gfx/mute.png", 0, 0);
			break;
		case 2:
			soundTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundTexture,
							this, "gfx/mute.png", 0, 0);
			break;
		case 3:
			soundTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundTexture,
							this, "gfx/mute.png", 0, 0);
			break;
		case 5:
			soundTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundTexture,
							this, "gfx/mute.png", 0, 0);
			break;
		case 6:
			soundTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.soundTexture,
							this, "gfx/mute.png", 0, 0);
			break;
		case 7:
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

		case 0:
			shareTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.shareTexture,
							this, "gfx/share_icon.png", 0, 0);
			break;
		case 1:
			shareTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.shareTexture,
							this, "gfx/share_icon.png", 0, 0);
			break;
		case 2:
			shareTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.shareTexture,
							this, "gfx/share_icon.png", 0, 0);
			break;
		case 3:
			shareTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.shareTexture,
							this, "gfx/share_icon.png", 0, 0);
			break;
		case 5:
			shareTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.shareTexture,
							this, "gfx/share_icon.png", 0, 0);
			break;
		case 6:
			shareTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.shareTexture,
							this, "gfx/share_icon.png", 0, 0);
			break;
		case 7:
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
		case 0:
			homeTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.homeTexture,
							this, "gfx/home.png", 0, 0);
			break;
		case 1:
			homeTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.homeTexture,
							this, "gfx/home.png", 0, 0);
			break;
		case 2:
			homeTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.homeTexture,
							this, "gfx/home.png", 0, 0);
			break;
		case 3:
			homeTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.homeTexture,
							this, "gfx/home.png", 0, 0);
			break;
		case 5:
			homeTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.homeTexture,
							this, "gfx/home.png", 0, 0);
			break;
		case 6:
			homeTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.homeTexture,
							this, "gfx/home.png", 0, 0);
			break;
		case 7:
			homeTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.homeTexture,
							this, "gfx/home.png", 0, 0);
			break;
		}
		return homeTextureRegion;
	}

	private TextureRegion loadLighterCap(int lightercolor) {
		switch (lightercolor) {

		case 0:
			lighterCapRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.lighterCapTexture, this,
							"gfx/lightercapa.png", 0, 0);
			break;
		case 1:
			lighterCapRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.lighterCapTexture, this,
							"gfx/lightercapb.png", 0, 0);
			break;
		case 2:
			lighterCapRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.lighterCapTexture, this,
							"gfx/lightercapc.png", 0, 0);
			break;
		case 3:
			lighterCapRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.lighterCapTexture, this,
							"gfx/lightercapd.png", 0, 0);
			break;
		case 5:
			lighterCapRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.lighterCapTexture, this,
							"gfx/lightercape.png", 0, 0);
			break;
		case 6:
			lighterCapRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.lighterCapTexture, this,
							"gfx/lightercapf.png", 0, 0);
			break;
		case 7:

			lighterCapRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(
							(BitmapTextureAtlas) this.lighterCapTexture, this,
							"gfx/lightercapg.png", 0, 0);
			break;
		}
		return lighterCapRegion;
	}

	private TextureRegion loadLighter(int lightercolor) {
		TextureRegion lighterTextureRegion1 = null;
		switch (lightercolor) {

		case 0:
			lighterTextureRegion1 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.lighterTexture,
							this, "gfx/lightera.png", 0, 0);
			break;
		case 1:
			lighterTextureRegion1 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.lighterTexture,
							this, "gfx/lighterb.png", 0, 0);
			break;
		case 2:
			lighterTextureRegion1 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.lighterTexture,
							this, "gfx/lighterc.png", 0, 0);
			break;
		case 3:
			lighterTextureRegion1 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.lighterTexture,
							this, "gfx/lighterd.png", 0, 0);
			break;
		case 5:
			lighterTextureRegion1 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.lighterTexture,
							this, "gfx/lightere.png", 0, 0);
			break;
		case 6:
			lighterTextureRegion1 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.lighterTexture,
							this, "gfx/lighterf.png", 0, 0);
			break;
		case 7:
			lighterTextureRegion1 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset((BitmapTextureAtlas) this.lighterTexture,
							this, "gfx/lighterg.png", 0, 0);
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

		// adding Character sprite and making its body........
		CharacterSprite = new AnimatedSprite(400, 200,
				characterTiledTextureRegion);
		// characterRectangle = new Rectangle(CharacterSprite.getX(),
		// CharacterSprite.getY(), CharacterSprite.getWidth(),
		// CharacterSprite.getHeight() + 5);

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

					final int viewWidth = ParticleViewActivity.this.mRenderSurfaceView
							.getWidth();
					final int viewHeight = ParticleViewActivity.this.mRenderSurfaceView
							.getHeight();

					// making a file for storing image
					File direct = new File(
							Environment.getExternalStorageDirectory()
									+ "/Virtual Ligher");
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
									ParticleViewActivity.this
											.runOnUiThread(new Runnable() {

												public void run() {
													// Toast.makeText(ParticleViewActivity.this,
													// "Screenshot: " +
													// pFilePath + " taken!",
													// Toast.LENGTH_SHORT).show();

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
									ParticleViewActivity.this
											.runOnUiThread(new Runnable() {

												public void run() {

													// Toast.makeText(ParticleViewActivity.this,
													// "FAILED capturing Screenshot: "
													// + pFilePath + " !",
													// Toast.LENGTH_SHORT).show();
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

		// sprite for spark produced
		sparkSprite = new Sprite((CAMERA_WIDTH / 2) - 70

		- (sparkTextureRegion.getWidth() / 2) + 70, 400 // adjusting the
														// horizontal movment of
														// arroow

				- sparkTextureRegion.getHeight() + 140, sparkTextureRegion);

		// sprite for lighter body produced

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

		// sprite for lighter button produced

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

						CharacterSprite.animate(100);

						if (value_for_clr == 1) {
							particleSystem.setVisible(true);
							particleSystem_red.setVisible(false);
							particleSystem_blue.setVisible(false);
							particleSystem_green.setVisible(false);

						} else if (value_for_clr == 2) {
							particleSystem_blue.setVisible(true);
							particleSystem.setVisible(false);
							particleSystem_red.setVisible(false);
							particleSystem_green.setVisible(false);
						} else if (value_for_clr == 3) {
							particleSystem_green.setVisible(true);
							particleSystem.setVisible(false);
							particleSystem_red.setVisible(false);
							particleSystem_blue.setVisible(false);
						} else {
							particleSystem.setVisible(true);
						}

						sparkSprite.setVisible(true);

						Thread t = new Thread() {
							@Override
							public void run() {
								try {
									Thread.sleep(100);
									handlernew.sendEmptyMessage(1);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						};
						t.start();

					}
				}
				System.out.println("ACTION2222222222222222");
				return true;
			};
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

		// sprite for flame color change button
		FlameColorSprite = new Sprite(
				(CAMERA_WIDTH / 4) + 95

				- (FlameColorTextureRegion.getWidth() / 4) + 200,
				75 // adjusting the
					// horizontal movment of
					// share icon

				- FlameColorTextureRegion.getHeight() / 4 + 40,
				FlameColorTextureRegion) {

			public boolean onAreaTouched(
					org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {

				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {

					// handlernew.sendEmptyMessage(2);
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
		// build method for yellow flame
		particleSystem = build(mEngine);
		// build method for red flame
		particleSystem_red = build_red(mEngine);
		// build method for blue flame
		particleSystem_blue = build_blue(mEngine);

		// build method for green flame
		particleSystem_green = build_green(mEngine);

		rectangleGroup.attachChild(particleSystem);
		rectangleGroup.attachChild(particleSystem_red);
		rectangleGroup.attachChild(particleSystem_blue);
		rectangleGroup.attachChild(particleSystem_green);

		particleSystem.setVisible(false);
		particleSystem_red.setVisible(false);
		particleSystem_blue.setVisible(false);
		particleSystem_green.setVisible(false);

		rectangleGroup.attachChild(sparkSprite);

		rectangleGroup.attachChild(shareSprite);
		rectangleGroup.attachChild(lighterOnBtnSprite);

		rectangleGroup.attachChild(lighterSprite);
		rectangleGroup.attachChild(homeSprite);
		rectangleGroup.attachChild(soundSprite);
		rectangleGroup.attachChild(soundOffSprite);
		rectangleGroup.attachChild(FlameColorSprite);
		soundOffSprite.setVisible(false);

		rectangleGroup.attachChild(lighterCapSprite);
		rectangleGroup.attachChild(arrowSprite);
		sparkSprite.setVisible(false);

		scene = new Scene();
		screenCapture = new ScreenCapture();

		scene.setBackground(new ColorBackground(0f, 0f, 0f));

		scene.setTouchAreaBindingEnabled(true);
		// scene.attachChild(CharacterSprite);
		// CharacterSprite.setVisible(true);

		scene.attachChild(rectangleGroup);
		scene.attachChild(screenCapture);

		// scene.registerTouchArea(lighterOnBtnSprite);
		scene.registerTouchArea(lighterCapSprite);
		scene.registerTouchArea(homeSprite);
		scene.registerTouchArea(soundSprite);
		scene.registerTouchArea(shareSprite);
		scene.registerTouchArea(FlameColorSprite);
		// scene.registerTouchArea(soundOffSprite);

		scene.registerUpdateHandler(new org.anddev.andengine.engine.handler.IUpdateHandler() {

			public void reset() {
				// TODO Auto-generated method stub

			}

			public void onUpdate(float pSecondsElapsed) {

				System.out.println("onUpdate ---------------onUpdate-");
				rotateLighterCap();

				if (value_for_clr == 1) {

					System.out.println("in if case update..");
					/*
					 * particleSystem = build2(mEngine);
					 * particleSystem.setVisible(true);
					 */
				}

			}
		});
		return scene;

	}

	double pressedX, releasedX;
	boolean isLighterLidOpen = false, isLighterLidClosed = false;

	public ParticleSystemFactory getFactoryByIndex(int index) {
		return FightParticle.PARTICLE_SYSTEMS[index];
	}

	VelocityInitializer velocityInitializer, velocityInitializer_red,
			velocityInitializer_blue, velocityInitializer_green;

	// build method for yellow flame

	public ParticleSystem build(Engine engine) {
		IParticleEmitter emitter = new CircleParticleEmitter(
				lighterSprite.getX() + lighterSprite.getWidth() / 2 - 25,
				lighterSprite.getY() - 25, 5);

		particleSystem = new ParticleSystem(emitter, RATE_MIN, RATE_MAX,
				PARTICLES_MAX, this.mParticleTextureRegion);

		particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

		velocityInitializer = new VelocityInitializer(-30, 30, -120, -100);

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

		return particleSystem;

	}

	// build method for red flame

	public ParticleSystem build_red(Engine engine) {
		System.out.println("in build 2");
		IParticleEmitter emitter = new CircleParticleEmitter(
				lighterSprite.getX() + lighterSprite.getWidth() / 2 - 25,
				lighterSprite.getY() - 25, 5);

		particleSystem_red = new ParticleSystem(emitter, RATE_MIN, RATE_MAX,
				PARTICLES_MAX, this.mParticleTextureRegion_red);

		particleSystem_red.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

		velocityInitializer_red = new VelocityInitializer(-30, 30, -120, -100);

		velocityInitializer_red.setVelocity(-30, 30, -120, -80);
		particleSystem_red.addParticleInitializer(velocityInitializer);
		particleSystem_red
				.addParticleInitializer(new ColorInitializer(1, 1, 1));
		particleSystem_red.addParticleInitializer(new AlphaInitializer(0.7f));
		particleSystem_red.addParticleModifier(new ScaleModifier(2f, 1.7f, 0f,
				2f));
		particleSystem_red.addParticleModifier(new AlphaModifier(0.7f, 0.2f,
				1f, 2f));
		particleSystem_red.addParticleModifier(new ExpireModifier(2f));

		particleSystem.addParticleModifier(new ColorModifier(1f, 0.98f, 1f,
				0.5f, 0.8f, 0.2f, 0f, 0.5f));

		return particleSystem_red;

	}

	// build method for blue flame
	public ParticleSystem build_blue(Engine engine) {
		System.out.println("in build 2");
		IParticleEmitter emitter = new CircleParticleEmitter(
				lighterSprite.getX() + lighterSprite.getWidth() / 2 - 25,
				lighterSprite.getY() - 25, 5);

		particleSystem_blue = new ParticleSystem(emitter, RATE_MIN, RATE_MAX,
				PARTICLES_MAX, this.mParticleTextureRegion_blue);

		particleSystem_blue.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

		velocityInitializer_blue = new VelocityInitializer(-30, 30, -120, -100);

		velocityInitializer_blue.setVelocity(-30, 30, -120, -80);
		particleSystem_blue.addParticleInitializer(velocityInitializer);
		particleSystem_blue
				.addParticleInitializer(new ColorInitializer(1, 1, 1));
		particleSystem_blue.addParticleInitializer(new AlphaInitializer(0.7f));
		particleSystem_blue.addParticleModifier(new ScaleModifier(2f, 1.7f, 0f,
				2f));
		particleSystem_blue.addParticleModifier(new AlphaModifier(0.7f, 0.2f,
				1f, 2f));
		particleSystem_blue.addParticleModifier(new ExpireModifier(2f));

		particleSystem.addParticleModifier(new ColorModifier(1f, 0.98f, 1f,
				0.5f, 0.8f, 0.2f, 0f, 0.5f));

		return particleSystem_blue;

	}

	// build method for green flame
	public ParticleSystem build_green(Engine engine) {
		System.out.println("in build 2");
		IParticleEmitter emitter = new CircleParticleEmitter(
				lighterSprite.getX() + lighterSprite.getWidth() / 2 - 25,
				lighterSprite.getY() - 25, 5);

		particleSystem_green = new ParticleSystem(emitter, RATE_MIN, RATE_MAX,
				PARTICLES_MAX, this.mParticleTextureRegion_green);

		particleSystem_green.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

		velocityInitializer_green = new VelocityInitializer(-30, 30, -120, -100);

		velocityInitializer_green.setVelocity(-30, 30, -120, -80);
		particleSystem_green.addParticleInitializer(velocityInitializer);
		particleSystem_green.addParticleInitializer(new ColorInitializer(1, 1,
				1));
		particleSystem_green.addParticleInitializer(new AlphaInitializer(0.7f));
		particleSystem_green.addParticleModifier(new ScaleModifier(2f, 1.7f,
				0f, 2f));
		particleSystem_green.addParticleModifier(new AlphaModifier(0.7f, 0.2f,
				1f, 2f));
		particleSystem_green.addParticleModifier(new ExpireModifier(2f));

		particleSystem.addParticleModifier(new ColorModifier(1f, 0.98f, 1f,
				0.5f, 0.8f, 0.2f, 0f, 0.5f));

		return particleSystem_green;

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
				particleSystem_red.setVisible(false);
				particleSystem_blue.setVisible(false);
				particleSystem_green.setVisible(false);

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

				velocityInitializer_red.setVelocityX(80, 60);
				velocityInitializer_blue.setVelocityX(80, 60);
				velocityInitializer_green.setVelocityX(80, 60);
			} else if (changeInX > 2 && changeInX < 3) {
				velocityInitializer.setVelocityX(90, 80); // to make flame go
															// right

				velocityInitializer_red.setVelocityX(90, 80);
				velocityInitializer_blue.setVelocityX(90, 80);
				velocityInitializer_green.setVelocityX(90, 80);
			} else if (changeInX > 3) {
				velocityInitializer.setVelocityX(200, 180); // to make flame go
															// right

				velocityInitializer_red.setVelocityX(200, 180);
				velocityInitializer_blue.setVelocityX(200, 180);
				velocityInitializer_green.setVelocityX(200, 180);
			}

		}

		else if (changeInX <= -1) {

			if (changeInX < -1 && changeInX > -2) {
				velocityInitializer.setVelocityX(-80, -60); // to make flame go
															// left

				velocityInitializer_red.setVelocityX(-80, -60);
				velocityInitializer_blue.setVelocityX(-80, -60);
				velocityInitializer_green.setVelocityX(-80, -60);
			} else if (changeInX < -2 && changeInX > -3) {
				velocityInitializer.setVelocityX(-90, -80);
				velocityInitializer_red.setVelocityX(-90, -80);
				velocityInitializer_blue.setVelocityX(-90, -80);
				velocityInitializer_green.setVelocityX(-90, -80);
			} else if (changeInX < -3) {
				velocityInitializer.setVelocityX(-200, -180);
				velocityInitializer_red.setVelocityX(-200, -180);
				velocityInitializer_blue.setVelocityX(-200, -180);
				velocityInitializer_green.setVelocityX(-200, -180);
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
