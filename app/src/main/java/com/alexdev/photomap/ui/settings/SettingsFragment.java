package com.alexdev.photomap.ui.settings;


import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.widget.Toast;

import com.alexdev.photomap.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {

    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        ListPreference markersTypeChooser = (ListPreference) findPreference(
                getString(R.string.pref_key_markers_type)
        );
        markersTypeChooser.setOnPreferenceClickListener(p -> showToastAvailableSoon());
        SwitchPreferenceCompat friendsPhotoShowingSwitcher = (SwitchPreferenceCompat) findPreference(
                getString(R.string.pref_key_friends_photo)
        );
        friendsPhotoShowingSwitcher.setOnPreferenceClickListener(p -> showToastAvailableSoon());
        Preference vkSignInButton = findPreference(getString(R.string.pref_key_vk_sign_in));
        vkSignInButton.setOnPreferenceClickListener(p -> showToastAvailableSoon());
    }

    private boolean showToastAvailableSoon() {
        Toast.makeText(getContext(), getString(R.string.toast_available_soon), Toast.LENGTH_SHORT).show();
        return false;
    }
}
