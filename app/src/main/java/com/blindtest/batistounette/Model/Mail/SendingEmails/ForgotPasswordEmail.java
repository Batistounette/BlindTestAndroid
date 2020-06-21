package com.blindtest.batistounette.Model.Mail.SendingEmails;

import android.os.AsyncTask;
import android.util.Log;

import com.blindtest.batistounette.Model.Mail.JavaMailApi.GMailSender;

/**
 * Created by Batistounette on 16/06/2020.
 */

public class ForgotPasswordEmail extends AsyncTask<GMailSender, Void, Void> {

    private String mEmail;
    private String mPassword;

    public ForgotPasswordEmail(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    @Override
    protected Void doInBackground(GMailSender... params) {
        final String USER = "batistounette.blindtest@gmail.com";
        final String PASSWORD = "Teck100802?";
        final String RECIPIENT = mEmail;
        final String SUBJECT = "Nouveau mot de passe";
        final String BODY = "Vous avez initié une procédure pour réinitialiser votre mot de passe.\n" +
                "Nous avons généré aléatoirement le mot de passe suivant : " + mPassword + "\n" +
                "Entrer ce mot de passe, associé à votre identifiant pour vous connecter. \n" +
                "Il vous est ensuite conseillé de modifier ce mot de passe depuis l'onglet : Changer de mot de passe.\n\n" +
                "Pour tout renseignement, n'hésitez pas à nous contacter !";

        try {
            GMailSender sender = new GMailSender(USER, PASSWORD);
            sender.sendMail(SUBJECT, BODY, RECIPIENT);
        }

        catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }

        return null;
    }
}