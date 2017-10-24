package com.divanoapps.learnwords;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by dmitry on 10.10.17.
 */

public class RenameDeckDialogFragment extends DialogFragment {
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface RenameDeckDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
//        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    RenameDeckDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (RenameDeckDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement RenameDeckDialogListener");
        }
    }

    private String mNewDeckName = "";

    public String getNewDeckName() {
        return mNewDeckName;
    }

    public static RenameDeckDialogFragment newInstance(String oldDeckName) {
        RenameDeckDialogFragment instance = new RenameDeckDialogFragment();

        Bundle args = new Bundle();
        args.putString("deckName", oldDeckName);
        instance.setArguments(args);

        return instance;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        mNewDeckName = getArguments().getString("deckName");

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_rename_deck, null);
        final EditText deckNameEdit = (EditText) dialogView.findViewById(R.id.deck_name_edit);
        deckNameEdit.setText(mNewDeckName);
        Dialog dialog = new AlertDialog.Builder(getActivity())
            .setView(dialogView)
            .setPositiveButton(R.string.rename, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mNewDeckName = deckNameEdit.getText().toString();
                    mListener.onDialogPositiveClick(RenameDeckDialogFragment.this);
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RenameDeckDialogFragment.this.getDialog().cancel();
                }
            })
            .create();
        return dialog;
    }
}
