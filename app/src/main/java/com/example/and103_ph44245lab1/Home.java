package com.example.and103_ph44245lab1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.and103_ph44245lab1.Adapter.cities_adapter;
import com.example.and103_ph44245lab1.cities.City;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Home extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private FirebaseAuth mAuth;
    private RecyclerView recycler_listCities;
    private SwipeRefreshLayout swipeRefreshLayout;
    FirebaseFirestore db;
    Button btnLogout,btnPUT;
    EditText inputRegions1,inputRegions2,inputPopulation,inputCountry,inputState,inputName;
    CheckBox chkcap;

    ArrayList<City> listCity = new ArrayList<>();
    Context context;
    cities_adapter cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inputName = findViewById(R.id.inputName);
        inputState = findViewById(R.id.inputState);
        inputCountry = findViewById(R.id.inputCountry);
        chkcap = findViewById(R.id.chkcap);
        inputPopulation = findViewById(R.id.inputPopulation);
        inputRegions1 = findViewById(R.id.inputRegions1);
        inputRegions2 = findViewById(R.id.inputRegions2);
        btnLogout = findViewById(R.id.btnLogout);
        btnPUT = findViewById(R.id.btnPUT);
        recycler_listCities = findViewById(R.id.recycler_listCities);
        swipeRefreshLayout = findViewById(R.id.refesh);

        mAuth =FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        listCity = new ArrayList<>();
        cAdapter = new cities_adapter(listCity);
        recycler_listCities.setAdapter(cAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recycler_listCities.setLayoutManager(linearLayoutManager);

        db.collection("cities2").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d:list){
                    City ct =d.toObject(City.class);
                    listCity.add(ct);
                }
                cAdapter.notifyDataSetChanged();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);
//        dao_city = new DAO_city(this);
//        ghiDulieu();
//        docDulieu();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                mAuth.signOut();
                Intent intent = new Intent(Home.this, MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(Home.this, "Dang xuat thanh cong", Toast.LENGTH_SHORT).show();
            }
        });
        btnPUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPushData();
                cAdapter.notifyDataSetChanged();
            }

        });
//        recycler_listCities();
    }
    private void recycler_listCities(){


        //tạo dòng dẻ phân cách các item
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        recycler_listCities.addItemDecoration(dividerItemDecoration);
    }


    private void onClickPushData() {

        CollectionReference cities2 = db.collection("cities2");
//        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("name", ""+inputName.getText().toString());
        data.put("state", ""+inputState.getText().toString());
        data.put("country", ""+inputCountry.getText().toString());
        data.put("capital", chkcap.isChecked());
        data.put("population",Integer.valueOf(inputPopulation.getText().toString()));
        data.put("regions", Arrays.asList(""+inputRegions1.getText().toString(), ""+inputRegions2.getText().toString()));

        if(inputName.getText().toString().isEmpty()||
                inputState.getText().toString().isEmpty()||
                inputCountry.getText().toString().isEmpty()||
                inputPopulation.getText().toString().isEmpty()||
                inputRegions1.getText().toString().isEmpty()||
                inputRegions2.getText().toString().isEmpty()){
            Toast.makeText(this, "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
        } else {
            cities2.document(inputName.getText().toString().toUpperCase()).set(data);
            Toast.makeText(this, "Ghi thanh cong", Toast.LENGTH_SHORT).show();
        }
    }


    private void ghiDulieu () {
        CollectionReference cities = db.collection("cities");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "San Francisco");
        data1.put("state", "CA");
        data1.put("country", "USA");
        data1.put("capital", false);
        data1.put("population", 860000);
        data1.put("regions", Arrays.asList("west_coast", "norcal"));
        cities.document("SF").set(data1);

//        Map<String, Object> data2 = new HashMap<>();
//        data2.put("name", "Los Angeles");
//        data2.put("state", "CA");
//        data2.put("country", "USA");
//        data2.put("capital", false);
//        data2.put("population", 3900000);
//        data2.put("regions", Arrays.asList("west_coast", "socal"));
//        cities.document("LA").set(data2);
//
//        Map<String, Object> data3 = new HashMap<>();
//        data3.put("name", "Washington D.C.");
//        data3.put("state", null);
//        data3.put("country", "USA");
//        data3.put("capital", true);
//        data3.put("population", 680000);
//        data3.put("regions", Arrays.asList("east_coast"));
//        cities.document("DC").set(data3);
//
//        Map<String, Object> data4 = new HashMap<>();
//        data4.put("name", "Tokyo");
//        data4.put("state", null);
//        data4.put("country", "Japan");
//        data4.put("capital", true);
//        data4.put("population", 9000000);
//        data4.put("regions", Arrays.asList("kanto", "honshu"));
//        cities.document("TOK").set(data4);
//
//        Map<String, Object> data5 = new HashMap<>();
//        data5.put("name", "Beijing");
//        data5.put("state", null);
//        data5.put("country", "China");
//        data5.put("capital", true);
//        data5.put("population", 21500000);
//        data5.put("regions", Arrays.asList("jingjinji", "hebei"));
//        cities.document("BJ").set(data5);

        //cities.add();
    }

    String TAG = "Home";

    private void docDulieu () {
        DocumentReference docRef = db.collection("cities").document("LA");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        Intent intent = new Intent(Home.this, Home.class);
        startActivity(intent);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        },3000);
    }
}