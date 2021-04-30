package PostAd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oop.petrehome.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DisplayDogAd extends AppCompatActivity {

    TextView  display_dog_ad_title,display_dog_ad_location,display_dog_ad_breed,
            display_dog_ad_age,display_dog_ad_gender,display_dog_ad_size,display_dog_ad_description,
            display_dog_ad_date,display_dog_ad_email,display_dog_ad_mobile,t1,t2,t3,t4,t5,t6,t7,t8;

    ImageView display_dog_ad_image;

    Button display_dog_ad_send_msg,display_dog_ad_call;
    ProgressBar progressBar_display_ad,progressBar_display_ad_img;

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storageReference;
    String userID ;
    ImageSlider imageSlider;
    List<SlideModel> slideModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_dog_ad);
        display_dog_ad_title =findViewById(R.id.display_dog_ad_title);
        display_dog_ad_location =findViewById(R.id.display_dog_ad_location);
        display_dog_ad_breed =findViewById(R.id.display_dog_ad_breed);
        display_dog_ad_age =findViewById(R.id.display_dog_ad_age);
        display_dog_ad_gender =findViewById(R.id.display_dog_ad_gender);
        display_dog_ad_size =findViewById(R.id.display_dog_ad_size);
        display_dog_ad_description =findViewById(R.id.display_dog_ad_description);
        display_dog_ad_date =findViewById(R.id.display_dog_ad_date);
        display_dog_ad_email =findViewById(R.id.display_dog_ad_email);
        display_dog_ad_mobile =findViewById(R.id.display_dog_ad_mobile);

        t1 =findViewById(R.id.textView15);
        t2 =findViewById(R.id.textView16);
        t3 =findViewById(R.id.textView17);
        t4 =findViewById(R.id.textView18);
        t5 =findViewById(R.id.textView23);
        t6 =findViewById(R.id.textView25);
        t7 =findViewById(R.id.textView27);
        t8 =findViewById(R.id.textView28);


        progressBar_display_ad =findViewById(R.id.progressBar_display_ad);
        progressBar_display_ad_img =findViewById(R.id.progressBar_display_ad_img);
        progressBar_display_ad.setVisibility(View.VISIBLE);
        progressBar_display_ad_img.setVisibility(View.VISIBLE);

        display_dog_ad_send_msg =findViewById(R.id.display_dog_ad_send_msg);
        display_dog_ad_call =findViewById(R.id.display_dog_ad_call);
        hideText();
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        imageSlider =findViewById(R.id.display_dog_ad_image);

        slideModels = new ArrayList<>();



        String USERID = getIntent().getExtras().getString("USERID");
        String IMGNUMBER = getIntent().getExtras().getString("IMGNUMBER");
//        Toast.makeText(this, USERID+" "+IMGNUMBER,Toast.LENGTH_SHORT).show();

        DocumentReference documentReference =fstore.collection("DogListings").document(USERID).collection("Listings").document(IMGNUMBER);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()){

                    display_dog_ad_title.setText(value.getString("title"));
                    display_dog_ad_breed.setText(value.getString("breed"));
                    display_dog_ad_gender.setText(value.getString("gender"));
                    display_dog_ad_age.setText(value.getString("age"));
                    display_dog_ad_size.setText(value.getString("size"));
                    display_dog_ad_description.setText(value.getString("description"));
                    display_dog_ad_email.setText(value.getString("email"));
                    display_dog_ad_mobile.setText(value.getString("phone"));
                    display_dog_ad_date.setText(value.getString("date"));
                    display_dog_ad_location.setText(value.getString("city")+", "+value.getString("district"));
                    progressBar_display_ad.setVisibility(View.INVISIBLE);
                    showText();

                }
            }

        });
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef=  storageRef.child("users/"+USERID+"/"+ IMGNUMBER+"/img1.jpg");
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (!(uri.equals(Uri.EMPTY))){
                    slideModels.add(new SlideModel(uri.toString()));
                    imageSlider.setImageList(slideModels,true);
                }

            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                for (int i =2;i<5;i++){
                    Integer x = (Integer)i;
                    StorageReference fileRef=  storageRef.child("users/"+USERID+"/"+ IMGNUMBER+"/img"+x.toString() +".jpg");
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (!(uri.equals(Uri.EMPTY))){
                                slideModels.add(new SlideModel(uri.toString()));
                            }
//                  display_dog_ad_image.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageSlider.setImageList(slideModels,true);

                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            progressBar_display_ad_img.setVisibility(View.INVISIBLE);
                        }
                    });
                }


            }
        });


    }


    public  void hideText(){
        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);
        t3.setVisibility(View.INVISIBLE);
        t4.setVisibility(View.INVISIBLE);
        t5.setVisibility(View.INVISIBLE);
        t6.setVisibility(View.INVISIBLE);
        t7.setVisibility(View.INVISIBLE);
        t8.setVisibility(View.INVISIBLE);
        display_dog_ad_send_msg.setVisibility(View.INVISIBLE);
        display_dog_ad_call.setVisibility(View.INVISIBLE);
    }
    public  void showText(){
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);
        t3.setVisibility(View.VISIBLE);
        t4.setVisibility(View.VISIBLE);
        t5.setVisibility(View.VISIBLE);
        t6.setVisibility(View.VISIBLE);
        t7.setVisibility(View.VISIBLE);
        t8.setVisibility(View.VISIBLE);
        display_dog_ad_send_msg.setVisibility(View.VISIBLE);
        display_dog_ad_call.setVisibility(View.VISIBLE);
    }

}