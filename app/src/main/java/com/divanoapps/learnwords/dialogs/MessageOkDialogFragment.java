package com.divanoapps.learnwords.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class MessageOkDialogFragment extends DialogFragment {
    public static String getUniqueTag() {
        return "com.divanoapps.learnwords.dialogs.MessageOkDialogFragment";
    }

    public static MessageOkDialogFragment newInstance(String message) {
        MessageOkDialogFragment instance = new MessageOkDialogFragment();

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
            .setPositiveButton("Ok", (dialog, which) -> {})
            .create();
    }

    public static void show(AppCompatActivity activity, String message) {
        MessageOkDialogFragment.newInstance(message)
                .show(activity.getSupportFragmentManager(), getUniqueTag());
    }
}
