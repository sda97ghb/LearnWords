package com.divanoapps.learnwords.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.divanoapps.learnwords.R;

/**
 * Created by dmitry on 28.11.17.
 */

public class AddDeckDialogFragment extends DialogFragment {

    public interface OnOkClickedListener {
        void onOkClicked(String name, String languageFrom, String languageTo);
    }

    OnOkClickedListener mOnOkClickedListener = (a1, a2, a3) -> {};

    public void setOnOkClickedListener(OnOkClickedListener listener) {
        mOnOkClickedListener = listener;
    }

    public static String getUniqueTag() {
        return "com.divanoapps.learnwords.dialogs.AddDeckDialogFragment";
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_deck, null);
        final EditText deckNameEdit = (EditText) dialogView.findViewById(R.id.deck_name_edit);
        final Spinner languageFromPicker = (Spinner) dialogView.findViewById(R.id.language_from_picker);
        final Spinner languageToPicker = (Spinner) dialogView.findViewById(R.id.language_to_picker);
        return new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton(R.string.dialog_add_button_text, (dialog, which) ->
                    mOnOkClickedListener.onOkClicked(deckNameEdit.getText().toString(),
                                                     languageFromPicker.getSelectedItem().toString(),
                                                     languageToPicker.getSelectedItem().toString())
                )
                .setNegativeButton(R.string.cancel, (dialog, which) -> this.getDialog().cancel())
                .create();
    }
}
