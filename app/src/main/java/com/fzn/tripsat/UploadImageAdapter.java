package com.fzn.tripsat;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.ViewHolder>{

    private ArrayList<Uri> imagePaths;
    public UploadImageAdapter(ArrayList<Uri> imagePaths) {
        this.imagePaths = imagePaths;
    }

    @NonNull
    @Override
    public UploadImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_image_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadImageAdapter.ViewHolder holder, int position) {
        holder.uploadImage.setImageURI(imagePaths.get(position));
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView uploadImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            uploadImage = itemView.findViewById(R.id.uploadImg);
        }
    }
}
