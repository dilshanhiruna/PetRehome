package LostDogs;

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
import android.util.Patterns;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
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



public class DogLDListning extends AppCompatActivity {

    EditText dname,dage,dgender,dsize,dbreed,dlostdate;
    EditText oname,ocontact,oemail,olocation,description;
    Button publish_ad_btn;
    ImageView myld_ads_back_btn,img1,img2,img3,img4;
    int count =0;
    Long Lcount;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storageReference;
    String userID ;
    DatabaseReference databaseReference;



    Uri img1URI1 = Uri.EMPTY,img1URI2= Uri.EMPTY,img1URI3= Uri.EMPTY,img1URI4= Uri.EMPTY;
    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_l_d_listning);

        dname=findViewById(R.id.txtdogname);
        dage=findViewById(R.id.txtdogage);
        dgender=findViewById(R.id.txtdogsize);
        dsize=findViewById(R.id.txtdogsize);
        dbreed=findViewById(R.id.txtdogbreed);
        dlostdate=findViewById(R.id.txtlostdate);
        oname=findViewById(R.id.txtownername);
        ocontact=findViewById(R.id.txtcontact);
        oemail=findViewById(R.id.txtxemail);
        olocation=findViewById(R.id.txtxlocation);
        description=findViewById(R.id.txtdes);
        img1=findViewById(R.id.LDimageView3);
        img2=findViewById(R.id.LDimageView4);
        img3=findViewById(R.id.LDimageView5);
        img4=findViewById(R.id.LDimageView6);
        //img5=findViewById(R.id.LDimageView7);
        publish_ad_btn=(Button) findViewById(R.id.LDpublishbtn);
        myld_ads_back_btn=findViewById(R.id.LDhome_btn);

        img1=findViewById(R.id.dog_imageButton);
        img2=findViewById(R.id.dog_imageButton2);
        img3=findViewById(R.id.dog_imageButton3);
        img4=findViewById(R.id.dog_imageButton4);



        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

        //getting the listing current count from the user
//        DocumentReference documentReferenceCount = fstore.collection("users").document(userID);
//        documentReferenceCount.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                count = value.getLong("ListingCount").intValue();
//            }
//        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("lostdogs").child(userID);
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




        publish_ad_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String Dname = capitalizeWord(dname.getText().toString().trim());
                String Dage = dage.getText().toString().trim();
                String Dgender = dgender.getText().toString().trim();
                String Dsize = dsize.getText().toString().trim();
                String Dbreed = dbreed.getText().toString().trim();
                String Dlostdate = dlostdate.getText().toString().trim();
                String Oname = oname.getText().toString().trim();
                String Ocontact = ocontact.getText().toString().trim();
                String Oemail = oemail.getText().toString().trim();
                String Olocation = olocation.getText().toString().trim();
                String Des = description.getText().toString().trim();


                if(TextUtils.isEmpty(Dname)){
                    dname.setError("Dog name is required");
                    return;
                }
                if(TextUtils.isEmpty(Dage)){
                    dage.setError("Dog Age is required");
                    return;
                }
                if(TextUtils.isEmpty(Dgender)){
                    dgender.setError("Dog gender is required");
                    return;
                }
                if(TextUtils.isEmpty(Dsize)){
                    dsize.setError("Dog Size is required");
                    return;
                }
                if(!isValidEmail(Dbreed)){
                    dbreed.setError("Dog breed is invalid");
                    return;
                }
                if(TextUtils.isEmpty(Dlostdate)){
                    dlostdate.setError("Lost Date is required");
                    return;
                }

                if(TextUtils.isEmpty(Oname)){
                    oname.setError("Owner Name is required");
                    return;
                }

                if(!isValidEmail(Oemail)){
                    oemail.setError("Email is invalid");
                    return;
                }
                if(TextUtils.isEmpty(Oemail)){
                    oemail.setError("Owner email is required");
                    return;
                }
                if(TextUtils.isEmpty(Olocation)){
                    olocation.setError("Owner location is required");
                    return;
                }
                if(TextUtils.isEmpty(Ocontact)){
                    ocontact.setError("Owner contact is required");
                    return;
                }
                if(TextUtils.isEmpty(Des)){
                    description.setError("Description about the lost dog is required");
                    return;
                }

                if ((img1URI1.equals(Uri.EMPTY))){
                    Toast.makeText(DogLDListning.this, "Main Image Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                count++;
                //update the current listing count by 1 of the user
//                DocumentReference documentReferenceCount = fstore.collection("users").document(userID);
                databaseReference = FirebaseDatabase.getInstance().getReference().child("lostdogs").child(userID);
                Map<String,Object> lostdog = new HashMap<>();
                lostdog.put("ListingCount",count);
                databaseReference.updateChildren(lostdog).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if (!(img1URI1.equals(Uri.EMPTY))){
                            StorageReference fileRef1 = storageReference.child("lostdogs/"+fAuth.getCurrentUser().getUid()+"/"+count+"/img1.jpg");
                            fileRef1.putFile(img1URI1);
                        }
                        if (!(img1URI2.equals(Uri.EMPTY))){
                            StorageReference fileRef2 = storageReference.child("lostdogs/"+fAuth.getCurrentUser().getUid()+"/"+count+"/img2.jpg");
                            fileRef2.putFile(img1URI2);
                        }
                        if (!(img1URI3.equals(Uri.EMPTY))){
                            StorageReference fileRef3 = storageReference.child("lostdogs/"+fAuth.getCurrentUser().getUid()+"/"+count+"/img3.jpg");
                            fileRef3.putFile(img1URI3);
                        }
                        if (!(img1URI4.equals(Uri.EMPTY))){
                            StorageReference fileRef4 = storageReference.child("lostdogs/"+fAuth.getCurrentUser().getUid()+"/"+count+"/img4.jpg");
                            fileRef4.putFile(img1URI4);
                        }

//                        DocumentReference documentReference =fstore.collection("DogListings").document(userID).collection("Listings").document(String.valueOf(count));
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("LostDogListings").child(userID).child("Listings").child(String.valueOf(count));
                        Map<String,Object> LostDogListings = new HashMap<>();
                        LostDogListings.put("dogname",Dname);
                        LostDogListings.put("dogage",Dage);
                        LostDogListings.put("doggender",Dgender);
                        LostDogListings.put("dogsize",Dsize);
                        LostDogListings.put("dogbreed",Dbreed);
                        LostDogListings.put("doglostdate",Dlostdate);
                        LostDogListings.put("ownername",Oname);
                        LostDogListings.put("contactno",Ocontact);
                        LostDogListings.put("email",Oemail);
                        LostDogListings.put("olocation",Olocation);
                        LostDogListings.put("description",Des);
                        LostDogListings.put("date",date);
                        LostDogListings.put("viewCount",1);


                        databaseReference.setValue(LostDogListings).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                Toast.makeText(LostDogs.DogLDListning.this, "Listing Published", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MyListings.class));
                                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LostDogs.DogLDListning.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LostDogs.DogLDListning.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        myld_ads_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyLostDogsListning.class));
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

















    public static String capitalizeWord(String str){

        String text = null;

        if (str.isEmpty()) {
            text= null;

        }else {
            String words[]=str.split("\\s");
            String capitalizeWord="";
            for(String w:words){
                String first=w.substring(0,1);
                String afterfirst=w.substring(1);
                capitalizeWord+=first.toUpperCase()+afterfirst+" ";
                text = capitalizeWord.trim();

            }
        }

        return text;
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}