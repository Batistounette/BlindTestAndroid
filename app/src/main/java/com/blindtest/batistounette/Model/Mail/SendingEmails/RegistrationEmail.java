package com.blindtest.batistounette.Model.Mail.SendingEmails;

import android.os.AsyncTask;
import android.util.Log;

import com.blindtest.batistounette.Model.Mail.JavaMailApi.GMailSender;

/**
 * Created by Batistounette on 08/06/2020.
 */

public class RegistrationEmail extends AsyncTask<GMailSender, Void, Void> {

    private String mEmail;

    public RegistrationEmail(String email) {
        this.setEmail(email);
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    @Override
    protected Void doInBackground(GMailSender... params) {
        final String USER = "batistounette.blindtest@gmail.com";
        final String PASSWORD = "Teck100802?";
        final String RECIPIENT = mEmail;
        final String SUBJECT = "Inscription";
        final String BODY = "Bien le bonjour, toute l'équipe de la Batistounette Society (une seule personne)" +
                " vous remercie d'avoir créé un compte sur cette application d'ores et déjà culte.\n" +
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