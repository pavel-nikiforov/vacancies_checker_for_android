package com.pavel_nikiforov.android.vacancieschecker.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.pavel_nikiforov.android.vacancieschecker.logic.Preferences;
import com.pavel_nikiforov.android.vacancieschecker.logic.VacancyChecker;
import com.pavel_nikiforov.android.vacanciescheckerforandroid.R;

import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SettingsFragment extends Fragment {
//    private String vacancy_list_url = "https://spb.hh.ru/search/vacancy?text=&specialization=1.117&area=2&salary=&currency_code=RUR&experience=doesNotMatter&order_by=publication_time&search_period=1&items_on_page=50&no_magic=true";
//    private String accepted_keywords = "QA,test,Test,quality,Quality,тест,Тест,качеств";
    private VacancyChecker appInstance;
    private List<String> mKeywordsList;
    private String mStartURL;

    private EditText mEditURLedit;
    private Button mTestInBrowserBtn;
    private Button mSaveSettingsBtn;
    private ChipGroup mChipGroup;

    private AppCompatEditText mEditChip;


    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setHasOptionsMenu(true);

        appInstance = VacancyChecker.getInstance();
        mStartURL = appInstance.getStartURL();
        mKeywordsList = appInstance.getKeywordsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mEditURLedit = (EditText) view.findViewById(R.id.settings_url_edittext);
        mEditURLedit.setText(mStartURL);

        mTestInBrowserBtn = (Button) view.findViewById(R.id.settings_test_in_browser_btn);
        mTestInBrowserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mEditURLedit.getText().toString()));
                startActivity(browserIntent);
            }
        });

        mChipGroup = (ChipGroup) view.findViewById(R.id.settings_chipgroup);


        mEditChip = (AppCompatEditText) view.findViewById(R.id.settings_chip_edit);
        mEditChip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    String text = v.getText().toString();
                    if(!text.isEmpty()){
                        mKeywordsList.add(text);
                        Log.d("SETTINGS", "--- Item added: " + mKeywordsList.toString());
                        addChip(text);
                        mEditChip.setText("");
                    }
                }
                return false;
            }
        });


        populateChipGroup();


        mSaveSettingsBtn = (Button) view.findViewById(R.id.settings_save_button);
        mSaveSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VacancyChecker app = VacancyChecker.getInstance();
                app.setStartURL(mEditURLedit.getText().toString());
                app.setKeywordsList(mKeywordsList);

                Toast.makeText(getActivity(), R.string.settings_saved_toast, Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }



    private void addChip(String chipText){
        final Chip chip = new Chip(getContext());

        chip.setText(chipText);
        chip.setCloseIconVisible(true);
        chip.setChipIconResource(android.R.drawable.ic_menu_preferences);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKeywordsList.remove(chip.getText().toString());
                Log.d("SETTINGS", "--- Item removed: " + mKeywordsList.toString());
                mChipGroup.removeView(view);
            }
        });

        mChipGroup.addView(chip);
    }


    private void populateChipGroup() {
//        mKeywordsList = new ArrayList<String>(Arrays.asList(accepted_keywords.split(",")));

        for (String keyword : mKeywordsList) {
            addChip(keyword);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_settings_fragment, menu);

        MenuItem resetItem = menu.findItem(R.id.menu_item_reset);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_reset:
                appInstance.resetSettings();
                refreshValues();
                Toast.makeText(getActivity(), "Resetted to defaults", Toast.LENGTH_LONG).show();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void refreshValues(){
        mStartURL = appInstance.getStartURL();
        mKeywordsList = appInstance.getKeywordsList();

        mEditURLedit.setText(mStartURL);
        mChipGroup.removeAllViews();
        populateChipGroup();

    }

}
