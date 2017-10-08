package com.divanoapps.learnwords;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.divanoapps.learnwords.Data.DB;
import com.divanoapps.learnwords.Data.DeckInfo;
import com.divanoapps.learnwords.Entities.Deck;

class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.ViewHolder> {

    private Context context = null; // as DeckListActivity

    private DB mDb;

    DeckListAdapter(Context context) {
        this.context = context;
        mDb = DB.getInstance();
        new DownloadFilesTask().execute();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.item_deck, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeckInfo deckInfo = mDb.getListOfDeckInfos().get(position);
        holder.setContent(deckInfo.getName(), deckInfo.getNumberOfCards(), position);
    }

    @Override
    public int getItemCount() {
        return mDb.getListOfDeckInfos().size();
    }

    private class DownloadFilesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            DB.getInstance().loadAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            notifyDataSetChanged();
        }
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
