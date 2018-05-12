package com.divanoapps.learnwords.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.data.local.Card;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    public interface EditCardClicked {
        void emit(Card card);
    }
    private EditCardClicked editCardClicked = (unused) -> {};
    public void setEditCardClickedListener(EditCardClicked listener) {
        editCardClicked = listener;
    }

    public interface ToggleCardEnabledClicked {
        void emit(Card card);
    }
    private ToggleCardEnabledClicked toggleCardHiddenClicked = (unused) -> {};
    public void setToggleCardHiddenClickedListener(ToggleCardEnabledClicked listener) {
        toggleCardHiddenClicked = listener;
    }

    // DeleteCardsClicked
    public interface DeleteCardsClicked {
        void emit(List<Card> cards);
    }
    private DeleteCardsClicked deleteCardsClicked = (unused) -> {};
    public void setDeleteCardsClickedListener(DeleteCardsClicked listener) {
        deleteCardsClicked = listener;
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
            mode.getMenuInflater().inflate(R.menu.menu_card_list_selection, menu);
            mode.setTitle("Select cards");
            selectedCards.clear();
            CardListAdapter.this.notifyDataSetChanged();
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
                    deleteCardsClicked.emit(selectedCards);
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            selectionActionMode = null;
            CardListAdapter.this.notifyDataSetChanged();
            selectionModeFinished.emit();
        }
    };

    private List<Card> selectedCards = new LinkedList<>();

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
        holder.setFromCard(position, cards.get(position));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.picture_view)
        ImageView pictureView;

        @BindView(R.id.word_view)
        TextView wordView;

        @BindView(R.id.comment_view)
        TextView commentView;

        @BindView(R.id.translation_view)
        TextView translationView;

        @BindView(R.id.difficulty_view)
        TextView difficultyView;

        @BindView(R.id.difficulty_maximum_view)
        TextView difficultyMaximumView;

        @BindView(R.id.switch_hidden_button)
        ImageButton toggleCardHiddenButton;

        @BindView(R.id.toggle_selection_button)
        ImageButton toggleSelectionButton;

        @BindView(R.id.group_selection)
        Group selectionGroup;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        void setFromCard(int position, final Card card) {
            // Appearance
            wordView.setText(card.getWord());
            commentView.setText(card.getComment());
            translationView.setText(card.getTranslation());

            commentView.setVisibility(card.getComment().trim().isEmpty() ? View.GONE : View.VISIBLE);

            difficultyView.setText(card.getDifficulty().toString());
            difficultyMaximumView.setText(Card.getMaxDifficulty().toString());

            toggleCardHiddenButton.setImageResource(card.isHidden()
                ? R.drawable.ic_card_item_invisible
                : R.drawable.ic_card_item_visible);

            toggleSelectionButton.setImageResource(selectedCards.contains(card)
                ? R.drawable.ic_item_card_selection_selected
                : R.drawable.ic_item_card_selection_not_selected);
            selectionGroup.setVisibility(selectionActionMode == null ? View.GONE : View.VISIBLE);

            // Behavior
            if (selectionActionMode == null) {
                itemView.setOnClickListener(v ->
                    editCardClicked.emit(card));

                itemView.setOnLongClickListener(v -> {
                    selectionActionMode = ((AppCompatActivity) v.getContext()).startSupportActionMode(selectionActionModeCallback);
                    selectedCards.add(card);
                    return true;
                });
            }
            else {
                View.OnClickListener toggleSelection = v -> {
                    if (selectedCards.contains(card))
                        selectedCards.remove(card);
                    else
                        selectedCards.add(card);
                    notifyItemChanged(position);
                };
                itemView.setOnClickListener(toggleSelection);
                toggleSelectionButton.setOnClickListener(toggleSelection);

                itemView.setOnLongClickListener(v -> false);
            }

            toggleCardHiddenButton.setOnClickListener(v -> toggleCardHiddenClicked.emit(card));
        }
    }
}
