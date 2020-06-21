package com.blindtest.batistounette.Controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blindtest.batistounette.Model.Mail.SendingEmails.ForgotPasswordEmail;
import com.blindtest.batistounette.Model.User.User;
import com.blindtest.batistounette.Model.User.UserManager;
import com.blindtest.batistounette.R;
import com.blindtest.batistounette.View.ForgotPasswordDialog;

import static com.blindtest.batistounette.Model.Encryption.EncryptionPassword.encrypt;
import static com.blindtest.batistounette.Model.Encryption.EncryptionPassword.generateRandomString;

public class MainActivity extends AppCompatActivity implements ForgotPasswordDialog.EmailDialogListener {

    private EditText mUsernameEmail;
    private EditText mPassword;
    private Button mLogin;
    private TextView mForgotPassword;
    private TextView mSignUp;

    private SharedPreferences mPreferences;

    private long backPressedTime;
    private Toast backToast;

    public static final int REGISTER_ACTIVITY_REQUEST_CODE = 0;

    public static final String PREF_KEY_USERNAME = "PREF_KEY_USERNAME";
    public static final String PREF_KEY_PASSWORD = "PREF_KEY_PASSWORD";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REGISTER_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            assert data != null;

            final String USERNAME = data.getStringExtra(RegisterActivity.BUNDLE_LAST_USERNAME);
            final String PASSWORD = data.getStringExtra(RegisterActivity.BUNDLE_LAST_PASSWORD);

            mPreferences.edit().putString(PREF_KEY_USERNAME, USERNAME).apply();
            mPreferences.edit().putString(PREF_KEY_PASSWORD, PASSWORD).apply();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final UserManager M_USER = new UserManager(this);

        mPreferences = getPreferences(MODE_PRIVATE);

        mUsernameEmail = findViewById(R.id.activity_main_username_email);
        mPassword = findViewById(R.id.activity_main_password);
        mLogin = findViewById(R.id.activity_main_login);
        mForgotPassword = findViewById(R.id.activity_main_forgot_password);
        mSignUp = findViewById(R.id.activity_main_sign_up);

        mLogin.setEnabled(this.greetUser());

        mUsernameEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLogin.setEnabled(!s.toString().isEmpty() && !mPassword.toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLogin.setEnabled(!s.toString().isEmpty() && !mUsernameEmail.toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(checkUser(M_USER)) {
                        final Intent GAME_ACTIVITY_INTENT = new Intent(MainActivity.this, GameActivity.class);
                        startActivity(GAME_ACTIVITY_INTENT);
                    }
                }

                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent REGISTER_ACTIVITY_INTENT = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(REGISTER_ACTIVITY_INTENT, REGISTER_ACTIVITY_REQUEST_CODE);
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private boolean checkUser(final UserManager MY_USER) throws Exception {
        final String USERNAME_EMAIL = mUsernameEmail.getText().toString();
        final String PASSWORD = encrypt(mPassword.getText().toString());
        boolean userIsChecked = false;

        MY_USER.openDatabase();

        Cursor c = MY_USER.getTable();

        if (c.moveToFirst()) {
            do {
                if ((USERNAME_EMAIL.equals(c.getString(c.getColumnIndex(UserManager.USER_NAME)))
                        && PASSWORD.equals(c.getString(c.getColumnIndex(UserManager.USER_PASSWORD))))
                        || (USERNAME_EMAIL.equals(c.getString(c.getColumnIndex(UserManager.USER_EMAIL)))
                        && PASSWORD.equals(c.getString(c.getColumnIndex(UserManager.USER_PASSWORD))))) {
                    Toast.makeText(this, R.string.Tsuccesful_connection, Toast.LENGTH_SHORT).show();

                    userIsChecked = true;
                }

            } while (c.moveToNext() && !userIsChecked);

            if (!userIsChecked) {Toast.makeText(this, R.string.Twrong_username_password, Toast.LENGTH_SHORT).show();}
        }

        else {Toast.makeText(this, R.string.Tno_account, Toast.LENGTH_SHORT).show();}

        c.close();
        MY_USER.closeDataBase();

        return userIsChecked;
    }

    private boolean greetUser() {
        final String USERNAME = mPreferences.getString(PREF_KEY_USERNAME, null);
        final String PASSWORD = mPreferences.getString(PREF_KEY_PASSWORD, null);
        boolean emptyPreferences = false;

        if (USERNAME != null && PASSWORD != null) {
            mUsernameEmail.setText(USERNAME);
            mPassword.setText(PASSWORD);

            emptyPreferences = true;
        }

        else {
            mUsernameEmail.setText(null);
            mPassword.setText(null);
        }

        return emptyPreferences;
    }

    public void openDialog() {
        ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog();

        forgotPasswordDialog.setCancelable(false);
        forgotPasswordDialog.show(getSupportFragmentManager(), "Forgot password dialog");
    }

    @Override
    public void validateEmail(final String EMAIL) throws Exception {
        final UserManager MY_USER = new UserManager(this);

        MY_USER.openDatabase();

        Cursor c = MY_USER.getTable();

        if (c.moveToFirst()) {
            boolean userIsChecked = false;

            do {
                if (EMAIL.equals(c.getString(c.getColumnIndex(UserManager.USER_EMAIL)))) {
                    Toast.makeText(this, R.string.Temail_sent, Toast.LENGTH_SHORT).show();

                    final String RANDOM_PASSWORD = generateRandomString();

                    MY_USER.editUser(new User(c.getInt(c.getColumnIndex(UserManager.USER_ID)),
                            "", "", "", encrypt(RANDOM_PASSWORD)));

                    new ForgotPasswordEmail(EMAIL, RANDOM_PASSWORD).execute();
                    userIsChecked = true;
                }

            } while (c.moveToNext() && !userIsChecked);

            if (!userIsChecked) {Toast.makeText(this, R.string.Temail_does_not_exist, Toast.LENGTH_SHORT).show();}
        }

        else {Toast.makeText(this, R.string.Tno_account, Toast.LENGTH_SHORT).show();}

        c.close();
        MY_USER.closeDataBase();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        }

        else {
            backToast = Toast.makeText(getBaseContext(), "Appuyer encore pour quitter", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}