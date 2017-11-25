package com.example.waqarahmed.neighbourlinking.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.waqarahmed.neighbourlinking.Activities.AdminActivities.MainActivityAdmin;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.MainActivity;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.Profile;
import com.example.waqarahmed.neighbourlinking.Classes.Tanant;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.ServiceManServices.SignInServiceMan;
import com.example.waqarahmed.neighbourlinking.Services.BrandServices.SignInBrand;
import com.example.waqarahmed.neighbourlinking.Services.DeviceRegistrationRelated;
import com.example.waqarahmed.neighbourlinking.Shared.AdminSharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
   TextView mEmailView,mPasswordView;
    Button mSignInButton;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReferenceUser;
    ProgressDialog mProg;
    TextView forgetPassword;
    RadioGroup radioGroup;
    private AwesomeValidation awesomValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mEmailView = (TextView) findViewById(R.id.email_edit_signIn);
        mPasswordView = (TextView) findViewById(R.id.password_edit_signInActivity);
        mSignInButton = (Button) findViewById(R.id.signIn_btn_signInActivity);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
        mDatabaseReferenceUser.keepSynced(true);
        mProg = new ProgressDialog(this);
        awesomValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomValidation.addValidation(this,R.id.email_edit_signIn, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        radioGroup = (RadioGroup) findViewById(R.id.signInRadioGroup);



        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startLogIn();
                String e , p;
                e = mEmailView.getText().toString();
                if(mEmailView.length()>0){
                    if (awesomValidation.validate()) {
                        if(mPasswordView.length()>6){

                            e = mEmailView.getText().toString();
                            p = mPasswordView.getText().toString();
                            mEmailView.clearFocus();
                            mPasswordView.clearFocus();
                            String optn = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                            if(optn.equals("Service Man")){
                                signInAsServiceMan(e,p);


                            }
                            else if(optn.equals("Brand")){
                                signInAsBrand(e,p);


                            }
                            else{
                                startLogIn( e , p);

                            }

                        }else{
                            mPasswordView.setError("Password should more than 6 character lenght");
                        }
                    }


                }else{

                    mEmailView.setError("Email Requird");
                }


            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPassword.setTextColor(Color.RED);
                if(mEmailView.length()>=6){
                   if(awesomValidation.validate())
                    checkUserForforgetPassword(mEmailView.getText().toString());
                }else{
                    mEmailView.setError("Enter Email");
                }
            }
        });
    }

    private void signInAsBrand(String e, String p) {
        SignInBrand signInBrand = new SignInBrand(this);
        signInBrand.execute(e,p);
    }

    private void signInAsServiceMan(String e, String p) {

        SignInServiceMan signInServiceMan = new SignInServiceMan(this);
        signInServiceMan.execute(e,p);

    }

    private void startLogIn(String email,String pass) {
        mProg.setMessage("LogIn...");
//        String email =  mEmailView.getText().toString().trim();
//        String pass = mPasswordView.getText().toString().trim();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            mProg.show();
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        mProg.dismiss();
                        DeviceRegistrationRelated backgroundWorker = new DeviceRegistrationRelated(getApplicationContext());
                        backgroundWorker.execute();


                        checkUserExists();

                    } else {
                        mProg.dismiss();
                        Toast.makeText(SignInActivity.this, "Sign Problem", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(SignInActivity.this, "Text Fields Problem", Toast.LENGTH_LONG).show();
        }


    }

    private void checkUserExists() {

        mDatabaseReferenceUser.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(mAuth.getCurrentUser() != null) {

                    String currentUserId = mAuth.getCurrentUser().getUid();
                    if (dataSnapshot.hasChild("image")) {
                           if(dataSnapshot.child("isAdmin").getValue().toString().equals("Yes"))
                           {
                               AdminSharedPref.init(SignInActivity.this);
                               AdminSharedPref.write(AdminSharedPref.IS_ADMIN,"yes");
                               String img =dataSnapshot.child("image").getValue().toString();
                              AdminSharedPref.write(AdminSharedPref.IMAGE,img);
                               AdminSharedPref.write(AdminSharedPref.EMAIL,mAuth.getCurrentUser().getEmail());
                               Intent mainIntent = new Intent(SignInActivity.this, MainActivityAdmin.class);
                               mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                               startActivity(mainIntent);

                           }else{
                               Intent mainIntent = new Intent(SignInActivity.this, MainActivity.class);
                               mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                               startActivity(mainIntent);
                           }


                    } else {

                        Intent AccuntSetupIntent = new Intent(SignInActivity.this, Profile.class);
                        AccuntSetupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(AccuntSetupIntent);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public  void checkUserForforgetPassword(final String email){
        mAuth.createUserWithEmailAndPassword(email, "123wwww")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                               // Toast.makeText(SignInActivity.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
                                mAuth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignInActivity.this, "Password Reset email sended you", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }


                        } else {
                            Toast.makeText(SignInActivity.this, "You are not registerd", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


}
