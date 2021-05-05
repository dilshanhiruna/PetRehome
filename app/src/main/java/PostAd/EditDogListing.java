package PostAd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oop.petrehome.MainActivity;
import com.oop.petrehome.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import user.Login;
import user.Register;

public class EditDogListing extends AppCompatActivity {
    EditText title,age,description,email,phone;
    Spinner breed_spinner,gender_spinner,size_spinner,district_spinner ,city_spinner;;
    Button postad_newlisting_btn;
    ImageView postad_newlisting_back_btn,img1,img2,img3,img4,update_listing_delete_btn;

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String userID ;
    int count;
    private ArrayAdapter<District> districtArrayAdapter;
    private ArrayAdapter<City> cityArrayAdapter;

    private ArrayList<District> districts;
    private ArrayList<City> cities;

    Uri  img1URI1 = Uri.EMPTY,img1URI2= Uri.EMPTY,img1URI3= Uri.EMPTY,img1URI4= Uri.EMPTY;
    String BREED,CITY,GENDER,SIZE,DISTRICT;

    AlertDialog.Builder builder;
    public Long Lcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dog_listing);

        String USERID = getIntent().getExtras().getString("USERID");
        String IMGNUMBER = getIntent().getExtras().getString("IMGNUMBER");

        String TITLE = getIntent().getExtras().getString("TITLE");
         BREED = getIntent().getExtras().getString("BREED");
         CITY = getIntent().getExtras().getString("CITY");
        String AGE = getIntent().getExtras().getString("AGE");
         GENDER = getIntent().getExtras().getString("GENDER");
         SIZE = getIntent().getExtras().getString("SIZE");
        String DESCRIPTION = getIntent().getExtras().getString("DESCRIPTION");
        String EMAIL = getIntent().getExtras().getString("EMAIL");
        String MOBILE = getIntent().getExtras().getString("MOBILE");
         DISTRICT = getIntent().getExtras().getString("DISTRICT");

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
        update_listing_delete_btn=findViewById(R.id.update_listing_delete_btn);

        title.setText(TITLE);
        age.setText(AGE);
        description.setText(DESCRIPTION);
        email.setText(EMAIL);
        phone.setText(MOBILE);

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

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef1=  storageRef.child("users/"+USERID+"/"+ IMGNUMBER +"/img1.jpg");
        fileRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (!(uri.equals(Uri.EMPTY))){
                    Picasso.get().load(uri).into(img1);
                    img1.setScaleType(ImageView.ScaleType.FIT_XY);
                    img1URI1 = uri;
                }
            }
        });
        StorageReference fileRef2=  storageRef.child("users/"+USERID+"/"+ IMGNUMBER +"/img2.jpg");
        fileRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (!(uri.equals(Uri.EMPTY))){
                    Picasso.get().load(uri).into(img2);
                    img2.setScaleType(ImageView.ScaleType.FIT_XY);
                    img1URI2 = uri;
                }
            }
        });
        StorageReference fileRef3=  storageRef.child("users/"+USERID+"/"+ IMGNUMBER +"/img3.jpg");
        fileRef3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (!(uri.equals(Uri.EMPTY))){
                    Picasso.get().load(uri).into(img3);
                    img3.setScaleType(ImageView.ScaleType.FIT_XY);
                    img1URI3 = uri;
                }
            }
        });
        StorageReference fileRef4=  storageRef.child("users/"+USERID+"/"+ IMGNUMBER +"/img4.jpg");
        fileRef4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (!(uri.equals(Uri.EMPTY))){
                    Picasso.get().load(uri).into(img4);
                    img4.setScaleType(ImageView.ScaleType.FIT_XY);
                    img1URI4 = uri;
                }
            }
        });


