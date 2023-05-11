package com.example.vayam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.example.vayam.png.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import p009es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
    private DatabaseReference databaseRef;
    /* access modifiers changed from: private */
    public FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private TabLayout myTabLayout;
    private TabAccessorAdapter myTabsAccessorAdapter;
    private ViewPager myViewPager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        this.mAuth = FirebaseAuth.getInstance();
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
        this.currentUser = this.mAuth.getCurrentUser();
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        this.mToolbar = toolbar;
        setSupportActionBar(toolbar);
        this.myViewPager = (ViewPager) findViewById(R.id.main_tab_pager);
        TabAccessorAdapter tabAccessorAdapter = new TabAccessorAdapter(getSupportFragmentManager());
        this.myTabsAccessorAdapter = tabAccessorAdapter;
        this.myViewPager.setAdapter(tabAccessorAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab);
        this.myTabLayout = tabLayout;
        tabLayout.setupWithViewPager(this.myViewPager);
        Toasty.custom((Context) this, (CharSequence) "Welcome " + this.mAuth.getCurrentUser().getDisplayName(), getResources().getDrawable(R.drawable.ic_smiley), getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent), 0, true, true).show();
        getPermission();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        verifyUserExistence();
    }

    private void verifyUserExistence() {
        this.databaseRef.child("Users").child(this.mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child("UserName").exists()) {
                    MainActivity.this.mAuth.getCurrentUser().getDisplayName();
                    return;
                }
                MainActivity mainActivity = MainActivity.this;
                Toasty.custom((Context) mainActivity, (CharSequence) "Please update your User name first !", mainActivity.getResources().getDrawable(R.drawable.ic_account_focused), MainActivity.this.getResources().getColor(R.color.colorPrimary), MainActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
                Intent initial_settings_Intent = new Intent(MainActivity.this, SettingsActivity.class);
                initial_settings_Intent.addFlags(268468224);
                MainActivity.this.startActivity(initial_settings_Intent);
                MainActivity.this.finish();
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.options_settings) {
            sendUserToSettingsActivity();
        }
        item.getItemId();
        if (item.getItemId() == R.id.options_Find_Friends) {
            sendUserToFindFriendsActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendUserToFindFriendsActivity() {
        startActivity(new Intent(this, FindFriendsActivity.class));
    }

    private void SendCurrentUserToLoginActivity() {
        Intent back_login = new Intent(this, loginActivity.class);
        back_login.addFlags(268468224);
        startActivity(back_login);
        finish();
    }

    private void sendUserToSettingsActivity() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void getPermission() {
        requestPermissions(new String[]{"android.permission.WRITE_CONTACTS", "android.permission.READ_CONTACTS"}, 1);
    }
}
