package com.hiweb.ide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Keep;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.snackbar.Snackbar;

@Keep
public class MoveUpwardBehavior extends CoordinatorLayout.Behavior {
    public MoveUpwardBehavior() {
        super();

    }

    public MoveUpwardBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override

    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;

    }

    @Override

    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float translationY = Math.min(0, ViewCompat.getTranslationY(dependency) - dependency.getHeight());

        child.setTranslationY(translationY);

        return true;

    }

    @Override

    public void onDependentViewRemoved(CoordinatorLayout parent, View child, View dependency) {
        ViewCompat.animate(child).translationY(0).start();

    }

}