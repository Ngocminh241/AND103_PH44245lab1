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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login_OTP extends AppCompatActivity {
    EditText edtPhoneNumber, edtOTP;
    Button btnOTP, btnLoginOTP;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);
        //
        mAuth = FirebaseAuth.getInstance();
        //
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtOTP = findViewById(R.id.edtOTP);
        //
        btnOTP = findViewById(R.id.btnOTP);
        btnLoginOTP = findViewById(R.id.btnLoginOTP);

        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPhoneNumber.getText().toString().trim().isEmpty()){
                    Toast.makeText(Login_OTP.this, "Vui long nhap so dien thoai", Toast.LENGTH_SHORT).show();
                } else {
                    getOTP(edtPhoneNumber.getText().toString());
                }
            }
        });
        btnLoginOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtOTP.getText().toString().trim().isEmpty()){
                    Toast.makeText(Login_OTP.this, "Vui long nhap ma OTP", Toast.LENGTH_SHORT).show();
                } else {
                    verifyOTP(edtOTP.getText().toString());
                }
            }
        });
    }
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
                        Toast.makeText(Login_OTP.this, "gui OTP that bai", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, token);
                        Log.d(TAG, "onCodeSent:" + verificationId);
                        mVerificationId = verificationId;
//                            mResendToken = token;
                        Toast.makeText(Login_OTP.this, "gui OTP  Thanh cong", Toast.LENGTH_SHORT).show();
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
                            // Update UI
                            Intent intent = new Intent(Login_OTP.this, Home.class);
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
}