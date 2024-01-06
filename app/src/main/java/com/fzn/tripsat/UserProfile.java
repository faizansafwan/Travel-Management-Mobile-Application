package com.fzn.tripsat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class UserProfile extends AppCompatActivity {


    TextView name, uId, gmail, bio;
    Button signOut;
    ImageView profile;
    String fname, lname, mail, Bio, profileUrl;

    ListView list1;
    FirebaseUser currentUser;
    private DatabaseReference reference;

    FirebaseAuth auth;
    FirebaseStorage databaseReference;
    StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = findViewById(R.id.name);
        uId = findViewById(R.id.userId);
        gmail = findViewById(R.id.gmail);
        bio = findViewById(R.id.bio);
        profile = findViewById(R.id.profile);
        signOut = findViewById(R.id.sign_out_btn);
//        list1 = findViewById(R.id.listView);

        databaseReference = FirebaseStorage.getInstance();
        storage = databaseReference.getReference();
        reference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        getUserData();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSignOut();
            }
        });
    }

    //    get user data include name, gmail, username and profile picture.
    public void getUserData(){
        mail = currentUser.getEmail();
        uId.setText(currentUser());
        gmail.setText(mail);
        reference.child("User").child(currentUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fname = snapshot.child("FirstName").getValue().toString();
                lname = snapshot.child("LastName").getValue().toString();
                Bio = snapshot.child("Bio").getValue().toString();
                profileUrl = snapshot.child("ImgUrl").getValue().toString();
                name.setText(fname + " " + lname);
                bio.setText(Bio);

//                get the profile image to the image view
                Picasso.get().load(profileUrl).into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                Log.e("Error: " , error.getMessage());
            }
        });

    }

    //    get current account and return User Id
    public String currentUser(){
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uId = user.getUid();
        return uId;
    }

    //    create sign out function and navigate to Login page
    public void setSignOut(){
        FirebaseAuth.getInstance().signOut();
        Intent navigateLoginPage = new Intent(this, Login.class);
        startActivity(navigateLoginPage);
    }
}