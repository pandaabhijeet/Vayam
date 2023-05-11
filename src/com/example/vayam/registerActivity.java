package com.example.vayam;

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
import com.google.firebase.database.DatabaseReference;
import com.hbb20.CountryCodePicker;
import java.util.concurrent.TimeUnit;
import p009es.dmoral.toasty.Toasty;

public class registerActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public CountryCodePicker ccp;
    /* access modifiers changed from: private */
    public String codeId;
    private DatabaseReference dataBaseRef;
    /* access modifiers changed from: private */
    public ProgressBar loadingBar;
    private TextView loginLink;
    private FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            String unused = registerActivity.this.codeId = s;
            registerActivity.this.loadingBar.setVisibility(8);
            registerActivity registeractivity = registerActivity.this;
            Toasty.custom((Context) registeractivity, (CharSequence) "A verification code has been sent to this phone number", registeractivity.getResources().getDrawable(R.drawable.ic_checked), registerActivity.this.getResources().getColor(R.color.colorInfo), registerActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
            Intent verification_intent = new Intent(registerActivity.this, regVerifyActivity.class);
            verification_intent.putExtra("phoneNo", registerActivity.this.ccp.getFullNumberWithPlus().replace(" ", ""));
            verification_intent.putExtra("codeId", registerActivity.this.codeId);
            verification_intent.addFlags(268468224);
            registerActivity.this.startActivity(verification_intent);
        }

        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            Intent verification_intent = new Intent(registerActivity.this, regVerifyActivity.class);
            verification_intent.putExtra("phoneNo", registerActivity.this.ccp.getFullNumberWithPlus().replace(" ", ""));
            verification_intent.putExtra("codeId", code);
            registerActivity.this.startActivity(verification_intent);
        }

        public void onVerificationFailed(FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toasty.custom((Context) registerActivity.this, (CharSequence) e.getMessage(), registerActivity.this.getResources().getDrawable(R.drawable.ic_error), registerActivity.this.getResources().getColor(R.color.colorError), registerActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toasty.custom((Context) registerActivity.this, (CharSequence) e.getMessage(), registerActivity.this.getResources().getDrawable(R.drawable.ic_error), registerActivity.this.getResources().getColor(R.color.colorError), registerActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
            }
        }
    };
    private Button regOtpButton;
    private EditText regUserPhone;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_register);
        initialiseFields();
        this.mAuth = FirebaseAuth.getInstance();
        this.loginLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                registerActivity.this.loadingBar.setVisibility(0);
                registerActivity.this.SendCurrentUserToLoginActivity();
            }
        });
        this.regOtpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                registerActivity.this.initiateCode();
            }
        });
    }

    private void initialiseFields() {
        this.regUserPhone = (EditText) findViewById(R.id.reg_phone_id);
        CountryCodePicker countryCodePicker = (CountryCodePicker) findViewById(R.id.countryCodePicker);
        this.ccp = countryCodePicker;
        countryCodePicker.registerCarrierNumberEditText(this.regUserPhone);
        this.loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        this.loginLink = (TextView) findViewById(R.id.back_login_link);
        this.regOtpButton = (Button) findViewById(R.id.reg_send_otp_button);
    }

    /* access modifiers changed from: private */
    public void initiateCode() {
        String phone_no = this.regUserPhone.getText().toString();
        if (TextUtils.isEmpty(phone_no) || phone_no.replace(" ", "").length() != 10) {
            this.regUserPhone.setError("Invalid Phone number !");
            Toasty.custom((Context) this, (CharSequence) "Enter a Valid phone number.", getResources().getDrawable(R.drawable.ic_error), getResources().getColor(R.color.colorError), getResources().getColor(R.color.colorAccent), 0, true, true).show();
            return;
        }
        this.loadingBar.setVisibility(0);
        generateCode();
    }

    private void generateCode() {
        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions.newBuilder(this.mAuth).setPhoneNumber(this.ccp.getFullNumberWithPlus().replace(" ", "")).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(this.mCallbacks).build());
    }

    /* access modifiers changed from: private */
    public void SendCurrentUserToLoginActivity() {
        startActivity(new Intent(this, loginActivity.class));
    }
}
