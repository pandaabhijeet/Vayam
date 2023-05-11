package com.example.vayam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vayam.png.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import p009es.dmoral.toasty.Toasty;

public class AccountActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
    private DatabaseReference databaseRef;
    /* access modifiers changed from: private */
    public ProgressDialog loadingBar;
    private Button logout_btn;
    /* access modifiers changed from: private */
    public FirebaseAuth mAuth;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_account);
        this.mAuth = FirebaseAuth.getInstance();
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
        this.currentUser = this.mAuth.getCurrentUser();
        initialiseFields();
        this.logout_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccountActivity.this.loadingBar.setMessage("Logging you out");
                AccountActivity.this.loadingBar.setCanceledOnTouchOutside(false);
                AccountActivity.this.loadingBar.show();
                AccountActivity.this.mAuth.signOut();
                AccountActivity.this.SendCurrentUserToLoginActivity();
            }
        });
    }

    private void initialiseFields() {
        this.logout_btn = (Button) findViewById(R.id.logout_btn);
        this.loadingBar = new ProgressDialog(this);
    }

    /* access modifiers changed from: private */
    public void SendCurrentUserToLoginActivity() {
        Intent back_login = new Intent(this, loginActivity.class);
        back_login.addFlags(268468224);
        startActivityForResult(back_login, 100);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == -1) {
            this.loadingBar.dismiss();
            Toasty.custom((Context) this, (CharSequence) "Logged Out Successfully", getResources().getDrawable(R.drawable.ic_checked), getResources().getColor(R.color.colorSuccess), getResources().getColor(R.color.colorAccent), 0, true, true).show();
        }
    }
}
