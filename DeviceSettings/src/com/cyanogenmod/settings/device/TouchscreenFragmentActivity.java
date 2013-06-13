/*
 * Copyright (C) 2013 The Evervolv Project
 * Portions Copyright (C) 2012-2013 The CyanogenMod Project
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
 * limitations under the License.
 */

package com.cyanogenmod.settings.device;

import android.content.Context;
import android.content.res.Resources;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.TwoStatePreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.cyanogenmod.settings.device.R;

public class TouchscreenFragmentActivity extends PreferenceFragment {

    private static final String TAG = "DeviceSettings_Touchscreen";
    private static final String KEY_SWEEP_TO_WAKE = "pref_sweep_to_wake";

    private static final String SWEEP_2_WAKE_FILE = "/sys/android_touch/sweep2wake";
    
    private CheckBoxPreference mSweep2Wake;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        addPreferencesFromResource(R.xml.touchscreen_preferences);

        if (res.getBoolean(R.bool.has_sweep2wake)) {
            mSweep2Wake = (CheckBoxPreference) findPreference(KEY_SWEEP_TO_WAKE);
            mSweep2Wake.setEnabled(isSweep2WakeSupported());
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String boxValue;
        if (preference == mSweep2Wake) {
            boolean enabled = mSweep2Wake.isChecked();
            if(enabled) {
                Utils.writeValue(SWEEP_2_WAKE_FILE, "1\n");
            } else {
                Utils.writeValue(SWEEP_2_WAKE_FILE, "0\n");
            }
        }
        return true;
    }

    private static boolean isSweep2WakeSupported() {
        return Utils.fileExists(SWEEP_2_WAKE_FILE);
    }
    
    public static void restore(Context context) {
        if (!isSweep2WakeSupported()) { return; }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean enabled = sharedPrefs.getBoolean(KEY_SWEEP_TO_WAKE, false);
        if(enabled) {
            Utils.writeValue(SWEEP_2_WAKE_FILE, "1\n");
        } else {
            Utils.writeValue(SWEEP_2_WAKE_FILE, "0\n");
        }
    }
}
