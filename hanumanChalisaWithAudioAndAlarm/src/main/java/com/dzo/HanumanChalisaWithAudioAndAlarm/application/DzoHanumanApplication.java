package com.dzo.HanumanChalisaWithAudioAndAlarm.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class DzoHanumanApplication extends Application {

	private static Typeface text_TF;

	private static DzoHanumanApplication gurbaniNitnemApplication;

	private static String[] mGuruNames;
	private static String[] gurbaniArray;

	@Override
	public void onCreate() {
		super.onCreate();
		gurbaniNitnemApplication = this;
		initImageLoader(this);
	}

	public static synchronized DzoHanumanApplication getApplicationInstance() {
		return gurbaniNitnemApplication;
	}
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = ImageLoaderConfiguration
				.createDefault(context);
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	public Typeface getTextTypeface() {
		if (text_TF == null) {
			text_TF = Typeface
					.createFromAsset(getAssets(), "fonts/arialbd.ttf");
		}
		return text_TF;
	}

	/*public String[] getGuruNames() {
		if (mGuruNames == null) {
			mGuruNames = getResources()
					.getStringArray(R.array.guru_names_array);
		}
		return mGuruNames;
	}

	public String[] getGurbaniArray() {
		if (gurbaniArray == null) {
			gurbaniArray = new String[] {
					getResources().getString(R.string.japji_sahib),
					getResources().getString(R.string.jappu_sahib),
					getResources().getString(R.string.tav_prasad_swayya),
					getResources().getString(R.string.sukhmani_sahib),
					getResources().getString(R.string.chaupai_sahib),
					getResources().getString(R.string.anand_sahib),
					getResources().getString(R.string.rehras_sahib),
					getResources().getString(R.string.kirtan_sohila),
					getResources().getString(R.string.ardas) };
		}
		return gurbaniArray;
	}

	public Drawable[] getGuruImages() {
		return new Drawable[] {
				getResources().getDrawable(R.drawable.guru_nanak_dev_ji),
				getResources().getDrawable(R.drawable.guru_angad_dev_ji),
				getResources().getDrawable(R.drawable.guru_amar_das_ji),
				getResources().getDrawable(R.drawable.guru_ramdas_sahib_ji),
				getResources().getDrawable(R.drawable.guru_arjan_dev_ji),
				getResources().getDrawable(R.drawable.guru_har_gobind_sahib),
				getResources().getDrawable(R.drawable.guru_har_rai_sahib_ji),
				getResources().getDrawable(R.drawable.guru_harkrishan_sahib_ji),
				getResources().getDrawable(R.drawable.guru_tegh_bahadur_ji),
				getResources().getDrawable(R.drawable.guru_govind_sahib_ji),
				getResources().getDrawable(R.drawable.guru_granth_sahib_ji) };
	}*/
}