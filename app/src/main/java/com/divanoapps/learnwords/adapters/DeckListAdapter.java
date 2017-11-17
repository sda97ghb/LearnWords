package com.divanoapps.learnwords.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.divanoapps.learnwords.CardRetriever;
import com.divanoapps.learnwords.entities.DeckId;
import com.divanoapps.learnwords.entities.DeckShort;
import com.divanoapps.learnwords.R;

import java.util.LinkedList;
import java.util.List;

public class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.ViewHolder> {

    public interface EditDeckClickedListener {
        void onEditDeckClicked(DeckId id);
    }

    public interface StartExerciseClickedListener {
        void onStartExerciseClicked(DeckId id, CardRetriever.Order order);
    }

    private EditDeckClickedListener mEditDeckClickedListener = id -> {};
    private StartExerciseClickedListener mStartExerciseClickedListener = (id, order) -> {};

    public void setEditDeckClickedListener(EditDeckClickedListener listener) {
        mEditDeckClickedListener = listener;
    }

    public void setStartExerciseClickedListener(StartExerciseClickedListener listener) {
        mStartExerciseClickedListener = listener;
    }

    private void notifyEditDeckClicked(DeckId id) {
        mEditDeckClickedListener.onEditDeckClicked(id);
    }

    private void notifyStartExerciseClicked(DeckId id, CardRetriever.Order order) {
        mStartExerciseClickedListener.onStartExerciseClicked(id, order);
    }

    private Context mContext = null; // as DeckListActivity

    private List<DeckShort> mDecks = new LinkedList<>();

    public void setDecks(List<DeckShort> decks) {
        mDecks = decks == null ? new LinkedList<>() : decks;
        notifyDataSetChanged();
    }

    public DeckListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.item_deck, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setContent(mDecks.get(position));
    }

    @Override
    public int getItemCount() {
        return mDecks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mDeckNameView;
        private TextView mDeckSizeView;

        private ImageButton mAlphabetOrderButton;
        private ImageButton mFileOrderButton;
        private ImageButton mRandomOrderButton;
        private ImageButton mHigher30Button;
        private ImageButton mLower30Button;

        ViewHolder(View itemView) {
            super(itemView);

            mDeckNameView = (TextView) itemView.findViewById(R.id.deck_name_view);
            mDeckSizeView = (TextView) itemView.findViewById(R.id.card_count_view);

            mAlphabetOrderButton = (ImageButton) itemView.findViewById(R.id.alphabet_order_button);
            mFileOrderButton     = (ImageButton) itemView.findViewById(R.id.file_order_button);
            mRandomOrderButton   = (ImageButton) itemView.findViewById(R.id.random_order_button);
            mHigher30Button      = (ImageButton) itemView.findViewById(R.id.higher_30_button);
            mLower30Button       = (ImageButton) itemView.findViewById(R.id.lower_30_button);
        }

        @SuppressLint("SetTextI18n")
        void setContent(final DeckShort deck) {
            mDeckNameView.setText(deck.getName());
            mDeckSizeView.setText(Integer.valueOf(deck.getNumberOfCards()).toString());

            itemView.setOnClickListener(v -> notifyEditDeckClicked(deck.getId()));
            mDeckNameView.setOnClickListener(v -> notifyEditDeckClicked(deck.getId()));
            mDeckSizeView.setOnClickListener(v -> notifyEditDeckClicked(deck.getId()));

            mAlphabetOrderButton.setOnClickListener(v -> notifyStartExerciseClicked(deck.getId(), CardRetriever.Order.alphabetical));
            mFileOrderButton.setOnClickListener(v -> notifyStartExerciseClicked(deck.getId(), CardRetriever.Order.file));
            mRandomOrderButton.setOnClickListener(v -> notifyStartExerciseClicked(deck.getId(), CardRetriever.Order.random));
            mHigher30Button.setOnClickListener(v -> notifyStartExerciseClicked(deck.getId(), CardRetriever.Order.higher30));
            mLower30Button.setOnClickListener(v -> notifyStartExerciseClicked(deck.getId(), CardRetriever.Order.lower30));
        }
    }
}
