package com.pavel_nikiforov.android.vacancieschecker.ui;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pavel_nikiforov.android.vacanciescheckerforandroid.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = UpdateFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setClickable(true);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();

        Log.d("MAIN_MENU","Item selected in nav drawer: " + id);

        if(id == R.id.drawer_menu_item_new_empl){
            proceedTo("new_employers");
        } else if (id == R.id.drawer_menu_item_new_vacancies){
            proceedTo("new");
        } else if (id == R.id.drawer_menu_item_updated_vacancies){
            proceedTo("updated");
        } else if (id == R.id.drawer_menu_item_accepted_vacancies){
            proceedTo("accepted");
        } else if (id == R.id.drawer_menu_item_rejected_vacancies){
            proceedTo("rejected");
        } else if (id == R.id.drawer_menu_item_total_vacancies){
            proceedTo("total");
        } else if (id == R.id.drawer_menu_item_statistics){
            proceedTo("stats");
        } else if (id == R.id.drawer_menu_item_start){
            proceedTo("start");
        } else if (id == R.id.drawer_menu_item_settings){
            proceedTo("settings");
        } else if (id == R.id.drawer_menu_item_view_database){
            proceedTo("view-database");
        } else if (id == R.id.drawer_menu_item_recent_vacs){
            proceedTo("recent");
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void proceedTo(String destination){
        Bundle args = new Bundle();
        args.putString("type", destination);

        Fragment nextFrag;
        if(destination.contentEquals("new_employers")){
            nextFrag = new NewEmployersFragment();
        } else if(destination.contentEquals("stats")){
            nextFrag = new StatsFragment();
        } else if(destination.contentEquals("start")){
            nextFrag = new UpdateFragment();
        } else if(destination.contentEquals("settings")){
            nextFrag = new SettingsFragment();
        } else if(destination.contentEquals("view-database")){
            nextFrag = new EmployersListFragment();
        } else if(destination.contentEquals("recent")){
            nextFrag = new RecentVacanciesListFragment();
        } else {
            nextFrag = new VacanciesListFragment();
        }

        nextFrag.setArguments(args);
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }
}
