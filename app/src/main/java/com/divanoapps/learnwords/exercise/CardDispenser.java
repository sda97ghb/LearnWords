package com.divanoapps.learnwords.exercise;

import com.divanoapps.learnwords.data.local.Card;

/**
 * Created by dmitry on 06.05.18.
 */

public interface CardDispenser {
    boolean hasNext();
    Card getNext();
}
