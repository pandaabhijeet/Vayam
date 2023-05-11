package com.example.vayam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vayam.png.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import java.util.concurrent.TimeUnit;
import p009es.dmoral.toasty.Toasty;

public class verificationActivity extends AppCompatActivity {
    private CountryCodePicker ccp;
    /* access modifiers changed from: private */
    public String codeId;
    /* access modifiers changed from: private */
    public String fullPhoneNo;
    /* access modifiers changed from: private */
    public ProgressBar loadingBar;
    /* access modifiers changed from: private */
    public Button loginButton;
    private FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            verificationActivity.this.verificationInfo.setText(verificationActivity.this.fullPhoneNo + " was verified successfully");
            verificationActivity.this.verificationCode.setText(code);
            verificationActivity.this.loginButton.setText("LOGIN");
            verificationActivity verificationactivity = verificationActivity.this;
            Toasty.custom((Context) verificationactivity, (CharSequence) "Verified Successfully.", verificationactivity.getResources().getDrawable(R.drawable.ic_verified), verificationActivity.this.getResources().getColor(R.color.colorSuccess), verificationActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
            verificationActivity.this.progressDialog.dismiss();
        }

        public void onVerificationFailed(FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                verificationActivity.this.progressDialog.dismiss();
                Toasty.custom((Context) verificationActivity.this, (CharSequence) e.getMessage(), verificationActivity.this.getResources().getDrawable(R.drawable.ic_error), verificationActivity.this.getResources().getColor(R.color.colorError), verificationActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                verificationActivity.this.progressDialog.dismiss();
                Toasty.custom((Context) verificationActivity.this, (CharSequence) e.getMessage(), verificationActivity.this.getResources().getDrawable(R.drawable.ic_error), verificationActivity.this.getResources().getColor(R.color.colorError), verificationActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
            }
        }
    };
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    /* access modifiers changed from: private */
    public EditText verificationCode;
    /* access modifiers changed from: private */
    public TextView verificationInfo;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_verification);
        initialiseFields();
        this.mAuth = FirebaseAuth.getInstance();
        this.fullPhoneNo = getIntent().getStringExtra("phoneNo");
        this.codeId = getIntent().getStringExtra("codeId");
        this.progressDialog.show();
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.progressDialog.setMessage("Auto-verifying. Please wait");
        autoVerify();
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (verificationActivity.this.verificationCode.getText().toString().isEmpty() || verificationActivity.this.verificationCode.getText().toString().length() != 6) {
                    verificationActivity.this.verificationCode.setError("Invalid Code");
                    verificationActivity verificationactivity = verificationActivity.this;
                    Toasty.custom((Context) verificationactivity, (CharSequence) "Oops! Seems you entered an invalid code.", verificationactivity.getResources().getDrawable(R.drawable.ic_redo), verificationActivity.this.getResources().getColor(R.color.colorError), verificationActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
                    return;
                }
                verificationActivity.this.loadingBar.setVisibility(0);
                verificationActivity.this.signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationActivity.this.codeId, verificationActivity.this.verificationCode.getText().toString()));
            }
        });
    }

    private void autoVerify() {
        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions.newBuilder(this.mAuth).setPhoneNumber(this.fullPhoneNo).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(this.mCallbacks).build());
    }

    private void initialiseFields() {
        this.loginButton = (Button) findViewById(R.id.login_btn);
        this.verificationCode = (EditText) findViewById(R.id.enter_otp);
        this.verificationInfo = (TextView) findViewById(R.id.verify_inst);
        this.loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        this.progressDialog = new ProgressDialog(this, R.style.progressDialogue);
    }

    /* access modifiers changed from: private */
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        this.mAuth.signInWithCredential(credential).addOnCompleteListener((Activity) this, new OnCompleteListener<AuthResult>() {
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    verificationActivity.this.loadingBar.setVisibility(8);
                    verificationActivity.this.SendCurrentUserToMainActivity();
                    verificationActivity verificationactivity = verificationActivity.this;
                    Toasty.custom((Context) verificationactivity, (CharSequence) "Logged in successfully", verificationactivity.getResources().getDrawable(R.drawable.ic_checked), verificationActivity.this.getResources().getColor(R.color.colorSuccess), verificationActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
                    return;
                }
                verificationActivity.this.loadingBar.setVisibility(8);
                String errorMessage = task.getException().toString();
                verificationActivity verificationactivity2 = verificationActivity.this;
                Toasty.custom((Context) verificationactivity2, (CharSequence) errorMessage, verificationactivity2.getResources().getDrawable(R.drawable.ic_error), verificationActivity.this.getResources().getColor(R.color.colorError), verificationActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void SendCurrentUserToMainActivity() {
        Intent loginIntent = new Intent(this, MainActivity.class);
        loginIntent.addFlags(268468224);
        startActivity(loginIntent);
        finish();
    }
}
