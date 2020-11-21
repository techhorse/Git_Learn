package Blood.Donate.BloodDonate.blood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class search extends AppCompatActivity implements  View.OnClickListener {
    EditText city;
    EditText blood;
    Button search_btn;
    Spinner bloodSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        city = findViewById(R.id.city_search);
        bloodSpinner = findViewById(R.id.bloodSpinner);

        search_btn = findViewById(R.id.search_bt);
        search_btn.setOnClickListener(this);
        List<String> bloodList = new ArrayList<String>();
        bloodList.add("A+");
        bloodList.add("A-");
        bloodList.add("B+");
        bloodList.add("B-");
        bloodList.add("O+");
        bloodList.add("O-");
        bloodList.add("AB+");
        bloodList.add("AB-");
        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<>(search.this,R.layout.support_simple_spinner_dropdown_item,bloodList);
        bloodSpinner.setAdapter(bloodGroupAdapter);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.search_bt:
                String city1 = city.getText().toString();
                String blood1 = bloodSpinner.getSelectedItem().toString();
                Intent intent = new Intent(getApplicationContext(),ViewListContentsSearch.class);
                intent.putExtra("city", city1);
                intent.putExtra("blood", blood1);
                startActivity(intent);
                break;
                default:

        }
    }
}


