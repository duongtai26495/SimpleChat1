package com.example.simplechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button btnRegi,btnLogin;
    TextView tvRegByFb;
    EditText edtUsername,edtPassword;
    Switch saveSwitch;
    SharedPreferences sharedPreferences;
    ConstraintLayout layoutBig;
    ImageView imgLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        
        sharedPreferences = getSharedPreferences("dalaLogin",MODE_PRIVATE);
        edtUsername.setText(sharedPreferences.getString("userName",""));
        edtPassword.setText(sharedPreferences.getString("passWord",""));
        saveSwitch.setChecked(sharedPreferences.getBoolean("active",false));

        if(!saveSwitch.isChecked()){
            Intent intent = getIntent();
            edtUsername.setText(intent.getStringExtra("1"));
        }
        btnRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Signup.class));
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });
    }
    private void AnhXa(){
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        layoutBig = (ConstraintLayout) findViewById(R.id.layoutBig);
        saveSwitch = (Switch) findViewById(R.id.saveSwitch);
        btnRegi = (Button) findViewById(R.id.btnReg);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvRegByFb = (TextView) findViewById(R.id.tvLoginByFb);
        edtUsername = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
    }
    private void SignIn() {
            final String userName = edtUsername.getText().toString();
            final String passWord = edtPassword.getText().toString();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference users = firebaseDatabase.getReference("USERS").child(userName);
            users.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                    if (dataSnapshot.getValue() == null) {
                        Toast.makeText(MainActivity.this, "Tài khoản / Mật khẩu sai hoặc không tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        // lấy dữ liệu từ dataSnapshot gán vào model User,
                        // lưu ý : biến ở User cần trùng khớp với tên các giá trị trên firebase
                        User user = dataSnapshot.getValue(User.class);
                        if (user.password.equals(passWord)) {
                            Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            if(saveSwitch.isChecked()){
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userName",userName);
                                editor.putString("passWord",passWord);
                                editor.putBoolean("active",true);
                                editor.commit();
                                startActivity(new Intent(MainActivity.this,Home.class));
                                finish();
                            }else{
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("userName");
                                editor.remove("passWord");
                                editor.remove("active");
                                editor.commit();
                            }
                            Intent intent = new Intent(MainActivity.this,Home.class);
                            intent.putExtra("1",user.firstName);
                            intent.putExtra("2",user.lastName);
                            startActivity(intent);
                            finish();
                        } else {
// thông báo sai mật khẩu
                            Toast.makeText(MainActivity.this, "Tài khoản hoặc mật khẩu sai, hãy thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                    }catch(Exception e){
                        Toast.makeText(MainActivity.this, "Có lỗi xảy ra, hãy thử lại", Toast.LENGTH_SHORT).show();
                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

        }
}
