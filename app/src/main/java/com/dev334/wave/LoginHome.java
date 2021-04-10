package com.dev334.wave;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginHome extends AppCompatActivity {

    private Button SignUp, SignIn, Next;
    private TextView Tagline;
    private LinearLayout emailLayout;
    private ImageView textLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);
        getSupportActionBar().hide();


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//            getWindow().getSharedElementEnterTransition().setDuration(1000);
//            getWindow().getSharedElementReturnTransition().setDuration(1000)
//                    .setInterpolator(new DecelerateInterpolator());
//        }

        SignUp=findViewById(R.id.BtnSignUp);
        SignIn=findViewById(R.id.BtnSignIn);
        Next=findViewById(R.id.btnNext2);
        textLogo=findViewById(R.id.textLogo);
        Tagline=findViewById(R.id.textTagline);
        emailLayout=findViewById(R.id.linearEmail);


        show(SignUp,1000);
        show(SignIn,1000);
        show(Tagline,1000);


        //First Time User
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide(SignUp,500);
                hide(SignIn,500);
                hide(Tagline,500);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getWindow().setExitTransition(null);
                        Intent i=new Intent(LoginHome.this, EmailLogin.class);
                        i.putExtra("Status", 0); //0->new User
                        ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(LoginHome.this,
                                new Pair<View, String>(emailLayout, "emailTransition"),
                                new Pair<View, String>(Next, "nextTransition"),
                                new Pair<View, String>(textLogo, "logoTransition"));
                        startActivity(i, options.toBundle());
                    }
                }, 500);
            }
        });

        //login for already existing User
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide(SignUp,500);
                hide(SignIn,500);
                hide(Tagline,500);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getWindow().setExitTransition(null);
                        Intent i=new Intent(LoginHome.this, EmailLogin.class);
                        i.putExtra("Status", 1); //1->already existing User
                        ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(LoginHome.this,
                                new Pair<View, String>(emailLayout, "emailTransition"),
                                new Pair<View, String>(Next, "nextTransition"),
                                new Pair<View, String>(textLogo, "logoTransition"));
                        startActivity(i, options.toBundle());
                    }
                }, 500);
            }
        });

    }

    private void hide(View v, int duration) {
        v.animate().alpha(0f).setDuration(duration);
    }

    private void show(View v, int duration) {
        v.animate().alpha(1f).setDuration(duration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        show(SignUp,500);
        show(SignIn,500);
        show(Tagline,500);
    }
}