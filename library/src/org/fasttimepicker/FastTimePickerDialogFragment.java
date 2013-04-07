/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.fasttimepicker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Dialog to set alarm time.
 */
public class FastTimePickerDialogFragment extends DialogFragment {

    private static final String KEY_24_HOURS_MODE = "24_hours_mode";

    private Button mSet, mCancel;
    private FastTimePicker mPicker;

    private Boolean mIs24HoursMode = null;

    public static FastTimePickerDialogFragment newInstance() {
        return new FastTimePickerDialogFragment();
    }

    public static FastTimePickerDialogFragment newInstanceOverrideMode(
            Boolean is24HoursMode) {
        final FastTimePickerDialogFragment frag = newInstance();
        Bundle args = new Bundle();
        args.putSerializable(KEY_24_HOURS_MODE, is24HoursMode);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        if (mIs24HoursMode == null)
            mIs24HoursMode = (Boolean) getArguments().getSerializable(KEY_24_HOURS_MODE);
        if (mIs24HoursMode == null)
            mIs24HoursMode = DateFormat.is24HourFormat(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.time_picker_dialog, null);
        mSet = (Button) v.findViewById(R.id.set_button);
        mCancel = (Button) v.findViewById(R.id.cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mPicker = (FastTimePicker) v.findViewById(R.id.time_picker);
        mPicker.set24HoursMode(mIs24HoursMode);
        mPicker.setSetButton(mSet);
        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Activity activity = getActivity();
                if (activity instanceof OnTimeSetListener) {
                    final OnTimeSetListener act = (OnTimeSetListener) activity;
                    act.onTimeSet(mPicker, mPicker.getHours(),
                            mPicker.getMinutes());
                } else {
                    Log.e("Error! Activities that use FastTimePickerDialogFragment must implement "
                            + "OnTimeSetListener");
                }
                dismiss();
            }
        });

        return v;
    }

    /**
     * The callback interface used to indicate the user is done filling in the
     * time (they clicked on the 'Set' button).
     */
    public interface OnTimeSetListener {

        /**
         * @param view
         *            The view associated with this listener.
         * @param hourOfDay
         *            The hour that was set.
         * @param minute
         *            The minute that was set.
         */
        void onTimeSet(FastTimePicker view, int hourOfDay, int minute);
    }
}
