package postpc.y2020.stockfarm_app;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import postpc.y2020.stockfarm_app.R;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    int RC_GOOGLE_SIGN_IN = 646;
    StockFarmApplication app;
    LoginActivity activity;
    private GoogleSignInClient googleClient;
    private FirebaseAuth mAuth;
    EditText emailBox;
    EditText passwordBox;
    Button logInButton;
    SignInButton googleButton;
    AlertDialog loadingAlert;
    AlertDialog regAlert;
    View loadingView;
    View registerView;
    Button skip;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = (StockFarmApplication) getApplication();
        activity = this;
        emailBox = findViewById(R.id.email_box);
        passwordBox = findViewById(R.id.password_box);
        logInButton = findViewById(R.id.login_button);
        skip = findViewById(R.id.skip);
        logInButton.setOnClickListener(this);
        TextWatcher textListen = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                logInButtonEnabler();
            }

            @Override
            public void afterTextChanged(Editable s) {
                logInButtonEnabler();
            }
        };
        emailBox.addTextChangedListener(textListen);
        passwordBox.addTextChangedListener(textListen);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        googleClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        googleButton = findViewById(R.id.sign_in_button);
        googleButton.setSize(SignInButton.SIZE_WIDE);
        googleButton.setOnClickListener(this);
        skip.setOnClickListener(this);
        getSupportActionBar().hide();
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String lastUserId = app.sp.getString(getString(R.string.last_user_id), "");
        if (currentUser != null) {
            openLoadingWindow();
            getUserById(currentUser.getUid());
        }
        else if (app.userData != null)
        {
            goToFarm();
        }
        else if (!lastUserId.equals(""))
        {
            app.currId = lastUserId;
            openLoadingWindow();
            getUserById(lastUserId);
        }
    }

    void openLoadingWindow() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        loadingView = inflater.inflate(R.layout.loading_window, null);
        builder.setView(loadingView);
        loadingAlert = builder.create();
        loadingAlert.setCancelable(false);
        loadingAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingAlert.setCanceledOnTouchOutside(false);
        GifImageView gifImageView = (GifImageView) loadingView.findViewById(R.id.loading_gif);
        gifImageView.setGifImageResource(R.drawable.loading_drop);
        loadingAlert.show();
    }

    void closeLoadingWindow() {
        loadingAlert.cancel();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button: // google sign in button pressed
                openLoadingWindow();
                Intent signInIntent = googleClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
                break;
            case R.id.login_button:
                openLoadingWindow();
                regularSignInOrRegister();
                break;
            case R.id.cancel_button:
                regAlert.cancel();
                closeLoadingWindow();
                break;
            case R.id.register_button:
                EditText emailRegBox = (EditText) registerView.findViewById(R.id.email_box);
                EditText nameRegBox = (EditText) registerView.findViewById(R.id.name_box);
                EditText passRegBox = (EditText) registerView.findViewById(R.id.password_box);
                String regEmail = emailRegBox.getText().toString();
                String regName = nameRegBox.getText().toString();
                String regPassword = passRegBox.getText().toString();
                if (!validEmailPassword(findViewById(R.id.sign_in_layout), regEmail, regPassword)) return;
                regAlert.cancel();
                openLoadingWindow();
                app.generateAccountRegularUser(regEmail, regEmail, regName, regPassword, activity);
                break;
            case R.id.skip:
                dummyUser();
//                goToFarm();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            HandleGoogleSignIn(task);
        }
    }

    private void HandleGoogleSignIn(@NonNull Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount acct = task.getResult(ApiException.class);
            if (acct != null) firebaseAuthWithGoogle(acct);

        } catch (ApiException e) {
            Log.w("signIn", "handleSignInResult:error", e);
            // updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            getUserById(user.getUid());
                        } else {
                            // Sign in failed
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.sign_in_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void getUserById(final String Id)
    {
        DocumentReference userDocRef = app.db.collection("users").document(Id);
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    FirebaseUser user = mAuth.getCurrentUser();
                    String name;
                    DocumentSnapshot document = (DocumentSnapshot) task.getResult();
                    if (document != null && document.exists()) {
                        // found existing StockFarm account with uid, now we retrieve its data
                        String json = (String) document.get(getString(R.string.firestore_fieldname_userdata));
                        if (user != null) {
                            app.setUserDataFromServer(user.getUid(), json);
                            name = user.getDisplayName();
                        }
                        else if (app.currId != null){
                            app.setUserDataFromServer(app.currId, json);
                            name = app.userData.getName();
                        }
                        else throw new NullPointerException();
                        String welcomeMsg = "Welcome Back, " + name;
                        Snackbar.make(loadingView, welcomeMsg, Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        // no such account, need to create one for google user
                        app.generateAccountGoogleUser(user, activity);
                    }
                    new CountDownTimer(2000, 2000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            closeLoadingWindow();
                            goToFarm();
                        }
                    }.start();
                } else {
                    closeLoadingWindow();
                    Snackbar.make(findViewById(R.id.sign_in_layout), getString(R.string.err_query_user_data), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void regularSignInOrRegister() {
        final String email = emailBox.getText().toString();
        final String password = passwordBox.getText().toString();
        if (!validEmailPassword(findViewById(R.id.sign_in_layout), email, password))
        {
            closeLoadingWindow();
            return;
        }
        DocumentReference userDocRef = app.db.collection("users").document(email);
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = (DocumentSnapshot) task.getResult();
                    if (document != null && document.exists()) {
                        passwordBox.setText(null);
                        // found existing StockFarm account with uid, now we retrieve its data
                        if (verifyPassword(document, password)) {   // password match
                            String json = (String) document.get(getString(R.string.firestore_fieldname_userdata));
                            String name = app.setUserDataFromServer(email, json);
                            app.saveUserForAutoLogIn(email);
                            String welcomeMsg = "Welcome Back, " + name;
                            Snackbar.make(loadingView, welcomeMsg, Snackbar.LENGTH_SHORT).show();
                            new CountDownTimer(2000, 2000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    closeLoadingWindow();
                                    goToFarm();
                                }
                            }.start();
                        } else {   // wrong password
                            Snackbar.make(findViewById(R.id.sign_in_layout),
                                    getString(R.string.wrong_password), Snackbar.LENGTH_SHORT).show();
                            closeLoadingWindow();
                        }
                    } else {   // no account associated with mail, refer to register
                        registerNewAccount();
                    }
                }
            }
        });
    }


    private boolean verifyPassword(DocumentSnapshot document, String enteredPassword) {
        String password = (String) document.get("password");
        if (password == null) return false;
        return (password.equals(enteredPassword));
    }

    private void logInButtonEnabler() {
        if (!emailBox.getText().toString().equals("")
                && !passwordBox.getText().toString().equals(""))
            logInButton.setVisibility(View.VISIBLE);
            logInButton.setEnabled(true);
    }



    private void registerNewAccount()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        registerView = inflater.inflate(R.layout.register_window, null);
        builder.setView(registerView);
        regAlert = builder.create();
        regAlert.setCancelable(false);
        regAlert.setCanceledOnTouchOutside(false);
        final EditText emailRegBox = (EditText) registerView.findViewById(R.id.email_box);
        final EditText nameRegBox = (EditText) registerView.findViewById(R.id.name_box);
        final EditText passRegBox = (EditText) registerView.findViewById(R.id.password_box);
        Button regButton = (Button) registerView.findViewById(R.id.register_button);
        Button cancelButton = (Button) registerView.findViewById(R.id.cancel_button);
        emailRegBox.setText(emailBox.getText().toString());
        passRegBox.setText(passwordBox.getText().toString());
        regButton.setOnClickListener(activity);
        cancelButton.setOnClickListener(activity);
        regAlert.show();

    }

    private boolean validEmailPassword(View v, String email, String password)
    {
        if (email.equals("") || !isEmailValid(email)) {
            Snackbar.make(findViewById(R.id.sign_in_layout),
                    getString(R.string.invalid_email), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if (!isPasswordValid(password)) {
            Snackbar.make(findViewById(R.id.sign_in_layout),
                    getString(R.string.invalid_password), 10000).show();
            return false;
        }
        return true;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPasswordValid(String password) {
        String expression = "[\\w]+";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return (matcher.matches() && password.length() >= 6);
    }

    public void registerResult(boolean successful)
    {
        if (successful)
        {
            String msg = "Welcome, " + app.userData.getName() + getString(R.string.new_account_created);
            Snackbar.make(loadingView,
                    msg, 4000).show();
            new CountDownTimer(2000, 2000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    closeLoadingWindow();
                    goToFarm();
                }
            }.start();
        }
        else
        {
            Snackbar.make(loadingView, getString(R.string.account_creation_failed) , 3000).show();
            closeLoadingWindow();
        }
    }

    private void goToFarm()
    {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void dummyUser()
    {
        emailBox.setText("rishon.babidur@hotmail.co.il");
        passwordBox.setText("123456");
        openLoadingWindow();
        regularSignInOrRegister();
    }
}

