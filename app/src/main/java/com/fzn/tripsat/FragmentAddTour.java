package com.fzn.tripsat;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import java.util.Timer;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddTour#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddTour extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int postCount = 0;
    static final int REQUEST_IMAGE_GET = 1;
    Uri profilePic;
    private FirebaseAuth auth;

    private ImageView pic1, pic2, pic3;
    private DatabaseReference reference;
    FirebaseStorage storage;
    EditText dates;
    Uri imgUri;
    String imgUriString, fName, lName, profileUrl;
    Button uploadBtn;
    ArrayList<String> imgUrlList;
    ProgressBar progressBar;
    EditText destination, dateVisited, description;



    public FragmentAddTour() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAddTour.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddTour newInstance(String param1, String param2) {
        FragmentAddTour fragment = new FragmentAddTour();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        TransitionInflater inflater =TransitionInflater.from(getContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_add_tour, container, false);

        destination = view.findViewById(R.id.visitedLocation);
        dateVisited = view.findViewById(R.id.dateVisited);
        description = view.findViewById(R.id.description);

        pic1 = view.findViewById(R.id.img1);
        pic2 = view.findViewById(R.id.img2);
        uploadBtn = view.findViewById(R.id.upload);
        progressBar = view.findViewById(R.id.progress_bar);
        reference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        imgUrlList = new ArrayList<>();



        getUserData();

        pic1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setImage(pic1);

            }
        });

        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage(pic2);
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Warning: ");
                alertDialog.setMessage("Are you sure to upload post?");
                alertDialog.setCancelable(false);


                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        writeData(destination, dateVisited, description, fName, lName, profileUrl);
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.setCancelable(true);
                    }
                });
                AlertDialog builder = alertDialog.create();
                builder.show();
            }
        });

        return view;
    }

//    Set image to the ImageView
    public void setImage(ImageView pickImg){
        pic3 = pickImg;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_GET);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK && data != null) {
            profilePic = data.getData();
            pic3.setImageURI(profilePic);
            uploadImgPost(profilePic);
        }
    }

    class wait extends Thread{

        @Override
        public void run() {

        }
    }
    //  Upload image to the firebase storage
    private void uploadImgPost(Uri imageUri) {

        StorageReference storageRef = storage.getReference().child("post");

        StorageReference storageRef2 = storageRef.child(currentUser() + "+" + currentDateAndTime() + "." +  getFileExtension(imageUri));
        ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Uploading...");
        progress.show();
        storageRef2.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imgUri = uri;
                        imgUriString = uri.toString();
                        imgUrlList.add(imgUriString);
                        Toast.makeText(getContext(), "Profile uploaded successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Profile uploaded Failed " + e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                long totalKb = snapshot.getTotalByteCount()/1024;
                long transferredKb = snapshot.getBytesTransferred()/1024;
                progress.setMessage(transferredKb+ "/" + totalKb);
                Handler handle = new Handler();
                if (transferredKb == totalKb){
                    handle.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 2000);
                    progress.dismiss();
                }
            }
        });
    }

    //    get current user by user id
    public String currentUser(){
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uId = user.getUid();
        return uId;
    }

    public void getUserData(){
        reference.child("User").child(currentUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profileUrl = snapshot.child("ImgUrl").getValue().toString();
                fName = snapshot.child("FirstName").getValue().toString();
                lName = snapshot.child("LastName").getValue().toString();
                Toast.makeText(getContext(), fName + " " + lName, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //    get file extension as string
    private String getFileExtension(Uri imageUri) {
        ContentResolver content = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(content.getType(imageUri));
    }

    //   Get current date and time as String
    private String currentDateAndTime(){
        Calendar calenderInstance = Calendar.getInstance();
        Date currentDate = calenderInstance.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    public void writeData(EditText destination, EditText dateVisited, EditText description, String fName, String lName, String profileUrl){
        String visitedLocation = destination.getText().toString();
        String visitedDate = dateVisited.getText().toString();
        String desc = description.getText().toString();
        String postId = UUID.randomUUID() + "+" + currentDateAndTime();

        if (!visitedDate.isEmpty() && !visitedLocation.isEmpty()){
            reference.child("Post").child(postId).child("firstName").setValue(fName);
            reference.child("Post").child(postId).child("lastName").setValue(lName);
            reference.child("Post").child(postId).child("destination").setValue(visitedLocation);
            reference.child("Post").child(postId).child("date").setValue(visitedDate);
            reference.child("Post").child(postId).child("description").setValue(desc);
            reference.child("Post").child(postId).child("image1").setValue(imgUrlList.get(0));
            reference.child("Post").child(postId).child("image2").setValue(imgUrlList.get(1));
            reference.child("Post").child(postId).child("createdDate").setValue(currentDateAndTime());

            if (profileUrl != null){
                reference.child("Post").child(postId).child("profileUrl").setValue(profileUrl);
            }
            Toast.makeText(getContext(), "Post Uploaded", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(), "One or more field empty", Toast.LENGTH_LONG).show();
        }



    }


}