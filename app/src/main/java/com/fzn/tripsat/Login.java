package com.fzn.tripsat;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    TextView forgotPassword;
    EditText inputUsername, inputPassword;
    Button signupButton, loginButton;
    String get;

    private FirebaseAuth auth;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();


        forgotPassword = findViewById(R.id.forgot_Password);
        signupButton = findViewById(R.id.signup_button);
        loginButton = findViewById(R.id.login_button);
        inputUsername = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToForgotPassword();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignup();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
                signin(inputUsername, inputPassword);

            }
        });

    }

    public void navigateToForgotPassword(){
        Intent navigate = new Intent(this, ForgotPassword.class);
        startActivity(navigate);
    }
    public void navigateToSignup(){
        Intent navigate = new Intent(this, Signup.class);
        startActivity(navigate);
    }

    public void navigateToHome(){
        Intent navigate = new Intent(this, Home.class);
        startActivity(navigate);
    }

    public void signin(EditText email, EditText password){
        final String emailTxt = email.getText().toString();
        final String passwordTxt =  password.getText().toString();

        if ( emailTxt.isEmpty() || passwordTxt.isEmpty() ) {
            Toast.makeText(Login.this, "One or more field is empty",
                    Toast.LENGTH_LONG).show();
        }
        else {
            auth.signInWithEmailAndPassword(emailTxt, passwordTxt).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Log.d(TAG, "Login Successful");
                        FirebaseUser user = auth.getCurrentUser();

                        String users = user.getUid();
                        Toast.makeText(Login.this, "Login success" + users, Toast.LENGTH_LONG).show();
                        navigateToHome();
                    }
                    else {
                        Log.w(TAG, "Login unsuccessful");
                        Toast.makeText(Login.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }


    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            currentUser.getIdToken(true);
            currentUser.reload();
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }
    }
}