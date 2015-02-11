package com.stackbase.mobapp.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;

import com.stackbase.mobapp.R;
import com.stackbase.mobapp.ocr.OCRActivity;
import com.stackbase.mobapp.utils.Constant;

public class PreferencesActivity extends PreferenceActivity implements
        OnSharedPreferenceChangeListener {

    private ListPreference listPreferenceOcrEngineMode;
    private ListPreference listPreferencePageSegmentationMode;

    private CheckBoxPreference autoFocus;
    private CheckBoxPreference beep;
    private ListPreference flashLight;

    /**
     * Set the default preference values.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.camera_preferences);
        initPreferences();

    }

    private void initPreferences() {
        String preferType = getIntent().getStringExtra(Constant.INTENT_KEY_PREFERENCES_TYPE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        autoFocus = (CheckBoxPreference) getPreferenceScreen().findPreference(Constant.KEY_AUTO_FOCUS);
        beep = (CheckBoxPreference) getPreferenceScreen().findPreference(Constant.KEY_PLAY_BEEP);
        flashLight = (ListPreference) getPreferenceScreen().findPreference(Constant.KEY_TOGGLE_LIGHT);

        onSharedPreferenceChanged(sharedPreferences, Constant.KEY_AUTO_FOCUS);
        onSharedPreferenceChanged(sharedPreferences, Constant.KEY_PLAY_BEEP);
        onSharedPreferenceChanged(sharedPreferences, Constant.KEY_TOGGLE_LIGHT);

        if (OCRActivity.class.getSimpleName().equals(preferType)) {
            listPreferenceOcrEngineMode = (ListPreference) getPreferenceScreen().findPreference(Constant.KEY_OCR_ENGINE_MODE);
            onSharedPreferenceChanged(sharedPreferences, Constant.KEY_OCR_ENGINE_MODE);
        } else {
            // hidden the OCR settings
            PreferenceGroup ocrGroup = (PreferenceGroup) getPreferenceScreen().findPreference(Constant.KEY_PREFERENCE_CATEGORY_OCR);
            getPreferenceScreen().removePreference(ocrGroup);
        }

    }
    /**
     * Interface definition for a callback to be invoked when a shared
     * preference is changed. Sets summary text for the app's preferences. Summary text values show the
     * current settings for the values.
     *
     * @param sharedPreferences the Android.content.SharedPreferences that received the change
     * @param key               the key of the preference that was changed, added, or removed
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        // Update preference summary values to show current preferences
        switch (key) {
            case Constant.KEY_AUTO_FOCUS:
                autoFocus.setChecked(sharedPreferences.getBoolean(key, Constant.DEFAULT_TOGGLE_AUTO_FOCUS));
                break;
            case Constant.KEY_PLAY_BEEP:
                beep.setChecked(sharedPreferences.getBoolean(key, Constant.DEFAULT_TOGGLE_BEEP));
                break;
            case Constant.KEY_TOGGLE_LIGHT:
                flashLight.setSummary(flashLight.getEntry());
                break;
            case Constant.KEY_OCR_ENGINE_MODE:
                listPreferenceOcrEngineMode.setSummary(sharedPreferences.getString(key, Constant.DEFAULT_OCR_ENGINE_MODE));

        }
    }

    /**
     * Sets the list of available languages and the current target language for translation. Called
     * when the key for the current translator is changed.
     */
//  void initTranslationTargetList() {
//    // Set the preference for the target language code, in case we've just switched from Google
//    // to Bing, or Bing to Google.
//    String currentLanguageCode = sharedPreferences.getString(KEY_TARGET_LANGUAGE_PREFERENCE,
//        CaptureActivity.DEFAULT_TARGET_LANGUAGE_CODE);
//
//    // Get the name of our language
//    String currentLanguage = LanguageCodeHelper.getTranslationLanguageName(getBaseContext(),
//        currentLanguageCode);
//    String[] translators = getResources().getStringArray(R.array.translators);
//    String translator = sharedPreferences.getString(KEY_TRANSLATOR, CaptureActivity.DEFAULT_TRANSLATOR);
//    String newLanguageCode = "";
//    if (translator.equals(translators[0])) { // Bing
//      // Update the list of available languages for the currently-chosen translation API.
//      listPreferenceTargetLanguage.setEntries(R.array.translationtargetlanguagenames_microsoft);
//      listPreferenceTargetLanguage.setEntryValues(R.array.translationtargetiso6391_microsoft);
//
//      // Get the corresponding code for our language name
//      newLanguageCode = TranslatorBing.toLanguage(currentLanguage);
//    } else if (translator.equals(translators[1])) { // Google
//      // Update the list of available languages for the currently-chosen translation API.
//      listPreferenceTargetLanguage.setEntries(R.array.translationtargetlanguagenames_google);
//      listPreferenceTargetLanguage.setEntryValues(R.array.translationtargetiso6391_google);
//
//      // Get the corresponding code for our language name
//      newLanguageCode = TranslatorGoogle.toLanguage(currentLanguage);
//    }
//
//    // Store the code as the target language preference
//    String newLanguageName = LanguageCodeHelper.getTranslationLanguageName(getBaseContext(),
//        newLanguageCode);
//    listPreferenceTargetLanguage.setValue(newLanguageName); // Set the radio button in the list
//    sharedPreferences.edit().putString(PreferencesActivity.KEY_TARGET_LANGUAGE_PREFERENCE,
//        newLanguageCode).commit();
//    listPreferenceTargetLanguage.setSummary(newLanguageName);
//  }

    /**
     * Sets up initial preference summary text
     * values and registers the OnSharedPreferenceChangeListener.
     */
    @Override
    protected void onResume() {
        super.onResume();
//        initPreferences();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Called when Activity is about to lose focus. Unregisters the
     * OnSharedPreferenceChangeListener.
     */
    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}