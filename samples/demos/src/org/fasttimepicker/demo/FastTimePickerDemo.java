package org.fasttimepicker.demo;

import java.util.Calendar;

import org.fasttimepicker.Alarm;
import org.fasttimepicker.AlarmTimePickerDialogFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class FastTimePickerDemo extends Activity implements OnClickListener {

	private final Calendar mAndroidFragmentTime = Calendar.getInstance();
	private final Calendar mAndroidDialogTime = Calendar.getInstance();
	private final Calendar mFastFragmentTime = Calendar.getInstance();
	private final Calendar mFastDialogTime = Calendar.getInstance();

	private LinearLayout mAndroidFragment;
	private LinearLayout mAndroidDialog;
	private LinearLayout mFastFragment;
	private LinearLayout mFastDialog;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mAndroidFragment = (LinearLayout) findViewById(R.id.androidFragment);
		mAndroidDialog = (LinearLayout) findViewById(R.id.androidDialog);
		mFastFragment =  (LinearLayout) findViewById(R.id.fastFragment);
		mFastDialog = (LinearLayout) findViewById(R.id.fastDialog);

		mAndroidFragment.setOnClickListener(this);
		mAndroidDialog.setOnClickListener(this);
		mFastFragment.setOnClickListener(this);
		mFastDialog.setOnClickListener(this);

		onTimeSelect(mAndroidFragment, mAndroidFragmentTime);
		onTimeSelect(mAndroidDialog, mAndroidDialogTime);
		onTimeSelect(mFastFragment, mFastFragmentTime);
		onTimeSelect(mFastDialog, mFastDialogTime);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.androidFragment:

			break;
		case R.id.androidDialog:
			showAndroidTimePickerDialog();
			break;
		case R.id.fastFragment:

			break;
		case R.id.fastDialog:
			showFastTimePickerDialog();
			break;
			
		default:
		}

	}

	private void showFastTimePickerDialog() {
		final FragmentManager manager = getFragmentManager();
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("time_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        Alarm alarm = new Alarm();
        final AlarmTimePickerDialogFragment fragment = AlarmTimePickerDialogFragment.newInstance(
                alarm);
        fragment.show(ft, "time_dialog");
	}

	private void showAndroidTimePickerDialog() {

		OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

				mAndroidDialogTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
				mAndroidDialogTime.set(Calendar.MINUTE, minute);

				onTimeSelect(mAndroidDialog, mAndroidDialogTime);
			}
		};

		int hourOfDay = mAndroidDialogTime.get(Calendar.HOUR_OF_DAY);
		int minute = mAndroidDialogTime.get(Calendar.MINUTE);
		TimePickerDialog picker = new TimePickerDialog(FastTimePickerDemo.this,
				listener, hourOfDay, minute, true);
		picker.show();
	}

	private void onTimeSelect(LinearLayout layout, Calendar time) {
		TextView tv = ((TextView) layout.findViewById(android.R.id.text2));
		tv.setText(DateFormat.getTimeFormat(this).format(time.getTime()));
	}
}