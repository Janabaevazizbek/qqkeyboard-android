package com.shagalalab.qqkeyboard.util;

import android.content.res.Configuration;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewUtils {
    public static void applyInsetsToView(View root) {
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            view.setPadding(
                    view.getPaddingLeft(),
                    systemBars.top,
                    view.getPaddingRight(),
                    systemBars.bottom
            );

            return insets;
        });
    }

    /**
     * Applies window insets to an InputMethodService keyboard view.
     * Unlike Activities, keyboards only need bottom and (in landscape) side insets.
     * Top insets are not applied as the status bar doesn't overlap the keyboard.
     *
     * @param keyboardView The root keyboard view (typically LatinKeyboardView)
     */
    public static void applyInsetsToKeyboardView(View keyboardView) {
        ViewCompat.setOnApplyWindowInsetsListener(keyboardView, (view, insets) -> {
            // Use navigationBars instead of systemBars to avoid status bar insets
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Check if in landscape orientation for side inset handling
            boolean isLandscape = view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

            view.setPadding(
                isLandscape ? systemBars.left : 0,     // Left padding in landscape only
                0,                                  // No top padding for keyboard
                isLandscape ? systemBars.right : 0,    // Right padding in landscape only
                systemBars.bottom                    // Bottom padding - use systemBars based on testing
            );

            return insets;
        });

        // Request that window insets be dispatched to this view
        // This is critical for views that are recreated (like in onStartInputView)
        ViewCompat.requestApplyInsets(keyboardView);
    }
}
