package com.pacmac.imail;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;

public class StartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AsyncTaskCheckEmails.GetEmailsListener, Inbox.OnInboxListener {


    private Fragment fragment;
    private final String FRAG_COMPOSE = "compose";
    private final String FRAG_INBOX = "inbox";

    private String userName = null;
    private String password = null;
    private String port = null;
    private String server = null;
    private String type = null;

    private boolean isRefresh = false;

    private ProgressDialog progressDialog = null;

    private ArrayList<String> folderNames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //get Folders
        AccountManager accountManager = new AccountManager(getApplicationContext());
        accountManager.getProperties();
        userName = accountManager.getUserName();
        password = accountManager.getPassword();
        server = accountManager.getServer();
        port = accountManager.getPort();
        type = accountManager.getType();

        List<FolderRecord> listFolder = FolderRecord.find(FolderRecord.class, "user_name = ?", userName);
        for (FolderRecord folder : listFolder) {
            folderNames.add(folder.getFriendlyName());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView emailAddrNav = (TextView) navigationView.findViewById(R.id.emailAddressNavBar);
        emailAddrNav.setText(userName);

        ListView listDrawer = (ListView) navigationView.findViewById(R.id.navigation_drawer_list);
        FolderArrayAdapter folderAdapter = new FolderArrayAdapter(getApplicationContext(), folderNames);
        listDrawer.setAdapter(folderAdapter);


        //Menu m = navigationView.

/*
        Menu m = navigationView.getMenu();
        SubMenu topChannelMenu = m.addSubMenu("Folders");

        for (FolderRecord folder : listFolder) {
            topChannelMenu.addSubMenu(folder.getFriendlyName());
        }

        MenuItem mi = m.getItem(m.size() - 1);
        mi.setTitle(mi.getTitle());*/


        checkEmails();  // check emails on start


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
        getMenuInflater().inflate(R.menu.start, menu);
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
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        if (id == R.id.nav_compose) {
            fragment = NewEmail.newInstance("test1", "test2");
            fragmentTransaction.replace(R.id.mainFrame, fragment, FRAG_COMPOSE);
            fragmentTransaction.commit();

        } /*else if (id == R.id.nav_inbox) {
            fragment = new Inbox(); // should not be null
            fragmentTransaction.replace(R.id.mainFrame, fragment, FRAG_INBOX);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_sent) {

        } else if (id == R.id.nav_draft) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void checkEmails() {
        if (!isRefresh) {
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
        }
        AsyncTaskCheckEmails asyncTaskCheckEmails = new AsyncTaskCheckEmails();
        asyncTaskCheckEmails.mListener = StartActivity.this;
        asyncTaskCheckEmails.execute(userName, password, server, port, type);
    }

    @Override
    public void emailsLoaded(boolean isReady) {
        if (!isRefresh) {
            fragment = new Inbox();
            getFragmentManager().beginTransaction().replace(R.id.mainFrame, fragment, FRAG_INBOX).commit();
            progressDialog.hide();
        } else {
            ((Inbox) fragment).updateInboxList();
            isRefresh = false;
        }
    }

    @Override
    public void refreshInboxEmails() {
        isRefresh = true;
        checkEmails();
    }
}
