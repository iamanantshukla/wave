package com.dev334.wave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dev334.wave.Database.TinyDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG ="SplashScreen";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private Map<String, Object> map;
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //getSupportActionBar().hide();

        mAuth=FirebaseAuth.getInstance();
        firestore= FirebaseFirestore.getInstance();

        map=new HashMap<>();

        TinyDB tinyDB=new TinyDB(getApplicationContext());

        if(mAuth.getCurrentUser()==null){
            Intent i=new Intent(SplashScreen.this, LoginHome.class);
            startActivity(i);
        }else{
            firestore.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {
                    if(snapshot.exists()){
                        map=snapshot.getData();
                        tinyDB.putObject("UserProfile", map);
                        Intent i=new Intent(SplashScreen.this, HomeActivity.class);
                        startActivity(i);
                    }else{
                        Intent i=new Intent(SplashScreen.this, CreateProfile.class);
                        startActivity(i);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: "+e.getMessage());
                }
            });
        }

    }
}