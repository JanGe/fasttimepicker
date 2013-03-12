package org.fasttimepicker.demo;

import java.util.Calendar;

import org.fasttimepicker.AlarmTimePickerDialogFragment;
import org.fasttimepicker.ParcelableTime;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class FastTimePickerDemo extends Activity implements OnClickListener,
        AlarmTimePickerDialogFragment.OnTimeSetListener {
    
    private static Themes sTheme = Themes.DEFAULT;

    private final Calendar mAndroidFragmentTime = Calendar.getInstance();
    private final Calendar mAndroidDialogTime = Calendar.getInstance();
    private final Calendar mFastFragmentTime = Calendar.getInstance();
    private final Calendar mFastDialogTime = Calendar.getInstance();

    private LinearLayout mAndroidFragment;
    private LinearLayout mAndroidDialog;
    private LinearLayout mFastFragment;
    private LinearLayout mFastDialog;

    private Spinner mThemeSpinner;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        setTheme(sTheme.getId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mThemeSpinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<Themes> adapter =
                new ArrayAdapter<Themes>(this,
                        android.R.layout.simple_list_item_1, Themes.values()) {
                };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mThemeSpinner.setAdapter(adapter);
        mThemeSpinner.setSelection(sTheme.getPosition());

        mThemeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                changeTheme(Themes.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                changeTheme(Themes.values()[0]);
            }

            private void changeTheme(Themes theme) {
                if (sTheme != theme) {
                    sTheme = theme;
                    
                    Activity activity = FastTimePickerDemo.this;
                    activity.startActivity(new Intent(
                            activity, activity.getClass()));
                    activity.finish();
                }
            }

        });

        mAndroidFragment = (LinearLayout) findViewById(R.id.androidFragment);
        mAndroidDialog = (LinearLayout) findViewById(R.id.androidDialog);
        mFastFragment = (LinearLayout) findViewById(R.id.fastFragment);
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
    public void onClick(final View v) {

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

        ParcelableTime time = new ParcelableTime();
        final AlarmTimePickerDialogFragment fragment =
                AlarmTimePickerDialogFragment.newInstance(time);
        fragment.show(ft, "time_dialog");
    }

    private void showAndroidTimePickerDialog() {

        OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(final TimePicker view, final int hourOfDay,
                    final int minute) {

                mAndroidDialogTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mAndroidDialogTime.set(Calendar.MINUTE, minute);

                onTimeSelect(mAndroidDialog, mAndroidDialogTime);
            }
        };

        int hourOfDay = mAndroidDialogTime.get(Calendar.HOUR_OF_DAY);
        int minute = mAndroidDialogTime.get(Calendar.MINUTE);

        TimePickerDialog picker =
                new TimePickerDialog(FastTimePickerDemo.this, sTheme.getId(),
                        listener, hourOfDay, minute, true);
        picker.show();
    }

    private void onTimeSelect(LinearLayout layout, Calendar time) {
        TextView tv = ((TextView) layout.findViewById(android.R.id.text2));

        String timeString =
                DateFormat.getTimeFormat(this).format(time.getTime());
        tv.setText(timeString);
    }

    @Override
    public void onTimeSet(org.fasttimepicker.TimePicker view, int hourOfDay,
            int minute) {
        mFastDialogTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mFastDialogTime.set(Calendar.MINUTE, minute);

        onTimeSelect(mFastDialog, mFastDialogTime);
    }
}
