package com.divanoapps.learnwords;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.divanoapps.learnwords.Entities.Card;
import com.divanoapps.learnwords.Entities.Deck;

class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    public interface EditCardClickedListener {
        void onEditCardClicked(Card card);
    }

    public interface ToggleCardEnabledClickedListener {
        void onToggleCardEnabledClicked(Card card);
    }

    public interface DeleteCardClickedListener {
        void onDeleteCardClicked(Card card);
    }

    private EditCardClickedListener mEditCardClickedListener;
    private ToggleCardEnabledClickedListener mToggleCardEnabledClickedListener;
    private DeleteCardClickedListener mDeleteCardClickedListener;

    public void setEditCardClickedListener(EditCardClickedListener  listener) {
        mEditCardClickedListener = listener;
    }

    public void setToggleCardEnabledClickedListener(ToggleCardEnabledClickedListener listener) {
        mToggleCardEnabledClickedListener = listener;
    }

    public void setDeleteCardClickedListener(DeleteCardClickedListener listener) {
        mDeleteCardClickedListener = listener;
    }

    void notifyEditCardClicked(Card card) {
        mEditCardClickedListener.onEditCardClicked(card);
    }

    void notifyToggleCardEnabledClicked(Card card) {
        mToggleCardEnabledClickedListener.onToggleCardEnabledClicked(card);
    }

    void notifyDeleteCardClicked(Card card) {
        mDeleteCardClickedListener.onDeleteCardClicked(card);
    }

    void initializeListeners() {
        mEditCardClickedListener = new EditCardClickedListener() {
            @Override
            public void onEditCardClicked(Card card) {
            }
        };
        mToggleCardEnabledClickedListener = new ToggleCardEnabledClickedListener() {
            @Override
            public void onToggleCardEnabledClicked(Card card) {
            }
        };
        mDeleteCardClickedListener = new DeleteCardClickedListener() {
            @Override
            public void onDeleteCardClicked(Card card) {
            }
        };
    }

    private Context mContext; // as CardListActivity
    private Deck mDeck;

    public CardListAdapter(Context context, Deck deck) {
        mContext = context;
        mDeck = deck;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setFromCard(mDeck.getCards().get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDeck.getCards().size();
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

        void setFromCard(final Card card, final int cardIndex) {
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
                    notifyEditCardClicked(card);
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
                    notifyToggleCardEnabledClicked(card);
                }
            });

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyDeleteCardClicked(card);
                }
            });
        }
    }
}
