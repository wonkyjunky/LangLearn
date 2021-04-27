package com.example.langlearn.ui.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.langlearn.LangLearnActivity;
import com.example.langlearn.MainActivity;
import com.example.langlearn.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends LangLearnActivity {

    private LoginViewModel loginViewModel;
    //Google sign in variables
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    Map<String, String> logins;
    ParseUser user;

    private Button loginButton;
    private TextView usernameText;
    private TextView passwordText;

    //end Google sign in variables
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.login_username_text);
        passwordText = findViewById(R.id.login_password_text);

        user = new ParseUser();
        // Set the user's username and password, which can be obtained by a forms
        /*user.setUsername( "<Your username here>");
        user.setPassword( "<Your password here>");
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
                .requestIdToken("202818144577-a6mms4hlaik35ld031enrkdn8qu87mas.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.login_google_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(v1->{signIn();});

        Context currContext = this;

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(oc -> {

            String username = usernameText.getText().toString();
            String password = passwordText.getText().toString();

            boolean incomplete = false;

            if (username.isEmpty()) {
                incomplete = true;
                usernameText.setError("Username must not be empty");
            }

            if (password.isEmpty()) {
                incomplete = true;
                passwordText.setError("Password must not be empty");
            }

            if (incomplete) return;

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", username);
            ParseUser.logInInBackground(username, password, (user, e) -> {

                if (e != null || user == null) {
                    Log.e("LoginActivity", "Failed to create query");
                    usernameText.setError("Incorrect username or password");
                    return;
                }

                Toast.makeText(this, "Logged in user: " + username, Toast.LENGTH_SHORT).show();

                userId = user.getObjectId();

                finish();
            });


            Log.i("LoginActivity", "Login button pressed");
        });

        Button sign_out = findViewById(R.id.login_logout_button);
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
            Log.e("TAG", "Sign-in Error Code: " + e.getStatusCode());
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