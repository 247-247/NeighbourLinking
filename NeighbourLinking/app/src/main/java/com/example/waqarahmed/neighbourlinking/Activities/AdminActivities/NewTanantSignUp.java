package com.example.waqarahmed.neighbourlinking.Activities.AdminActivities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.waqarahmed.neighbourlinking.Activities.NewRegistrations;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.AdminSevices.RegisterNewBrand;
import com.example.waqarahmed.neighbourlinking.Services.AdminSevices.RegisterNewServiceMan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.Calendar;

public class NewTanantSignUp extends AppCompatActivity {
    EditText emai, password;
    Button saveBtn;
    ProgressDialog mProg;
    FirebaseAuth mAuth;
FirebaseAuth mmAuth;
    Spinner spnr;
    CheckBox isAdmin;
    Calendar c;
    DatabaseReference mDatabaseReferenceUser;
    RadioGroup radioGroup;

    private AwesomeValidation awesomValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tanant_sign_up);
        getSupportActionBar().setTitle("New Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        emai = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.paswword);
        saveBtn = (Button) findViewById(R.id.doneBtn);
        awesomValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomValidation.addValidation(this, R.id.Email, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        mAuth = FirebaseAuth.getInstance();
        mmAuth = FirebaseAuth.getInstance();
        mProg = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
         c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);




        BtnSaveFunction();

    }

    private void BtnSaveFunction() {


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String e , p;
                e = emai.getText().toString();
                if(emai.length()>0){
                    if (awesomValidation.validate()) {
                        if(password.length()>6){

                            e = emai.getText().toString();
                            p = password.getText().toString();
                            password.clearFocus();
                            emai.clearFocus();
                            String optn = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                               if(optn.equals("Service Man")){
                                   newServiceMan(e,p);


                               }
                            else if(optn.equals("Brand")){
                                   newBrand(e,p);


                               }
                            else{
                                   SignUpNewTennant( e , p);

                               }



                        }else{
                            password.setError("Password should more than 6 character lenght");
                        }
                    }


                }else{

                    emai.setError("Email Requird");
                }


            }
        });

    }


    public void SignUpNewTennant(String e ,String p){
        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("[https://neighbourlinking-d0561.firebaseio.com/]")  // database url
                .setApiKey("AIzaSyCPrpBFuYu6A1T2E8jf1O1zVScSQ8gU-F8")  // web api key
                .setApplicationId("neighbourlinking-d0561").build();  // project id
        int seconds = c.get(Calendar.SECOND);
        FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(),firebaseOptions,
                " NeighbourLinking"+seconds );  // app name

        mmAuth = FirebaseAuth.getInstance(myApp);

        mProg.setMessage("wait...");
        mProg.show();
        mmAuth.createUserWithEmailAndPassword(e, p)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mProg.dismiss();
                            upDateHoues();
                          //  mmAuth.signOut();

//                            Intent in = new Intent(NewTanantSignUp.this, NewRegistrations.class);
//                            startActivity(in);

                        } else {
                            // If sign in fails, display a message to the user.
                            mProg.dismiss();
                            Toast.makeText(NewTanantSignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }


    public void upDateHoues(){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        spnr= (Spinner) promptsView.findViewById(R.id.spinner);
        isAdmin = (CheckBox) promptsView.findViewById(R.id.isAdmin);

//        final EditText userInput = (EditText) promptsView
//                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,int id) {

                                if(isAdmin.isChecked()) {
                                    DatabaseReference mDatabaseChildUser = mDatabaseReferenceUser.child(mmAuth.getCurrentUser().getUid());
                                    mDatabaseChildUser.child("address").setValue(spnr.getSelectedItem().toString());
                                    mDatabaseChildUser.child("isAdmin").setValue("Yes");

                                    mmAuth.signOut();
                                    Intent intent = new Intent(NewTanantSignUp.this,NewRegistrations.class);
                                    startActivity(intent);
                                }else{
                                    DatabaseReference mDatabaseChildUser = mDatabaseReferenceUser.child(mmAuth.getCurrentUser().getUid());
                                    mDatabaseChildUser.child("address").setValue(spnr.getSelectedItem().toString());
                                    mDatabaseChildUser.child("isAdmin").setValue("No");
                                    mmAuth.signOut();
                                    Intent intent = new Intent(NewTanantSignUp.this,NewRegistrations.class);
                                    startActivity(intent);

                                }
                            }
                        });
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,int id) {
//                                dialog.cancel();
//                            }
//                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    public  void newServiceMan(String e,String p){
        password.setText("");
        emai.setText("");

        RegisterNewServiceMan registerNewServiceMan = new RegisterNewServiceMan(NewTanantSignUp.this);
        registerNewServiceMan.execute(e,p);

    }
    public void newBrand(String e,String p){
        password.setText("");
        emai.setText("");

        RegisterNewBrand brand = new RegisterNewBrand(NewTanantSignUp.this);
        brand.execute(e,p);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }





}
