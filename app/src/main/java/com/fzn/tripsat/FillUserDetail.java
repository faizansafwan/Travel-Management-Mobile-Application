package com.fzn.tripsat;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FillUserDetail extends AppCompatActivity {

    EditText firstName, lastName, bio;
    Button saveUser_button;
    ImageView addProfile;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    String imgUri;
    String urlTxt;

    FirebaseStorage storage;
    Uri profilePic;
    Uri url;
    static final int REQUEST_IMAGE_GET = 1;
    private Context context;
    StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_user_detail);

        firstName = findViewById(R.id.input_firstname);
        lastName = findViewById(R.id.input_lastname);
        bio = findViewById(R.id.input_bio);
        saveUser_button = findViewById(R.id.saveUser);
        addProfile =findViewById(R.id.addProfile);
        context = FillUserDetail.this;

        reference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        addProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setImage();

//                uploadProfilePic(profilePic);
            }
        });

        saveUser_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    writeData(firstName, lastName, bio);
                    FirebaseAuth.getInstance().signOut();


                }
                catch (Exception e){
                    Toast.makeText(FillUserDetail.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                }
            }
        });


    }

    //  pick image from files
    public void setImage(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null ){
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK && data != null) {
            profilePic = data.getData();
            addProfile.setImageURI(profilePic);
            uploadProfilePic(profilePic);
        }
    }

    //  Upload image to the firebase storage
    private void uploadProfilePic(Uri imageuri) {

        StorageReference storageRef = storage.getReference().child("profile");

        StorageReference storageRef2 = storageRef.child(currentUser() + "." +  getFileExtension(imageuri));

        storageRef2.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url = uri;
                        imgUri = uri.toString();
                        Log.d(TAG, imgUri);
                        Toast.makeText(FillUserDetail.this, "Profile uploaded successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FillUserDetail.this, "Profile uploaded Failed " + e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }
    //    Write Data into firebase realtime database
    public void writeData(EditText fName, EditText lName, EditText bio) {

        String fNameTxt = fName.getText().toString();
        String lNameTxt = lName.getText().toString();
        String bioTxt = bio.getText().toString();

        if ( fNameTxt.isEmpty() || lNameTxt.isEmpty()) {
            Toast.makeText(FillUserDetail.this, "One or more field is empty",
                    Toast.LENGTH_LONG).show();
        }
        else {

            reference.child("User").child(currentUser()).child("FirstName").setValue(fNameTxt);
            reference.child("User").child(currentUser()).child("LastName").setValue(lNameTxt);
            reference.child("User").child(currentUser()).child("Bio").setValue(bioTxt);
            reference.child("User").child(currentUser()).child("ImgUrl").setValue(imgUri);
            Toast.makeText(this, "Data saved successful", Toast.LENGTH_LONG).show();
        }
    }

    //    get current user by user id
    public String currentUser(){
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uId = user.getUid();
        return uId;
    }
    //    get file extension as string
    private String getFileExtension(Uri imageUri) {
        ContentResolver content = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(content.getType(imageUri));
    }
}