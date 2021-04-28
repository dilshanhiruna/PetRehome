package PostAd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.storage.UploadTask;
import com.oop.petrehome.MainActivity;
import com.oop.petrehome.R;

import java.util.HashMap;
import java.util.Map;

import user.Login;
import user.Register;

public class DogListing extends AppCompatActivity {
    EditText title,age,description,email,phone;
    Spinner breed_spinner,gender_spinner,size_spinner;
    Button postad_newlisting_btn;
    ImageView postad_newlisting_back_btn,img1,img2,img3,img4;
    int count =0;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storageReference;
    String userID ;

    Uri  img1URI1 = Uri.EMPTY,img1URI2= Uri.EMPTY,img1URI3= Uri.EMPTY,img1URI4= Uri.EMPTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_listing);

        title=findViewById(R.id.postad_newlisting_title);
        age=findViewById(R.id.postad_newlisting_age);
        description=findViewById(R.id.postad_newlisting_description);
        email=findViewById(R.id.postad_newlisting_email);
        phone=findViewById(R.id.postad_newlisting_phone);
        breed_spinner=(Spinner)findViewById(R.id.postad_newlisting_breed);
        gender_spinner=(Spinner)findViewById(R.id.postad_newlisting_gender);
        size_spinner=(Spinner)findViewById(R.id.postad_newlisting_size);
        postad_newlisting_btn=findViewById(R.id.postad_newlisting_btn);
        postad_newlisting_back_btn=findViewById(R.id.postad_newlisting_back_btn);

        img1=findViewById(R.id.dog_imageButton);
        img2=findViewById(R.id.dog_imageButton2);
        img3=findViewById(R.id.dog_imageButton3);
        img4=findViewById(R.id.dog_imageButton4);

        initializeUI();
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

        //getting the listing current count from the user
        DocumentReference documentReferenceCount = fstore.collection("users").document(userID);
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
                    Toast.makeText(DogListing.this, "Select a Breed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mgender.equals("gender")){
                    Toast.makeText(DogListing.this, "Select a Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (msize.equals("size")){
                    Toast.makeText(DogListing.this, "Select a Size", Toast.LENGTH_SHORT).show();
                    return;
                }


                count++;
                //update the current listing count by 1 of the user
                DocumentReference documentReferenceCount = fstore.collection("users").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("ListingCount",count);
                documentReferenceCount.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        DocumentReference documentReference =fstore.collection("DogListings").document(userID).collection("Listings").document(String.valueOf(count));
                        Map<String,Object> DogListings = new HashMap<>();
                        DogListings.put("title",mtitle);
                        DogListings.put("breed",mbreed);
                        DogListings.put("age",mage);
                        DogListings.put("gender",mgender);
                        DogListings.put("size",msize);
                        DogListings.put("description",mdescription);
                        DogListings.put("email",memail);
                        DogListings.put("phone",mphone);

                        documentReference.set(DogListings).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                if (!(img1URI1.equals(Uri.EMPTY))){
                                    StorageReference fileRef1 = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/"+count+"/img1.jpg");
                                    fileRef1.putFile(img1URI1);
                                }
                                if (!(img1URI2.equals(Uri.EMPTY))){
                                    StorageReference fileRef2 = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/"+count+"/img2.jpg");
                                    fileRef2.putFile(img1URI2);
                                }
                                if (!(img1URI3.equals(Uri.EMPTY))){
                                    StorageReference fileRef3 = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/"+count+"/img3.jpg");
                                    fileRef3.putFile(img1URI3);
                                }
                                if (!(img1URI4.equals(Uri.EMPTY))){
                                    StorageReference fileRef4 = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/"+count+"/img4.jpg");
                                    fileRef4.putFile(img1URI4);
                                }

                                Toast.makeText(DogListing.this, "Listing Published", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DogListing.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                        //add pictures to firebase


                        }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DogListing.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        postad_newlisting_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyListings.class));
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


    private void initializeUI() {
        String breed[] = {"breed","Blue","White","Yellow","Black", "Green","Purple","Orange","Grey"};
        String gender[] = {"gender","Blue","White","Yellow","Black", "Green","Purple","Orange","Grey"};
        String size[] = {"size","Blue","White","Yellow","Black", "Green","Purple","Orange","Grey"};


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