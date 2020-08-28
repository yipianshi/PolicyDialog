package com.d.policydialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.d.policydialog.utils.StringUtils;

import java.util.Objects;

/**
 * 弹出隐私政策对话框
 * <p>
 * String[] keyTexts = new String[]{...};
 * String tipsText = ...
 * URLSpan urlSpan = new URLSpan(...) {
 *
 * @Override public void updateDrawState(@NonNull TextPaint ds) {
 * ds.setColor(...);
 * ds.setUnderlineText(false);
 * }
 * };
 * //富文本
 * SpannableStringBuilder spannableStringBuilder = StringUtils.getMultiSpans(tipsText, new URLSpan[]{urlSpan}, keyTexts);
 * policyDialog = new PolicyDialog.Builder(mContext)
 * .title(...)
 * .contentText(spannableStringBuilder)
 * .setNegativeButton(..., new OnPolicyDialogNegativeClickListener() {
 * @Override public void onPolicyDialogNegativeClick() {
 * ...
 * }
 * })
 * .setPositiveButton(..., new OnPolicyDialogPositiveClickListener() {
 * @Override public void onPolicyDialogPositiveClick() {
 * ...
 * }
 * })
 * .show();
 * </p>
 */
public class PolicyDialog {

    private AlertDialog dialog;

    protected PolicyDialog(Builder builder) {
        initDialog(builder);
    }

    private void initDialog(final Builder builder) {
        Objects.requireNonNull(builder.context);
        View view = View.inflate(builder.context, R.layout.privacy_policy_dialog_layout, null);
        TextView titleTv = view.findViewById(R.id.title_tv);
        TextView contentMsgTv = view.findViewById(R.id.content_msg_tv);
        Button positiveBtn = view.findViewById(R.id.positive_btn);
        TextView negativeBtn = view.findViewById(R.id.negative_btn);
        positiveBtn.setText(builder.positiveText);
        negativeBtn.setText(builder.negativeText);
        //必须设置，不然没有点击效果
        contentMsgTv.setMovementMethod(LinkMovementMethod.getInstance());
        if (TextUtils.isEmpty(builder.contentText)) {
            Objects.requireNonNull(builder.tipsText);
            Objects.requireNonNull(builder.keyTexts);
            Objects.requireNonNull(builder.urlSpans);
            SpannableStringBuilder spannableStringBuilder = StringUtils.getMultiSpans(builder.tipsText, builder.urlSpans, builder.keyTexts);
            contentMsgTv.setText(spannableStringBuilder);
        } else {
            contentMsgTv.setText(builder.contentText);
        }
        if (!TextUtils.isEmpty(builder.dialogTitle)) {
            titleTv.setText(builder.dialogTitle);
        }
        dialog = new AlertDialog.Builder(builder.context, builder.themeResId)
                .setView(view)
                .setCancelable(false)
                .show();

        if (builder.onPositiveClickListener != null) {
            positiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.onPositiveClickListener.onPolicyDialogPositiveClick();
                }
            });
        }
        if (builder.onNegativeClickListener != null) {
            negativeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.onNegativeClickListener.onPolicyDialogNegativeClick();
                }
            });
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void onDestroy() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static class Builder {
        Context context;

        String dialogTitle;
        String[] webViewTitles;
        int themeResId = R.style.AppTheme_Dialog_RoundedBackground;

        OnPolicyDialogNegativeClickListener onNegativeClickListener;
        OnPolicyDialogPositiveClickListener onPositiveClickListener;

        CharSequence contentText;
        String[] keyTexts;
        URLSpan[] urlSpans;
        String tipsText;
        String negativeText = "";
        String positiveText = "";

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置对话框标题
         *
         * @param dialogTitle
         * @return
         */
        public Builder dialogTitle(String dialogTitle) {
            this.dialogTitle = dialogTitle;
            return this;
        }

        /**
         * 设置WebViewActivity标题
         *
         * @param webViewTitles
         * @return
         */
        public Builder webViewTitles(String[] webViewTitles) {
            this.webViewTitles = webViewTitles;
            return this;
        }

        public Builder themeResId(int themeResId) {
            this.themeResId = themeResId;
            return this;
        }

        public Builder setNegativeButton(String text, OnPolicyDialogNegativeClickListener onNegativeClickListener) {
            this.negativeText = text;
            this.onNegativeClickListener = onNegativeClickListener;
            return this;
        }

        public Builder setPositiveButton(String text, OnPolicyDialogPositiveClickListener onPositiveClickListener) {
            this.positiveText = text;
            this.onPositiveClickListener = onPositiveClickListener;
            return this;
        }

        /**
         * 设置内容显示
         * 如果不设置此内容，则必须设置{@link Builder#urlSpans(URLSpan[])} }、{@link Builder#keyTexts(String[])}、{@link Builder#tipsText(String)}
         *
         * @param contentText
         * @return
         */
        public Builder contentText(CharSequence contentText) {
            this.contentText = contentText;
            return this;
        }

        /**
         * 设置内容显示，点击跳转将会在内置WebView上
         * urls、 urlColors 和keyTexts长度需要一致
         * 需要先配置title
         * 如果不设置此内容，则必须设置{@link Builder#urlSpans(URLSpan[])} }、{@link Builder#keyTexts(String[])}、{@link Builder#tipsText(String)}
         *
         * @param tipsText
         * @param urls
         * @param urlColors
         * @param keyTexts
         * @return
         */
        public Builder contentText(String tipsText, String[] urls, @ColorInt int[] urlColors, String[] keyTexts) {
            Objects.requireNonNull(urls);
            Objects.requireNonNull(urlColors);
            Objects.requireNonNull(keyTexts);
            if (urls.length != urlColors.length || urls.length != keyTexts.length) {
                throw new RuntimeException("urls 、 urlColors 或 keyTexts 的长度不一致");
            }
            this.contentText = StringUtils.createSpannableString(tipsText, webViewTitles, urls, urlColors, keyTexts);
            return this;
        }

        /**
         * 富文本替换内容
         * <p>
         * 长度必须要小于或等于{@link Builder#keyTexts(String[])}的长度
         * </p>
         * <p>
         * 如果设置了{@link Builder#contentText(CharSequence)}
         * 则此方法无效
         * </p>
         *
         * @param urlSpans
         * @return
         */
        public Builder urlSpans(URLSpan[] urlSpans) {
            this.urlSpans = urlSpans;
            return this;
        }

        /**
         * 富文本替换的关键词
         *
         * <p>
         * 长度大于或等于{@link Builder#urlSpans(URLSpan[])}的长度，超过{@link Builder#urlSpans(URLSpan[])}的长度的元素将会被舍弃
         * </p>
         * <p>
         * 如果设置了{@link Builder#contentText(CharSequence)}
         * 则此方法无效
         * </p>
         *
         * @param keyTexts
         * @return
         */
        public Builder keyTexts(String[] keyTexts) {
            this.keyTexts = keyTexts;
            return this;
        }

        /**
         * 显示的内容
         * <p>
         * 如果设置了{@link Builder#contentText(CharSequence)}
         * 则此方法无效
         * </p>
         *
         * @param tipsText
         * @return
         */
        public Builder tipsText(String tipsText) {
            this.tipsText = tipsText;
            return this;
        }

        public PolicyDialog show() {
            return new PolicyDialog(this);
        }
    }

}
