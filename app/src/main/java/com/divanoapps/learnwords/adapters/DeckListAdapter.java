package com.divanoapps.learnwords.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.divanoapps.learnwords.CardRetriever;
import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.data.local.Card;
import com.divanoapps.learnwords.data.local.Deck;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.ViewHolder> {

    public interface EditDeckClickedListener {
        void onEditDeckClicked(String deckName);
    }

    public interface StartExerciseClickedListener {
        void onStartExerciseClicked(String deckName, CardRetriever.Order order);
    }

    private EditDeckClickedListener mEditDeckClickedListener = id -> {};
    private StartExerciseClickedListener mStartExerciseClickedListener = (id, order) -> {};

    public void setEditDeckClickedListener(EditDeckClickedListener listener) {
        mEditDeckClickedListener = listener;
    }

    public void setStartExerciseClickedListener(StartExerciseClickedListener listener) {
        mStartExerciseClickedListener = listener;
    }

    private void notifyEditDeckClicked(String deckName) {
        mEditDeckClickedListener.onEditDeckClicked(deckName);
    }

    private void notifyStartExerciseClicked(String deckName, CardRetriever.Order order) {
        mStartExerciseClickedListener.onStartExerciseClicked(deckName, order);
    }

    private LayoutInflater layoutInflater;

    private List<Deck> decks = new LinkedList<>();

    public void setDecks(List<Deck> decks) {
        this.decks = decks == null ? new LinkedList<>() : decks;
        notifyDataSetChanged();
    }

    public DeckListAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_deck, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setContent(decks.get(position));
    }

    @Override
    public int getItemCount() {
        return decks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.deck_name_view)
        TextView deckNameView;

        @BindView(R.id.card_count_view)
        TextView deckSizeView;

        @BindView(R.id.hidden_card_count_view)
        TextView hiddenCardsCountView;

        @BindView(R.id.language_from_view)
        TextView languageFromView;

        @BindView(R.id.language_to_view)
        TextView languageToView;

        @BindView(R.id.alphabet_order_button)
        ImageButton alphabetOrderButton;

        @BindView(R.id.file_order_button)
        ImageButton fileOrderButton;

        @BindView(R.id.random_order_button)
        ImageButton randomOrderButton;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        void setContent(Deck deck) {
            String name = deck.getName();

            int numberOfCards = deck.getCards().size();
            int numberOfHiddenCards = 0;
            for (Card card : deck.getCards())
                if (card.isHidden())
                    ++ numberOfHiddenCards;

            deckNameView.setText(deck.getName());
            deckSizeView.setText(Integer.valueOf(numberOfCards).toString());
            hiddenCardsCountView.setText(Integer.valueOf(numberOfHiddenCards).toString());
            languageFromView.setText(deck.getFromLanguage());
            languageToView.setText(deck.getToLanguage());

            itemView.setOnClickListener(v -> notifyEditDeckClicked(name));
            deckNameView.setOnClickListener(v -> notifyEditDeckClicked(name));
            deckSizeView.setOnClickListener(v -> notifyEditDeckClicked(name));

            alphabetOrderButton.setOnClickListener(v -> notifyStartExerciseClicked(name, CardRetriever.Order.alphabetical));
            fileOrderButton.setOnClickListener(v -> notifyStartExerciseClicked(name, CardRetriever.Order.file));
            randomOrderButton.setOnClickListener(v -> notifyStartExerciseClicked(name, CardRetriever.Order.random));

//            mMoreButton.setOnClickListener(view -> {
//                PopupMenu popupMenu = new PopupMenu(mContext, view);
//                MenuInflater menuInflater = popupMenu.getMenuInflater();
//                menuInflater.inflate(R.menu.menu_deck_item, popupMenu.getMenu());
//                popupMenu.setOnMenuItemClickListener(item -> {
//                    if (item.getItemId() == R.id.action_delete) {
//                        notifyDeleteDeckClicked(deck.getId());
//                        return true;
//                    }
//                    else {
//                        return false;
//                    }
//                });
//                popupMenu.show();
//            });
        }
    }
}
