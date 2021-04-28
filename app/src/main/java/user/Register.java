package user;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oop.petrehome.MainActivity;
import com.oop.petrehome.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    Button login_instead,createAccount;
    EditText  password,fname,lname,phone,email;
    ImageView signing_back_btn;
    Spinner district_spinner ,city_spinner;

    private ArrayAdapter<District> districtArrayAdapter;
    private ArrayAdapter<City> cityArrayAdapter;

    private ArrayList<District> districts;
    private ArrayList<City> cities;

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login_instead = findViewById(R.id.login_instead);
        password = findViewById(R.id.reg_password_txt);
        fname = findViewById(R.id.reg_fname_txt);
        lname = findViewById(R.id.reg_lname_txt);
        phone = findViewById(R.id.reg_phone_txt);
        email = findViewById(R.id.reg_email_txt);
        district_spinner =(Spinner) findViewById(R.id.district_spinner);
        city_spinner = (Spinner)findViewById(R.id.city_spinner);
        createAccount = findViewById(R.id.createAccountBtn);
        signing_back_btn = findViewById(R.id.signing_back_btn);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        initializeUI();


        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

            finish();
        }


        signing_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            }
        });

        login_instead.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            }
        });


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memail = email.getText().toString().trim();
                String mpassword = password.getText().toString().trim();
                String mfname = fname.getText().toString().trim();
                String mlname = lname.getText().toString().trim();
                String mphone = phone.getText().toString().trim();
                String mdistrict = district_spinner.getSelectedItem().toString();
                String mcity = city_spinner.getSelectedItem().toString();

                if(TextUtils.isEmpty(memail)){
                    email.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(mpassword)){
                    password.setError("Password is required");
                    return;
                }
                if(mpassword.length() < 5){
                    password.setError("Password must greater than 4 characters");
                }
                if(TextUtils.isEmpty(mfname)){
                    fname.setError("First name is required");
                    return;
                }
                if(TextUtils.isEmpty(mlname)){
                    lname.setError("Last name is required");
                    return;
                }
                if(TextUtils.isEmpty(mphone)){
                    phone.setError("Phone number is required");
                    return;
                }

                if (mdistrict.equals("Select District")){
                    Toast.makeText(Register.this, "Select a District", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mcity.equals("Select City")){
                    Toast.makeText(Register.this, "Select a City", Toast.LENGTH_SHORT).show();
                    return;
                }

                fAuth.createUserWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference =fstore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("email",memail);
                            user.put("first_name",mfname);
                            user.put("last_name",mlname);
                            user.put("phone",mphone);
                            user.put("district",mdistrict);
                            user.put("city",mcity);
                            user.put("ListingCount",0);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"user profile is created for "+ userID);
                                }
                            });

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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
        district_spinner.setAdapter(districtArrayAdapter);

        cityArrayAdapter= new ArrayAdapter<> (this, android.R.layout.simple_spinner_item, cities); //selected item will look like a spinner set from XML
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_spinner.setAdapter(cityArrayAdapter);

        district_spinner.setOnItemSelectedListener(district_listener);
        city_spinner.setOnItemSelectedListener(city_listener);

    }
    private  AdapterView.OnItemSelectedListener city_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            TextView tv = (TextView) view;
            if (position == 0) {
                // Set the hint text color gray
                tv.setTextColor(Color.parseColor("#AAAAAA"));
                tv.setTextSize(14);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener district_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            TextView tv = (TextView) view;
            if (position == 0) {
                // Set the hint text color gray
                tv.setTextColor(Color.parseColor("#AAAAAA"));
                tv.setTextSize(14);
            }
            if (position > 0) {
                final District district =(District) district_spinner.getItemAtPosition(position);
                ArrayList<City> tempStates = new ArrayList<>();

                tempStates.add(new City(0, new District(0, "Select District"), "Select City"));
                for (City singleState : cities) {
                    if (singleState.getDistrict().getDistrictID() == district.getDistrictID()) {
                        tempStates.add(singleState);
                    }
                }
                cityArrayAdapter = new ArrayAdapter<>(getApplicationContext(),  android.R.layout.simple_spinner_item, tempStates);
                cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                city_spinner.setAdapter(cityArrayAdapter);

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

        districts.add(new District(0, "Select District"));
        districts.add(new District(1, "Batticaloa"));
        districts.add(new District(2, "Trincomalee"));
        districts.add(new District(3, "Anuradhapura"));
        districts.add(new District(4, "Polonnaruwa"));
        districts.add(new District(5, "Badulla"));
        districts.add(new District(6, "Moneragala"));
        districts.add(new District(7, "Colombo"));
        districts.add(new District(8, "Gampaha"));

        City selectCity = new City(0 ,selectDistrict,"Select City");
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

        public City(int cityID,District district, String cityName){
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
