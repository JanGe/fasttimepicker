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

package org.fasttimepicker.timer;

import org.fasttimepicker.R;
import org.fasttimepicker.ZeroTopPaddingTextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimerView extends LinearLayout {

    private ZeroTopPaddingTextView mHoursOnes, mMinutesOnes;
    private ZeroTopPaddingTextView mHoursTens, mMinutesTens;
    private TextView mSeconds;
    private final Typeface mAndroidClockMonoThin;
    private Typeface mOriginalHoursTypeface;
    private final int mPrimaryColor, mTertiaryColor;

    public TimerView(Context context) {
        this(context, null);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            mAndroidClockMonoThin =
                    Typeface.createFromAsset(context.getAssets(),
                            "fonts/AndroidClockMono-Thin.ttf");
        } else {
            mAndroidClockMonoThin = Typeface.SANS_SERIF;
        }
        
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValue , true);
        mPrimaryColor = getResources().getColor(typedValue.resourceId);
        
        typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.textColorTertiary, typedValue , true);
        mTertiaryColor = getResources().getColor(typedValue.resourceId);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mHoursTens = (ZeroTopPaddingTextView) findViewById(R.id.hours_tens);
        mMinutesTens = (ZeroTopPaddingTextView) findViewById(R.id.minutes_tens);
        mHoursOnes = (ZeroTopPaddingTextView) findViewById(R.id.hours_ones);
        mMinutesOnes = (ZeroTopPaddingTextView) findViewById(R.id.minutes_ones);
        mSeconds = (TextView) findViewById(R.id.seconds);
        if (mHoursOnes != null) {
            mOriginalHoursTypeface = mHoursOnes.getTypeface();
        }
        // Set the lowest time unit with thin font (excluding hundredths)
        if (mSeconds != null) {
            mSeconds.setTypeface(mAndroidClockMonoThin);
        } else {
            if (mMinutesTens != null) {
                setTypeface(mMinutesTens);
                mMinutesTens.updatePadding();
            }
            if (mMinutesOnes != null) {
                mMinutesOnes.setTypeface(mAndroidClockMonoThin);
                mMinutesOnes.updatePadding();
            }
        }
    }

    private void setTypeface(ZeroTopPaddingTextView mMinutesTens) {
        mMinutesTens.setTypeface(mAndroidClockMonoThin);
    }

    public void setTime(int hoursTensDigit, int hoursOnesDigit,
            int minutesTensDigit, int minutesOnesDigit, int seconds) {
        if (mHoursTens != null) {
            // Hide digit
            if (hoursTensDigit == -2) {
                mHoursTens.setVisibility(View.INVISIBLE);
            } else if (hoursTensDigit == -1) {
                mHoursTens.setText("-");
                mHoursTens.setTypeface(mAndroidClockMonoThin);
                mHoursTens.setTextColor(mTertiaryColor);
                mHoursTens.updatePadding();
                mHoursTens.setVisibility(View.VISIBLE);
            } else {
                mHoursTens.setText(String.format("%d", hoursTensDigit));
                mHoursTens.setTypeface(mOriginalHoursTypeface);
                mHoursTens.setTextColor(mPrimaryColor);
                mHoursTens.updatePadding();
                mHoursTens.setVisibility(View.VISIBLE);
            }
        }
        if (mHoursOnes != null) {
            if (hoursOnesDigit == -1) {
                mHoursOnes.setText("-");
                mHoursOnes.setTypeface(mAndroidClockMonoThin);
                mHoursOnes.setTextColor(mTertiaryColor);
                mHoursOnes.updatePadding();
            } else {
                mHoursOnes.setText(String.format("%d", hoursOnesDigit));
                mHoursOnes.setTypeface(mOriginalHoursTypeface);
                mHoursOnes.setTextColor(mPrimaryColor);
                mHoursOnes.updatePadding();
            }
        }
        if (mMinutesTens != null) {
            if (minutesTensDigit == -1) {
                mMinutesTens.setText("-");
                mMinutesTens.setTextColor(mTertiaryColor);
            } else {
                mMinutesTens.setTextColor(mPrimaryColor);
                mMinutesTens.setText(String.format("%d", minutesTensDigit));
            }
        }
        if (mMinutesOnes != null) {
            if (minutesOnesDigit == -1) {
                mMinutesOnes.setText("-");
                mMinutesOnes.setTextColor(mTertiaryColor);
            } else {
                mMinutesOnes.setText(String.format("%d", minutesOnesDigit));
                mMinutesOnes.setTextColor(mPrimaryColor);
            }
        }

        if (mSeconds != null) {
            mSeconds.setText(String.format("%02d", seconds));
        }
    }
}
