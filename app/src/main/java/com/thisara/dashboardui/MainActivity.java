/*
 * Copyright (c) 2026 Thisara Kavinda.
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package com.thisara.dashboardui;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private View headerContainer;
    private View searchBarContainer;
    private View mainHighlightCard;
    private View tvSectionTitle;
    private View widgetCard1;
    private View widgetCard2;
    private View widgetCard3;
    private View widgetCard4;
    
    // Elements to add premium numeric counting & bar expansion animations
    private TextView tvCounterFocus;
    private TextView tvCounterStreak;
    private View animatedProgressLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Views
        headerContainer = findViewById(R.id.header_container);
        searchBarContainer = findViewById(R.id.search_bar_container);
        mainHighlightCard = findViewById(R.id.main_highlight_card);
        tvSectionTitle = findViewById(R.id.tv_section_title);
        widgetCard1 = findViewById(R.id.widget_card_1);
        widgetCard2 = findViewById(R.id.widget_card_2);
        widgetCard3 = findViewById(R.id.widget_card_3);
        widgetCard4 = findViewById(R.id.widget_card_4);
        
        tvCounterFocus = findViewById(R.id.tv_counter_focus);
        tvCounterStreak = findViewById(R.id.tv_counter_streak);
        animatedProgressLine = findViewById(R.id.animated_progress_line);

        // Set Dynamic Date Text
        TextView tvDate = findViewById(R.id.tv_date);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault());
        String currentDate = sdf.format(new Date()).toUpperCase();
        tvDate.setText(currentDate);

        // Setup tactile iOS spring touch feedback
        setupIosTouchFeedback(mainHighlightCard);
        setupIosTouchFeedback(widgetCard1);
        setupIosTouchFeedback(widgetCard2);
        setupIosTouchFeedback(widgetCard3);
        setupIosTouchFeedback(widgetCard4);
        setupIosTouchFeedback(findViewById(R.id.btn_notification));
        setupIosTouchFeedback(findViewById(R.id.btn_profile));

        // Start entrance animations & premium numbers rolling animations
        runEntranceAnimations();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupIosTouchFeedback(final View view) {
        if (view == null) return;
        
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // iOS-style scale down
                        view.animate()
                                .scaleX(0.95f)
                                .scaleY(0.95f)
                                .setDuration(120)
                                .setInterpolator(new DecelerateInterpolator())
                                .start();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // iOS-style spring bounce back
                        view.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(250)
                                .setInterpolator(new OvershootInterpolator(1.5f))
                                .start();
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            view.performClick();
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void runEntranceAnimations() {
        View[] animatedViews = new View[]{
                headerContainer,
                searchBarContainer,
                mainHighlightCard,
                tvSectionTitle,
                widgetCard1,
                widgetCard2,
                widgetCard3,
                widgetCard4
        };

        long baseDelay = 100; // ms
        
        for (int i = 0; i < animatedViews.length; i++) {
            View view = animatedViews[i];
            if (view == null) continue;

            // Set initial invisible states
            view.setAlpha(0f);
            view.setTranslationY(120f);

            // Staggered reveal animation
            view.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setStartDelay(baseDelay * i)
                    .setDuration(600)
                    .setInterpolator(new DecelerateInterpolator(1.8f))
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            // When the Main Hero card finishes entering, start the premium sub-animations!
                            if (view == mainHighlightCard) {
                                startPremiumSubAnimations();
                            }
                        }
                    })
                    .start();
        }
    }

    private void startPremiumSubAnimations() {
        // 1. Dynamic Focus Time Counter Animation (0.0 to 24.5 hrs)
        ValueAnimator focusAnimator = ValueAnimator.ofFloat(0.0f, 24.5f);
        focusAnimator.setDuration(1200);
        focusAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
        focusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                tvCounterFocus.setText(String.format(Locale.US, "%.1f hrs", value));
            }
        });
        focusAnimator.start();

        // 2. Dynamic Streak Counter Animation (0 to 12 Days)
        ValueAnimator streakAnimator = ValueAnimator.ofInt(0, 12);
        streakAnimator.setDuration(1000);
        streakAnimator.setInterpolator(new DecelerateInterpolator(1.2f));
        streakAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                tvCounterStreak.setText(value + " Days 🔥");
            }
        });
        streakAnimator.start();

        // 3. Immersive Line Expansion Animation (Simulating a data loading bar indicator)
        if (animatedProgressLine != null) {
            animatedProgressLine.post(new Runnable() {
                @Override
                public void run() {
                    int parentWidth = ((ViewGroup) animatedProgressLine.getParent()).getWidth();
                    // Let's expand the line to 84% of parent width dynamically
                    int targetWidth = (int) (parentWidth * 0.84f);
                    
                    ValueAnimator lineAnimator = ValueAnimator.ofInt(0, targetWidth);
                    lineAnimator.setDuration(1400);
                    lineAnimator.setInterpolator(new OvershootInterpolator(0.8f));
                    lineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            ViewGroup.LayoutParams params = animatedProgressLine.getLayoutParams();
                            params.width = (int) animation.getAnimatedValue();
                            animatedProgressLine.setLayoutParams(params);
                        }
                    });
                    lineAnimator.start();
                }
            });
        }
    }
}