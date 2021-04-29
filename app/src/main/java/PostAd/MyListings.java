package PostAd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oop.petrehome.MainActivity;
import com.oop.petrehome.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import user.Login;
import user.Register;
import user.UserProfile;

public class MyListings extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storageReference;
    RecyclerView my_listing_recyclerview;
    String userID ;
    List<String> titles;
    List<String> breed;
    List<String> gender;
    List<String> district;
    List<String> city;
    List<Integer> images;
    Adapter adapternew;

    Button nav_logout,nav_login,create_new_listing_btn;
    DrawerLayout drawerLayout;
    TextView nav_home_txt,nav_postad_txt,nav_lostdogs_txt,nav_dogwalkers_txt,nav_petdaycares_txt,nav_profile_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        drawerLayout =findViewById(R.id.drawer_layout);

        nav_login =findViewById(R.id.nav_login);
        nav_logout =findViewById(R.id.nav_logout);

        nav_home_txt =findViewById(R.id.nav_home_txt);
        nav_postad_txt =findViewById(R.id.nav_postad_txt);
        nav_lostdogs_txt =findViewById(R.id.nav_lostdogs_txt);
        nav_dogwalkers_txt =findViewById(R.id.nav_dogwalkers_txt);
        nav_petdaycares_txt =findViewById(R.id.nav_petdaycares_txt);
        nav_profile_txt =findViewById(R.id.nav_profile_txt);
        create_new_listing_btn =findViewById(R.id.create_new_listing_btn);
        my_listing_recyclerview =findViewById(R.id.my_listing_recyclerview);

        //initialized firebaseAuth
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //check if user is already logged in
        if (fAuth.getCurrentUser() != null){
            userID = fAuth.getCurrentUser().getUid();
            titles = new ArrayList<>();
            breed = new ArrayList<>();
            gender = new ArrayList<>();
            district = new ArrayList<>();
            city = new ArrayList<>();
            images =new ArrayList<>();

            for (int i = 1 ; i<50 ;i++){
                DocumentReference documentReference =fstore.collection("DogListings").document(userID).collection("Listings").document(String.valueOf(i));
                int finalI = i;
                documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()){

                            titles.add(value.getString("title"));
                            breed.add(value.getString("breed"));
                            gender.add(value.getString("gender"));
                            district.add(value.getString("district"));
                            city.add(value.getString("city"));
                            images.add(finalI);
//                            Toast.makeText(getApplicationContext(), value.getString("title"), Toast.LENGTH_SHORT).show();
                            initializedAdapter();
                        }
                    }
                });
            }
            nav_login.setVisibility(View.GONE);
            nav_logout.setVisibility(View.VISIBLE);
        }
        else {
            nav_login.setVisibility(View.VISIBLE);
            nav_logout.setVisibility(View.GONE);
        }

        create_new_listing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fAuth.getCurrentUser() != null){
                    startActivity(new Intent(getApplicationContext(), DogListing.class));
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
                else {
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }

            }
        });


    }





    public  void ClickMenu(View view){
        //open drawer
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layer
        drawerLayout.openDrawer(GravityCompat.START);

    }

    //navigation drawer button functions
    public  void  login(View view){
        startActivity(new Intent(getApplicationContext(), Login.class));

    }

    public  void  logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();

    }
    public void navClickHome(View view){
        nav_home_txt.setTextColor(ContextCompat.getColor(this, R.color.black));
        nav_postad_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_lostdogs_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_dogwalkers_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_petdaycares_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_profile_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));


        startActivity(new Intent(getApplicationContext(), MainActivity.class));


    }
    public void navClickPostad(View view){
        nav_home_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_postad_txt.setTextColor(ContextCompat.getColor(this, R.color.black));
        nav_lostdogs_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_dogwalkers_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_petdaycares_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_profile_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        startActivity(new Intent(getApplicationContext(), MyListings.class));

    }
    public void navClickLostdogs(View view){
        nav_home_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_postad_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_lostdogs_txt.setTextColor(ContextCompat.getColor(this, R.color.black));
        nav_dogwalkers_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_petdaycares_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_profile_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
//        startActivity(new Intent(getApplicationContext(), Activity_Here.class));

    }
    public void navClickDogwalkers(View view){
        nav_home_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_postad_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_lostdogs_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_dogwalkers_txt.setTextColor(ContextCompat.getColor(this, R.color.black));
        nav_petdaycares_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_profile_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
//        startActivity(new Intent(getApplicationContext(), Activity_Here.class));

    }
    public void navClickPetDaycares(View view){
        nav_home_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_postad_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_lostdogs_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_dogwalkers_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_petdaycares_txt.setTextColor(ContextCompat.getColor(this, R.color.black));
        nav_profile_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
//        startActivity(new Intent(getApplicationContext(), Activity_Here.class));

    }
    public void navClickProfile(View view){
        nav_home_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_postad_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_lostdogs_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_dogwalkers_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_petdaycares_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_profile_txt.setTextColor(ContextCompat.getColor(this, R.color.black));
        startActivity(new Intent(getApplicationContext(), UserProfile.class));

    }
    private void initializedAdapter(){
        adapternew = new Adapter(getApplicationContext(),titles,breed,gender,district,city, images,userID);
        GridLayoutManager gridLayoutManagernew = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        my_listing_recyclerview.setLayoutManager(gridLayoutManagernew);
        my_listing_recyclerview.setAdapter(adapternew);
    }


}