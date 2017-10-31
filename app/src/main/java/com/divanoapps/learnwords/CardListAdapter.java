package com.divanoapps.learnwords;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.divanoapps.learnwords.Entities.Card;
import com.divanoapps.learnwords.Entities.CardId;
import com.divanoapps.learnwords.Entities.Deck;

import java.util.LinkedList;
import java.util.List;

class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    public interface EditCardClickedListener {
        void onEditCardClicked(CardId id);
    }

    public interface ToggleCardEnabledClickedListener {
        void onToggleCardEnabledClicked(CardId id);
    }

    public interface DeleteCardClickedListener {
        void onDeleteCardClicked(CardId id);
    }

    private EditCardClickedListener mEditCardClickedListener = new EditCardClickedListener() {
        @Override
        public void onEditCardClicked(CardId id) {}
    };

    private ToggleCardEnabledClickedListener mToggleCardEnabledClickedListener = new ToggleCardEnabledClickedListener() {
        @Override
        public void onToggleCardEnabledClicked(CardId id) {}
    };

    private DeleteCardClickedListener mDeleteCardClickedListener = new DeleteCardClickedListener() {
        @Override
        public void onDeleteCardClicked(CardId id) {}
    };

    public void setEditCardClickedListener(EditCardClickedListener  listener) {
        mEditCardClickedListener = listener;
    }

    public void setToggleCardEnabledClickedListener(ToggleCardEnabledClickedListener listener) {
        mToggleCardEnabledClickedListener = listener;
    }

    public void setDeleteCardClickedListener(DeleteCardClickedListener listener) {
        mDeleteCardClickedListener = listener;
    }

    void notifyEditCardClicked(CardId id) {
        mEditCardClickedListener.onEditCardClicked(id);
    }

    void notifyToggleCardEnabledClicked(CardId id) {
        mToggleCardEnabledClickedListener.onToggleCardEnabledClicked(id);
    }

    void notifyDeleteCardClicked(CardId id) {
        mDeleteCardClickedListener.onDeleteCardClicked(id);
    }

    private Context mContext; // as CardListActivity
    private List<Card> mCards = new LinkedList<>();

    public CardListAdapter(Context context) {
        mContext = context;
    }

    public void setCards(List<Card> cards) {
        mCards = cards == null ? new LinkedList<Card>() : cards;
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

        void setFromCard(final Card card) {
            mWordView.setText(card.getWord());
            mWordCommentView.setText(card.getWordComment());
            mTranslationView.setText(card.getTranslation());
            mTranslationCommentView.setText(card.getTranslationComment());

            mWordCommentView.setVisibility(card.getWordComment().isEmpty() ? View.GONE : View.VISIBLE);
            mTranslationCommentView.setVisibility(card.getTranslationComment().isEmpty() ? View.GONE : View.VISIBLE);

            mDifficultyView.setText(Integer.valueOf(card.getDifficulty()).toString());
            mDifficultyMaximumView.setText(Integer.valueOf(Card.getMaxDifficulty()).toString());

            if (card.isHidden())
                mToggleCardEnableButton.setImageResource(R.drawable.ic_card_item_invisible);
            else
                mToggleCardEnableButton.setImageResource(R.drawable.ic_card_item_visible);

            View.OnClickListener editCardClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyEditCardClicked(card.getId());
                }
            };
            mItemView.setOnClickListener(editCardClickListener);
            mWordView.setOnClickListener(editCardClickListener);
            mWordCommentView.setOnClickListener(editCardClickListener);
            mTranslationView.setOnClickListener(editCardClickListener);
            mTranslationCommentView.setOnClickListener(editCardClickListener);
            mDifficultyView.setOnClickListener(editCardClickListener);
            mDifficultyMaximumView.setOnClickListener(editCardClickListener);

            mToggleCardEnableButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyToggleCardEnabledClicked(card.getId());
                }
            });

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyDeleteCardClicked(card.getId());
                }
            });
        }
    }
}
