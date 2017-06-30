package com.example.waqarahmed.neighbourlinking.Activities;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePasswordActivity extends AppCompatActivity {
EditText oldPassword,newPassword;
    Button saveBtn;
    FirebaseAuth mAuth;
    String nPassword,oPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setTitle("Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        oldPassword = (EditText) findViewById(R.id.oldPassword);
        newPassword = (EditText) findViewById(R.id.NewPssword);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        mAuth = FirebaseAuth.getInstance();


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();

            }
        });



    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("o",oPassword);
        outState.putString("p",nPassword);

    }
    public void changePassword(){
        nPassword = newPassword.getText().toString();
        oPassword = oldPassword.getText().toString();
        if(!TextUtils.isEmpty(nPassword) && !TextUtils.isEmpty(oPassword)){
            if(TextUtils.equals(nPassword,oPassword)){
                mAuth.getCurrentUser().updatePassword(nPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent mainIntent = new Intent(ChangePasswordActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                        }
                        else{
                            Toast.makeText(ChangePasswordActivity.this,"Error Occur",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{

                Toast.makeText(ChangePasswordActivity.this,"Password Not Match",Toast.LENGTH_SHORT).show();
            }


        }else{
            Toast.makeText(ChangePasswordActivity.this,"Empty Field",Toast.LENGTH_SHORT).show();
        }



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
