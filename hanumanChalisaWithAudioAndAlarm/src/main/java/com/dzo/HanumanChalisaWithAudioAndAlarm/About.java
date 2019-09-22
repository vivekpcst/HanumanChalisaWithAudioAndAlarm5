package com.dzo.HanumanChalisaWithAudioAndAlarm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.OperationApplicationException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class About extends BottomNav
{
	ImageButton btn_Exit, btn_Start, btnAudio, btn_Logo,imageButton;
	LinearLayout linHomebtn, linHindibtn, linEnglishbtn, linAudiobtn, linAboutbtn;
	TextView emailId, about_us_no, about_germany_no, us_name, germany_name, txtPlay
             , txtHome, txtRead, txtInfo, txtExit;
	Intent serviceIntent;
	Button btnrate,btnshare,btnfeedback,btnmoreapps;
	protected String[] lblPhone = {"Add Contact"};
	boolean chalisaPlaying = false;
	boolean showOnce;
	String SHOW_RATING_DIALOG = "showRatingDialog";

	public static final String UPDATE_UI_ACTION = "com.dzo.HanumanChalisaWithAudioAndAlarm.UPDATE_UI";

    @Override
    public int getContentViewId() {
        return R.layout.info;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_info;
    }
	public static void setForceShowIcon(PopupMenu popupMenu) {
		try {
			Field[] fields = popupMenu.getClass().getDeclaredFields();
			for (Field field : fields) {
				if ("mPopup".equals(field.getName())) {
					field.setAccessible(true);
					Object menuPopupHelper = field.get(popupMenu);
					Class<?> classPopupHelper = Class.forName(menuPopupHelper
							.getClass().getName());
					Method setForceIcons = classPopupHelper.getMethod(
							"setForceShowIcon", boolean.class);
					setForceIcons.invoke(menuPopupHelper, true);
					break;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

    @RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.about);

getLayoutInflater().inflate(R.layout.info, content_frame);

		/**
		 * Setting title and itemChecked  
		 */
//		mDrawerList.setItemChecked(position, true);
		//setTitle(mDrawerMenuList[position]);

//		getSupportActionBar().setBackgroundDrawable(
//                getResources().getDrawable(R.drawable.header));
		btnmoreapps=findViewById(R.id.btnmoreapps);
		linHomebtn = (LinearLayout)findViewById(R.id.linHomebtn);
		linHindibtn = (LinearLayout)findViewById(R.id.linHindibtn);
	//	linEnglishbtn = (LinearLayout)findViewById(R.id.linEnglishbtn);
		linAudiobtn = (LinearLayout)findViewById(R.id.linAudiobtn);
		linAboutbtn = (LinearLayout)findViewById(R.id.linAboutbtn);
		serviceIntent = new Intent(About.this, ChalisaService.class);
		
		txtHome = (TextView)findViewById(R.id.txtHome);
	//	txtRead = (TextView)findViewById(R.id.txtRead);
		txtPlay = (TextView)findViewById(R.id.txtPlay);
		txtInfo = (TextView)findViewById(R.id.txtInfo);
		txtExit = (TextView)findViewById(R.id.txtExit);

		TextView about=findViewById(R.id.about);
		TextView about_1=findViewById(R.id.about_1);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		if((width*height)>=(1080*1920)) {
//			about.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//			about_1.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
		}else{
			about.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			about_1.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
			((TextView)findViewById(R.id.about_dotzoo)).setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
			((TextView)findViewById(R.id.textView9)).setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
			((TextView)findViewById(R.id.emailId)).setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
			((TextView)findViewById(R.id.us_name)).setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
			((TextView)findViewById(R.id.about_us_no)).setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
			((TextView)findViewById(R.id.germany_name)).setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
			((TextView)findViewById(R.id.about_germany_no)).setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

		}

		imageButton=findViewById(R.id.imageButton);
		
		btnrate=(Button) findViewById(R.id.btnRate);
		btnshare=(Button) findViewById(R.id.btnShare);
		btnfeedback=(Button) findViewById(R.id.btnfeedback);
		
		//getSupportActionBar().setTitle(getResources().getString(R.string.info));
//		final PopupMenu popupMenu=new PopupMenu(this,imageButton);
//		popupMenu.inflate(R.menu.more_apps);
		Context wrapper = new ContextThemeWrapper(this, R.style.MyStyle);
		final PopupMenu popupMenu = new PopupMenu(wrapper, imageButton);

		setForceShowIcon(popupMenu);
		popupMenu.inflate(R.menu.more_apps);


		btnmoreapps.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//			popupMenu.show();
				FragmentManager fm = getSupportFragmentManager();
				OtherFreeApps newFragment = new OtherFreeApps();
				newFragment.show(fm, "Other apps");

			}
		});

		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()){

					case R.id.menu_arti:{
//						Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.dzo.aarti"));
//						startActivity(intent);
						try {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dzo.aarti")));
						} catch (android.content.ActivityNotFoundException anfe) {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dzo.aarti")));
						}


						break;
					}
					case R.id.menu_gurbani:{
//						Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.dzo.gurbani"));
//						startActivity(intent);
						try {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dzo.gurbani")));
						} catch (android.content.ActivityNotFoundException anfe) {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dzo.gurbani")));
						}

						break;
					}
					case R.id.menu_hc_multi:{
//						Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.dzo.HanumanChalisaMultilingual"));
//						startActivity(intent);
						try {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dzo.HanumanChalisaMultilingual")));
						} catch (android.content.ActivityNotFoundException anfe) {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dzo.HanumanChalisaMultilingual")));
						}

						break;
					}
					case R.id.menu_krishna:{
//						Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.dzo.kriana"));
//						startActivity(intent);
						try {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dzo.kriana")));
						} catch (android.content.ActivityNotFoundException anfe) {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dzo.kriana")));
						}

						break;
					}
					case R.id.menu_sai_baba:{
//						Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.dzo.saibaba"));
//						startActivity(intent);
						try {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dzo.saibaba")));
						} catch (android.content.ActivityNotFoundException anfe) {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dzo.saibaba")));
						}

						break;
					}
				}


				return false;
			}
		});
		btnrate.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				rateApp();
			}
		});
		
		btnshare.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				openShare();
				
			}
		});
		
		
		btnfeedback.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				feedback();
				
			}
		});
		
		
		/*txtInfo.setTextColor(getResources().getColor(R.color.redwine));
		
		btnAudio = (ImageButton)findViewById(R.id.btnAudio);
		btn_Logo = (ImageButton)findViewById(R.id.btn_Logo);
		
		btn_Logo.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				txtInfo.setTextColor(getResources().getColor(R.color.redwine));
				Intent in = new Intent(About.this, About.class);
				startActivity(in);
			}//onClick
		});
		
		btn_Exit = (ImageButton)findViewById(R.id.btn_Exit);
		//btn_Exit.setBackgroundResource(R.drawable.btnexitselected);
		
		btnAudio.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				if(ChalisaService.playerFlag == 0)
				{
					startService(serviceIntent);
					ChalisaService.playerFlag = 1;
					//Log.i("HanuAlarm play button if", ""+chalisaPlaying);
					txtPlay.setText("Pause");
					txtPlay.setTextColor(getResources().getColor(R.color.redwine));
					btnAudio.setBackgroundResource(R.drawable.btnpause);
				}//if
				else if(ChalisaService.playerFlag == 1)
				{
					ChalisaService.mediaPlayer.pause();
					ChalisaService.playerFlag = 0;
					//Log.i("HanuAlarm play button else", ""+chalisaPlaying);
					txtPlay.setText("Play");
					txtPlay.setTextColor(getResources().getColor(R.color.white));
					btnAudio.setBackgroundResource(R.drawable.btnplay_a);
				}//else if
			}
		});*/
		emailId = (TextView)findViewById(R.id.emailId);
		about_us_no = (TextView)findViewById(R.id.about_us_no);
		about_germany_no = (TextView)findViewById(R.id.about_germany_no);
		
		us_name = (TextView)findViewById(R.id.us_name);
		germany_name = (TextView)findViewById(R.id.germany_name);
		
		
		
		
		/*btn_Start = (ImageButton)findViewById(R.id.btn_Start);
		
		btn_Start.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				txtRead.setTextColor(getResources().getColor(R.color.redwine));
				if(Build.MANUFACTURER.equalsIgnoreCase("samsung"))
				{
					Intent intent = new Intent(About.this, StartManualActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}//if
				else
				{
					Toast.makeText(About.this, 
							"Sorry, Devanagari Script is not supported by your device", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(About.this, English.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}//else
			}
		});*/
		
		/*ImageButton btnHome = (ImageButton) findViewById(R.id.btnHome);
		btnHome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				txtHome.setTextColor(getResources().getColor(R.color.redwine));
				Intent intent = new Intent(About.this, HanuAlarm.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});*/
		
		/*btn_Exit.setOnClickListener(new View.OnClickListener() 
		{
			
			public void onClick(View v) 
			{
				txtExit.setTextColor(getResources().getColor(R.color.redwine));
				showOnce = Prefs.getBoolean(About.this, SHOW_RATING_DIALOG);
				if(showOnce == false)
				{
					showDialog();
					Prefs.setBoolean(About.this, SHOW_RATING_DIALOG, true);
				}//if
				else
				{
					//System.runFinalizersOnExit(true);
					//Process.killProcess(Process.myPid());
					Intent in = new Intent(Intent.ACTION_MAIN);
					in.addCategory(Intent.CATEGORY_HOME);
					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(in);
					finish();
				}//else
			}//onClick
		});
		*/
		emailId.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				sendEmail("mail");
			}
		});
		
		about_us_no.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				
				openSelectDialog(about_us_no, "", lblPhone);
				
			}
		});
		
		about_germany_no.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				openSelectDialog(about_germany_no, "", lblPhone);
			}
		});
		registerReceiver(mUpdateUIReceiver, new IntentFilter(UPDATE_UI_ACTION));
	}//onCreate

	private void sendEmail(String type) 
	{
		boolean found = false;
		Intent in = new Intent(android.content.Intent.ACTION_SEND);
		in.setType("application/octet-stream");
		List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(in, 0);
		if (!resInfo.isEmpty())
		{
		    for (ResolveInfo info : resInfo) 
		    {
		        if (info.activityInfo.packageName.toLowerCase().contains(type) || 
		                info.activityInfo.name.toLowerCase().contains(type) ) 
		        {
		            in.putExtra(Intent.EXTRA_EMAIL, new String[]{"Dotzoo Inc.<"+"info@dzoapps.com"+">"});
		            in.putExtra(Intent.EXTRA_SUBJECT,  "Your Subject");
		            in.putExtra(Intent.EXTRA_TEXT,     "your text");
		            in.setPackage(info.activityInfo.packageName);
		            found = true;
		            break;
		        }//if
		    }//for
		    if (!found)
		        return;
		    startActivity(Intent.createChooser(in, "Select"));
		}//if
	}//sendEmail
	
	protected void onStop()
	{
		super.onStop();
		this.finish();
	}//onStop
	
	private void createContact(String name, String phone) {
	    
	    ContentResolver cr = getContentResolver();
	    
	    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
	               null, null, null, null);
	       
	    if (cur.getCount() > 0) {
	        while (cur.moveToNext()) {
	         String existName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	         if (existName.contains(name)) {
	                Toast.makeText(About.this,"The contact name: " + name + " already exists", Toast.LENGTH_SHORT).show();
	                return;           
	         }
	        }
	    }
	    
	       ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
	       ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
	               .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
	               .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
	               .build());
	       ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
	               .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
	               .withValue(ContactsContract.Data.MIMETYPE,
	                       ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
	               .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
	               .build());
	       ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
	               .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
	               .withValue(ContactsContract.Data.MIMETYPE,
	                       ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
	               .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
	               .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
	               .build());

	       
	       try {
	   cr.applyBatch(ContactsContract.AUTHORITY, ops);
	  } 
	       catch (RemoteException e) {
	   e.printStackTrace();
	  } catch (OperationApplicationException e) {
	   e.printStackTrace();
	  }

	    Toast.makeText(About.this, "Contact added to your contact list ", Toast.LENGTH_SHORT).show();
	    
	   }
	
	 public void openSelectDialog(final TextView button,final String string,final String[] items)
	   {
		  AlertDialog.Builder ad=new AlertDialog.Builder(About.this);
		
		  ad.setTitle(string);			

		ad.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() 
		{
			

			public void onClick(DialogInterface dialog, int which) 
			 {
	         }
	     });
		
		ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int i) 
			{
				System.out.println("Clicked on Yes");
				
				DisplayContactAlert(button); 
			}
		});
		ad.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i) {
				dialog.dismiss();
			}
		});
		ad.show();
	}
	
