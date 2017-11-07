package com.alexdev.photomap.ui.settings;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.alexdev.photomap.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
}
