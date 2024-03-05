package com.example.and103_ph44245lab1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
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

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText edtUserdk, edtPassdk;
    Button btnSign;
    TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtUserdk = findViewById(R.id.edtUserdk);
        edtPassdk = findViewById(R.id.edtPassdk);
        btnSign = findViewById(R.id.btnSign);
        txtLogin = findViewById(R.id.txtLogin);

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtUserdk.getText().toString().trim().isEmpty()&&edtPassdk.getText().toString().trim().isEmpty()){
                    Toast.makeText(SignUp.this, "Vui long nhap day du thong tin", Toast.LENGTH_SHORT).show();
                } else if (edtUserdk.getText().toString().trim().isEmpty()){
                    Toast.makeText(SignUp.this, "Tai khoan khong duoc de trong", Toast.LENGTH_SHORT).show();
                } else if (edtPassdk.getText().toString().trim().isEmpty()){
                    Toast.makeText(SignUp.this, "Mat khau khong duoc de trong", Toast.LENGTH_SHORT).show();
                }else {
                    String email = edtUserdk.getText().toString();
                    String password = edtPassdk.getText().toString();
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                //lay thong tin tai khoan moi dang ky
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(SignUp.this, "Dang ky thanh cong", Toast.LENGTH_SHORT).show();
                                finishAffinity();
                                Intent intent = new Intent(SignUp.this,MainActivity.class);
                                intent.putExtra("username",email);
                                intent.putExtra("password",password);
                                startActivity(intent);
                            } else {
                                Log.w(TAG,"createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUp.this, "Dang ky that bai", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
