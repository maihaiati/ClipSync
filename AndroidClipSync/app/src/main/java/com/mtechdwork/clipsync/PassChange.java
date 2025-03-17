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
import java.util.Objects;

public class PassChange extends AppCompatActivity {

    private final boolean debug = true;
    private SettingManager settingManager;

    // VIEWS
    private Button btnChangeUser;
    private Button btnChangePass;
    private TextView edtOldPass;
    private TextView edtNewPass;
    private TextView edtNewPassConfirm;
    private TextView edtUsername;

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

        String username = settingManager.getUsername();
        if (!username.equals("FAILED_TO_GET_USERNAME")) edtUsername.setText(username);
        else edtUsername.setText("");

        btnChangeUser.setOnClickListener(v -> {
            if (writeUsername())
                Toast.makeText(this, "Thay đổi tên người dùng thành công!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Thay đổi tên người dùng thất bại!", Toast.LENGTH_SHORT).show();
        });

        btnChangePass.setOnClickListener(v -> {
            if (checkPasswordMatch(edtOldPass.getText().toString())) { // Check current password
                if (writePassword())
                    Toast.makeText(this, "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Thay đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Sai mật khẩu hiện tại!", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean writeUsername() {
        String username = edtUsername.getText().toString();
        if (!username.equals("FAILED_TO_GET_USERNAME")) {
            settingManager.setUsername(username); // Write username
            return true;
        }
        Toast.makeText(this, "Tên người dùng không hợp lệ!", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean writePassword() {
        String password = edtNewPass.getText().toString();
        if (password.equals(edtNewPassConfirm.getText().toString())) {
            if (!password.isEmpty())
                settingManager.setPassword(sha512(password)); // Write password
            else
                settingManager.setPassword("");
            return true;
        }
        Toast.makeText(this, "Xác nhận mật khẩu mới không trùng khớp!", Toast.LENGTH_SHORT).show();
        return false;
    }

    private String sha512(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            byte[] digest = md.digest(password.getBytes());
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
        btnChangeUser = findViewById(R.id.btnChangeUser);
        btnChangePass = findViewById(R.id.btnChangePass);
        edtOldPass = findViewById(R.id.edtOldPass);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtNewPassConfirm = findViewById(R.id.edtNewPassConfirm);
        edtUsername = findViewById(R.id.edtUsername);
    }
}