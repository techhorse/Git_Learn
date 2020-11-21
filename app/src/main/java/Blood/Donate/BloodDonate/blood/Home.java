package Blood.Donate.BloodDonate.blood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

 public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private Button btnnext;
    EditText edit_num;
    CountryCodePicker code;
    private FirebaseAuth mAuth;
    EditText edit_otp;
    Button signin;
     private boolean mVerificationInProgress = false;
    TextView resend;
    String mVerificationId;
    final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    private String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home);
        btnnext = findViewById(R.id.otp_btn);
        edit_num = findViewById(R.id.edit_num);
        code = findViewById(R.id.ccp);
        btnnext = findViewById(R.id.otp_btn);
        edit_num = findViewById(R.id.edit_num);
        signin = findViewById(R.id.buttonSignIn);
         edit_otp = findViewById(R.id.edit_otp);
        code = findViewById(R.id.ccp);
        mAuth = FirebaseAuth.getInstance();
        resend = findViewById(R.id.resend);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mobile = edit_num.getText().toString().trim();
                num = "+" + code.getSelectedCountryCode() + edit_num.getText().toString();
                if (mobile.isEmpty() || mobile.length() < 10) {
                    edit_num.setError("Enter a valid mobile");
                    edit_num.requestFocus();
                    return;
                }
                databaseOperation();
            }


        });

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edit_otp.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    edit_otp.setError("Enter valid code");
                    edit_otp.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });
    }
    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            userRef.push().setValue(num).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Registration successfully Done", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Registration not successfully Done", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            Intent intent = new Intent(Home.this, ProfileActivity.class);
                            intent.putExtra("number", num);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(Home.this, "User with this Number already exist.", Toast.LENGTH_SHORT).show();
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }
    public void databaseOperation(){

        userRef
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DatabaseError databaseError;

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null && ((HashMap) dataSnapshot.getValue()).containsValue(num)) {

                            //it means user already registered
                            //Add code to show your prompt
                            Intent intent = new Intent(Home.this, ProfileActivity.class);
                            intent.putExtra("number", num);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            /*userRef.push().setValue(num).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Registration successfully Done", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Registration not successfully Done", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });*/
                            sendVerificationCode(num);
                            edit_otp.setVisibility(View.VISIBLE);
                            signin.setVisibility(View.VISIBLE);
                            btnnext.setVisibility(View.GONE);
                            resend.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        this.databaseError = databaseError;
                    }
                });
    }
    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mobile)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NotNull PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                edit_otp.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
            mVerificationInProgress = false;
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Home.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            edit_otp.setVisibility(View.VISIBLE);
            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        switch (id) {
            case R.id.search:
                intent = new Intent(this, search.class);
                startActivity(intent);
                break;
            case R.id.login:
                //Yet to be implemented
                intent = new Intent(this, Login.class);
                startActivity(intent);
                break;
            case R.id.contact:
                intent = new Intent(this, contact.class);
                startActivity(intent);
                break;
            case R.id.about:
                intent = new Intent(this, about.class);
                startActivity(intent);
                break;
            case R.id.signup:
                intent = new Intent(this, signup.class);
                startActivity(intent);
                break;
            case R.id.donor_list1:
                intent = new Intent(this, ViewListContents.class);
                startActivity(intent);
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.search:
                intent = new Intent(this, search.class);
                startActivity(intent);
                break;
            case R.id.login:
                //Yet to be implemented
                intent = new Intent(this, Login.class);
                startActivity(intent);
                break;
            case R.id.contact:
                intent = new Intent(this, contact.class);
                startActivity(intent);
                break;
            case R.id.about:
                intent = new Intent(this, about.class);
                startActivity(intent);
                break;
            case R.id.signup:
                intent = new Intent(this, signup.class);
                startActivity(intent);
                break;
            case R.id.donor_list1:
                intent = new Intent(this, ViewListContents.class);
                startActivity(intent);
                break;

        }
    }
}
