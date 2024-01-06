package com.fzn.tripsat;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    EditText fname, lname, mail, username, password, conPassword;
    Button signupBtn;

    FragmentContainerView containerView;


    private FirebaseAuth mAuth;

    DatabaseReference dbRef = FirebaseDatabase.getInstance()
            .getReference();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

//        fname = findViewById(R.id.input_firstname);
//        lname = findViewById(R.id.input_lastname);
        mail = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        conPassword = findViewById(R.id.input_confirm_password);
        signupBtn = findViewById(R.id.signup_button);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registration(mail, password, conPassword);

            }
        });

    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            FirebaseAuth.getInstance().signOut();
            currentUser.reload();

        }
    }
    public void registration(EditText email, EditText password, EditText conPassword){

        String mailTxt = email.getText().toString();
        String passwordTxt = password.getText().toString();
        String conPasswordTxt = conPassword.getText().toString();
        if ( mailTxt.isEmpty() || passwordTxt.isEmpty() || conPasswordTxt.isEmpty()) {
            Toast.makeText(Signup.this, "One or more field is empty",
                    Toast.LENGTH_LONG).show();
        }
        else if (passwordTxt.length() < 8){
            Toast.makeText(Signup.this, "Password too short", Toast.LENGTH_LONG).show();
        }
        else if (!conPasswordTxt.equals(passwordTxt)) {
            Toast.makeText(Signup.this, "Confirm Password not matched with password", Toast.LENGTH_LONG).show();
        }
        else {

            mAuth.createUserWithEmailAndPassword(mailTxt, passwordTxt).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){
                        Log.d(TAG, "Successful");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Signup.this, "Account Created Successful", Toast.LENGTH_SHORT).show();
                        navigateToUserDetail();
                    }
                    else {
                        Log.w(TAG, "Signup fail");
                        Toast.makeText(Signup.this, "failure", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    public void navigateToUserDetail(){
        Intent navigateUserDetail = new Intent(Signup.this, FillUserDetail.class);
        startActivity(navigateUserDetail);
    }
}