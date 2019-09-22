package com.dzo.HanumanChalisaWithAudioAndAlarm;







import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Feedback extends BottomNav {
	
	
	EditText txtfeedback;
	Button feedbackbtn;
	String s1;

	@Override
	public int getContentViewId() {
		return R.layout.feed_back;
	}

	@Override
	public int getNavigationMenuItemId() {
		return R.id.navigation_info;
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.feedback);
//		getActionBar().setBackgroundDrawable(
//                getResources().getDrawable(R.drawable.header));
getLayoutInflater().inflate(R.layout.feed_back, content_frame);
		
		/**
		 * Setting title and itemChecked  
		 */
//		mDrawerList.setItemChecked(position, true);
		//setTitle(mDrawerMenuList[position]);

//		getSupportActionBar().setTitle("Feedback");
		txtfeedback=(EditText)findViewById(R.id.feedbackText);
		feedbackbtn=(Button)findViewById(R.id.feedbackbtn);

		feedbackbtn.setOnClickListener(new View.OnClickListener() {
			
		
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				sendMail();
				
			}
		});
		
	}
	
	
	private void sendMail()
	{
		s1=txtfeedback.getText().toString();
		String[] To={"developer@dotzoo.net"};
		//String[] Cc={"abhishekvermavs1992@gmail.com"};
		
		Intent email_intent=new Intent(Intent.ACTION_SEND);
		email_intent.setData(Uri.parse("mailTo:"));
		email_intent.setType("text/plain");
		
		email_intent.putExtra(Intent.EXTRA_EMAIL, To);
		//email_intent.putExtra(Intent.EXTRA_CC, Cc);
		email_intent.putExtra(Intent.EXTRA_SUBJECT, "HanumanChalisaWithAudioAndAlarm - Feedback");
		email_intent.putExtra(Intent.EXTRA_TEXT, s1);
		
		
		
		try {
	         startActivity(Intent.createChooser(email_intent, "Send mail..."));
	         finish();
	         Log.i("Finished sending ema", "");
	      } catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(Feedback.this, 
	         "There is no email client installed.", Toast.LENGTH_SHORT).show();
	      }
	}
	
	
	
}
