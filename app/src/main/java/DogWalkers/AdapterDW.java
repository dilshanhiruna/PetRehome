package DogWalkers;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oop.petrehome.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import PostAd.Adapter;

public class AdapterDW extends RecyclerView.Adapter<AdapterDW.ViewHolder> {

    String UserID;
    List<String> name;
    List<String> uid;
    List<String> description;
    Context ctx;
    LayoutInflater layoutInflater;

    public AdapterDW (Context ctx,List<String> name,List<String> description,List<String> uid,String userID){
        this.uid = uid;
        this.UserID=userID;
        this.name = name;
        this.description = description;
        this.layoutInflater=LayoutInflater.from(ctx);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.dow_walker_card_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(name.get(position));
        holder.description.setText(description.get(position));

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef=  storageRef.child("dogwalkers/"+uid.get(position)+"/img.jpg");
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.image);
                holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),name.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.ad_dw_box_title_txt);
            description = itemView.findViewById(R.id.ad_dw_box_des_txt);
            image = itemView.findViewById(R.id.ad_dw_box_img);


        }
    }

}

