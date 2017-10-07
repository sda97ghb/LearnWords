package com.divanoapps.learnwords;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.ViewHolder> {

    private Context context = null; // as DeckListActivity

    private DataProvider provider = null;

    DeckListAdapter(Context context, DataProvider provider) {
        this.context = context;
        this.provider = provider;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.item_deck, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setFromDeck(provider.getDeckList().get(position), position);
    }

    @Override
    public int getItemCount() {
        return provider.getDeckList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        private TextView deckNameView;
        private TextView cardCountView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            this.deckNameView = (TextView) itemView.findViewById(R.id.deck_name_view);
            this.cardCountView = (TextView) itemView.findViewById(R.id.card_count_view);
        }

        public void setFromDeck(Deck deck, final int position) {
            deckNameView.setText(deck.getName());
            cardCountView.setText("??");
        }
    }
}
