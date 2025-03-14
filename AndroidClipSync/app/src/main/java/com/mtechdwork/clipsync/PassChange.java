package com.mtechdwork.clipsync;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class PassChange extends AppCompatActivity {

    private final boolean debug = true;
    private SettingManager settingManager;

    // VIEWS
    private Button btnApply;
    private TextView edtOldPass;
    private TextView edtNewPass;
    private TextView edtNewPassConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pass_change);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addViews();

        settingManager = new SettingManager(this);

        btnApply.setOnClickListener(v -> {
            if (checkPasswordMatch(edtOldPass.getText().toString())) {
                if (edtNewPass.getText().equals(edtNewPassConfirm.getText()))
                    settingManager.setPassword(sha512(edtNewPass.getText().toString()));
                else
                    Toast.makeText(this, "Xác nhận mật khẩu mới không trùng khớp!", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Sai mật khẩu hiện tại!", Toast.LENGTH_SHORT).show();
        });
    }

    private String sha512(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest)
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

            return sb.toString();
        } catch (Exception e) {
            if (debug) Log.e("[Pass Change]", Objects.requireNonNull(e.getMessage()));
        }
        return "";
    }

    private boolean checkPasswordMatch(String password) {
        String currentPass = settingManager.getPassword();
        if (!password.isEmpty()) return sha512(password).equals(currentPass);
        return password.equals(currentPass);
    }

    private void addViews() {
        btnApply = findViewById(R.id.btnApply);
        edtOldPass = findViewById(R.id.edtOldPass);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtNewPassConfirm = findViewById(R.id.edtNewPassConfirm);
    }
}