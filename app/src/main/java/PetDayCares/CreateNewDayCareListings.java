package PetDayCares;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oop.petrehome.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import PostAd.MyListings;

public class CreateNewDayCareListings extends AppCompatActivity {

    EditText title,age,description,email,phone;
    Spinner breed_spinner,gender_spinner,size_spinner,district_spinner ,city_spinner;;
    Button postad_newlisting_btn;
    ImageView postad_newlisting_back_btn,img1,img2,img3,img4;
    int count =0;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storageReference;
    String userID ;

    private ArrayAdapter<District> districtArrayAdapter;
    private ArrayAdapter<City> cityArrayAdapter;

    private ArrayList<District> districts;
    private ArrayList<City> cities;

    Uri  img1URI1 = Uri.EMPTY,img1URI2= Uri.EMPTY,img1URI3= Uri.EMPTY,img1URI4= Uri.EMPTY;
    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_day_care_listings);

        title=findViewById(R.id.postad_newlisting_title);
        age=findViewById(R.id.postad_newlisting_age);
        description=findViewById(R.id.postad_newlisting_description);
        email=findViewById(R.id.postad_newlisting_email);
        phone=findViewById(R.id.postad_newlisting_phone);
        breed_spinner=(Spinner)findViewById(R.id.postad_newlisting_breed);
        gender_spinner=(Spinner)findViewById(R.id.postad_newlisting_gender);
        size_spinner=(Spinner)findViewById(R.id.postad_newlisting_size);
        district_spinner=(Spinner)findViewById(R.id.postad_newlisting_district);
        city_spinner=(Spinner)findViewById(R.id.postad_newlisting_city);
        postad_newlisting_btn=findViewById(R.id.postad_newlisting_btn);
        postad_newlisting_back_btn=findViewById(R.id.postad_newlisting_back_btn);

        img1=findViewById(R.id.dog_imageButton);
        img2=findViewById(R.id.dog_imageButton2);
        img3=findViewById(R.id.dog_imageButton3);
        img4=findViewById(R.id.dog_imageButton4);

        initializeUIDC();
        initializeUI();

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

        //getting the listing current count from the user
        DocumentReference documentReferenceCount = fstore.collection("Daycare").document(userID);
        documentReferenceCount.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                count = value.getLong("ListingCount").intValue();
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_Gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(open_Gallery,1111);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_Gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(open_Gallery,2222);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_Gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(open_Gallery,3333);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_Gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(open_Gallery,4444);
            }
        });




        postad_newlisting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mtitle = title.getText().toString().trim();
                String mage = age.getText().toString().trim();
                String mdescription = description.getText().toString().trim();
                String memail = email.getText().toString().trim();
                String mphone = phone.getText().toString().trim();
                String mbreed = breed_spinner.getSelectedItem().toString();
                String mgender = gender_spinner.getSelectedItem().toString();
                String msize = size_spinner.getSelectedItem().toString();
                String mdistrict = district_spinner.getSelectedItem().toString();
                String mcity = city_spinner.getSelectedItem().toString();

                if(TextUtils.isEmpty(mtitle)){
                    title.setError("Title is required");
                    return;
                }
                if(TextUtils.isEmpty(mage)){
                    age.setError("Age is required");
                    return;
                }
                if(TextUtils.isEmpty(mdescription)){
                    description.setError("Description is required");
                    return;
                }
                if(TextUtils.isEmpty(memail)){
                    email.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(mphone)){
                    phone.setError("Phone number is required");
                    return;
                }
                if (mbreed.equals("breed")){
                    Toast.makeText(CreateNewDayCareListings.this, "Select a Breed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mgender.equals("gender")){
                    Toast.makeText(CreateNewDayCareListings.this, "Select a Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (msize.equals("size")){
                    Toast.makeText(CreateNewDayCareListings.this, "Select a Size", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mdistrict.equals("Select District")){
                    Toast.makeText(CreateNewDayCareListings.this, "Select a District", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mcity.equals("Select City")){
                    Toast.makeText(CreateNewDayCareListings.this, "Select a City", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((img1URI1.equals(Uri.EMPTY))){
                    Toast.makeText(CreateNewDayCareListings.this, "Main Image Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                count++;
                //update the current listing count by 1 of the user
                DocumentReference documentReferenceCount = fstore.collection("daycare").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("ListingCount",count);
                documentReferenceCount.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if (!(img1URI1.equals(Uri.EMPTY))){
                            StorageReference fileRef1 = storageReference.child("daycare/"+fAuth.getCurrentUser().getUid()+"/"+count+"/img1.jpg");
                            fileRef1.putFile(img1URI1);
                        }
                        if (!(img1URI2.equals(Uri.EMPTY))){
                            StorageReference fileRef2 = storageReference.child("daycare/"+fAuth.getCurrentUser().getUid()+"/"+count+"/img2.jpg");
                            fileRef2.putFile(img1URI2);
                        }
                        if (!(img1URI3.equals(Uri.EMPTY))){
                            StorageReference fileRef3 = storageReference.child("daycare/"+fAuth.getCurrentUser().getUid()+"/"+count+"/img3.jpg");
                            fileRef3.putFile(img1URI3);
                        }
                        if (!(img1URI4.equals(Uri.EMPTY))){
                            StorageReference fileRef4 = storageReference.child("daycare/"+fAuth.getCurrentUser().getUid()+"/"+count+"/img4.jpg");
                            fileRef4.putFile(img1URI4);
                        }

                        DocumentReference documentReference =fstore.collection("CreateNewDayCareListings").document(userID).collection("Listings").document(String.valueOf(count));
                        Map<String,Object> DogListings = new HashMap<>();
                        DogListings.put("title",mtitle);
                        DogListings.put("breed",mbreed);
                        DogListings.put("age",mage);
                        DogListings.put("gender",mgender);
                        DogListings.put("size",msize);
                        DogListings.put("description",mdescription);
                        DogListings.put("email",memail);
                        DogListings.put("phone",mphone);
                        DogListings.put("district",mdistrict);
                        DogListings.put("city",mcity);
                        DogListings.put("date",date);


                        documentReference.set(DogListings).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                Toast.makeText(CreateNewDayCareListings.this, "Listing Published", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MyListings.class));
                                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateNewDayCareListings.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateNewDayCareListings.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        postad_newlisting_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyDayCareListings.class));
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1111){
            if (resultCode == Activity.RESULT_OK ){

                img1URI1 = data.getData();
                img1.setImageURI(img1URI1);
                img1.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
        if (requestCode ==2222){
            if (resultCode == Activity.RESULT_OK ){

                img1URI2 = data.getData();
                img2.setImageURI(img1URI2);
                img2.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
        if (requestCode ==3333){
            if (resultCode == Activity.RESULT_OK ){

                img1URI3 = data.getData();
                img3.setImageURI(img1URI3);
                img3.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
        if (requestCode ==4444){
            if (resultCode == Activity.RESULT_OK ){

                img1URI4 = data.getData();
                img4.setImageURI(img1URI4);
                img4.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }

    }

    private void initializeUIDC() {
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

       /* @Override
        public int compareTo(CreateNewDayCareListings.District district) {
            return 0;
        }*/
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

    private void initializeUI() {
        String breed[] = {"breed","Any Breed","Labrador Retriever","German Shepherd","Bulldog","Beagle", "Poodle","Rottweiler","Boxer","Chihuahua"};
        String gender[] = {"gender","Any Gender","Male","Female"};
        String size[] = {"size","Any Size","Small","Medium","Large"};


        ArrayAdapter<String> dataAdapterbreed = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, breed);
        dataAdapterbreed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breed_spinner.setAdapter(dataAdapterbreed);

        ArrayAdapter<String> dataAdaptergender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        dataAdaptergender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(dataAdaptergender);

        ArrayAdapter<String> dataAdaptersize = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, size);
        dataAdaptersize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size_spinner.setAdapter(dataAdaptersize);

        breed_spinner.setOnItemSelectedListener(breed_listner);
        gender_spinner.setOnItemSelectedListener(gender_listner);
        size_spinner.setOnItemSelectedListener(size_listner);

    }
    private AdapterView.OnItemSelectedListener breed_listner = new AdapterView.OnItemSelectedListener() {
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
    } ;
    private AdapterView.OnItemSelectedListener gender_listner = new AdapterView.OnItemSelectedListener() {
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
    } ;
    private AdapterView.OnItemSelectedListener size_listner = new AdapterView.OnItemSelectedListener() {
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
    } ;

}