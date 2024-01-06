package com.fzn.tripsat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    Context context;
    ArrayList<UserData> userDataList;


    public HomeAdapter(Context context, ArrayList<UserData> userDataList){
        this.userDataList = userDataList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserData user = userDataList.get(position);
        holder.firstName.setText(user.getFirstName());
        holder.lastname.setText(user.getLastName());
        holder.destination.setText(user.getDestination());
        holder.date.setText(user.getDate());
        holder.description.setText(user.getDescription());
        holder.currentTime.setText(user.getCreatedDate());
        Glide.with(holder.profilePic.getContext()).load(user.getProfileUrl())
                .circleCrop().into(holder.profilePic);
        Glide.with(holder.image1.getContext()).load(user.getImage1())
                .into(holder.image1);
        Glide.with(holder.image2.getContext()).load(user.getImage2())
                .into(holder.image2);

    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView firstName, lastname, description, date, destination, currentTime;
        ImageView  image1, image2;
        CircleImageView profilePic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.firstName);
            lastname = itemView.findViewById(R.id.lastName);
            description = itemView.findViewById(R.id.description);
            profilePic = itemView.findViewById(R.id.profilePic);
            date = itemView.findViewById(R.id.visitedDate);
            destination = itemView.findViewById(R.id.visitedPlace);
            image1 = itemView.findViewById(R.id.image1);
            image2 = itemView.findViewById(R.id.image2);
            currentTime = itemView.findViewById(R.id.currentTime);

            image2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(image2.getContext(), "Long Pressed", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
    }
}
