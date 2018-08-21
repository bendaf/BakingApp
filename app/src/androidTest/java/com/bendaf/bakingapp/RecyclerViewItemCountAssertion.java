package com.bendaf.bakingapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by nenick, copied and improved by bendaf on 2018. 08. 21. from https://stackoverflow.com/a/37339656/3162918.
 */

public class RecyclerViewItemCountAssertion implements ViewAssertion {
    private final int expectedCount;

    RecyclerViewItemCountAssertion(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assert adapter != null;
        assertThat(adapter.getItemCount(), is(expectedCount));
    }
}
