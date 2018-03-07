package com.divanoapps.learnwords.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class YesNoMessageDialogFragment extends DialogFragment {

    public interface OnYesClickedListener {
        void onClicked();
    }

    public interface OnNoClickedListener {
        void onClicked();
    }

    OnYesClickedListener mOnYesClickedListener = () -> {};
    OnNoClickedListener mOnNoClickedListener  = () -> {};

    public YesNoMessageDialogFragment setOnYesClickedListener(OnYesClickedListener listener) {
        mOnYesClickedListener = listener;
        return this;
    }

    public YesNoMessageDialogFragment setOnNoClickedListener(OnNoClickedListener listener) {
        mOnNoClickedListener = listener;
        return this;
    }

    public static String getUniqueTag() {
        return "com.divanoapps.learnwords.dialogs.MessageOkDialogFragment";
    }

    public static YesNoMessageDialogFragment newInstance(String message) {
        YesNoMessageDialogFragment instance = new YesNoMessageDialogFragment();

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
                .setPositiveButton("Yes", (dialog, which) -> mOnYesClickedListener.onClicked())
                .setNegativeButton("No",  (dialog, which) -> mOnNoClickedListener.onClicked())
                .create();
    }

    public static void show(AppCompatActivity activity, String message) {
        YesNoMessageDialogFragment.newInstance(message)
                .show(activity.getSupportFragmentManager(), getUniqueTag());
    }

    public static void show(AppCompatActivity activity, String message,
                            OnYesClickedListener yesClickedListener) {
        YesNoMessageDialogFragment.newInstance(message)
                .setOnYesClickedListener(yesClickedListener)
                .show(activity.getSupportFragmentManager(), getUniqueTag());
    }

    public static void show(AppCompatActivity activity, String message,
                            OnYesClickedListener yesClickedListener, OnNoClickedListener noClickedListener) {
        YesNoMessageDialogFragment.newInstance(message)
                .setOnYesClickedListener(yesClickedListener)
                .setOnNoClickedListener(noClickedListener)
                .show(activity.getSupportFragmentManager(), getUniqueTag());
    }
}
