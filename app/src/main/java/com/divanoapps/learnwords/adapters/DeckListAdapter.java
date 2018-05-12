package com.divanoapps.learnwords.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.divanoapps.learnwords.exercise.CardDispenserFactory;
import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.data.local.Card;
import com.divanoapps.learnwords.data.local.Deck;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.ViewHolder> {

    // EditDeckClicked
    public interface EditDeckClicked {
        void emit(Deck deck);
    }
    private EditDeckClicked editDeckClicked = unused -> {};
    public void setEditDeckClickedListener(EditDeckClicked listener) {
        editDeckClicked = listener;
    }

    // StartExerciseClicked
    public interface StartExerciseClicked {
        void emit(Deck deck, CardDispenserFactory.Order order);
    }
    private StartExerciseClicked startExerciseClicked = (unused1, unused2) -> {};
    public void setStartExerciseClickedListener(StartExerciseClicked listener) {
        startExerciseClicked = listener;
    }

    // DeleteDecksClicked
    public interface DeleteDecksClicked {
        void emit(List<Deck> decks);
    }
    private DeleteDecksClicked deleteDecksClicked = (unused) -> {};
    public void setDeleteDecksClickedListener(DeleteDecksClicked listener) {
        deleteDecksClicked = listener;
    }

    // SelectionModeStarted
    public interface SelectionModeStarted {
        void emit();
    }
    private SelectionModeStarted selectionModeStarted = () -> {};
    public void setSelectionModeStartedListener(SelectionModeStarted listener) {
        selectionModeStarted = listener;
    }

    // SelectionModeFinished
    public interface SelectionModeFinished {
        void emit();
    }
    private SelectionModeFinished selectionModeFinished = () -> {};
    public void setSelectionModeFinishedListener(SelectionModeFinished listener) {
        selectionModeFinished = listener;
    }

    private ActionMode selectionActionMode;
    private ActionMode.Callback selectionActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_deck_list_selection, menu);
            mode.setTitle("Select decks");
            selectedDecks.clear();
            DeckListAdapter.this.notifyDataSetChanged();
            selectionModeStarted.emit();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    deleteDecksClicked.emit(selectedDecks);
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            selectionActionMode = null;
            DeckListAdapter.this.notifyDataSetChanged();
            selectionModeFinished.emit();
        }
    };

    private List<Deck> selectedDecks = new LinkedList<>();

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
        holder.setContent(position, decks.get(position));
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

        @BindView(R.id.action_buttons_bar_layout)
        ConstraintLayout actionButtonsBarLayout;

        @BindView(R.id.selection_buttons_bar_layout)
        ConstraintLayout selectionButtonsBarLayout;

        @BindView(R.id.toggle_selection_button)
        ImageButton toggleSelectionButton;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        void setContent(int position, Deck deck) {
            String name = deck.getName();

            int numberOfCards = deck.getCards().size();
            int numberOfHiddenCards = 0;
            for (Card card : deck.getCards())
                if (card.isHidden())
                    ++ numberOfHiddenCards;

            // Appearance
            deckNameView.setText(name);
            deckSizeView.setText(Integer.valueOf(numberOfCards).toString());
            hiddenCardsCountView.setText(Integer.valueOf(numberOfHiddenCards).toString());
            languageFromView.setText(deck.getFromLanguage());
            languageToView.setText(deck.getToLanguage());
            toggleSelectionButton.setImageResource(selectedDecks.contains(deck)
                ? R.drawable.ic_item_deck_selection_selected
                : R.drawable.ic_item_deck_selection_not_selected);
            actionButtonsBarLayout.setVisibility(selectionActionMode == null ? View.VISIBLE : View.GONE);
            selectionButtonsBarLayout.setVisibility(selectionActionMode == null ? View.GONE : View.VISIBLE);

            // Behavior
            if (selectionActionMode == null) {
                itemView.setOnClickListener(v -> editDeckClicked.emit(deck));

                alphabetOrderButton.setOnClickListener(v -> startExerciseClicked.emit(deck, CardDispenserFactory.Order.alphabetical));
                fileOrderButton.setOnClickListener(v -> startExerciseClicked.emit(deck, CardDispenserFactory.Order.timestamp));
                randomOrderButton.setOnClickListener(v -> startExerciseClicked.emit(deck, CardDispenserFactory.Order.random));

                itemView.setOnLongClickListener(v -> {
                    selectionActionMode = ((AppCompatActivity) v.getContext()).startSupportActionMode(selectionActionModeCallback);
                    selectedDecks.add(deck);
                    return true;
                });
            }
            else {
                View.OnClickListener toggleSelection = v -> {
                    if (selectedDecks.contains(deck))
                        selectedDecks.remove(deck);
                    else
                        selectedDecks.add(deck);
                    notifyItemChanged(position);
                };
                itemView.setOnClickListener(toggleSelection);
                toggleSelectionButton.setOnClickListener(toggleSelection);

                itemView.setOnLongClickListener(v -> false);
            }

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
