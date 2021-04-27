package com.example.langlearn.ui.login;

import android.Manifest;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.langlearn.MainActivity;
import com.example.langlearn.R;
import com.example.langlearn.ui.login.LoginViewModel;
import com.example.langlearn.ui.login.LoginViewModelFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.Parse;
import com.parse.ParseException;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    //Google sign in variables
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    Map<String, String> logins;
    ParseUser user;
    //end Google sign in variables
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //regular sign in
        user = new ParseUser();
       /* user.setUsername( "test");
        user.setPassword( "1234");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    showAlert("Successful Sign Up!", "Welcome" + "<Your username here>" +"!");
                } else {
                    ParseUser.logOut();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });*/


         /*google sign in
        Reference:
            https://developers.google.com/identity/sign-in/android/start-integrating?authuser=1
        methods:
            *signIn
            *handleSignInResult
            *signOut
            *onActivityResult (request code ==1)
            *
        */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("202818144577-cejbver949b7t185qd3hgki8lpj0sfpl.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(v1->{signIn();});



        Button sign_out = findViewById(R.id.logout);
        sign_out.setOnClickListener(v1 -> {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("signedout","done");
                        }
                    });
        });

    }
    @Override
    protected void onStart(){
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            Log.d("stuff:",""+account.getIdToken());
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    private void showAlert(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {


            // Signed in successfully, show authenticated UI.
            if(account!=null){
                //authenticating with AWS
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            showAlert("Successful Sign Up!", "Welcome" + "<Your username here>" +"!");
                        }else{

                        }
                    }
                });
                Map<String, String> authData = new HashMap<String, String>();
                authData.put("access_token", account.getIdToken());
                authData.put("id", account.getDisplayName());
                Log.d("please3",account.getDisplayName());
                Log.d("please2",account.getIdToken());
                com.parse.boltsinternal.Task<ParseUser> x = ParseUser.logInWithInBackground("google", authData);
                Thread tmp = new Thread(()->{
                    while(!x.isCompleted()){
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    runOnUiThread(()->{
                        showAlert("Successful Login", "Welcome back " + account.getDisplayName() + " !");
                    });
                });
                tmp.start();

            }else{
                account = completedTask.getResult(ApiException.class);
                Map<String, String> authData = new HashMap<String, String>();
                authData.put("access_token", account.getIdToken());
                Log.d("please1",account.getIdToken());
                authData.put("id", account.getDisplayName());
                ParseUser.logInWithInBackground("google", authData);
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" );e.printStackTrace();
            int x = 2+2;
            //updateUI(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){//google login activity
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


}