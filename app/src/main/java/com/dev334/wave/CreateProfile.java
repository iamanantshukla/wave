package com.dev334.wave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dev334.wave.Database.TinyDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateProfile extends AppCompatActivity {

    private TinyDB tinyDB;
    private String PhoneNo,Username,Organisation,Facebook, Instagram;
    private TextView EditName,EditFB,EditInsta,EditInterest;
    private TextView EditOrg;
    private Button btnCreate;
    private FirebaseFirestore firestore;
    private ArrayList<String> Organisations,userInterest;
    private static String TAG="CreateProfile";
    private String uInterest;
    private FirebaseAuth mAuth;
    private ArrayAdapter<String> adapter;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        //getSupportActionBar().hide();

        EditName=findViewById(R.id.EditName);
        EditOrg=findViewById(R.id.EditOrg);
        EditFB=findViewById(R.id.EditFacebook);
        EditInsta=findViewById(R.id.EditInsta);
        btnCreate=findViewById(R.id.btnCreate);
        EditInterest=findViewById(R.id.EditInterest);

        mAuth=FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        Organisations=new ArrayList<>();
        userInterest=new ArrayList<>();
        tinyDB=new TinyDB(getApplicationContext());
        userInterest=tinyDB.getListString("UserInterest");
        setupText();
        PhoneNo=tinyDB.getString("PhoneNumber");

        firestore=FirebaseFirestore.getInstance();

        getOrganisationList(firestore);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, Organisations);

        EditOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CreateProfile.this, ChooseOrganisation.class);
                i.putExtra("Organisations",Organisations);
                startActivity(i);
            }
        });

        EditInterest.setOnClickListener(v->{
            Intent i=new Intent(CreateProfile.this, ChooseInterest.class);
            i.putExtra("Organisations",Organisations);
            startActivity(i);
        });



        btnCreate.setOnClickListener(v -> {
            Username=EditName.getText().toString();
            Organisation=EditOrg.getText().toString();
            Facebook=EditFB.getText().toString();
            Instagram=EditInsta.getText().toString();

            if(check()){
                createProfile();
            }
        });


    }

    private void setupText() {

        uInterest="";
        for(int i=0;i<userInterest.size();i++){
            uInterest=uInterest+userInterest.get(i)+" | ";
        }
        EditInterest.setText(uInterest);
    }

    private void createProfile() {
        Map<String, Object> map=new HashMap<>();
        map.put("Username", Username);
        map.put("Organisation", Organisation);
        map.put("Facebook", Facebook);
        map.put("Instagram", Instagram);
        map.put("UserID", mAuth.getCurrentUser().getUid());
        map.put("Bio","");
        map.put("ProfilePic","");
        map.put("PhoneNumber", PhoneNo);
        map.put("Interest", userInterest);
        tinyDB.putObject("UserProfile", map);
        firestore.collection("Users").document(mAuth.getCurrentUser().getUid()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CreateProfile.this, "Profile Created", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(CreateProfile.this, HomeActivity.class);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void getOrganisationList(FirebaseFirestore firestore) {
        firestore.collection("Data").document("Organisations").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                Log.i(TAG, "onSuccess: Organisation List");
                Organisations= (ArrayList<String>) snapshot.get("College");
                adapter.addAll(Organisations);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: Organisation List");
            }
        });
    }

    private boolean check() {
        if(Username.isEmpty()){
            return false;
        }else if(Organisation.isEmpty()){
            return false;
        }else{
            if(Organisations.contains(Organisation)){
                if(!Instagram.isEmpty()){
                    if(!URLUtil.isValidUrl(Instagram)){
                        EditInsta.setError("Enter valid URL or else leave empty");
                        return false;
                    }else{
                        if(!Instagram.contains("instagram") || !Instagram.toLowerCase().contains("instagram")){
                            EditInsta.setError("Enter valid URL or else leave empty");
                            return false;
                        }
                    }
                }
                if(!Facebook.isEmpty()){
                    if(!URLUtil.isValidUrl(Facebook)){
                        EditFB.setError("Enter valid URL or else leave empty");
                        return false;
                    }else{
                        if(!Facebook.contains("facebook") || !Facebook.toLowerCase().contains("facebook")){
                            EditFB.setError("Enter valid URL or else leave empty");
                            return false;
                        }
                    }
                }
                return true;
            }else{
                return false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EditOrg.setText(tinyDB.getString("Organisation"));
        userInterest=tinyDB.getListString("UserInterest");
        setupText();
    }
}