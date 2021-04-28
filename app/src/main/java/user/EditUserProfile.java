package user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.oop.petrehome.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditUserProfile extends AppCompatActivity {

    EditText update_user_email,update_user_fname,update_user_lname,update_user_phone;
    Spinner update_user_district_spinner,update_user_city_spinner;
    Button update_user_btn;
    ImageView update_user_back_btn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userID,default_district,default_city;

    private ArrayAdapter<District> districtArrayAdapter;
    private ArrayAdapter<City> cityArrayAdapter;

    private ArrayList<District> districts;
    private ArrayList<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        update_user_email =findViewById(R.id.update_user_email_txt);
        update_user_fname =findViewById(R.id.update_user_fname_txt);
        update_user_lname =findViewById(R.id.update_user_lname_txt);
        update_user_phone =findViewById(R.id.update_user_phone_txt);
        update_user_district_spinner =(Spinner) findViewById(R.id.update_user_district_spinner);
        update_user_city_spinner =(Spinner) findViewById(R.id.update_user_city_spinner);
        update_user_back_btn =findViewById(R.id.update_user_back_btn);
        update_user_btn = findViewById(R.id.update_user_btn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();

        if (fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }


            userID = fAuth.getCurrentUser().getUid();


            DocumentReference documentReference = fStore.collection("users").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    update_user_email.setText(value.getString("email"));
                    update_user_phone.setText(value.getString("phone"));
                    update_user_fname.setText(value.getString("first_name"));
                    update_user_lname.setText(value.getString("last_name"));
                    default_district = value.getString("district");
                    default_city = value.getString("city");

                    initializeUI();

                }
            });

        update_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String memail = update_user_email.getText().toString().trim();
                String mfname = update_user_fname.getText().toString().trim();
                String mlname = update_user_lname.getText().toString().trim();
                String mphone = update_user_phone.getText().toString().trim();
                String mdistrict = update_user_district_spinner.getSelectedItem().toString();
                String mcity = update_user_city_spinner.getSelectedItem().toString();

                if(TextUtils.isEmpty(memail)){
                    update_user_email.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(mfname)){
                    update_user_fname.setError("First name is required");
                    return;
                }
                if(TextUtils.isEmpty(mlname)){
                    update_user_lname.setError("Last name is required");
                    return;
                }
                if(TextUtils.isEmpty(mphone)){
                    update_user_phone.setError("Phone number is required");
                    return;
                }

                if (mdistrict.equals("Select District")){
                    Toast.makeText(EditUserProfile.this, "Select a District", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mcity.equals("Select City")){
                    Toast.makeText(EditUserProfile.this, "Select a City", Toast.LENGTH_SHORT).show();
                    return;
                }
                user.updateEmail(memail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference documentReference1 =fStore.collection("users").document(user.getUid());
                        Map<String,Object> user = new HashMap<>();
                        user.put("email",memail);
                        user.put("first_name",mfname);
                        user.put("last_name",mlname);
                        user.put("phone",mphone);
                        user.put("district",mdistrict);
                        user.put("city",mcity);

                        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditUserProfile.this, "Update Successful", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditUserProfile.this, "Update Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        });



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditUserProfile.this, "Email Already Exists", Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });

        update_user_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserProfile.class));
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);

            }
        });



    }
    private void initializeUI() {
        //assign values to district spinner and city spinner
//        String colors[] = {"Red","Blue","White","Yellow","Black", "Green","Purple","Orange","Grey"};
        districts = new ArrayList<>();
        cities = new ArrayList<>();

        createLists();

        districtArrayAdapter= new ArrayAdapter<> (this, android.R.layout.simple_spinner_item, districts); //selected item will look like a spinner set from XML
        districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_user_district_spinner.setAdapter(districtArrayAdapter);

        cityArrayAdapter= new ArrayAdapter<> (this, android.R.layout.simple_spinner_item, cities); //selected item will look like a spinner set from XML
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_user_city_spinner.setAdapter(cityArrayAdapter);

        update_user_district_spinner.setOnItemSelectedListener(district_listener);


    }
    private AdapterView.OnItemSelectedListener district_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (position > 0) {
                final District district =(District) update_user_district_spinner.getItemAtPosition(position);
                ArrayList<City> tempStates = new ArrayList<>();

                tempStates.add(new City(0, new District(0, "Select District"), "Select City"));
                for (City singleState : cities) {

                    if (singleState.getDistrict().getDistrictID() == district.getDistrictID()) {
                        tempStates.add(singleState);
                    }
                }
                cityArrayAdapter = new ArrayAdapter<>(getApplicationContext(),  android.R.layout.simple_spinner_item, tempStates);
                cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                update_user_city_spinner.setAdapter(cityArrayAdapter);

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void createLists() {




        District selectDistrict =new District(0, "Select District");
        District Batticaloa =new District(1, "Batticaloa");
        District Trincomalee =new District(2, "Trincomalee");

//        District Anuradhapura =new District(3, "Anuradhapura");
//        District Polonnaruwa =new District(4, "Polonnaruwa");
//        District Badulla =new District(5, "Badulla");
//        District Moneragala =new District(6, "Moneragala");
//        District Colombo =new District(7, "Colombo");
//        District Gampaha =new District(8, "Gampaha");

        districts.add(new District(0, default_district ));
        districts.add(new District(1, "Batticaloa"));
        districts.add(new District(2, "Trincomalee"));
        districts.add(new District(3, "Anuradhapura"));
        districts.add(new District(4, "Polonnaruwa"));
        districts.add(new District(5, "Badulla"));
        districts.add(new District(6, "Moneragala"));
        districts.add(new District(7, "Colombo"));
        districts.add(new District(8, "Gampaha"));

        City selectCity = new City(0 ,selectDistrict,default_city);
        City Araiyampathy = new City(0 ,Batticaloa,"Araiyampathy");
        City Chenkalady = new City(0 ,Batticaloa,"Chenkalady");
        City Eravur = new City(0 ,Batticaloa,"Eravur");
        City Kaluvanchikudy = new City(0 ,Batticaloa,"Kaluvanchikudy");
        City Gomarankadawala = new City(0 ,Trincomalee,"Gomarankadawala");
        City Kantalai = new City(0 ,Trincomalee,"Kantalai");
        City Kinniya = new City(0 ,Trincomalee,"Kinniya");
        City Kuchchaveli = new City(0 ,Trincomalee,"Kuchchaveli");

        cities.add(selectCity);
        cities.add(Araiyampathy);
        cities.add(Chenkalady);
        cities.add(Eravur);
        cities.add(Kaluvanchikudy);
        cities.add(Gomarankadawala);
        cities.add(Kantalai);
        cities.add(Kinniya);
        cities.add(Kuchchaveli);


    }
    private class District implements Comparable<District> {
        private int districtID;
        private String districtName;

        public District(int districtID,String districtName){
            this.districtID=districtID;
            this.districtName=districtName;
        }
        public int getDistrictID(){
            return districtID;
        }
        public String getDistrictName(){
            return districtName;
        }
        @Override
        public String toString() {
            return districtName;
        }


        @Override
        public int compareTo(District another) {
            return this.getDistrictID() - another.getDistrictID();//ascending order

        }

    }
    private class City implements Comparable<City> {

        private int cityID;
        private District district;
        private String cityName;

        public City(int cityID, District district, String cityName){
            this.cityID=cityID;
            this.district=district;
            this.cityName=cityName;
        }
        public int getCityID(){
            return cityID;
        }
        public District getDistrict(){
            return district;
        }
        public String getCityName(){
            return cityName;
        }
        @Override
        public String toString() {
            return cityName;
        }


        @Override
        public int compareTo(City another) {
            return this.getCityID() - another.getCityID();//ascending order
        }
    }
}