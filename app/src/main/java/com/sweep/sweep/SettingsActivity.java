package com.sweep.sweep;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;


/**
 * Setting
 */
public class SettingsActivity extends AppCompatActivity
{
    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "SettingsActivity";

    private static String DIR_PATH_DOWNLOAD;

    SettingsFragment mSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mSettingFragment = new SettingsFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, mSettingFragment).commit();

        DIR_PATH_DOWNLOAD = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_DIRECTORY) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                mSettingFragment.handleDirectoryChoice(data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR));
            } else {
                // Nothing selected
            }
        }
    }

    private static final int REQUEST_DIRECTORY = 0;

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener
    {
        Preference prefTargetFolder;
        PreferenceCategory prefCatCabinet;

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            //
            prefTargetFolder = findPreference("setting_target_folder");
            prefTargetFolder.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    final Intent chooserIntent = new Intent(getActivity(), DirectoryChooserActivity.class);

                    final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                            .newDirectoryName("New Directory")
                            .allowReadOnlyDirectory(true)
                            .allowNewDirectoryNameModification(true)
                            .initialDirectory(DIR_PATH_DOWNLOAD)
                            .build();

                    chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);

                    // REQUEST_DIRECTORY is a constant integer to identify the request, e.g. 0
                    startActivityForResult(chooserIntent, REQUEST_DIRECTORY);
                    return false;
                }
            });
            prefTargetFolder.setOnPreferenceChangeListener(this);

            //
            prefCatCabinet = (PreferenceCategory)findPreference("setting_cat_cabinet_folder");
        }

        public void handleDirectoryChoice(String result_dir)
        {

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue)
        {
            return true;
        }
    }
}
