package com.github.leandroruiz96.behaviourstests.chainedfab;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ScrollingView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by leandro on 6/3/17.
 */
public class ChainedFabBehavior extends CoordinatorLayout.Behavior<ChainedFab> {


    private static final float THRESHOLD = 100f;

    public ChainedFabBehavior() {
        super();
    }

    public ChainedFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ChainedFab child, View dependency) {
        return dependency instanceof AppBarLayout || dependency instanceof ScrollingView;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, ChainedFab child, int layoutDirection) {

        ChainedFab linked = child.getLinkedFab();
        if (child!=linked) {
            child.setX(linked.getX());
            child.setY(linked.getY() - child.getHeight() - child.mChainedMargin);
        }

        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ChainedFab child, View dependency) {

        if (dependency instanceof AppBarLayout) {
            AppBarLayout appbar = (AppBarLayout) dependency;
            float percent = (((float)appbar.getTotalScrollRange()+(float)appbar.getTop())/(float)appbar.getTotalScrollRange());

            ChainedFab first = child.getLinkedFab();

            if (first != child) {
                //I'm a linked FAB
                float deltaY = child.getHeight() + child.mChainedMargin;
                child.setY(first.getY() - (deltaY*percent));
                return true;
            }
        }

        if (dependency instanceof ScrollingView) {
            ScrollingView scroll = (ScrollingView) dependency;
            float scale = Math.max(THRESHOLD-scroll.computeVerticalScrollOffset(),0)/THRESHOLD;
            child.setScaleY(scale);
            child.setScaleX(scale);
        }

        return false;
    }

}
