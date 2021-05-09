package LostDogs;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oop.petrehome.R;
import com.squareup.picasso.Picasso;

import java.util.List;

//import PostAd.DisplayDogAd;
//import PostAd.DisplayDogAd;
import LostDogs.Display_LD_AD;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    StorageReference storageReference;
    String UserID;
    List<String> dogname;
    List<String> uid;
    List<Integer> imgNumber;
    List<String> dogage;
    List<String> dogbreed;
    List<String> doglostdate;
    List<String> location;
    List<Integer> views;
    Context ctx;
    LayoutInflater layoutInflater;

    public Adapter(Context ctx, List<String> uid, List<Integer> imgNumber, List<String> dogname, List<String> dogage, List<String> dogbreed, List<String> doglostdate, List<String> location, String UserID, List<Integer> views){
        this.uid=uid;
        this.imgNumber=imgNumber;
        this.dogname = dogname;
        this.dogage = dogage;
        this.dogbreed= dogbreed;
        this.doglostdate = doglostdate;
        this.location= location;
        this.ctx=ctx;
        this.UserID=UserID;
        this.views=views;
        this.layoutInflater=LayoutInflater.from(ctx);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.listning_ld_ad_box,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Integer p = (Integer)position+1;
        holder.progressBar_listings_img.setVisibility(View.VISIBLE);
        holder.dogname.setText(dogname.get(position));
        holder.dogage.setText(dogage.get(position));
        holder.dogbreed.setText(dogbreed.get(position));
        holder.location.setText(location.get(position));
        holder.viewCount.setText(views.get(position).toString()+" Views");

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef=  storageRef.child("LDog_ads/"+uid.get(position)+"/"+ imgNumber.get(position).toString() +"/img1.jpg");
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

                Intent intent = new Intent(v.getContext(), MyLostDogsListning.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("USERID",uid.get(position));
                intent.putExtra("IMGNUMBER",imgNumber.get(position).toString());

                v.getContext().startActivity(intent);

            }

        });



    }

    @Override
    public int getItemCount() {
        return dogname.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dogname,dogage,dogbreed,location,viewCount;
        ImageView img;
        ProgressBar progressBar_listings_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            dogage =itemView.findViewById(R.id.ad_box_gender_txt);
            dogbreed =itemView.findViewById(R.id.ad_box_title_txt);
            location =itemView.findViewById(R.id.ad_box_location_txt);
            viewCount =itemView.findViewById(R.id.view_count_on_card);
//            progressBar_listings_img.setVisibility(View.VISIBLE);
            progressBar_listings_img =itemView.findViewById(R.id.progressBar_listings_img);

            img=itemView.findViewById(R.id.ad_box_img);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Toast.makeText(v.getContext(), imgNumber.get(getAdapterPosition()).toString() ,Toast.LENGTH_SHORT).show();
//                }
//            });

        }
    }




}



