package com.divanoapps.learnwords;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.divanoapps.learnwords.Data.DB;
import com.divanoapps.learnwords.Data.DeckInfo;

class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.ViewHolder> {

    private Context mContext = null; // as DeckListActivity

    DeckListAdapter(Context context) {
        mContext = context;
        DB.addListener(new DB.AllDecksLoadedListener() {
            @Override
            public void onAllDecksLoaded() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.item_deck, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeckInfo deckInfo = DB.getInstance().getListOfDeckInfos().get(position);
        holder.setContent(deckInfo.getName(), deckInfo.getNumberOfCards(), position);
    }

    @Override
    public int getItemCount() {
        return DB.getInstance().getListOfDeckInfos().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView deckNameView;
        private TextView cardCountView;

        ViewHolder(View itemView) {
            super(itemView);

            this.deckNameView = (TextView) itemView.findViewById(R.id.deck_name_view);
            this.cardCountView = (TextView) itemView.findViewById(R.id.card_count_view);
        }

        void setContent(String deckName, int numberOfCards, int position) {
            deckNameView.setText(deckName);
            cardCountView.setText(Integer.valueOf(numberOfCards).toString());
        }
    }
}
