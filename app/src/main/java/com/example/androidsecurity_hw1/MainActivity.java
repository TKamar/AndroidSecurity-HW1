package com.example.androidsecurity_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout username;
    private TextInputLayout password;
    private Button btnLogin;
    private CheckBox cboxNotARobot;
    private boolean isPlugged;
    private boolean isMuted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initObjects();
                Log.d("username", "onClick: " + username.getEditText());
                if (checkUsername() && checkPassword() && cboxNotARobot.isChecked() && isPlugged && isMuted) {
                    Toast.makeText(getApplicationContext(), "All are VALID!", Toast.LENGTH_SHORT).show();
                    movePage();
                } else {
                    if (!checkUsername() || !checkPassword()) {
                        Toast.makeText(getApplicationContext(), "Username or Password are NOT VALID!", Toast.LENGTH_SHORT).show();
                    }
                    if (!cboxNotARobot.isChecked()) {
                        System.out.println(cboxNotARobot.isChecked());
                        Log.d("btnClicked", "onClick: " + cboxNotARobot.isChecked());
                        Toast.makeText(getApplicationContext(), "Check must be checked", Toast.LENGTH_SHORT).show();
                    }
                    if (!isPlugged) {
                        Toast.makeText(getApplicationContext(), "Phone must be plugged in", Toast.LENGTH_SHORT).show();
                    }
                    if (!isMuted) {
                        Toast.makeText(getApplicationContext(), "Phone must be muted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void movePage() {
        String sUsername = username.getEditText().getText().toString();
        Intent i = new Intent(getApplicationContext(), WelcomePage.class);
        i.putExtra("currUsername", sUsername);
        startActivity(i);
    }

    private boolean checkPassword() {
        return PatternValidPassword(this.password.getEditText().getText().toString());
    }

    private boolean checkUsername() {
        return PatternValidUsername(this.username.getEditText().getText().toString());
    }

    public boolean PatternValidUsername(String username){
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(username);
        return matcher.find();
    }

    public boolean PatternValidPassword(String password){
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public boolean isPlugged(Context context) {
        isPlugged= false;
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        isPlugged = plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            isPlugged = isPlugged || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
        }
        return isPlugged;
    }

    public int getSoundLevelOfPhoneRing(Context context){
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //The call returns 0 when your phone is in mute or vibration mode. Checkout your ringer mode.
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_RING);
        return currentVolume;
    }

    public void initObjects() {
        isPlugged = isPlugged(this);
        isMuted= getSoundLevelOfPhoneRing(this)==0;
        cboxNotARobot.isChecked();
    }

    public void findViews() {
        btnLogin = findViewById(R.id.btnLogin);
        username = findViewById(R.id.edtUsername);
        password = findViewById(R.id.edtPassword);
        cboxNotARobot = findViewById((R.id.cboxNotARobot));
    }
}