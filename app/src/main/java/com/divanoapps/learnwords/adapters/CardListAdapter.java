package com.divanoapps.learnwords.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.CardId;

import java.util.LinkedList;
import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    public interface EditCardClickedListener {
        void onEditCardClicked(CardId id);
    }

    public interface ToggleCardEnabledClickedListener {
        void onToggleCardEnabledClicked(CardId id);
    }

    public interface DeleteCardClickedListener {
        void onDeleteCardClicked(CardId id);
    }

    private EditCardClickedListener mEditCardClickedListener = id -> {};
    private ToggleCardEnabledClickedListener mToggleCardEnabledClickedListener = id -> {};
    private DeleteCardClickedListener mDeleteCardClickedListener = id -> {};

    public void setEditCardClickedListener(EditCardClickedListener  listener) {
        mEditCardClickedListener = listener;
    }

    public void setToggleCardEnabledClickedListener(ToggleCardEnabledClickedListener listener) {
        mToggleCardEnabledClickedListener = listener;
    }

    public void setDeleteCardClickedListener(DeleteCardClickedListener listener) {
        mDeleteCardClickedListener = listener;
    }

    private void notifyEditCardClicked(CardId id) {
        mEditCardClickedListener.onEditCardClicked(id);
    }

    private void notifyToggleCardEnabledClicked(CardId id) {
        mToggleCardEnabledClickedListener.onToggleCardEnabledClicked(id);
    }

    private void notifyDeleteCardClicked(CardId id) {
        mDeleteCardClickedListener.onDeleteCardClicked(id);
    }

    private Context mContext; // as CardListActivity
    private List<Card> mCards = new LinkedList<>();

    public CardListAdapter(Context context) {
        mContext = context;
    }

    public void setCards(List<Card> cards) {
        mCards = cards == null ? new LinkedList<>() : cards;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setFromCard(mCards.get(position));
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mItemView;

        TextView mWordView;
        TextView mWordCommentView;
        TextView mTranslationView;
        TextView mTranslationCommentView;

        TextView mDifficultyView;
        TextView mDifficultyMaximumView;

        ImageButton mToggleCardEnableButton;
        ImageButton mDeleteButton;

        ViewHolder(View itemView) {
            super(itemView);

            mItemView = itemView;

            mWordView = (TextView) itemView.findViewById(R.id.word_view);
            mWordCommentView = (TextView) itemView.findViewById(R.id.word_comment_view);
            mTranslationView = (TextView) itemView.findViewById(R.id.translation_view);
            mTranslationCommentView = (TextView) itemView.findViewById(R.id.translation_comment_view);

            mDifficultyView = (TextView) itemView.findViewById(R.id.difficulty_view);
            mDifficultyMaximumView = (TextView) itemView.findViewById(R.id.difficulty_maximum_view);

            mDeleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);
            mToggleCardEnableButton = (ImageButton) itemView.findViewById(R.id.switch_enable_button);
        }

        @SuppressLint("SetTextI18n")
        void setFromCard(final Card card) {
            mWordView.setText(card.getWord());
            mWordCommentView.setText(card.getWordComment());
            mTranslationView.setText(card.getTranslation());
            mTranslationCommentView.setText(card.getTranslationComment());

            mWordCommentView.setVisibility(card.getWordComment().isEmpty() ? View.GONE : View.VISIBLE);
            mTranslationCommentView.setVisibility(card.getTranslationComment().isEmpty() ? View.GONE : View.VISIBLE);

            mDifficultyView.setText(Integer.valueOf(card.getDifficulty()).toString());
            mDifficultyMaximumView.setText(Integer.valueOf(Card.getMaxDifficulty()).toString());

            mToggleCardEnableButton.setImageResource(card.isHidden() ?
                                                     R.drawable.ic_card_item_invisible :
                                                     R.drawable.ic_card_item_visible);

            View.OnClickListener editCardClickListener = v -> notifyEditCardClicked(card.getId());
            mItemView.setOnClickListener(editCardClickListener);
            mWordView.setOnClickListener(editCardClickListener);
            mWordCommentView.setOnClickListener(editCardClickListener);
            mTranslationView.setOnClickListener(editCardClickListener);
            mTranslationCommentView.setOnClickListener(editCardClickListener);
            mDifficultyView.setOnClickListener(editCardClickListener);
            mDifficultyMaximumView.setOnClickListener(editCardClickListener);

            mToggleCardEnableButton.setOnClickListener(v -> notifyToggleCardEnabledClicked(card.getId()));

            mDeleteButton.setOnClickListener(v -> notifyDeleteCardClicked(card.getId()));
        }
    }
}
