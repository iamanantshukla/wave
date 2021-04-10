package com.dev334.wave;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dev334.wave.Database.TinyDB;
import com.dev334.wave.Firebase.UserFirebase;
import com.dev334.wave.Home.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OtherProfile extends AppCompatActivity {

    private ImageView profilePic;
    private TextView TextName, TextBio, TextSkill1, TextSkill2, TextSkill3;
    private UserModel userModel;
    private Button FRBtn;
    private FloatingActionButton SCBtn;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private TinyDB tinyDB;
    private ArrayList<String> Friends, ReqSent,ReqReceived;
    private String profileUserID, UserID;
    private int STATUS=3;  //Loading->3, SendFR=2, SentFR=1, AlreadyF=0
    private static String TAG="UserSTATUS";
    private UserFirebase userFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        TextName=findViewById(R.id.oUsername);
        TextBio=findViewById(R.id.oBioText);
        profilePic=findViewById(R.id.oProImage);
        TextSkill1=findViewById(R.id.oprofileSkill1);
        TextSkill2=findViewById(R.id.oprofileSkill2);
        TextSkill3=findViewById(R.id.oprofileSkill3);
        FRBtn=findViewById(R.id.oFRbtn);
        SCBtn=findViewById(R.id.oSCbtn);

        firestore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        tinyDB=new TinyDB(getApplicationContext());

        userModel= (UserModel) getIntent().getSerializableExtra("ProfileModel");
        profileUserID=userModel.getUserID();
        UserID=mAuth.getCurrentUser().getUid();


        userFirebase=new UserFirebase(getApplicationContext(), UserID, profileUserID);

        TextName.setText(userModel.getUsername());
        TextBio.setText(userModel.getBio());
        ArrayList<String> Interests = userModel.getInterest();
        for(int i=0;i<Interests.size();i++){
            if(i==0){
                TextSkill1.setText(Interests.get(i));
                TextSkill1.setVisibility(View.VISIBLE);
            }else if(i==1){
                TextSkill2.setText(Interests.get(i));
                TextSkill2.setVisibility(View.VISIBLE);
            }else if(i==2){
                TextSkill3.setText(Interests.get(i));
                TextSkill3.setVisibility(View.VISIBLE);
                break;
            }
        }
        String url=userModel.getProfilePic();
        if(url.isEmpty()){
            profilePic.setImageResource(R.drawable.profile);
        }else {
            Picasso.get().load(url).into(profilePic);
        }

        STATUS=userFirebase.checkStatus();
        if(STATUS==0){
            FRBtn.setText("Remove Friend");
        }else if(STATUS==1){
            FRBtn.setText("Pending Request");
        }else if(STATUS==3){
            FRBtn.setText("ACCEPT REQUEST");
        }else{
            FRBtn.setText("Send Friend Request");
        }

        FRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(STATUS==0){
                        //Remove Friend
                        userFirebase.RemoveFriend();
                        FRBtn.setText("Send Friend Request");
                        STATUS=2;
                    }else if(STATUS==1){
                        //Pending Request
                        Toast.makeText(getApplicationContext(), "You have already requested", Toast.LENGTH_SHORT).show();
                    }else if(STATUS==2){
                        //Send Friend request
                        userFirebase.sendFriendReq();
                        FRBtn.setText("Pending Request");
                        STATUS=1;
                    }else if(STATUS==3){
                        userFirebase.AcceptReq();
                        FRBtn.setText("Remove Friend");
                        STATUS=0;
                    }

                    else{
                        Toast.makeText(getApplicationContext(), "None", Toast.LENGTH_SHORT).show();
                    }
            }
        });

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}