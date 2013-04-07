package org.fasttimepicker.demo;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A fragment that lists different options to start a fast time picker or a
 * standard Android one
 *
 * Parent activity must implement
 * {@link org.fasttimepicker.FastTimePickerDialogFragment.OnTimeSetListener}
 *
 * @author Jan Gerlinger
 *
 */
public class ViewSelectorFragment extends Fragment implements OnClickListener {

    /**
     * Declaration of the supported time pickers with the associated views that
     * start them
     */
    public enum TimePickers {
        ANDROID_FRAGMENT(R.id.androidFragment), ANDROID_DIALOG(
                R.id.androidDialog), FAST_FRAGMENT(R.id.fastFragment),
        FAST_DIALOG(R.id.fastDialog);

        private final static SparseArray<TimePickers> mInstanceForViewId =
                new SparseArray<TimePickers>(TimePickers.values().length);

        private final Calendar mTime = Calendar.getInstance();
        private int mViewId;
        private ViewSelectorFragment mListener = null;

        static {
            for (TimePickers picker : TimePickers.values())
                mInstanceForViewId.put(picker.getViewId(), picker);
        }

        private TimePickers(int viewId) {
            mViewId = viewId;
        }

        public Calendar getCalendar() {
            return mTime;
        }

        public void setTime(int hourOfDay, int minute) {
            mTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mTime.set(Calendar.MINUTE, minute);

            if (mListener != null)
                mListener.updateTimeShown(this);
        }

        public int getViewId() {
            return mViewId;
        }

        public static TimePickers getInstanceForViewId(int viewId) {
            return mInstanceForViewId.get(viewId);
        }

        public void setListener(ViewSelectorFragment listener) {
            mListener = listener;
        }

    }

    private static final String TIMEPICKER_PREFIX = "time_";;

    private View mView;
    private Spinner mThemeSpinner;

    OnThemeSelectedListener mThemeListener;
    OnStartTimePickerListener mStartTimePickerListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mThemeListener = (OnThemeSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnThemeSelectedListener");
        }
        try {
            mStartTimePickerListener = (OnStartTimePickerListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnStartTimePickerListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.viewselectorlist, container);
        mThemeSpinner = (Spinner) mView.findViewById(R.id.spinner);

        RelativeLayout twentyFourHourLayout =
                (RelativeLayout) mView
                        .findViewById(R.id.twentyfourhourcheckbox);
        update24HourCheckBox(twentyFourHourLayout,
                mThemeListener.getCurrentlySelected24HourMode());
        twentyFourHourLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox checkBox =
                        (CheckBox) v.findViewById(android.R.id.checkbox);
                update24HourCheckBox(v, !checkBox.isChecked());

                for (TimePickers picker : TimePickers.values())
                    updateTimeShown(picker);
            }

        });

        final FragmentActivity parentActivity = getActivity();
        ArrayAdapter<Themes> adapter =
                new ArrayAdapter<Themes>(parentActivity,
                        android.R.layout.simple_spinner_item, Themes.values()) {
                };
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mThemeSpinner.setAdapter(adapter);
        mThemeSpinner.setSelection(mThemeListener.getCurrentlySelectedTheme()
                .getPosition());

        mThemeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                mThemeListener.onThemeSelected(Themes.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mThemeListener.onThemeSelected(Themes.values()[0]);
            }

        });

        if (savedInstanceState != null) {
            for (TimePickers picker : TimePickers.values()) {
                picker.getCalendar().setTimeInMillis(
                        savedInstanceState.getLong(TIMEPICKER_PREFIX
                                + picker.toString()));
            }
        }

        for (TimePickers picker : TimePickers.values()) {
            LinearLayout layout =
                    (LinearLayout) mView.findViewById(picker.getViewId());
            layout.setOnClickListener(this);

            picker.setListener(this);

            updateTimeShown(picker);
        }

        return mView;
    }

    private void update24HourCheckBox(View layout, boolean is24HourMode) {
        CheckBox checkBox =
                (CheckBox) layout.findViewById(android.R.id.checkbox);
        checkBox.setChecked(is24HourMode);

        TextView tv = (TextView) layout.findViewById(android.R.id.text2);
        if (is24HourMode) {
            tv.setText(R.string.use_24_hour_format_yes);
        } else {
            tv.setText(R.string.use_24_hour_format_no);
        }

        mThemeListener.on24HourModeChanged(is24HourMode);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        for (TimePickers picker : TimePickers.values()) {
            savedInstanceState.putLong(TIMEPICKER_PREFIX + picker.toString(),
                    picker.getCalendar().getTimeInMillis());
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(final View v) {

        TimePickers picker = TimePickers.getInstanceForViewId(v.getId());
        if (picker != null)
            mStartTimePickerListener.onStartTimePicker(picker);
    }

    private void updateTimeShown(TimePickers picker) {
        LinearLayout layout =
                (LinearLayout) mView.findViewById(picker.getViewId());
        TextView tv = ((TextView) layout.findViewById(android.R.id.text2));

        String timeString =
                getTimeFormat(getActivity(),
                        mThemeListener.getCurrentlySelected24HourMode())
                        .format(picker.getCalendar().getTime());
        tv.setText(timeString);
    }

    public interface OnThemeSelectedListener {

        /**
         * Gets called when a new theme is selected
         *
         * Implementers have to change the fragment theme to the selected one
         *
         * @param theme
         *            selected for displaying this fragment
         */
        public void onThemeSelected(Themes theme);

        /**
         * Has to return the currently selected theme this fragment is displayed
         * in out of the pre-specified list
         *
         * @return the currently selected theme
         */
        public Themes getCurrentlySelectedTheme();

        /**
         * Gets called when the 24 hour mode is selected or unselected
         */
        public void on24HourModeChanged(boolean is24HourMode);

        /**
         * Has to return the currently selected 24 hour mode
         *
         * @return the currently selected theme
         */
        public boolean getCurrentlySelected24HourMode();
    }

    public interface OnStartTimePickerListener {
        public void onStartTimePicker(TimePickers picker);
    }

    /**
     * Returns a {@link java.text.DateFormat} object that can format the time
     * according to the current locale and the user's 12-/24-hour clock
     * preference.
     *
     * @param context
     *            the application context
     * @return the {@link java.text.DateFormat} object that properly formats the
     *         time.
     */
    public static java.text.DateFormat getTimeFormat(Context context,
            boolean is24HoursMode) {
        int res;

        if (is24HoursMode) {
            res = R.string.twenty_four_hour_time_format;
        } else {
            res = R.string.twelve_hour_time_format;
        }

        return new java.text.SimpleDateFormat(context.getString(res));
    }

}
