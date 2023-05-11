package com.example.vayam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vayam.png.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import java.util.concurrent.TimeUnit;
import p009es.dmoral.toasty.Toasty;

public class loginActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public CountryCodePicker ccp;
    /* access modifiers changed from: private */
    public String codeId;
    private TextView createAccountLink;
    private String fullPhoneNo;
    /* access modifiers changed from: private */
    public ProgressBar loadingBar;
    private FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            String unused = loginActivity.this.codeId = s;
            loginActivity.this.loadingBar.setVisibility(8);
            loginActivity loginactivity = loginActivity.this;
            Toasty.custom((Context) loginactivity, (CharSequence) "A verification code has been sent to this phone number", loginactivity.getResources().getDrawable(R.drawable.ic_checked), loginActivity.this.getResources().getColor(R.color.colorInfo), loginActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
            Intent verification_intent = new Intent(loginActivity.this, verificationActivity.class);
            verification_intent.putExtra("phoneNo", loginActivity.this.ccp.getFullNumberWithPlus().replace(" ", ""));
            verification_intent.putExtra("codeId", loginActivity.this.codeId);
            verification_intent.addFlags(268468224);
            loginActivity.this.startActivity(verification_intent);
        }

        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            loginActivity.this.loadingBar.setVisibility(8);
            String code = phoneAuthCredential.getSmsCode();
            Intent verification_intent = new Intent(loginActivity.this, verificationActivity.class);
            verification_intent.putExtra("phoneNo", loginActivity.this.ccp.getFullNumberWithPlus().replace(" ", ""));
            verification_intent.putExtra("codeId", code);
            loginActivity.this.startActivity(verification_intent);
        }

        public void onVerificationFailed(FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                loginActivity.this.loadingBar.setVisibility(8);
                Toasty.custom((Context) loginActivity.this, (CharSequence) e.getMessage(), loginActivity.this.getResources().getDrawable(R.drawable.ic_error), loginActivity.this.getResources().getColor(R.color.colorError), loginActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                loginActivity.this.loadingBar.setVisibility(8);
                Toasty.custom((Context) loginActivity.this, (CharSequence) e.getMessage(), loginActivity.this.getResources().getDrawable(R.drawable.ic_error), loginActivity.this.getResources().getColor(R.color.colorError), loginActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
            }
        }
    };
    private ProgressDialog progressDialog;
    private Button sendOtpButton;
    /* access modifiers changed from: private */
    public EditText userPhone;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_login);
        initialiseFields();
        this.mAuth = FirebaseAuth.getInstance();
        this.createAccountLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loginActivity.this.loadingBar.setVisibility(0);
                loginActivity.this.SendCurrentUserToRegisterActivity();
            }
        });
        this.sendOtpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String phone_no = loginActivity.this.userPhone.getText().toString();
                if (TextUtils.isEmpty(phone_no) || phone_no.replace(" ", "").length() != 10) {
                    loginActivity.this.userPhone.setError("Invalid Phone number !");
                    loginActivity loginactivity = loginActivity.this;
                    Toasty.custom((Context) loginactivity, (CharSequence) "Enter a Valid phone number.", loginactivity.getResources().getDrawable(R.drawable.ic_error), loginActivity.this.getResources().getColor(R.color.colorError), loginActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
                    return;
                }
                loginActivity.this.loadingBar.setVisibility(0);
                loginActivity.this.generateCode();
            }
        });
    }

    private void initialiseFields() {
        this.sendOtpButton = (Button) findViewById(R.id.send_otp_button);
        this.userPhone = (EditText) findViewById(R.id.phone_id);
        this.createAccountLink = (TextView) findViewById(R.id.create_account_link);
        CountryCodePicker countryCodePicker = (CountryCodePicker) findViewById(R.id.countryCodePicker);
        this.ccp = countryCodePicker;
        countryCodePicker.registerCarrierNumberEditText(this.userPhone);
        this.loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        this.progressDialog = new ProgressDialog(this, R.style.progressDialogue);
    }

    /* access modifiers changed from: private */
    public void generateCode() {
        this.fullPhoneNo = this.ccp.getFullNumberWithPlus().replace(" ", "");
        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions.newBuilder(this.mAuth).setPhoneNumber(this.fullPhoneNo).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(this.mCallbacks).build());
    }

    /* access modifiers changed from: private */
    public void SendCurrentUserToRegisterActivity() {
        startActivity(new Intent(this, registerActivity.class));
    }
}
