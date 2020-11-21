package Blood.Donate.BloodDonate.blood;

import android.os.Bundle;

import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Mitch on 2016-05-13.
 */
public class ViewListContentsSearch extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ListView mUserList;
    private List<String> mUserNames = new ArrayList<>();
    String city;
    String blood;
    private static ArrayList<User> data;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card);
        try{
            Bundle bundle = getIntent().getExtras();
            city = bundle.getString("city");
            blood = bundle.getString("blood");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        data = new ArrayList<User>();
        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        /*mUserList = (ListView)findViewById(R.id.display);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mUserNames);
        mUserList.setAdapter(arrayAdapter);*/
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //String value = dataSnapshot.getValue(String.class);
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    if(u.City.equalsIgnoreCase(city) && u.BloodGrp.equalsIgnoreCase(blood)){
                        try{data.add(u);

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                }
                adapter = new CustomAdapter(data);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}

