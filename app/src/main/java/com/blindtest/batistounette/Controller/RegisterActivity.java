package com.blindtest.batistounette.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blindtest.batistounette.Model.Mail.SendingEmails.RegistrationEmail;
import com.blindtest.batistounette.Model.User.User;
import com.blindtest.batistounette.Model.User.UserManager;
import com.blindtest.batistounette.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import static com.blindtest.batistounette.Model.Encryption.EncryptionPassword.decrypt;
import static com.blindtest.batistounette.Model.Encryption.EncryptionPassword.encrypt;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mFullName;
    private TextInputLayout mUsername;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mSignUp;

    public static final String BUNDLE_LAST_USERNAME = "BUNDLE_LAST_USERNAME";
    public static final String BUNDLE_LAST_PASSWORD = "BUNDLE_LAST_PASSWORD";

    public static final Pattern USERNAME_PATTERN =
            Pattern.compile("^" + ".{4,20}" + "$");

    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" + "(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" +
                    "(?=.*[²&é\"#'|è_ç^à@°+=/*£$¤%ùµ<>?,.;:§!])" + "(?=.*[\\S+$])" +
                    ".{8,20}" + "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final UserManager M_USER = new UserManager(this);

        mFullName = findViewById(R.id.activity_register_full_name);
        mUsername = findViewById(R.id.activity_register_username);
        mEmail = findViewById(R.id.activity_register_email);
        mPassword = findViewById(R.id.activity_register_password);
        mSignUp = findViewById(R.id.activity_register_sign_up);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFullName() && validateUsername() && validateEmail() && validatePassword()) {
                    try {
                        newUser(M_USER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void newUser(final UserManager MY_USER) throws Exception {
        final String FULLNAME = mFullName.getEditText().getText().toString().trim();
        final String USERNAME = mUsername.getEditText().getText().toString().trim();
        final String EMAIL = mEmail.getEditText().getText().toString().trim();
        final String PASSWORD = encrypt(mPassword.getEditText().getText().toString().trim());

        MY_USER.openDatabase();

        Cursor c = MY_USER.getTable();

        if (c.moveToFirst()) {
            boolean userAlreadyExists = false;

            do {

                if (USERNAME.equals(c.getString(c.getColumnIndex(UserManager.USER_NAME)))) {
                    Toast.makeText(this, R.string.Tusername_already_exists, Toast.LENGTH_SHORT).show();

                    userAlreadyExists = true;
                }

                else if (EMAIL.equals(c.getString(c.getColumnIndex(UserManager.USER_EMAIL)))) {
                    Toast.makeText(this, R.string.Temail_already_exists, Toast.LENGTH_SHORT).show();

                    userAlreadyExists = true;
                }

            } while (c.moveToNext() && !userAlreadyExists);

            if (!userAlreadyExists) {validateUser(MY_USER, FULLNAME, USERNAME, EMAIL, PASSWORD);}
        }

        else {validateUser(MY_USER, FULLNAME, USERNAME, EMAIL, PASSWORD);}

        c.close();
        MY_USER.closeDataBase();
    }

    private void validateUser(final UserManager MY_USER, final String FULLNAME, final String USERNAME, final String EMAIL, final String PASSWORD) throws Exception {
        MY_USER.addUser(new User(0, FULLNAME, USERNAME, EMAIL, PASSWORD));

        MY_USER.openDatabase();

        Cursor c = MY_USER.getTable();

        if (c.moveToFirst()) {
            boolean userIsChecked = false;

            do {

                if (FULLNAME.equals(c.getString(c.getColumnIndex(UserManager.USER_FULL_NAME)))
                        && USERNAME.equals(c.getString(c.getColumnIndex(UserManager.USER_NAME)))
                        && EMAIL.equals(c.getString(c.getColumnIndex(UserManager.USER_EMAIL)))
                        && PASSWORD.equals(c.getString(c.getColumnIndex(UserManager.USER_PASSWORD)))) {
                    userIsChecked = true;
                }

            } while (c.moveToNext() && !userIsChecked);

            if (!userIsChecked) {Toast.makeText(this, R.string.Tregistration_failed, Toast.LENGTH_SHORT).show();}

            else {
                Toast.makeText(this, R.string.Tsuccesful_registration, Toast.LENGTH_SHORT).show();

                this.saveUser(USERNAME, decrypt(PASSWORD));
                new RegistrationEmail(EMAIL).execute();

                final Intent GAME_ACTIVITY_INTENT = new Intent(RegisterActivity.this, GameActivity.class);
                startActivity(GAME_ACTIVITY_INTENT);
            }
        }

        c.close();
        MY_USER.closeDataBase();
    }

    private boolean validateFullName() {
        final String FULL_NAME_INTPUT = mFullName.getEditText().getText().toString().trim();
        boolean enabledButton = false;

        if (FULL_NAME_INTPUT.isEmpty()) {
            mFullName.setError("Ce champ ne doit pas être vide.");
        }

        else {
            mFullName.setError(null);
            enabledButton = true;
        }

        return enabledButton;
    }

    private boolean validateUsername() {
        final String USERNAME_INPUT = mUsername.getEditText().getText().toString().trim();
        boolean enabledButton = false;

        if (USERNAME_INPUT.isEmpty()) {
            mUsername.setError("Ce champ ne doit pas être vide.");
        }

        else if (!USERNAME_PATTERN.matcher(USERNAME_INPUT).matches()) {
            if (USERNAME_INPUT.length() < 4) {
                mUsername.setError("Le nom d'utilisateur doit contenir au moins 4 caractères.");
            }

            else {
                mUsername.setError("Le nom d'utilisateur ne doit pas contenir plus de 20 caractères.");
            }
        }

        else {
            mUsername.setError(null);
            enabledButton = true;
        }

        return enabledButton;
    }

    private boolean validateEmail() {
        final String EMAIL_INPUT = mEmail.getEditText().getText().toString().trim();
        boolean enabledButton = false;

        if (EMAIL_INPUT.isEmpty()) {
            mEmail.setError("Ce champ ne doit pas être vide.");
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(EMAIL_INPUT).matches()) {
            mEmail.setError("Veuillez entrer une adresse mail valide.");
        }

        else {
            mEmail.setError(null);
            enabledButton = true;
        }

        return enabledButton;
    }

    private boolean validatePassword() {
        final String PASSWORD_INPUT = mPassword.getEditText().getText().toString().trim();
        boolean enabledButton = false;

        if (PASSWORD_INPUT.isEmpty()) {
            mPassword.setError("Ce champ ne doit pas être vide.");
        }

        else if (!PASSWORD_PATTERN.matcher(PASSWORD_INPUT).matches()) {
            if (PASSWORD_INPUT.length() < 8) {
                mPassword.setError("Le mot de passe doit contenir au moins 8 caractères.");
            }

            else if (!Pattern.compile("[a-zA-Z]").matcher(PASSWORD_INPUT).find()) {
                mPassword.setError("Le mot de passe doit contenir des lettres.");
            }

            else if (!Pattern.compile("[a-z]").matcher(PASSWORD_INPUT).find()) {
                mPassword.setError("Le mot de passe doit contenir au moins une minuscule.");
            }

            else if (!Pattern.compile("[A-Z]").matcher(PASSWORD_INPUT).find()) {
                mPassword.setError("Le mot de passe doit contenir au moins une majuscule.");
            }

            else if (!Pattern.compile("[0-9]").matcher(PASSWORD_INPUT).find()) {
                mPassword.setError("Le mot de passe doit contenir au moins un chiffre.");
            }

            else if (!Pattern.compile("[²&é\"#'|è_ç^à@°+=/*£$¤%ùµ<>?,.;:§!]").matcher(PASSWORD_INPUT).find()) {
                mPassword.setError("Le mot de passe doit contenir au moins un caractère spécial.");
            }

            else if (!Pattern.compile("[\\S+$]").matcher(PASSWORD_INPUT).find()) {
                mPassword.setError("Le mot de passe ne doit pas contenir d'espace.");
            }

            else {
                mPassword.setError("Le mot de passe ne doit pas contenir plus de 20 caractères.");
            }
        }

        else {
            mPassword.setError(null);
            enabledButton = true;
        }

        return enabledButton;
    }

    private void saveUser(final String USERNAME, final String PASSWORD) {
        final Intent INTENT = new Intent();

        INTENT.putExtra(BUNDLE_LAST_USERNAME, USERNAME);
        INTENT.putExtra(BUNDLE_LAST_PASSWORD, PASSWORD);

        setResult(RESULT_OK, INTENT);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}