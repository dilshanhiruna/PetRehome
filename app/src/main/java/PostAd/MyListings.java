package PostAd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.oop.petrehome.MainActivity;
import com.oop.petrehome.R;

import user.Login;
import user.Register;
import user.UserProfile;

public class MyListings extends AppCompatActivity {
    FirebaseAuth fAuth;
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

        //initialized firebaseAuth
        fAuth = FirebaseAuth.getInstance();

        //check if user is already logged in
        if (fAuth.getCurrentUser() != null){
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



}