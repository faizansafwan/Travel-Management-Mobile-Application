package com.fzn.tripsat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddEventAdapter extends RecyclerView.Adapter<AddEventAdapter.ViewHolder> {

    Context context;

    ArrayList<AddEventModel> eventModel;


    public AddEventAdapter(Context context, ArrayList<AddEventModel> eventModel){
        this.context=context;
        this.eventModel = eventModel;
    }
    @NonNull
    @Override
    public AddEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_event_layout, parent, false);
        return new AddEventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddEventAdapter.ViewHolder holder, int position) {
        AddEventModel eventList = eventModel.get(position);
        holder.groupName.setText(eventList.getGroupName());
        Glide.with(holder.groupIcon.getContext()).load(eventList.getGroupIcon())
                .circleCrop().into(holder.groupIcon);

    }

    @Override
    public int getItemCount() {
        return eventModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView groupIcon;
        TextView groupName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            groupIcon = itemView.findViewById(R.id.groupImage);
            groupName = itemView.findViewById(R.id.groupName);

            groupName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
