package com.example.vayam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vayam.png.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public FirebaseUser currentUser;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_splash_screen);
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (SplashScreenActivity.this.currentUser == null) {
                    SplashScreenActivity.this.SendCurrentUserToLoginActivity();
                } else {
                    SplashScreenActivity.this.sendCurrentUserToMainActivity();
                }
            }
        }, 500);
    }

    /* access modifiers changed from: private */
    public void SendCurrentUserToLoginActivity() {
        Intent back_login = new Intent(this, loginActivity.class);
        back_login.addFlags(268468224);
        startActivity(back_login);
        finish();
    }

    /* access modifiers changed from: private */
    public void sendCurrentUserToMainActivity() {
        Intent MainIntent = new Intent(this, MainActivity.class);
        MainIntent.addFlags(268468224);
        startActivity(MainIntent);
        finish();
    }
}
