package com.stackbase.mobapp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;

import com.stackbase.mobapp.activity.PreferencesActivity;
import com.stackbase.mobapp.objects.Borrower;
import com.stackbase.mobapp.utils.Constant;
import com.stackbase.mobapp.utils.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CollectActivity extends FragmentActivity implements ActionBar.TabListener {
    List<Fragment> frags;
    Fragment idCard, otherInfo;
    ActionBar actionBar;
    ViewPager vp;
    private SharedPreferences prefs;
    private String TAG = CollectActivity.class.getSimpleName();

    FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return frags.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return frags.size();
        }
    };
    private EditText nameEdit;
    private EditText idEdit;
    private RadioButton maleButton;
    private EditText minzuEdit;
    private EditText dobEdit;
    private EditText addrEdit;
    private EditText locationEdit;
    private EditText expiryFromEdit;
    private EditText expiryToEdit;

    private boolean isValidInputs = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect);

        initView();

        // ActionBar is initiated
        actionBar = getActionBar();

        // Declaration a ViewPager
        vp = (ViewPager) findViewById(R.id.collect);
        //vp.setOffscreenPageLimit(2);

        // Tell the ActionBar we want to use Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab idcardTab = actionBar.newTab().setText("身份证");
        ActionBar.Tab otherTab = actionBar.newTab().setText("其他信息");

        // set the Tab listener. Now we can listen for clicks
        idcardTab.setTabListener(this);
        otherTab.setTabListener(this);

        // add the two tabs to the action bar
        actionBar.addTab(idcardTab);
        actionBar.addTab(otherTab);

        // Set a adapter for ViewPager
        vp.setAdapter(pagerAdapter);

        vp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    // if move to other info page, need input the id and name first
                    isValidInputs = validateIdCardInputs();
                    if (!isValidInputs) {
                        position = 0;
                        vp.setCurrentItem(position);
                    }
                }
                actionBar.setSelectedNavigationItem(position);
            }
        });
    }

    private void initView() {
        frags = new ArrayList<Fragment>();

        // create the 2 fragments to display content
        idCard = new FragmentIDCard();
        otherInfo = new FragmentOtherInfo();

        frags.add(idCard);
        frags.add(otherInfo);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        if (!isValidInputs) {
            vp.setCurrentItem(0);
            actionBar.setSelectedNavigationItem(0);
        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
    }

    private boolean validateIdCardInputs() {
        if (idCard != null && idCard.getView() != null){
            if (idEdit == null) {
                idEdit = (EditText) idCard.getView().findViewById(R.id.idEdit);
            }
            if (nameEdit == null) {
                nameEdit = (EditText) idCard.getView().findViewById(R.id.nameEdit);
            }
            if (maleButton == null) {
                maleButton = (RadioButton) idCard.getView().findViewById(R.id.maleButton);
            }
            if (minzuEdit == null) {
                minzuEdit = (EditText) idCard.getView().findViewById(R.id.minzuEdit);
            }
            if (dobEdit == null) {
                dobEdit = (EditText) idCard.getView().findViewById(R.id.dobEdit);
            }
            if (addrEdit == null) {
                addrEdit = (EditText) idCard.getView().findViewById(R.id.addrEdit);
            }
            if (locationEdit == null) {
                locationEdit = (EditText) idCard.getView().findViewById(R.id.locationEdit);
            }
            if (expiryFromEdit == null) {
                expiryFromEdit = (EditText) idCard.getView().findViewById(R.id.expiryFromEdit);
            }
            if (expiryToEdit == null) {
                expiryToEdit = (EditText) idCard.getView().findViewById(R.id.expiryToEdit);
            }

            String id = "";
            String name = "";
            String sex = "";
            String nation = "";
            Date dob = null;
            String address = "";
            String location = "";
            Date expiryFrom = null;
            Date expiryTo = null;
            if (idEdit != null) {
                id = idEdit.getText().toString();
            }
            if (nameEdit != null) {
                name = nameEdit.getText().toString();
            }
            if (maleButton != null && !maleButton.isSelected()) {
                sex = "男";
            } else {
                sex = "女";
            }
            if (minzuEdit != null) {
                nation = minzuEdit.getText().toString();
            }
            if (dobEdit != null) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    dob = dateFormat.parse(dobEdit.getText().toString());
                } catch (ParseException pe) {
                    Log.d(TAG, "unexpected date format: " + dobEdit.getText().toString()
                            + " , should be 'yyyy/MM/dd'");
                    //TODO: need should error message here
                }
            }
            if (addrEdit != null) {
                address = addrEdit.getText().toString();
            }
            if (locationEdit != null) {
                location = locationEdit.getText().toString();
            }
            if (expiryFromEdit != null) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    expiryFrom = dateFormat.parse(expiryFromEdit.getText().toString());
                } catch (ParseException pe) {
                    Log.d(TAG, "unexpected date format: " + expiryFromEdit.getText().toString()
                            + " , should be 'yyyy/MM/dd'");
                    //TODO: need should error message here
                }
            }
            if (expiryToEdit != null) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    expiryTo = dateFormat.parse(expiryToEdit.getText().toString());
                } catch (ParseException pe) {
                    Log.d(TAG, "unexpected date format: " + expiryToEdit.getText().toString()
                            + " , should be 'yyyy/MM/dd'");
                    //TODO: need should error message here
                }
            }


            if (id.equals("") || name.equals("")) {
                DialogInterface.OnClickListener alertListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (nameEdit != null) {
                            nameEdit.requestFocus();
                        }
                    }
                };
                Helper.showErrorMessage(CollectActivity.this, "错误", "必须输入姓名和身份证号码.",
                        null, alertListener);
                return false;
            } else {
                Borrower borrower = new Borrower();
                borrower.setId(id);
                borrower.setName(name);
                borrower.setSex(sex);
                borrower.setNation(nation);
                borrower.setBirthday(dob);
                borrower.setAddress(address);
                borrower.setLocation(location);
                borrower.setExpiryFrom(expiryFrom);
                borrower.setExpiryTo(expiryTo);
                getIntent().putExtra(Constant.INTENT_KEY_ID, id);
                getIntent().putExtra(Constant.INTENT_KEY_NAME, name);
                saveIDInfo(borrower);
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean saveIDInfo(Borrower borrower) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String rootDir = prefs.getString(PreferencesActivity.KEY_STORAGE_DIR,
                Constant.DEFAULT_STORAGE_DIR);
        return Helper.saveBorrower(borrower, rootDir);
    }
}
