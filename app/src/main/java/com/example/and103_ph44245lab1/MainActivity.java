package com.example.and103_ph44245lab1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView txtsignup, txtforgot;
    Button btnlogin;
    EditText edtUser, edtPass;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // neu user da dang nhap vao tu phien truoc thi su dung user luon
//            edtUser.setText(mAuth.getCurrentUser().toString());
//            edtPass.setText(mAuth.getCurrentUser().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtsignup = findViewById(R.id.txtsignup);
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnlogin = findViewById(R.id.btnlogin);
        txtforgot = findViewById(R.id.txtforgot);

        mAuth = FirebaseAuth.getInstance();

        edtUser.setText(getIntent().getStringExtra("username"));
        edtPass.setText(getIntent().getStringExtra("password"));

        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        txtforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUser.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this, "Vui long nhap email", Toast.LENGTH_SHORT).show();
                } else {
                    String emailAddress = edtUser.getText().toString();
                    mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Kiem tra email de cap nhat lai mat khau", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Loi gui mail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtUser.getText().toString().trim().isEmpty()&&edtPass.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this, "Vui long nhap day du thong tin", Toast.LENGTH_SHORT).show();
                } else if (edtUser.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this, "Tai khoan khong duoc de trong", Toast.LENGTH_SHORT).show();
                } else if (edtPass.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this, "Mat khau khong duoc de trong", Toast.LENGTH_SHORT).show();
                }else {
                    String email = edtUser.getText().toString();
                    String password = edtPass.getText().toString();
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, Home.class));
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Dang nhap that bai", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}