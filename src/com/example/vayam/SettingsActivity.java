package com.example.vayam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vayam.png.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.util.HashMap;
import p008de.hdodenhof.circleimageview.CircleImageView;
import p009es.dmoral.toasty.Toasty;

public class SettingsActivity extends AppCompatActivity {
    private static final int galleryProfileImage = 1;
    private Uri ImageUri;
    private Button accountButton;
    private ImageView backArrowBtn;
    /* access modifiers changed from: private */
    public String currentUserID;
    /* access modifiers changed from: private */
    public DatabaseReference databaseReference;
    /* access modifiers changed from: private */
    public CircleImageView displayPic;
    /* access modifiers changed from: private */
    public ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private Button needHelpBtn;
    private Button notificationBtn;
    private ImageView profileImageBtn;
    private StorageReference profileImageRef;
    private Button saveBtn;
    /* access modifiers changed from: private */
    public EditText status;
    private Button uiPrefBtn;
    /* access modifiers changed from: private */
    public EditText userName;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_settings);
        FirebaseAuth instance = FirebaseAuth.getInstance();
        this.mAuth = instance;
        this.currentUserID = instance.getCurrentUser().getUid();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.profileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        initialiseFields();
        retrieveUserInfo();
        retrieveProfileImage();
        this.saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SettingsActivity.this.updateUserSettings();
                SettingsActivity.this.retrieveUserInfo();
            }
        });
        this.backArrowBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SettingsActivity.this.sendUserToMainActivity();
            }
        });
        this.profileImageBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction("android.intent.action.GET_CONTENT");
                galleryIntent.setType("image/*");
                SettingsActivity.this.startActivityForResult(galleryIntent, 1);
            }
        });
        this.accountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SettingsActivity.this.sendUserToAccountActivity();
            }
        });
        this.uiPrefBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SettingsActivity.this.sendUserToUIPreferencesActivity();
            }
        });
    }

    private void initialiseFields() {
        this.displayPic = (CircleImageView) findViewById(R.id.profile_image);
        this.userName = (EditText) findViewById(R.id.User_Name);
        this.status = (EditText) findViewById(R.id.User_Status);
        this.accountButton = (Button) findViewById(R.id.Account_info);
        this.notificationBtn = (Button) findViewById(R.id.Notifications);
        this.uiPrefBtn = (Button) findViewById(R.id.UI_Pref);
        this.needHelpBtn = (Button) findViewById(R.id.need_help);
        this.saveBtn = (Button) findViewById(R.id.save_button);
        this.backArrowBtn = (ImageView) findViewById(R.id.back_arrow_btn);
        this.profileImageBtn = (ImageView) findViewById(R.id.profile_image_pick);
        this.loadingBar = new ProgressDialog(this, R.style.progressDialogue);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1 && data != null) {
            Uri data2 = data.getData();
            this.ImageUri = data2;
            CropImage.activity(data2).setGuidelines(CropImageView.Guidelines.ON).start(this);
        }
        if (requestCode == 203) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == -1) {
                this.loadingBar.setMessage("Updating display image");
                this.loadingBar.setCanceledOnTouchOutside(false);
                if (Build.VERSION.SDK_INT >= 21) {
                    this.loadingBar.setProgressDrawable(getDrawable(R.drawable.loading_bar));
                }
                this.loadingBar.show();
                Uri ImageUri2 = result.getUri();
                StorageReference filepath = this.profileImageRef.child(this.currentUserID + ".image");
                filepath.putFile(ImageUri2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            public void onSuccess(Uri uri) {
                                Toasty.custom((Context) SettingsActivity.this, (CharSequence) "Updated Successfully", SettingsActivity.this.getResources().getDrawable(R.drawable.ic_smiley), SettingsActivity.this.getResources().getColor(R.color.colorSuccess), SettingsActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
                                SettingsActivity.this.databaseReference.child("Users").child(SettingsActivity.this.currentUserID).child("image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(Task<Void> task) {
                                        SettingsActivity.this.loadingBar.dismiss();
                                    }
                                });
                            }
                        });
                    }
                });
                filepath.putFile(ImageUri2).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                    public void onFailure(Exception e) {
                        SettingsActivity.this.loadingBar.dismiss();
                        Toasty.custom((Context) SettingsActivity.this, (CharSequence) e.getMessage(), SettingsActivity.this.getResources().getDrawable(R.drawable.ic_error), SettingsActivity.this.getResources().getColor(R.color.colorError), SettingsActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
                    }
                });
            } else if (resultCode == 204) {
                Toast.makeText(this, "Error: " + result.getError(), 0).show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateUserSettings() {
        String setUserName = this.userName.getText().toString();
        String setUserStatus = this.status.getText().toString();
        if (TextUtils.isEmpty(setUserName)) {
            this.userName.setError("Username is required !");
            return;
        }
        HashMap<String, String> ProfileMap = new HashMap<>();
        ProfileMap.put("userID", this.currentUserID);
        ProfileMap.put("UserName", setUserName);
        ProfileMap.put("UserStatus", setUserStatus);
        this.databaseReference.child("Users").child(this.currentUserID).setValue(ProfileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    SettingsActivity settingsActivity = SettingsActivity.this;
                    Toasty.custom((Context) settingsActivity, (CharSequence) "Updated Successfully", settingsActivity.getResources().getDrawable(R.drawable.ic_smiley), SettingsActivity.this.getResources().getColor(R.color.colorSuccess), SettingsActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
                    return;
                }
                String error = task.getException().toString();
                SettingsActivity settingsActivity2 = SettingsActivity.this;
                Toasty.custom((Context) settingsActivity2, (CharSequence) error, settingsActivity2.getResources().getDrawable(R.drawable.ic_error), SettingsActivity.this.getResources().getColor(R.color.colorError), SettingsActivity.this.getResources().getColor(R.color.colorAccent), 0, true, true).show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendUserToMainActivity() {
        Intent sendIntent = new Intent(this, MainActivity.class);
        sendIntent.addFlags(268468224);
        startActivity(sendIntent);
        finish();
    }

    private void retrieveProfileImage() {
        this.databaseReference.child("Users").child(this.currentUserID).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("image")) {
                    Picasso.get().load(snapshot.child("image").getValue().toString()).into((ImageView) SettingsActivity.this.displayPic);
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void retrieveUserInfo() {
        this.databaseReference.child("Users").child(this.currentUserID).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("UserName") && snapshot.hasChild("UserStatus")) {
                    String retrievedUserName = snapshot.child("UserName").getValue().toString();
                    String retrievedUserStatus = snapshot.child("UserStatus").getValue().toString();
                    SettingsActivity.this.userName.setText(retrievedUserName);
                    SettingsActivity.this.status.setText(retrievedUserStatus);
                } else if (snapshot.exists() && snapshot.hasChild("UserName")) {
                    SettingsActivity.this.userName.setText(snapshot.child("UserName").getValue().toString());
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendUserToAccountActivity() {
        startActivity(new Intent(this, AccountActivity.class));
    }

    /* access modifiers changed from: private */
    public void sendUserToUIPreferencesActivity() {
        startActivity(new Intent(this, UIPreferencesActivity.class));
    }
}
