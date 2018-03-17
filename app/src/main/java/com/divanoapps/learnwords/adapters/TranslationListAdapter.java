package com.divanoapps.learnwords.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.entities.TranslationOption;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dmitry on 08.03.18.
 */

public class TranslationListAdapter extends RecyclerView.Adapter<TranslationListAdapter.ViewHolder> {

    List<TranslationOption> mTranslationOptions = new LinkedList<>();

    public void setTranslationOptions(List<TranslationOption> translationOptions) {
        mTranslationOptions = translationOptions;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_translation_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(mTranslationOptions.get(position));
    }

    @Override
    public int getItemCount() {
        return mTranslationOptions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.part_of_speech_view)
        TextView partOfSpeechView;

        @BindView(R.id.translation_view)
        TextView translationView;

        @BindView(R.id.means_view)
        TextView meansView;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void setContent(TranslationOption translationOption) {
            partOfSpeechView.setText(translationOption.getPartOfSpeech());
            translationView.setText(translationOption.getTranslation());
            meansView.setText(translationOption.getMeans());
        }
    }
}
