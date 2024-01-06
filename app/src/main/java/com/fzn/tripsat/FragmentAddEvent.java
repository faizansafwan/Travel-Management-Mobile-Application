package com.fzn.tripsat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddEvent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference reference;

    RecyclerView eventRecyclerView;

    ArrayList<AddEventModel> list;
    AddEventAdapter addEventAdapter;
    public FragmentAddEvent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAddEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddEvent newInstance(String param1, String param2) {
        FragmentAddEvent fragment = new FragmentAddEvent();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_add_event, container, false);

        eventRecyclerView = view.findViewById(R.id.eventRecyclerView);
        reference = FirebaseDatabase.getInstance().getReference();
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list =new ArrayList<>();
        addEventAdapter =new AddEventAdapter(getContext(), list);
        eventRecyclerView.setAdapter(addEventAdapter);

        reference.child("Event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    AddEventModel eventModel = dataSnapshot.getValue(AddEventModel.class);
                    list.add(eventModel);
                }
                addEventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.addEvent){
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
}