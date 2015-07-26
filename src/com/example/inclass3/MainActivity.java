/*********************************
 * InClass #3
 * FileName: MainActivity.java
 *********************************
 * Team Members:
 * Richa Kandlikar
 * Sai Phaninder Reddy Jonnala
 * *******************************
 */

package com.example.inclass3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	public TextView tvNumber;
	
	ExecutorService threadpool;
	ProgressDialog pdRunning;
	Handler handler;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvNumber = (TextView) findViewById(R.id.tvNumber);
		threadpool = Executors.newFixedThreadPool(2);
		context=this;
		
		pdRunning = new ProgressDialog(this);
		pdRunning.setTitle("Doing Work");
		pdRunning.setMessage("Retrieving the number");
		
		pdRunning.setCancelable(false);
		pdRunning.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
		Button btnGenThread= (Button) findViewById(R.id.btnGenThread);
		btnGenThread.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				threadpool.execute(new DoWork());
			}
		});
		
		Button btnGenAsync = (Button) findViewById(R.id.btnGenAsync);
		btnGenAsync.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyHeavyWorker mhw=new MyHeavyWorker();
				mhw.execute();
			}
		});

		handler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case DoWork.START:
					pdRunning.show();
					break;

				case DoWork.STOP:
					Double no=msg.getData().getDouble("NUMBER");
					tvNumber.setText("" + no);
					pdRunning.dismiss();
					break;
				}
				return false;
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class MyHeavyWorker extends AsyncTask<Void, Void, Double> {
		protected void onPreExecute(){
			Log.d("DEBUG","Task started");
			pdRunning.show();
		}
		
		@Override
		protected Double doInBackground(Void... params) {
			return (double) HeavyWork.getNumber();
		}
		
		protected void onProgressUpdate(Integer... progress){
			//TODO: hack into the hw class
		}

		protected void onPostExecute(Double result) {
			tvNumber.setText(""+result);
			Log.d("DEBUG","Task ended: "+result);
			pdRunning.dismiss();
		}
	}
	
	class DoWork implements Runnable {

		Message msg = new Message();
		static final int START = 0x00;
		static final int STOP = 0x02;

		public void run() {
			Message msg = new Message();
			msg.what = START;
			handler.sendMessage(msg);
			
			double no = HeavyWork.getNumber();
			Log.d("DEBUG","number: "+no);
			
			msg = new Message();
			Bundle b=new Bundle();
			b.putDouble("NUMBER", no);
			msg.setData(b);
			
			msg.what = STOP;
			handler.sendMessage(msg);
		}
	}
	
}
