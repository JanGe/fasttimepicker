package org.fasttimepicker.demo;

import java.util.Calendar;
import java.util.Locale;

import org.fasttimepicker.FastTimePicker;
import org.fasttimepicker.FastTimePickerDialogFragment;
import org.fasttimepicker.ParcelableTime;
import org.fasttimepicker.demo.ViewSelectorFragment.TimePickers;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * Demo application for FastTimePicker
 * 
 * Demo application to showcase a numpad-style faster time picker in comparision
 * to the standard Android time picker
 * 
 * @author Jan Gerlinger
 * 
 */
public class FastTimePickerDemo extends FragmentActivity implements
        ViewSelectorFragment.OnThemeSelectedListener,
        ViewSelectorFragment.OnStartTimePickerListener,
        FastTimePickerDialogFragment.OnTimeSetListener {

    /**
     * Static to be preserved when this activity is restarted with another theme
     */
    private static Themes sTheme = Themes.DEFAULT;
    private static Boolean sIs24HoursMode = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        setTheme(sTheme.getId());
        if (sIs24HoursMode == null)
            sIs24HoursMode = DateFormat.is24HourFormat(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentlayout);
    }

    /**
     * Returns the currently selected theme that is used next time the activity
     * is created
     * 
     * @return the currently selected theme
     */
    public Themes getCurrentlySelectedTheme() {
        return sTheme;
    }

    /**
     * Sets the theme that is used the next time the activity is created
     * 
     * @param theme
     *            used the next time the activity is created
     */
    @Override
    public void onThemeSelected(Themes theme) {
        if (sTheme != theme) {
            sTheme = theme;
            restartActivity();
        }
    }

    private void restartActivity() {
        startActivity(new Intent(this, this.getClass()));
        finish();
    }

    @Override
    public void onStartTimePicker(TimePickers picker) {
        switch (picker) {
        case ANDROID_FRAGMENT:
            break;
        case ANDROID_DIALOG:
            showAndroidTimePickerDialog();
            break;
        case FAST_FRAGMENT:
            break;
        case FAST_DIALOG:
            showFastTimePickerDialog();
            break;
        }
    }

    private void showFastTimePickerDialog() {

        final FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("time_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        ParcelableTime time = new ParcelableTime();
        final FastTimePickerDialogFragment fragment =
                FastTimePickerDialogFragment.newInstance(time);
        fragment.show(ft, "time_dialog");
        fragment.set24HoursMode(sIs24HoursMode);
    }

    private void showAndroidTimePickerDialog() {

        final TimePickers picker = TimePickers.ANDROID_DIALOG;

        OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(final TimePicker view, final int hourOfDay,
                    final int minute) {

                picker.setTime(hourOfDay, minute);
            }
        };

        int hourOfDay = picker.getCalendar().get(Calendar.HOUR_OF_DAY);
        int minute = picker.getCalendar().get(Calendar.MINUTE);

        TimePickerDialog dialog =
                new TimePickerDialog(this, listener, hourOfDay, minute,
                        sIs24HoursMode);
        dialog.show();
    }

    @Override
    public void on24HourModeChanged(boolean is24HoursMode) {
        sIs24HoursMode = is24HoursMode;
    }

    @Override
    public void onTimeSet(FastTimePicker view, int hourOfDay, int minute) {
        TimePickers.FAST_DIALOG.setTime(hourOfDay, minute);
    }
}
