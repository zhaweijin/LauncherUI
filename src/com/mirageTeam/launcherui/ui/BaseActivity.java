package com.mirageTeam.launcherui.ui;

import android.app.Activity;
import android.view.KeyEvent;

import com.mirageTeam.widget.CleanMemoryBottomMenu;

public class BaseActivity extends Activity {

	protected boolean shortpressflag = false;
	protected boolean flag = false;

	private void showMemoryCleanView() {
		CleanMemoryBottomMenu memoryMenu = new CleanMemoryBottomMenu(this);
		memoryMenu.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			event.startTracking();
			if (flag == true) {
				shortpressflag = false;
			} else {
				shortpressflag = true;
				flag = false;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// Log.i("KEYCODE_MENU", "KEYCODE_MENU =======onKeyLongPress==== ");
			// Toast.makeText(mContext,
			// "KEYCODE_MENU =======onKeyLongPress====",
			// Toast.LENGTH_SHORT).show();
			shortpressflag = false;
			flag = true;

			showMemoryCleanView();
			return true;

		}
		return super.onKeyLongPress(keyCode, event);
	}
}
