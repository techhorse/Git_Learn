package Blood.Donate.BloodDonate.blood;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    EditText input_email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        /*input_email = findViewById(R.id.input_email);
        input_email.setTextColor(R.color.whiteText);*/

    }
}
