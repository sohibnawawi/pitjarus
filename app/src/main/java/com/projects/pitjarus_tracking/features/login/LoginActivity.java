package com.projects.pitjarus_tracking.features.login;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.projects.pitjarus_tracking.R;
import com.projects.pitjarus_tracking.connections.requests.PostLoginRequest;
import com.projects.pitjarus_tracking.databinding.ActivityLoginBinding;
import com.projects.pitjarus_tracking.features.BaseActivity;
import com.projects.pitjarus_tracking.features.mainmenu.MainmenuActivity;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private LoginViewModel loginViewModel;
    private String username;
    private String password;

    @Override
    protected int attachLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initAction() {
        super.initAction();

        binding.btnLogin.setOnClickListener(onClick -> {
            username = binding.etUsername.getText().toString();
            password = binding.etPassword.getText().toString();
            login();
        });
    }

    @Override
    protected void initData() {
        super.initData();

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    private void login(){
        PostLoginRequest request = PostLoginRequest.builder()
                .username(username)
                .password(password)
                .build();


        loginViewModel.postLogin(request);
        loginViewModel.getLoginLiveData().observe(this, response->{
            if (!response.isEmpty()){
                Log.v("responseObserve", response.toString());
                loginViewModel.insertResponseToDatabase(response);
                showMainmenuActivity();
            }
            else {
                Toast.makeText(getApplication(), "Username dan Password Tidak Sesuai", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMainmenuActivity(){
        Intent intent = new Intent(LoginActivity.this, MainmenuActivity.class);
        startActivity(intent);
    }
}