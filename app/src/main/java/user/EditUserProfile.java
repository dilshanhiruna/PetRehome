package user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;
import com.oop.petrehome.MainActivity;
import com.oop.petrehome.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import PostAd.MyListings;

public class EditUserProfile extends AppCompatActivity {

    EditText update_user_email,update_user_fname,update_user_lname,update_user_phone;
    Spinner update_user_district_spinner,update_user_city_spinner;
    Button update_user_btn;
    ImageView update_user_back_btn,user_delete_btn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    DatabaseReference databaseReference;
    String userID,default_district,default_city;

    private ArrayAdapter<District> districtArrayAdapter;
    private ArrayAdapter<City> cityArrayAdapter;

    private ArrayList<District> districts;
    private ArrayList<City> cities;

    AlertDialog.Builder builder;
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
        user_delete_btn = findViewById(R.id.user_delete_btn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();

        //is user is not logged in, return to Login page
        if (fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }


        userID = fAuth.getCurrentUser().getUid();
        //fetch user data from DB
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //assign values to edit text fields
                update_user_email.setText(snapshot.child("email").getValue().toString());
                update_user_phone.setText(snapshot.child("phone").getValue().toString());
                update_user_fname.setText(snapshot.child("first_name").getValue().toString());
                update_user_lname.setText(snapshot.child("last_name").getValue().toString());
                default_district=snapshot.child("district").getValue().toString();
                default_city=snapshot.child("city").getValue().toString();
                initializeUI();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        update_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //assign edit text values to variables
                String memail = update_user_email.getText().toString().trim();
                String mfname = update_user_fname.getText().toString().trim();
                String mlname = update_user_lname.getText().toString().trim();
                String mphone = update_user_phone.getText().toString().trim();
                String mdistrict = update_user_district_spinner.getSelectedItem().toString();
                String mcity = update_user_city_spinner.getSelectedItem().toString();

                //validations
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
                //update email first
                user.updateEmail(memail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Map<String,Object> user = new HashMap<>();
                        user.put("email",memail);
                        user.put("first_name",mfname);
                        user.put("last_name",mlname);
                        user.put("phone",mphone);
                        user.put("district",mdistrict);
                        user.put("city",mcity);

                        //update other properties next
                        databaseReference.updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        user_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Permanently Delete Account")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //delete user
                                fAuth.getCurrentUser().delete();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Delete Listing");
                alert.show();

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
        District Colombo =new District(3, "Colombo");
        District Gampaha =new District(4, "Gampaha");
        District Galle =new District(5, "Galle");
        District Matara =new District(7, "Matara");
        District Kandy =new District(8, "Kandy");

        districts.add(new District(0, default_district));
        districts.add(new District(1, "Batticaloa"));
        districts.add(new District(2, "Trincomalee"));
        districts.add(new District(3, "Colombo"));
        districts.add(new District(4, "Gampaha"));
        districts.add(new District(5, "Galle"));
        districts.add(new District(7, "Matara"));
        districts.add(new District(8, "Kandy"));


        City selectCity = new City(0 ,selectDistrict,default_city);
        City Araiyampathy = new City(2 ,Batticaloa,"Araiyampathy");
        City Chenkalady = new City(3 ,Batticaloa,"Chenkalady");
        City Eravur = new City(3 ,Batticaloa,"Eravur");
        City Kaluvanchikudy = new City(4 ,Batticaloa,"Kaluvanchikudy");
        City Gomarankadawala = new City(5,Trincomalee,"Gomarankadawala");
        City Kantalai = new City(6 ,Trincomalee,"Kantalai");
        City Kinniya = new City(7 ,Trincomalee,"Kinniya");
        City Kuchchaveli = new City(8 ,Trincomalee,"Kuchchaveli");
        City Athurugiriya = new City(8 ,Colombo,"Athurugiriya");
        City Avissawella = new City(10 ,Colombo,"Avissawella");
        City Battaramulla = new City(11 ,Colombo,"Battaramulla");
        City Boralesgamuwa = new City(12 ,Colombo,"Boralesgamuwa");
        City Colombo1 = new City(13 ,Colombo,"Colombo 01");
        City Colombo2 = new City(14 ,Colombo,"Colombo 02");
        City Colombo3 = new City(15 ,Colombo,"Colombo 03");
        City Colombo4 = new City(16 ,Colombo,"Colombo 04");
        City Colombo5 = new City(17 ,Colombo,"Colombo 05");
        City Colombo6 = new City(18 ,Colombo,"Colombo 06");
        City Colombo7 = new City(19 ,Colombo,"Colombo 07");
        City Dehiwala = new City(20 ,Colombo,"Dehiwala");
        City Homagama = new City(21 ,Colombo,"Homagama");
        City Kaduwela = new City(22 ,Colombo,"Kaduwela");
        City Kotte = new City(23 ,Colombo,"Kotte");
        City Malabe = new City(24 ,Colombo,"Malabe");
        City MountLavinia = new City(25 ,Colombo,"Mount Lavinia");
        City Piliyandala = new City(26 ,Colombo,"Piliyandala");
        City Rajagiriya = new City(27 ,Colombo,"Rajagiriya");
        City Ratmalana = new City(28 ,Colombo,"Ratmalana");
        City Kesbewa = new City(29 ,Colombo,"Kesbewa");
        City Attanagalla = new City(30 ,Gampaha,"Attanagalla");
        City Divlapitiya = new City(31 ,Gampaha,"Divlapitiya");
        City Katana = new City(31 ,Gampaha,"Katana");
        City JaEla = new City(32 ,Gampaha,"Ja-Ela");
        City Negombo = new City(33 ,Gampaha,"Negombo");
        City Kelaniya = new City(34 ,Gampaha,"Kelaniya");
        City Ahangama = new City(35 ,Galle,"Ahangama");
        City Ambalangoda = new City(36 ,Galle,"Ambalangoda");
        City Baddegama = new City(37 ,Galle,"Baddegama");
        City Bentota = new City(37 ,Galle,"Bentota");
        City Boossa = new City(38 ,Galle,"Boossa");
        City Hikkaduwa = new City(39 ,Galle,"Hikkaduwa");
        City Karapitiya = new City(38 ,Galle,"Karapitiya");
        City Koggala = new City(39 ,Galle,"Koggala");
        City Akuressa = new City(40 ,Matara,"Akuressa");
        City Mirissa = new City(41 ,Matara,"Mirissa");
        City Weligama = new City(42 ,Matara,"Weligama");
        City Dikwella = new City(43 ,Matara,"Dikwella");
        City Gampola = new City(44 ,Kandy,"Gampola");
        City Peradeniya = new City(45 ,Kandy,"Peradeniya");
        City Alawatugoda = new City(46 ,Kandy,"Alawatugoda");
        City Kundasale = new City(47 ,Kandy,"Kundasale");

        cities.add(selectCity);
        cities.add(Gampola);
        cities.add(Peradeniya);
        cities.add(Alawatugoda);
        cities.add(Kundasale);

        cities.add(Akuressa);
        cities.add(Mirissa);
        cities.add(Weligama);
        cities.add(Dikwella);

        cities.add(Ahangama);
        cities.add(Ambalangoda);
        cities.add(Baddegama);
        cities.add(Bentota);
        cities.add(Boossa);
        cities.add(Hikkaduwa);
        cities.add(Karapitiya);
        cities.add(Koggala);

        cities.add(Attanagalla);
        cities.add(Divlapitiya);
        cities.add(Katana);
        cities.add(JaEla);
        cities.add(Negombo);
        cities.add(Kelaniya);


        cities.add(Araiyampathy);
        cities.add(Chenkalady);
        cities.add(Eravur);
        cities.add(Kaluvanchikudy);
        cities.add(Gomarankadawala);
        cities.add(Kantalai);
        cities.add(Kinniya);
        cities.add(Kuchchaveli);

        cities.add(Athurugiriya);
        cities.add(Avissawella);
        cities.add(Battaramulla);
        cities.add(Boralesgamuwa);
        cities.add(Colombo1);
        cities.add(Colombo2);
        cities.add(Colombo3);
        cities.add(Colombo4);
        cities.add(Colombo5);
        cities.add(Colombo6);
        cities.add(Colombo7);
        cities.add(Dehiwala);
        cities.add(Homagama);
        cities.add(Kaduwela);
        cities.add(Kotte);
        cities.add(Malabe);
        cities.add(MountLavinia);
        cities.add(Piliyandala);
        cities.add(Rajagiriya);
        cities.add(Ratmalana);
        cities.add(Kesbewa);


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