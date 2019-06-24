package com.mirageTeam.widget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimeControl extends TextView implements Observer {

	public TimeControl(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public TimeControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		setText(dateFormat.format(date));
		TimeTickChange timeTick = new TimeTickChange();
		timeTick.addObserver(this);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			setText(msg.obj.toString());
		};
	};

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Message msg = mHandler.obtainMessage();
		msg.obj = dateFormat.format(date);
		msg.sendToTarget();
	}

	class TimeTickChange extends Observable {
		public TimeTickChange() {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {
						// Log.d(TAG, "running");
						try {
							Thread.sleep(60*1000);
							setChanged();
							notifyObservers();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}

}
