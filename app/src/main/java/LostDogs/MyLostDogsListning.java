package LostDogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oop.petrehome.MainActivity;
import com.oop.petrehome.R;

import java.util.ArrayList;
import java.util.List;

import DogWalkers.DogwalkersHome;
import LostDogs.Adapter;

import PetDayCares.MyDayCareListings;
import PostAd.MyListings;
import user.Login;
import user.UserProfile;



public class MyLostDogsListning extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storageReference;
    RecyclerView LD_my_listing_recyclerview;
    String userID ;
    List<String> uid;
    List<Integer> imgNumber;
    List<Integer> views;
    List<String> dogname;
    List<String> dogage;
    List<String> dogbreed;
    List<String> doglostdate;
    List<String> location;
    Integer finalI;
    LostDogs.Adapter adapternew;
    public int count=0;
    public Long Lcount;
    public Long VCcount;
    DatabaseReference databaseReference;


    Button nav_logout,nav_login,create_new_ad_btn;
    DrawerLayout drawerLayout;
    TextView nav_home_txt,nav_postad_txt,nav_lostdogs_txt,nav_dogwalkers_txt,nav_petdaycares_txt,nav_profile_txt;
    ProgressBar progressBar_listings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lost_dogs_listning);

        drawerLayout =findViewById(R.id.drawer_layout);

        nav_login =findViewById(R.id.nav_login);
        nav_logout =findViewById(R.id.nav_logout);

        nav_home_txt =findViewById(R.id.nav_home_txt);
        nav_postad_txt =findViewById(R.id.nav_postad_txt);
        nav_lostdogs_txt =findViewById(R.id.nav_lostdogs_txt);
        nav_dogwalkers_txt =findViewById(R.id.nav_dogwalkers_txt);
        nav_petdaycares_txt =findViewById(R.id.nav_petdaycares_txt);
        nav_profile_txt =findViewById(R.id.nav_profile_txt);
        create_new_ad_btn =findViewById(R.id.create_new_listing_btn);
        LD_my_listing_recyclerview =findViewById(R.id.my_listing_recyclerview);
        progressBar_listings =findViewById(R.id.progressBar_listings);

        //initialized firebaseAuth
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressBar_listings.setVisibility(View.VISIBLE);


        //check if user is already logged in
        if (fAuth.getCurrentUser() != null){
            userID = fAuth.getCurrentUser().getUid();
            uid = new ArrayList<>();
            imgNumber = new ArrayList<>();
            dogname = new ArrayList<>();
            dogage = new ArrayList<>();
            dogbreed = new ArrayList<>();
            doglostdate = new ArrayList<>();
            location = new ArrayList<>();
            views = new ArrayList<>();

            getListings(userID);

            nav_login.setVisibility(View.GONE);
            nav_logout.setVisibility(View.VISIBLE);
        }
        else {
            nav_logout.setVisibility(View.GONE);
            nav_login.setVisibility(View.VISIBLE);
            progressBar_listings.setVisibility(View.INVISIBLE);

        }

        create_new_ad_btn .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fAuth.getCurrentUser() != null){
                    startActivity(new Intent(getApplicationContext(), DogLDListning.class));
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
                else {
                    Intent i = new Intent(getApplicationContext(), Login.class).putExtra("from", "listing");
                    startActivity(i);
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
        Intent i = new Intent(getApplicationContext(), Login.class).putExtra("from", "main");
        startActivity(i);

    }

    public  void  logout(View view){
        FirebaseAuth.getInstance().signOut();
        nav_logout.setVisibility(View.GONE);
        nav_login.setVisibility(View.VISIBLE);



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
        startActivity(new Intent(getApplicationContext(), MyLostDogsListning.class));

    }
    public void navClickDogwalkers(View view){
        nav_home_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_postad_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_lostdogs_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_dogwalkers_txt.setTextColor(ContextCompat.getColor(this, R.color.black));
        nav_petdaycares_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_profile_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        startActivity(new Intent(getApplicationContext(), DogwalkersHome.class));

    }
    public void navClickPetDaycares(View view){
        nav_home_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_postad_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_lostdogs_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_dogwalkers_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        nav_petdaycares_txt.setTextColor(ContextCompat.getColor(this, R.color.black));
        nav_profile_txt.setTextColor(ContextCompat.getColor(this, R.color.black01));
        startActivity(new Intent(getApplicationContext(), MyDayCareListings.class));

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
    private void initializedAdapter(String userID){
        adapternew = new Adapter(getApplicationContext(),uid,imgNumber,dogname,dogage,dogbreed,doglostdate,location,userID,views);
        GridLayoutManager gridLayoutManagernew = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        LD_my_listing_recyclerview.setLayoutManager(gridLayoutManagernew);
        LD_my_listing_recyclerview.setAdapter(adapternew);
        progressBar_listings.setVisibility(View.INVISIBLE);



    }
    private  void getListings(String userID){

//        DocumentReference documentReferenceCount = fstore.collection("users").document(userID);
//        documentReferenceCount.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                assert value != null;
//                count = Objects.requireNonNull(value.getLong("ListingCount")).intValue();
//                getList(userID,count);
//
//                if (count ==0){
//                    progressBar_listings.setVisibility(View.INVISIBLE);
//                }
//
//            }
//        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("lostdogs").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    getList(userID);
                    if (count ==0){
                        progressBar_listings.setVisibility(View.INVISIBLE);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getList(String userID){

//            DocumentReference documentReference =fstore.collection("DogListings").document(userID).collection("Listings").document(String.valueOf(i));
            databaseReference = FirebaseDatabase.getInstance().getReference().child("LostDogListings").child(userID).child("Listings").child("1");

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot != null && snapshot.exists()){

                        uid.add(userID);
                        dogname.add(snapshot.child("dogname").getValue().toString());
                        dogage.add(snapshot.child("dogage").getValue().toString());
                        dogbreed.add(snapshot.child("doggender").getValue().toString());
                        doglostdate.add(snapshot.child("doglostdate").getValue().toString());
                        location.add(snapshot.child("olocation").getValue().toString());
                        VCcount = (Long) snapshot.child("viewCount").getValue();
                        views.add(VCcount.intValue());
                        initializedAdapter(userID);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

//            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//                @Override
//                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                    if (value != null && value.exists()){
//
//                        uid.add(userID);
//                        titles.add(value.getString("title"));
//                        breed.add(value.getString("breed"));
//                        gender.add(value.getString("gender"));
//                        district.add(value.getString("district"));
//                        city.add(value.getString("city"));
//                        imgNumber.add(finalI);
//                        views.add(Objects.requireNonNull(value.getLong("viewCount")).intValue());
//                        initializedAdapter(userID);
//                    }
//                }
//            });
        }

}