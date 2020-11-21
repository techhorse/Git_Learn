package Blood.Donate.BloodDonate.blood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView profile_text = findViewById(R.id.profile_text);
        Intent intent = getIntent();
        String phone = intent.getStringExtra("number");
        profile_text.setText("Welcome to profile: "+phone);
    }
}