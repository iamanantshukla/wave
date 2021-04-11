package com.dev334.wave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.util.HashMap;
import java.util.Map;

public class NewMeeting extends AppCompatActivity {

    private TextView nMtopic, nMdesc;
    private Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        nMtopic=findViewById(R.id.nMtopic);
        nMdesc=findViewById(R.id.nMDesc);
        create=findViewById(R.id.nMbtn);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nMtopic.getText().toString().isEmpty()){
                    nMtopic.setError("Empty");
                }else if(nMdesc.getText().toString().isEmpty()){
                    nMdesc.setError("Empty");
                }else{
                    String code=randomAlphaNumeric(3);
                    addToFirebase(code);
                }
            }
        });


    }

    private void addToFirebase(String code) {
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        Map<String, String> map;
        map=new HashMap<>();
        map.put("Topic", nMtopic.getText().toString());
        map.put("Desc", nMdesc.getText().toString());
        map.put("Code", code);
        map.put("Org", getIntent().getStringExtra("Org"));


        firebaseFirestore.collection("Meeting").document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                JitsiMeetConferenceOptions options=new JitsiMeetConferenceOptions.Builder()
                        .setRoom(code)
                        .setWelcomePageEnabled(false)
                        .build();
                JitsiMeetActivity.launch(NewMeeting.this,options);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}