//        DocumentReference documentReferenceCount = fstore.collection("users").document(USERID);
//        documentReferenceCount.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                count = value.getLong("ListingCount").intValue();
//            }
//        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(USERID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Lcount = (Long) snapshot.child("ListingCount").getValue();
                assert Lcount != null;
                count = Lcount.intValue();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

                String mtitle = capitalizeWord(title.getText().toString().trim());
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
                if(!isValidEmail(memail)){
                    email.setError("Email is invalid");
                    return;
                }
                if(TextUtils.isEmpty(mphone)){
                    phone.setError("Phone number is required");
                    return;
                }
                if (mbreed.equals("breed")){
                    Toast.makeText(EditDogListing.this, "Select a Breed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mgender.equals("gender")){
                    Toast.makeText(EditDogListing.this, "Select a Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (msize.equals("size")){
                    Toast.makeText(EditDogListing.this, "Select a Size", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mdistrict.equals("Select District")){
                    Toast.makeText(EditDogListing.this, "Select a District", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mcity.equals("Select City")){
                    Toast.makeText(EditDogListing.this, "Select a City", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((img1URI1.equals(Uri.EMPTY))){
                    Toast.makeText(EditDogListing.this, "Main Image Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                //update the current listing count by 1 of the user

                Map<String,Object> user = new HashMap<>();
                user.put("ListingCount",count);
                databaseReference.updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if (!(img1URI1.equals(Uri.EMPTY))){
                            StorageReference fileRef1 = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img1.jpg");
                            fileRef1.putFile(img1URI1);
                        }
                        if (!(img1URI2.equals(Uri.EMPTY))){
                            StorageReference fileRef2 = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img2.jpg");
                            fileRef2.putFile(img1URI2);
                        }
                        if (!(img1URI3.equals(Uri.EMPTY))){
                            StorageReference fileRef3 = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img3.jpg");
                            fileRef3.putFile(img1URI3);
                        }
                        if (!(img1URI4.equals(Uri.EMPTY))){
                            StorageReference fileRef4 = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img4.jpg");
                            fileRef4.putFile(img1URI4);
                        }

//                        DocumentReference documentReference =fstore.collection("DogListings").document(USERID).collection("Listings").document(String.valueOf(IMGNUMBER));
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("DogListings").child(USERID).child("Listings").child(String.valueOf(IMGNUMBER));
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

                        dbRef.updateChildren(DogListings).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(EditDogListing.this, "Listing Updated", Toast.LENGTH_SHORT).show();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditDogListing.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditDogListing.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        postad_newlisting_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        update_listing_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you want to delete this ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                DocumentReference df =fstore.collection("DogListings")
//                                        .document(USERID).collection("Listings").document(String.valueOf(IMGNUMBER));
                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("DogListings").child(USERID).child("Listings")
                                        .child(String.valueOf(IMGNUMBER));
                                dbRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (!(img1URI1.equals(Uri.EMPTY))){
                                            StorageReference dltStore = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img1.jpg");
                                            dltStore.delete();
                                        }
                                        if (!(img1URI2.equals(Uri.EMPTY))){
                                            StorageReference dltStore = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img2.jpg");
                                            dltStore.delete();
                                        }
                                        if (!(img1URI3.equals(Uri.EMPTY))){
                                            StorageReference dltStore = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img3.jpg");
                                            dltStore.delete();
                                        }
                                        if (!(img1URI4.equals(Uri.EMPTY))){
                                            StorageReference dltStore = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img4.jpg");
                                            dltStore.delete();
                                        }

                                        Toast.makeText(getApplicationContext(),"Delete Successful",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MyListings.class));
                                        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"Delete Unsuccessful",Toast.LENGTH_SHORT).show();
                                    }
                                });


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


    }

    private AdapterView.OnItemSelectedListener district_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
        District Colombo =new District(3, "Colombo");
        District Gampaha =new District(4, "Gampaha");
        District Galle =new District(5, "Galle");
        District Matara =new District(7, "Matara");
        District Kandy =new District(8, "Kandy");

        districts.add(new District(0, DISTRICT));
        districts.add(new District(1, "Batticaloa"));
        districts.add(new District(2, "Trincomalee"));
        districts.add(new District(3, "Colombo"));
        districts.add(new District(4, "Gampaha"));
        districts.add(new District(5, "Galle"));

        districts.add(new District(7, "Matara"));
        districts.add(new District(8, "Kandy"));


        City selectCity = new City(0 ,selectDistrict,CITY);
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

    private void initializeUI() {
        String breed[] = {BREED,"Labrador Retriever","German Shepherd","Bulldog","Beagle", "Poodle","Rottweiler","Boxer","Chihuahua"};
        String gender[] = {GENDER,"Male","Female"};
        String size[] = {SIZE,"Small","Medium","Large"};


        ArrayAdapter<String> dataAdapterbreed = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, breed);
        dataAdapterbreed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breed_spinner.setAdapter(dataAdapterbreed);

        ArrayAdapter<String> dataAdaptergender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        dataAdaptergender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(dataAdaptergender);

        ArrayAdapter<String> dataAdaptersize = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, size);
        dataAdaptersize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size_spinner.setAdapter(dataAdaptersize);



    }

    public static String capitalizeWord(String str){
        String words[]=str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


}