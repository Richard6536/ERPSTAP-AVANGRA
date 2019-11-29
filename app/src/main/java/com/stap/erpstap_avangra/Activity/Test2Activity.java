package com.stap.erpstap_avangra.Activity;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.View;

import com.stap.erpstap_avangra.Adapters.CardFragmentPagerAdapter;
import com.stap.erpstap_avangra.Adapters.ShadowTransformer;
import com.stap.erpstap_avangra.R;

public class Test2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        CardFragmentPagerAdapter pagerAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), dpToPixels(2, this), null);
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);
    }

    /**
     * Change value in dp to pixels
     * @param dp
     * @param context
     * @return
     */
    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }
}
