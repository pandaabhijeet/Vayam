package com.example.vayam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import p009es.dmoral.toasty.Toasty;

public class regVerifyActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public Button CreateButton;
    /* access modifiers changed from: private */
    public String codeId;
    /* access modifiers changed from: private */
    public String fullPhoneNo;
    private FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            regVerifyActivity.this.verificationInfo.setText(regVerifyActivity.this.fullPhoneNo + " was verified successfully");
            regVerifyActivity.this.regVerificationCode.setText(code);
            regVerifyActivity.this.CreateButton.setText("Register");
            regVerifyActivity regverifyactivity = regVerifyActivity.this;
            Toasty.custom((Context) regverifyactivity, (CharSequence) "Verified Successfully.", regverifyactivity.getResources().getDrawable(R.drawable.ic_verified), regVerifyActivity.this.getResources().getColor(R.color.colorSuccess), regVerifyActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
            regVerifyActivity.this.progressDialog.dismiss();
        }

        public void onVerificationFailed(FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                regVerifyActivity.this.progressDialog.dismiss();
                Toasty.custom((Context) regVerifyActivity.this, (CharSequence) e.getMessage(), regVerifyActivity.this.getResources().getDrawable(R.drawable.ic_error), regVerifyActivity.this.getResources().getColor(R.color.colorError), regVerifyActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                regVerifyActivity.this.progressDialog.dismiss();
                Toasty.custom((Context) regVerifyActivity.this, (CharSequence) e.getMessage(), regVerifyActivity.this.getResources().getDrawable(R.drawable.ic_error), regVerifyActivity.this.getResources().getColor(R.color.colorError), regVerifyActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
            }
        }
    };
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    /* access modifiers changed from: private */
    public EditText regVerificationCode;
    /* access modifiers changed from: private */
    public TextView verificationInfo;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_reg_verify_activity);
        initialiseFields();
        this.mAuth = FirebaseAuth.getInstance();
        this.fullPhoneNo = getIntent().getStringExtra("phoneNo");
        this.codeId = getIntent().getStringExtra("codeId");
        autoVerify();
        this.CreateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (regVerifyActivity.this.regVerificationCode.getText().toString().isEmpty() || regVerifyActivity.this.regVerificationCode.getText().toString().length() != 6) {
                    regVerifyActivity.this.regVerificationCode.setError("Invalid Code");
                    regVerifyActivity regverifyactivity = regVerifyActivity.this;
                    Toasty.custom((Context) regverifyactivity, (CharSequence) "Oops! Seems you entered an invalid code.", regverifyactivity.getResources().getDrawable(R.drawable.ic_redo), regVerifyActivity.this.getResources().getColor(R.color.colorError), regVerifyActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
                    return;
                }
                regVerifyActivity.this.progressDialog.setTitle("Creating new account!");
                regVerifyActivity.this.progressDialog.setMessage("Please wait.We are getting things ready for you!");
                regVerifyActivity.this.progressDialog.setCancelable(true);
                regVerifyActivity.this.progressDialog.setCanceledOnTouchOutside(false);
                regVerifyActivity.this.signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(regVerifyActivity.this.codeId, regVerifyActivity.this.regVerificationCode.getText().toString()));
            }
        });
    }

    private void initialiseFields() {
        this.regVerificationCode = (EditText) findViewById(R.id.reg_enter_otp);
        this.CreateButton = (Button) findViewById(R.id.register_btn);
        this.verificationInfo = (TextView) findViewById(R.id.verify_inst);
        this.progressDialog = new ProgressDialog(this, R.style.progressDialogue);
    }

    private void autoVerify() {
        this.progressDialog.show();
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.progressDialog.setMessage("Auto-verifying. Please wait");
        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions.newBuilder(this.mAuth).setPhoneNumber(this.fullPhoneNo).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(this.mCallbacks).build());
    }

    /* access modifiers changed from: private */
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        this.mAuth.signInWithCredential(credential).addOnCompleteListener((Activity) this, new OnCompleteListener<AuthResult>() {
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
                    if (User != null) {
                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(User.getUid());
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(DataSnapshot snapshot) {
                                if (!snapshot.exists()) {
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("phone", User.getPhoneNumber());
                                    databaseReference.updateChildren(userMap);
                                }
                            }

                            public void onCancelled(DatabaseError error) {
                            }
                        });
                    }
                    regVerifyActivity.this.progressDialog.dismiss();
                    regVerifyActivity.this.SendCurrentUserToMainActivity();
                    regVerifyActivity regverifyactivity = regVerifyActivity.this;
                    Toasty.custom((Context) regverifyactivity, (CharSequence) "Logged in successfully", regverifyactivity.getResources().getDrawable(R.drawable.ic_checked), regVerifyActivity.this.getResources().getColor(R.color.colorSuccess), regVerifyActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
                    return;
                }
                regVerifyActivity.this.progressDialog.dismiss();
                String errorMessage = task.getException().toString();
                regVerifyActivity regverifyactivity2 = regVerifyActivity.this;
                Toasty.custom((Context) regverifyactivity2, (CharSequence) errorMessage, regverifyactivity2.getResources().getDrawable(R.drawable.ic_error), regVerifyActivity.this.getResources().getColor(R.color.colorError), regVerifyActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
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
