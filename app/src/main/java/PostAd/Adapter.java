package PostAd;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oop.petrehome.MainActivity;
import com.oop.petrehome.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import user.UserProfile;

import static androidx.core.content.ContextCompat.startActivity;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    StorageReference storageReference;
    String UserID;
    List<String> title;
    List<String> uid;
    List<Integer> imgNumber;
    List<String> breed;
    List<String> district;
    List<String> city;
    List<Integer> views;
    List<String> gender;
    Context ctx;
    LayoutInflater layoutInflater;

    public Adapter (Context ctx,List<String> uid,List<Integer> imgNumber,List<String> title,List<String> breed,List<String> gender,List<String> district,List<String> city, String UserID,List<Integer> views){
        this.uid=uid;
        this.imgNumber=imgNumber;
        this.title=title;
        this.breed=breed;
        this.district=district;
        this.city=city;
        this.gender=gender;
        this.ctx=ctx;
        this.UserID=UserID;
        this.views=views;
        this.layoutInflater=LayoutInflater.from(ctx);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.listing_ad_box_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Integer p = (Integer)position+1;
        holder.progressBar_listings_img.setVisibility(View.VISIBLE);
        holder.title.setText(title.get(position));
        holder.breed.setText(breed.get(position));
        holder.location.setText(city.get(position)+", "+district.get(position));
        holder.gender.setText(gender.get(position));
        holder.viewCount.setText(views.get(position).toString()+" Views");

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef=  storageRef.child("users/"+uid.get(position)+"/"+ imgNumber.get(position).toString() +"/img1.jpg");
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.img);
                holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.progressBar_listings_img.setVisibility(View.INVISIBLE);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), DisplayDogAd.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("USERID",uid.get(position));
                intent.putExtra("IMGNUMBER",imgNumber.get(position).toString());

                v.getContext().startActivity(intent);

            }

        });



    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView  title,breed,gender,location,viewCount;
        ImageView img;
        ProgressBar progressBar_listings_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           breed =itemView.findViewById(R.id.ad_box_breed_txt);
           gender =itemView.findViewById(R.id.ad_box_gender_txt);
           title =itemView.findViewById(R.id.ad_box_title_txt);
           location =itemView.findViewById(R.id.ad_box_location_txt);
           viewCount =itemView.findViewById(R.id.view_count_on_card);
//            progressBar_listings_img.setVisibility(View.VISIBLE);
           progressBar_listings_img =itemView.findViewById(R.id.progressBar_listings_img);

           img=itemView.findViewById(R.id.ad_box_img);



        }
    }




}
