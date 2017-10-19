package mpower.org.elearning_module;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;

import mpower.org.elearning_module.adapter.ModuleGridViewAdapter;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.fragments.DashBoardFragment;
import mpower.org.elearning_module.model.Module;
import mpower.org.elearning_module.parser.CurriculumParser;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.UserCollection;
import mpower.org.elearning_module.utils.UserDataCollection;
import mpower.org.elearning_module.utils.UserType;
import mpower.org.elearning_module.utils.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private UserType userType;
    private DatabaseHelper databaseHelper;
    private ArrayList<Module> moduleArrayList;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..Please Wait");
        progressDialog.setCancelable(false);
        databaseHelper=new DatabaseHelper(this);
        databaseHelper.getWritableDatabase();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        userType= (UserType) getIntent().getSerializableExtra(AppConstants.USER_TYPE);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if (userType!=null){
            editor.putInt(AppConstants.USER_TYPE, userType.ordinal());
            editor.apply();
        }
        databaseHelper.saveInProgressTable(UserCollection.getInstance().getUserData().getUsername(),"1","1","1",userType);
        new JsonParserTask().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashBoardFragment()).commit();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   private class JsonParserTask extends AsyncTask<Void,Void,Void>{

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           progressDialog.show();
       }

       @Override
       protected Void doInBackground(Void... voids) {
           moduleArrayList= (ArrayList<Module>) CurriculumParser.returnCurriculum(Utils.readAssetContents("curriculum.json",MainActivity.this)).getModules();
           if (moduleArrayList!=null && moduleArrayList.size()>0){
               for (Module module:moduleArrayList){
                   databaseHelper.insertModule(module);
               }
           }
           return null;
       }

       @Override
       protected void onPostExecute(Void aVoid) {
           super.onPostExecute(aVoid);
           progressDialog.dismiss();

           callModuleFragment();
       }
   }

    private void callModuleFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashBoardFragment()).commit();
    }
}
