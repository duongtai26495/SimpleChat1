package com.example.simplechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup extends Activity {
    EditText edtUserName,edtPassword,edtRePassword,edtFirstName,edtLastName;
    Button btnReg,btnCancel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        anhXa();
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, MainActivity.class));
                finish();
            }
        });
    }
    private void anhXa(){
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtUserName = (EditText) findViewById(R.id.edtUsernameReg);
        edtPassword = (EditText) findViewById(R.id.edtPasswordReg);
        edtRePassword = (EditText) findViewById(R.id.edtRePasswordReg);
        btnReg = (Button) findViewById(R.id.btnReg);
        btnCancel = (Button) findViewById(R.id.btnCancel);
    }
    public void SignUp(){
        final String firstName = edtFirstName.getText().toString().trim();
        final String lastName = edtLastName.getText().toString().trim();
        final String userName = edtUserName.getText().toString().trim();
        final String passWord = edtPassword.getText().toString().trim();
        final String rePassWord = edtRePassword.getText().toString().trim();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("USERS").child(userName);
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        User user = new User();
                        user.firstName = firstName;
                        user.lastName = lastName;
                        user.password = passWord;
                        if (passWord.equals(rePassWord) == false) {
                            Toast.makeText(Signup.this, "Hãy nhập mật khẩu giống nhau ở 2 ô", Toast.LENGTH_SHORT).show();
                        } else {
                            users.setValue(user, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                    Toast.makeText(Signup.this, "Tạo thành công, mời đăng nhập", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Signup.this, MainActivity.class);
                                    intent.putExtra("1",edtUserName.getText().toString());
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(Signup.this, "Bị lỗi hoặc username đã tồn tại, hãy thử lại", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
}
}
