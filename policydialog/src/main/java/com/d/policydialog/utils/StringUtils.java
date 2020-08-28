package com.d.policydialog.utils;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.d.policydialog.WebViewActivity;

public class StringUtils {

    /**
     * @param tipsText  提示
     * @param title     WebViewActivity的标题
     * @param urls      点击时需要跳转的地方
     * @param urlColors 需要染色的颜色
     * @param keyTexts  提示中需要染色的关键词
     * @return
     */
    public static SpannableStringBuilder createSpannableString(String tipsText, String[] title, String[] urls, @ColorInt int[] urlColors, String[] keyTexts) {
        URLSpan[] urlSpans = createUrlSpans(urls, urlColors, title);
        return StringUtils.getMultiSpans(tipsText, urlSpans, keyTexts);
    }

    private static URLSpan[] createUrlSpans(String[] urls, final int[] urlColors, final String[] title) {
        URLSpan[] urlSpans = new URLSpan[urls.length];
        for (int i = 0; i < urls.length; i++) {
            final int finalI = i;
            final String finalTitle = title[i];
            URLSpan urlSpan = new URLSpan(urls[finalI]) {
                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setColor(urlColors[finalI]);
                    ds.setUnderlineText(false);
                }

                @Override
                public void onClick(View widget) {
                    WebViewActivity.start(widget.getContext(), finalTitle, getURL());
                }
            };
            urlSpans[i] = urlSpan;
        }
        return urlSpans;
    }

    /**
     * 对tipsText进替换富文本操作
     * <p>spans数组长度必须小于或等于spanTexts的长度，而spanTexts剩下的元素将会被舍弃</p>
     * <p>spanTexts数组中的元素如果在TipsText中不存在，则不处理</p>
     *
     * @param tipsText  原字符串
     * @param spans     带替换的span
     * @param spanTexts 需要被替换的字符串
     * @return
     */
    public static SpannableStringBuilder getMultiSpans(String tipsText, Object[] spans, String[] spanTexts) {
        if (TextUtils.isEmpty(tipsText)) {
            throw new RuntimeException("tipsText cannot be null or empty");
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tipsText);
        if (spans == null || spanTexts == null) {
            return spannableStringBuilder;
        }
        if (spans.length > spanTexts.length) {
            throw new RuntimeException("spans'length must be less than or equal to spanTexts'");
        }
        for (int i = 0; i < spans.length; i++) {
            if (!tipsText.contains(spanTexts[i])) {
                continue;
            }
            spannableStringBuilder.setSpan(spans[i], tipsText.indexOf(spanTexts[i]), tipsText.indexOf(spanTexts[i]) + spanTexts[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableStringBuilder;
    }
}
