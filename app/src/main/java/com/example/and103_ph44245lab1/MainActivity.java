package com.example.and103_ph44245lab1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView txtsignup, txtforgot, txtOTP;
    Button btnlogin, btnOTP, btnLoginOTP;
    EditText edtUser, edtPass, edtOTP, edtPhoneNumber;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;

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
        txtOTP = findViewById(R.id.txtOTP);

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

        txtOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, Login_OTP.class);
//                startActivity(intent);
                dialogThem();
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
    //
    private void getOTP(String phoneNumber){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(edtPhoneNumber.getText().toString())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                        edtOTP.setText(phoneAuthCredential.getSmsCode());
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.w(TAG, "onVerificationFailed", e);

//                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                            // Invalid request
//                        } else if (e instanceof FirebaseTooManyRequestsException) {
//                            // The SMS quota for the project has been exceeded
//                        } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
//                            // reCAPTCHA verification attempted with null Activity
//                        }
                        Toast.makeText(MainActivity.this, "gui OTP that bai", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, token);
                        Log.d(TAG, "onCodeSent:" + verificationId);
                        mVerificationId = verificationId;
//                            mResendToken = token;
                        Toast.makeText(MainActivity.this, "gui OTP  Thanh cong", Toast.LENGTH_SHORT).show();
                    }
                    //

                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOTP(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(MainActivity.this, "Login Thanh cong", Toast.LENGTH_SHORT).show();
                            // Update UI
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            startActivity(intent);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    //
    public void dialogThem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = ((Activity) MainActivity.this).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_otp, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        //

        //
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        edtOTP = view.findViewById(R.id.edtOTP);

        //
        btnOTP = view.findViewById(R.id.btnOTP);
        btnLoginOTP = view.findViewById(R.id.btnLoginOTP);
        //
        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOTP(edtPhoneNumber.getText().toString());
            }
        });
        btnLoginOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP(edtOTP.getText().toString());
                dialog.dismiss();
            }
        });
        //

        dialog.show();
    }
}