public void DisplayContactAlert(TextView tv){
		
		final Dialog dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.dialog_add_contact);
		dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
		dialog.setCancelable(false);

		
		Button btnOK = (Button) dialog.findViewById(R.id.btnDialogOK);	
		Button btnCancel = (Button) dialog.findViewById(R.id.btnDialogCancel);	
		final EditText txtName = (EditText)dialog.findViewById(R.id.txtName);
		final EditText txtContact = (EditText)dialog.findViewById(R.id.txtContact);
		
		txtContact.setText(tv.getText().toString());
		
		dialog.show();
		btnOK.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				
				if(txtName.getText().toString().length()>0&&txtContact.getText().toString().length()>0)
				{
				createContact(txtName.getText().toString(), txtContact.getText().toString());;
				dialog.dismiss();		
				
				}
				else
				{
					Toast.makeText(About.this, "Please fill fileds required", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				//createContact(txtName.getText().toString(), txtContact.getText().toString());;
				dialog.dismiss();				
			}
		});
	}
	
	@Override
	protected void onResume() 
	{
		
		/*if(ChalisaService.playerFlag == 0)
		{
			txtPlay.setText("Play");
			txtPlay.setTextColor(getResources().getColor(R.color.white));
			btnAudio.setBackgroundResource(R.drawable.btnplay_a);
		}//if
		else if(ChalisaService.playerFlag == 1)
		{
			txtPlay.setText("Pause");
			txtPlay.setTextColor(getResources().getColor(R.color.redwine));
			btnAudio.setBackgroundResource(R.drawable.btnpause);
		}//else
*/		super.onResume();
	}//makeCall
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		unregisterReceiver(mUpdateUIReceiver);
	}//onDestroy
	
	@Override
	public void onBackPressed() 
	{
		/*Intent in = new Intent(Intent.ACTION_MAIN);
		in.addCategory(Intent.CATEGORY_HOME);
		in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(in);*/
		super.onBackPressed();
	}
	
	private void showDialog() 
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(About.this);
		ad.setIcon(R.drawable.icon);
		ad.setTitle("Rate The App");
		ad.setMessage("Would you like to rate this app?");
		ad.setPositiveButton("Now", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				Intent intent = new Intent(Intent.ACTION_VIEW); 
				intent.setData(Uri.parse("market://details?id="+getPackageName())); 
				try 
				{
				   startActivity(intent);
				}//try 
				catch (android.content.ActivityNotFoundException ex) 
				{
				      Toast.makeText(About.this, "Android Market Not Availbale at this Device", Toast.LENGTH_SHORT).show();
				}//catch
			}//onClick
		});
		
		ad.setNegativeButton("Later", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				Intent in = new Intent(Intent.ACTION_MAIN);
				in.addCategory(Intent.CATEGORY_HOME);
				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
				finish();
			}//onClick
		});
		ad.show();
	}//showDialog
	
	
	private void showRatingDialog()
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(About.this);
		builder.setTitle(getResources().getString(R.string.ratethisapp));
		builder.setMessage(getResources().getString(R.string.askratethisapp));
		
		
		builder.setPositiveButton("Now",new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				rateApp();
			}
		} );
		
		
		builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		builder.show();
	}
	
	

	private void rateApp()
	{
		try{   //code specific to first list item
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.dzo.HanumanChalisaWithAudioAndAlarm"));
        //startActivity(intent);
		startActivity(intent);
		}catch(android.content.ActivityNotFoundException ex)
		{
			Toast.makeText(About.this, "Goolgle play not available on this device", Toast.LENGTH_LONG).show();
		}
	}
	
	
	private void openShare() {
		String mMailSubject = "Free Hanuman Chalisa App";
		String mMailMessage = "";
		/*mMailMessage = "Hi,\n I found this useful App to read and listen to the Hanuman Chalisa. It even comes with an Alram feature, so you can wake up to the sounds of the Conch (Shank) and Bells. And this App is completely FREE, with a great audio quality";
		mMailMessage += "\n";*/
		mMailMessage+=getString(R.string.share_txt);
		mMailMessage += "\n To download this app, click this link:\n https://market.android.com/details?id=" + getPackageName();
		mMailMessage += ",\n\n To enjoy other religious Apps from DZOApps, visit the :\n http://www.dotzoo.net to learn more about Dotzoo Inc.";
		mMailMessage+="\n\nप्रिय मित्र, हर सुबह-शाम हनुमान चालीसा पढ़ने और सुनने के लिए, मेरा सुझाव है की डॉट्ज़ू की उत्तम ऐप (App) जो की निशुल्क है| और इसी ऐप में सुबह-सुबह शंक और घंटा ध्वनि की आवाज़ सुन के उठने के लिए भी अलार्म (Alarm) सुविधा है|\n" +
				"\n";

		mMailMessage += "\n इस ऐप(App) को डाउनलोड करने के लिए इस नीले लिंक को दबाएँ : \n https://market.android.com/details?id=" + getPackageName();
		mMailMessage+="\n\n अगर आपको और भी ऐसी उत्तम धार्मिक ऐप्स (Apps) का आनंद लेना हो, तो ऐंड्रोइड प्ले स्टोर (Android App Store) से डॉट्ज़ू (Dotzoo, Inc.) के सौजन्य से मुफ़्त और बिल्कुल निशुल्क (Free) ऐप्स (App[s) का आनंद ले सकते हैं|"
		+"\n हमारी संस्थान के बारे में और अधिक जानकारी के लिए नीचे दिए गये लिंक पर जायें: \n\n"+"http://www.dotzoo.net"
		;
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("text/*");
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "" + mMailSubject);
		emailIntent.putExtra(Intent.EXTRA_TEXT, mMailMessage);
		startActivity(Intent.createChooser(emailIntent, "Share via..."));
	}

	private void feedback()
	{
		Intent fedack = new Intent(About.this,Feedback.class);
		startActivity(fedack);
		
	}
	
	private final BroadcastReceiver mUpdateUIReceiver = new BroadcastReceiver() 
    {
		@Override
		public void onReceive(Context context, Intent intent) {
			//updateStatus();
			int playerFlagValue = intent.getIntExtra("Player_FLAG_VALUE", 0);
			if(playerFlagValue == 0)
			{
				txtPlay.setText("Play");
				txtPlay.setTextColor(getResources().getColor(R.color.white));
				btnAudio.setBackgroundResource(R.drawable.play);
			}//if
			else
			{
				txtPlay.setText("Pause");
				txtPlay.setTextColor(getResources().getColor(R.color.redwine));
				btnAudio.setBackgroundResource(R.drawable.btnpause);
			}//else
		}
	};

	
}
