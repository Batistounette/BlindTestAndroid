package com.blindtest.batistounette.View;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.UserManager;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.blindtest.batistounette.R;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Created by Batistounette on 10/06/2020.
 */

public class ForgotPasswordDialog extends AppCompatDialogFragment {

    private TextInputLayout mEmail;
    private EmailDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_main_forgot_password_dialog, null);

        builder.setView(view)
                .setTitle("Nouveau mot de passe")
                .setMessage("Entrer l'adresse mail de votre compte pour réinitialiser le mot de passe.")
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Réinitialiser", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String EMAIL = mEmail.getEditText().getText().toString().trim();

                        try {
                            listener.validateEmail(EMAIL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        mEmail = view.findViewById(R.id.activity_main_forgot_password_dialog_email);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (EmailDialogListener) context;
        }

        catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    public interface EmailDialogListener {
        void validateEmail(final String EMAIL) throws Exception;
    }
}