package Blood.Donate.BloodDonate.blood;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class signup extends AppCompatActivity {

    Intent intent = null;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button submit, login;
    EditText name, password, gender, mobile, city;
    Spinner bloodSpinner;
    Spinner genderSpinner;
    TextView link_login;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_new);
        bloodSpinner = (Spinner)findViewById(R.id.blood_grp);
        genderSpinner = findViewById(R.id.gender);
        link_login = findViewById(R.id.link_login);
        List<String> bloodList = new ArrayList<String>();
        List<String> genderList = new ArrayList<String>();
        genderList.add("Male");
        genderList.add("Female");
        bloodList.add("A+");
        bloodList.add("A-");
        bloodList.add("B+");
        bloodList.add("B-");
        bloodList.add("O+");
        bloodList.add("O-");
        bloodList.add("AB+");
        bloodList.add("AB-");
        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<>(signup.this,R.layout.support_simple_spinner_dropdown_item,bloodList);
        bloodSpinner.setAdapter(bloodGroupAdapter);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(signup.this,R.layout.support_simple_spinner_dropdown_item,genderList);
        genderSpinner.setAdapter(genderAdapter);

        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        mobile = (EditText) findViewById(R.id.mobile);
        city = (EditText) findViewById(R.id.city);
        submit = (Button) findViewById(R.id.submit);
        login = (Button) findViewById(R.id.login);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name1 = name.getText().toString();
                String password1 = password.getText().toString();
                String blood_grp1 = bloodSpinner.getSelectedItem().toString();
                String gender1 = genderSpinner.getSelectedItem().toString();
                String mobile1 = mobile.getText().toString();
                String city1 = city.getText().toString();
                if(!(name1.contentEquals("")) && !(password1.contentEquals("")) && !(blood_grp1.contentEquals("")) &&!(mobile1.contentEquals("")) && !(city1.contentEquals(""))) {
                    HashMap<String,String> dataMap = new HashMap<>();
                    dataMap.put("Name",name1);
                    dataMap.put("Password",password1);
                    dataMap.put("BloodGrp",blood_grp1);
                    dataMap.put("Gender",gender1);
                    dataMap.put("Mobile",mobile1);
                    dataMap.put("City",city1);

                    mDatabase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Registration successfully Done", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Registration not successfully Done", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(), "Fill in all the details ", Toast.LENGTH_LONG).show();
                }
            }

        });


    }





}

