package com.fzn.tripsat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
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
import com.squareup.picasso.Target;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {


    BottomNavigationView bottomNavigation;

    FragmentManager.BackStackEntry backStack;

    ImageView profile_pic;
    String profileUrl;

    FirebaseUser currentUser;
    private DatabaseReference reference;
    CircleImageView itm;
    FirebaseAuth auth;
    FirebaseStorage databaseReference;
    StorageReference storage;
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        fragmentTransition(HomeFragment.class);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        profile_pic = findViewById(R.id.profile_pic);
        databaseReference = FirebaseStorage.getInstance();
        storage = databaseReference.getReference();
        reference = FirebaseDatabase.getInstance().getReference();
        itm = ()


        getUserProfile();



        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button signOut = findViewById(R.id.sign_out);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent signOut = new Intent(Home.this, Login.class);
                startActivity(signOut);
            }
        });

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigateProfileView = new Intent(Home.this, UserProfile.class);
                startActivity(navigateProfileView);
            }
        });

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if(itemId == R.id.homeNav){
                    fragmentTransition(HomeFragment.class);
//                    fragmentTransition(MapsActivity.class);
                } else if (itemId == R.id.addTour) {
                    fragmentTransition(FragmentAddTour.class);
                } else if (itemId == R.id.addEvent) {
                    fragmentTransition(FragmentAddEvent.class);
                } else if (itemId == R.id.savePost) {
                    fragmentTransition(FragmentSavedPost.class);
                } else if (itemId == R.id.proPic) {
                    Intent intent = new Intent(getBaseContext(), UserProfile.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
    //    get current account and return User Id
    private String currentUser(){
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uId = user.getUid();
        return uId;
    }
    private void getUserProfile(){
        reference.child("User").child(currentUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profileUrl = snapshot.child("ImgUrl").getValue().toString();

                Picasso.get().load(profileUrl).into(profile_pic);
                Glide.with(Home.this).load(profileUrl)
                        .circleCrop().into(itm);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void fragmentTransition(Class fragmentClass){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
        transaction.replace(R.id.fragmentContainer, fragmentClass, null);
        transaction.addToBackStack(null);
//        transaction.setReorderingAllowed(true);
        transaction.commit();

    }
}