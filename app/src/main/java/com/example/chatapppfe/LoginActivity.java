package com.example.chatapppfe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar ;
    private FirebaseAuth firebaseAuth;
    private EditText Email,Password  ;
    private Button Login,ForgotPassword ;
    private TextView SignUp ;
    private ProgressBar progressBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitializeFields();
        //Si l'utilisateur oublier son mot de pass il peut récupérer depuit l'activity Forgot Password
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this ,ForgetPasswordActivity.class));
                finish();
            }
        });
        //si l'utlisateur n'a pas un compte il peut inscripter depuit l'activity Signup
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this ,SignupActivity.class));
                finish();
            }
        });
        //sinon il effucter la connection par la remplisage de email et password corectement
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();

            }
        });

    }
    /*-------------------------------------InitializeFields --------------------------------------*/
    public  void  InitializeFields ()
    {
        toolbar=findViewById(R.id.activity_login_toolbar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth=FirebaseAuth.getInstance() ;
        ForgotPassword=findViewById(R.id.login_activity_forgotPassword_btn);
        Login=findViewById(R.id.login_activity_login_btn);
        Password=findViewById(R.id.login_activity_password_input);
        SignUp=findViewById(R.id.login_activity_signup_textView);
        Email=findViewById(R.id.login_activity_email_input);
        progressBar=findViewById(R.id.login_activity_ProgressBar);
    }
    /*-----------------------------------Sigin in Firebase ---------------------------------------*/
    public  void Login ()
    {
        progressBar.setVisibility(View.VISIBLE);
        //Tester si les champs remplir corectement 'Email and Password '
        if(TextUtils.isEmpty(Email.getText())||TextUtils.isEmpty(Password.getText()))
        {
            Toasty.error(LoginActivity.this,"All fields are required !", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        else
        {
            firebaseAuth.signInWithEmailAndPassword(Email.getText().toString(),Password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful())
                            {
                                Toasty.success(LoginActivity.this, "Logged in successfully...",Toast.LENGTH_SHORT).show() ;
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                startActivity(intent);
                            }
                            else
                            {
                                Toasty.error(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
