package com.divanoapps.learnwords;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.divanoapps.learnwords.Data.DB;
import com.divanoapps.learnwords.Data.DeckInfo;

import java.util.LinkedList;
import java.util.List;

class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.ViewHolder> {

    public interface EditDeckClickedListener {
        void onEditDeckClicked(String deckName);
    }

    public interface StartExerciseClickedListener {
        void onStartExerciseClickedListener(String deckName, CardRetriever.Order order);
    }

    private List<EditDeckClickedListener> mEditDeckClickedListeners;
    private List<StartExerciseClickedListener> mStartExerciseClickedListeners;

    public void addEditDeckClickedListener(EditDeckClickedListener listener) {
        mEditDeckClickedListeners.add(listener);
    }

    public void addStartExerciseClickedListener(StartExerciseClickedListener listener) {
        mStartExerciseClickedListeners.add(listener);
    }

    private void notifyEditDeckClicked(String deckName) {
        for (EditDeckClickedListener listener : mEditDeckClickedListeners) {
            listener.onEditDeckClicked(deckName);
        }
    }

    private void notifyStartExerciseClicked(String deckName, CardRetriever.Order order) {
        for (StartExerciseClickedListener listener : mStartExerciseClickedListeners) {
            listener.onStartExerciseClickedListener(deckName, order);
        }
    }

    private void initializeListenerLists() {
        mEditDeckClickedListeners = new LinkedList<>();
        mStartExerciseClickedListeners = new LinkedList<>();
    }

    private Context mContext = null; // as DeckListActivity

    DeckListAdapter(Context context) {
        initializeListenerLists();
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
        DeckInfo deckInfo = DB.getListOfDeckInfos().get(position);
        holder.setContent(deckInfo.getName(), deckInfo.getNumberOfCards(), position);
    }

    @Override
    public int getItemCount() {
        return DB.getListOfDeckInfos().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private View mItemView;

        private TextView mDeckNameView;
        private TextView mDeckSizeView;

        private ImageButton mAlphabetOrderButton;
        private ImageButton mFileOrderButton;
        private ImageButton mRandomOrderButton;
        private ImageButton mHigher30Button;
        private ImageButton mLower30Button;

        ViewHolder(View itemView) {
            super(itemView);

            mItemView = itemView;

            mDeckNameView = (TextView) itemView.findViewById(R.id.deck_name_view);
            mDeckSizeView = (TextView) itemView.findViewById(R.id.card_count_view);

            mAlphabetOrderButton = (ImageButton) itemView.findViewById(R.id.alphabet_order_button);
            mFileOrderButton = (ImageButton) itemView.findViewById(R.id.file_order_button);
            mRandomOrderButton = (ImageButton) itemView.findViewById(R.id.random_order_button);
            mHigher30Button = (ImageButton) itemView.findViewById(R.id.higher_30_button);
            mLower30Button = (ImageButton) itemView.findViewById(R.id.lower_30_button);
        }

        void setContent(final String deckName, int numberOfCards, int position) {
            mDeckNameView.setText(deckName);
            mDeckSizeView.setText(Integer.valueOf(numberOfCards).toString());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyEditDeckClicked(deckName);
                }
            });
            mDeckNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyEditDeckClicked(deckName);
                }
            });
            mDeckSizeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyEditDeckClicked(deckName);
                }
            });

            mAlphabetOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyStartExerciseClicked(deckName, CardRetriever.Order.alphabetical);
                }
            });
            mFileOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyStartExerciseClicked(deckName, CardRetriever.Order.file);
                }
            });
            mRandomOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyStartExerciseClicked(deckName, CardRetriever.Order.random);
                }
            });
            mHigher30Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyStartExerciseClicked(deckName, CardRetriever.Order.higher30);
                }
            });
            mLower30Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyStartExerciseClicked(deckName, CardRetriever.Order.lower30);
                }
            });
        }
    }
}
