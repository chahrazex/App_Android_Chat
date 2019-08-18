package com.example.chatapppfe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {
    private Button login, signup ;
    private FirebaseUser firebaseUser ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /*-------Réferencer  les élement de ficher XML--------*/
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        login=findViewById(R.id.splash_activity_login_btn);
        signup=findViewById(R.id.splash_activity_signup_btn);
        /*-----------------------------------------------------------*/

        //Si l'utilsateur est déja fait un Login dériger dérectement au Session ouvert
        if(firebaseUser!=null)
        {
            Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreenActivity.this,SignupActivity.class));
            }
        });
    }
}
