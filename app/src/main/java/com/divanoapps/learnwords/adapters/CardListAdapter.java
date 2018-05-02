package com.divanoapps.learnwords.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.data.local.Card;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    public interface EditCardClickedListener {
        void onEditCardClicked(String deckName, String word, String comment);
    }

    public interface ToggleCardEnabledClickedListener {
        void onToggleCardEnabledClicked(String deckName, String word, String comment);
    }

    public interface DeleteCardClickedListener {
        void onDeleteCardClicked(String deckName, String word, String comment);
    }

    private EditCardClickedListener mEditCardClickedListener = (deckName, word, comment) -> {};
    private ToggleCardEnabledClickedListener mToggleCardEnabledClickedListener = (deckName, word, comment) -> {};
    private DeleteCardClickedListener mDeleteCardClickedListener = (deckName, word, comment) -> {};

    public void setEditCardClickedListener(EditCardClickedListener  listener) {
        mEditCardClickedListener = listener;
    }

    public void setToggleCardEnabledClickedListener(ToggleCardEnabledClickedListener listener) {
        mToggleCardEnabledClickedListener = listener;
    }

    public void setDeleteCardClickedListener(DeleteCardClickedListener listener) {
        mDeleteCardClickedListener = listener;
    }

    private void notifyEditCardClicked(String deckName, String word, String comment) {
        mEditCardClickedListener.onEditCardClicked(deckName, word, comment);
    }

    private void notifyToggleCardEnabledClicked(String deckName, String word, String comment) {
        mToggleCardEnabledClickedListener.onToggleCardEnabledClicked(deckName, word, comment);
    }

    private void notifyDeleteCardClicked(String deckName, String word, String comment) {
        mDeleteCardClickedListener.onDeleteCardClicked(deckName, word, comment);
    }

    private LayoutInflater layoutInflater;

    private List<Card> cards = new LinkedList<>();

    public CardListAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards == null ? new LinkedList<>() : cards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setFromCard(cards.get(position));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.word_view)
        TextView wordView;

        @BindView(R.id.word_comment_view)
        TextView commentView;

        @BindView(R.id.translation_view)
        TextView translationView;

        @BindView(R.id.difficulty_view)
        TextView difficultyView;

        @BindView(R.id.difficulty_maximum_view)
        TextView difficultyMaximumView;

        @BindView(R.id.switch_enable_button)
        ImageButton toggleCardEnableButton;

        @BindView(R.id.delete_button)
        ImageButton deleteButton;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        void setFromCard(final Card card) {
            wordView.setText(card.getWord());
            commentView.setText(card.getComment());
            translationView.setText(card.getTranslation());

            commentView.setVisibility(card.getComment().trim().isEmpty() ? View.GONE : View.VISIBLE);

            difficultyView.setText(card.getDifficulty().toString());
            difficultyMaximumView.setText(Card.getMaxDifficulty().toString());

            toggleCardEnableButton.setImageResource(card.isHidden() ?
                                                     R.drawable.ic_card_item_invisible :
                                                     R.drawable.ic_card_item_visible);

            View.OnClickListener editCardClickListener = v -> notifyEditCardClicked(card.getDeckName(), card.getWord(), card.getComment());
            itemView.setOnClickListener(editCardClickListener);
            wordView.setOnClickListener(editCardClickListener);
            commentView.setOnClickListener(editCardClickListener);
            translationView.setOnClickListener(editCardClickListener);
            difficultyView.setOnClickListener(editCardClickListener);
            difficultyMaximumView.setOnClickListener(editCardClickListener);

            toggleCardEnableButton.setOnClickListener(v -> notifyToggleCardEnabledClicked(card.getDeckName(), card.getWord(), card.getComment()));

            deleteButton.setOnClickListener(v -> notifyDeleteCardClicked(card.getDeckName(), card.getWord(), card.getComment()));
        }
    }
}
