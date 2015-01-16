package gaia.cu9.ari.gaiaorbit;

import gaia.cu9.ari.gaiaorbit.util.GlobalConf;
import gaia.cu9.ari.gaiaorbit.util.math.MathUtilsd;

import java.io.IOException;

import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class GaiaSandboxActivity extends AndroidApplication {
    WakeLock mWakeLock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
	this.mWakeLock.acquire();

	try {
	    GlobalConf.initialize(this.getAssets().open("conf/android/global.properties"), GaiaSandboxActivity.class.getResourceAsStream("/version"));
	} catch (IOException e) {
	    Log.e(this.getApplicationInfo().name, "Error initializing global configuration");
	    this.finish();
	}

	AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
	cfg.numSamples = MathUtilsd.clamp(GlobalConf.instance.POSTPROCESS_ANTIALIAS, 0, 16);
	cfg.depth = 8;
	cfg.stencil = 8;

	initialize(new GaiaSandbox(true), cfg);
    }

    @Override
    public void onDestroy() {
	this.mWakeLock.release();
	super.onDestroy();
    }
}