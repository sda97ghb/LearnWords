package com.divanoapps.learnwords;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

//public class FAB_Hide_on_Scroll extends FloatingActionButton.Behavior {
//
//    public FAB_Hide_on_Scroll(Context context, AttributeSet attrs) {
//        super();
//    }
//
//    @Override
//    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
//                               View target, int dxConsumed, int dyConsumed,
//                               int dxUnconsumed, int dyUnconsumed) {
//        super.onNestedScroll(coordinatorLayout, child, target,
//                             dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
//
//        //child -> Floating Action Button
////        if (child.getVisibility() == View.VISIBLE && dyConsumed > 0) {
////            child.hide();
////        } else if (child.getVisibility() == View.GONE && dyConsumed < 0) {
////            child.show();
////        }
//        if (dyConsumed > 0) {
//            child.hide();
//        } else if (dyConsumed < 0) {
//            child.show();
//        }
//    }
//
//    @Override
//    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
//                                       FloatingActionButton child, View directTargetChild,
//                                       View target, int nestedScrollAxes) {
//        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
//    }
//}

public class FAB_Hide_on_Scroll extends FloatingActionButton.Behavior {
    private static final String TAG = "ScrollAwareFABBehavior";

    public FAB_Hide_on_Scroll(Context context, AttributeSet attrs) {
        super();
    }

    public boolean onStartNestedScroll(CoordinatorLayout parent, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,
                               FloatingActionButton child, View target, int dxConsumed,
                               int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        // TODO Auto-generated method stub
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed);

        if (dyConsumed > 0)// && child.getVisibility() == View.VISIBLE) {
            child.hide();
        else if (dyConsumed < 0)// && child.getVisibility() != View.VISIBLE) {
            child.show();
    }
}