package com.divanoapps.learnwords.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class CannotSaveCardDialogFragment extends DialogFragment {
    public static String getUniqueTag() {
        return "com.divanoapps.learnwords.dialogs.CannotSaveCardDialogFragment";
    }

    public static CannotSaveCardDialogFragment newInstance(String message) {
        CannotSaveCardDialogFragment instance = new CannotSaveCardDialogFragment();

        Bundle arguments = new Bundle();
        arguments.putString("message", message);
        instance.setArguments(arguments);

        return instance;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        return new AlertDialog.Builder(getActivity())
            .setMessage(getArguments().getString("message"))
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ;
                }
            })
            .create();
    }
}
