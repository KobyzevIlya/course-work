package ru.hse.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.hse.news.api.ApiClient;
import ru.hse.news.api.ApiService;
import ru.hse.news.api.LoginResponse;

public class LoginFragment extends Fragment {

    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextLogin = view.findViewById(R.id.editTextLogin);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);
        textViewResult = view.findViewById(R.id.textViewResult);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = editTextLogin.getText().toString();
                String password = editTextPassword.getText().toString();
                loginUser(login, password);
            }
        });

        checkLogin();

        return view;
    }

    private void checkLogin() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.checkLogin();
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        if (loginResponse.getResult()) {
                            textViewResult.setText(loginResponse.getMessage());
                            setLoginFormVisibility(View.GONE);
                        } else {
                            textViewResult.setText(loginResponse.getMessage());
                            setLoginFormVisibility(View.VISIBLE);
                        }
                    } else {
                        textViewResult.setText("Empty response body");
                        setLoginFormVisibility(View.VISIBLE);
                    }
                } else {
                    textViewResult.setText("Login failed: " + response.code());
                    setLoginFormVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                textViewResult.setText("Login request failed: " + t.getMessage());
                setLoginFormVisibility(View.VISIBLE);
            }
        });
    }

    private void loginUser(String login, String password) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.loginUser(login, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        if (loginResponse.getResult()) {
                            textViewResult.setText(loginResponse.getMessage());
                            setLoginFormVisibility(View.GONE);
                        } else {
                            textViewResult.setText(loginResponse.getMessage());
                        }
                    } else {
                        textViewResult.setText("Empty response body");
                    }
                } else {
                    textViewResult.setText("Login failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                textViewResult.setText("Login request failed: " + t.getMessage());
            }
        });
    }

    private void setLoginFormVisibility(int visibility) {
        editTextLogin.setVisibility(visibility);
        editTextPassword.setVisibility(visibility);
        buttonLogin.setVisibility(visibility);
    }
}
