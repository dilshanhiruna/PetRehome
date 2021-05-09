package LostDogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oop.petrehome.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import PostAd.MyListings;

/*public class EditLostDogListning extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lost_dog_listning);
    }
}*/

public class EditLostDogListning extends AppCompatActivity {


    EditText dname,dage,dgender,dsize,dbreed,dlostdate;
    EditText oname,ocontact,oemail,olocation,description;
    Button edit_ad_btn;
    ImageView myld_ads_back_btn,img1,img2,img3,img4,delete_btn;


    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String userID ;
    int count;


    Uri img1URI1 = Uri.EMPTY,img1URI2= Uri.EMPTY,img1URI3= Uri.EMPTY,img1URI4= Uri.EMPTY;
    String BREED,CITY,GENDER,SIZE,DISTRICT;

    AlertDialog.Builder builder;
    public Long Lcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lost_dog_listning);

        String USERID = getIntent().getExtras().getString("USERID");
        String IMGNUMBER = getIntent().getExtras().getString("IMGNUMBER");

        String DOGNAME = getIntent().getExtras().getString("dogname");
        String DOGAGE = getIntent().getExtras().getString("dogage");
        String DOGGENDER = getIntent().getExtras().getString("doggender");
        String DOGSIZE = getIntent().getExtras().getString("dogsize");
        String DOGBREED = getIntent().getExtras().getString("dogbreed");
        String LOSTDATE = getIntent().getExtras().getString("doglostdate");
        String OWNERNAME = getIntent().getExtras().getString("ownername");
        String OWNERCONTACT = getIntent().getExtras().getString("contactno");
        String OWNEREMAIL = getIntent().getExtras().getString("email");
        String OWNERLOCATION = getIntent().getExtras().getString("olocation");
        String DESCRIPTION = getIntent().getExtras().getString("description");

        dname=findViewById(R.id.txtdogname);
        dage=findViewById(R.id.txtdogage);
        dgender=findViewById(R.id.txtdoggender);
        dsize=findViewById(R.id.txtdogsize);
        dbreed=findViewById(R.id.txtdogbreed);
        dlostdate=findViewById(R.id.txtlostdate);
        oname=findViewById(R.id.txtownername);
        ocontact=findViewById(R.id.txtcontact);
        oemail=findViewById(R.id.txtxemail);
        olocation=findViewById(R.id.txtxlocation);
        description=findViewById(R.id.txtdes);
        edit_ad_btn=findViewById(R.id.btnedit);
        myld_ads_back_btn=findViewById(R.id.LDmylist_btn);
        delete_btn=findViewById(R.id.update_listing_delete_btn);

        dname.setText(DOGNAME);
        dage.setText(DOGAGE);
        dgender.setText(DOGGENDER);
        dsize.setText(DOGSIZE);
        dbreed.setText(DOGBREED);
        dlostdate.setText(LOSTDATE);
        oname.setText(OWNERNAME);
        ocontact.setText(OWNERCONTACT);
        oemail.setText(OWNEREMAIL);
        olocation.setText(OWNERLOCATION);
        description.setText(DESCRIPTION);

        img1=findViewById(R.id.dog_imageButton);
        img2=findViewById(R.id.dog_imageButton2);
        img3=findViewById(R.id.dog_imageButton3);
        img4=findViewById(R.id.dog_imageButton4);




        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();



        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef1=  storageRef.child("lostdogs/"+USERID+"/"+ IMGNUMBER +"/img1.jpg");
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
        StorageReference fileRef2=  storageRef.child("lostdogs/"+USERID+"/"+ IMGNUMBER +"/img2.jpg");
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
        StorageReference fileRef3=  storageRef.child("lostdogs/"+USERID+"/"+ IMGNUMBER +"/img3.jpg");
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
        StorageReference fileRef4=  storageRef.child("lostdogs/"+USERID+"/"+ IMGNUMBER +"/img4.jpg");
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




        databaseReference = FirebaseDatabase.getInstance().getReference().child("lostdogs").child(USERID);
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


        edit_ad_btn.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(EditLostDogListning.this, "Main Image Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                //update the current listing count by 1 of the user

                Map<String,Object> lostdog = new HashMap<>();
                lostdog.put("ListingCount",count);
                databaseReference.updateChildren(lostdog).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if (!(img1URI1.equals(Uri.EMPTY))){
                            StorageReference fileRef1 = storageReference.child("lostdogs/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img1.jpg");
                            fileRef1.putFile(img1URI1);
                        }
                        if (!(img1URI2.equals(Uri.EMPTY))){
                            StorageReference fileRef2 = storageReference.child("lostdogs/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img2.jpg");
                            fileRef2.putFile(img1URI2);
                        }
                        if (!(img1URI3.equals(Uri.EMPTY))){
                            StorageReference fileRef3 = storageReference.child("lostdogs/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img3.jpg");
                            fileRef3.putFile(img1URI3);
                        }
                        if (!(img1URI4.equals(Uri.EMPTY))){
                            StorageReference fileRef4 = storageReference.child("lostdogs/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img4.jpg");
                            fileRef4.putFile(img1URI4);
                        }

//
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("LostDogListings").child(USERID).child("Listings").child(String.valueOf(IMGNUMBER));
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
                        dbRef.updateChildren(LostDogListings).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(LostDogs.EditLostDogListning.this, "Listing Updated", Toast.LENGTH_SHORT).show();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LostDogs.EditLostDogListning.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LostDogs.EditLostDogListning.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        myld_ads_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you want to delete this ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("LostDogListings").child(USERID).child("Listings")
                                        .child(String.valueOf(IMGNUMBER));
                                dbRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (!(img1URI1.equals(Uri.EMPTY))){
                                            StorageReference dltStore = storageReference.child("lostdogs/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img1.jpg");
                                            dltStore.delete();
                                        }
                                        if (!(img1URI2.equals(Uri.EMPTY))){
                                            StorageReference dltStore = storageReference.child("lostdogs/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img2.jpg");
                                            dltStore.delete();
                                        }
                                        if (!(img1URI3.equals(Uri.EMPTY))){
                                            StorageReference dltStore = storageReference.child("lostdogs/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img3.jpg");
                                            dltStore.delete();
                                        }
                                        if (!(img1URI4.equals(Uri.EMPTY))){
                                            StorageReference dltStore = storageReference.child("lostdogs/"+fAuth.getCurrentUser().getUid()+"/"+IMGNUMBER+"/img4.jpg");
                                            dltStore.delete();
                                        }

                                        Toast.makeText(getApplicationContext(),"Delete Successful",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MyLostDogsListning.class));
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