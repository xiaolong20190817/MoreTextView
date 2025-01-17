package com.smart.moretext;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

/**
 * Created by xbb on 2017/5/10.
 */

/*
* 简单的富文本工具类
*
* 使用说明 new UtilMoreText(tv2,msg);
* */
public final class UtilMoreText {

    private float mExpectedWidth; //期望行宽度
    private TextView mTextView;//显示富文本的控件
    private String mOriMsg;//全部文本信息
    private Activity mActivity;
    private int mNum;

    /**
     * 是否展开
     */
    private boolean spread = true;

    /**
     * 文字颜色
     */
    private Integer mSpanTextColor;

    /**
     * 设置图片
     */
    private Drawable mDrawableOpen;
    private Drawable mDrawableClose;

    /**
     * 文字
     */
    private String mTextOpen = "...展开";
    private String mTextClose = "收起";

    /**
     * 行数
     */
    private int mLines = 2;

    /**
     * @param textView  文本框
     * @param oriMsg    原始信息
     * @param textOpen  展开性质的文字
     * @param textClose 关闭性质的文字
     */
    public UtilMoreText(Activity activity , final TextView textView, String oriMsg, String textOpen, String textClose) {
        mTextView = textView;
        mOriMsg = oriMsg;
        mTextOpen = textOpen;
        mTextClose = textClose;
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextView.getTextSize());
        this.mActivity = activity;
        mNum = getLineMaxNumber() * mLines;
    }

    /**
     * @param textView 文本框
     * @param oriMsg   原始文字
     */
    public UtilMoreText(Activity activity , final TextView textView, String oriMsg) {
        mTextView = textView;
        mOriMsg = oriMsg;
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextView.getTextSize());
        this.mActivity = activity;
        mNum = getLineMaxNumber() * mLines;
    }

    /**
     * @param textView      文本框
     * @param oriMsg        原始文字
     * @param drawableOpen  展开图标
     * @param drawableColse 关闭图标
     */
    public UtilMoreText(Activity activity , final TextView textView, String oriMsg, Drawable drawableOpen, Drawable drawableColse) {
        mTextView = textView;
        mOriMsg = oriMsg + "XX";
        mDrawableOpen = drawableOpen;
        mDrawableClose = drawableColse;
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextView.getTextSize());
        this.mActivity = activity;
        mNum = getLineMaxNumber() * mLines;
    }

    private static final String TAG = "ddd";

    /**
     * 设置行数
     *
     * @param lineNum 函数
     * @return UtilMoreText
     */
    public UtilMoreText setLines(int lineNum) {
        Log.i(TAG, "setLines: " + lineNum);
        this.mLines = lineNum;
        return this;
    }

    /**
     * 创建文本形式的结尾
     */
    public void createString() {
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());//必须设置否则无效
        mTextView.setText(compressedWithString());
    }

    /**
     * 创建图片形似的结尾
     */
    public void createImg() {
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());//必须设置否则无效
        mTextView.setText(compressedWithImg());
    }

    public UtilMoreText setExpectedWidth(float expectedWidth) {
        this.mExpectedWidth = expectedWidth;
        return this;
    }

    /**
     * 用  TextView  测量不同分辨率  下  一行能显示多少个字符
     */
    @SuppressLint("NewApi")
    public int getLineMaxNumber() {
        //获取用来测量的字符串的长度的
        int str_length = mOriMsg.length();
        //获取当前字符串所占的宽度   像素单位
        int total_str_dpi = (int) mTextView.getPaint().measureText(mOriMsg);
        //获取每个字符   占多少像素
        int c_dpi = total_str_dpi / str_length;
        //获取当前   手机的分辨率   获取横坐标像素
        DisplayMetrics displayMetrics = mActivity.getResources().getDisplayMetrics();
        float total_dpi = displayMetrics.widthPixels;
        //获取边距像素
        int parding_dpi=mTextView.getTotalPaddingLeft()+mTextView.getTotalPaddingRight();
//        int parding_dpi=0;
        //总宽像素   减去   边距像素   等于  最终显示一行字符的像素宽度
        int end_total_dpi=(int) total_dpi-parding_dpi;
        //总像素宽度   处于   单个字符占的宽度像素   得到一行占多少字符
        return  end_total_dpi / c_dpi;
    }

    /** 设置结尾 文字的颜色
     * @param spanTextColor 颜色id
     * @return 本类实例
     */
    public UtilMoreText setSpanTextColor(int spanTextColor) {
        mSpanTextColor = spanTextColor;
        return this;
    }


    class spanImgClickable extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            Log.i("colick", "onClick: ");
            TextView textView = (TextView) widget;
            if (spread) {//调用展开的方法
                spread = false;
                textView.setText(getSpannableImg(mOriMsg, mDrawableClose));

            } else {
                spread = true;
                textView.setText(getSpannableImg(textView.getText().subSequence(0, mOriMsg.length() >> 1), mDrawableOpen));
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
        }
    }

    class SpanTextClickable extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            if (spread) {//调用展开的方法
                spread = false;
                mTextView.setText(getSpannableString(mOriMsg + mTextClose));
            } else {
                spread = true;
                mTextView.setText(compressedWithString());
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            if (mSpanTextColor != null) {
                ds.setColor(mSpanTextColor);
            } else {
                ds.setColor(ds.linkColor);
            }
            ds.setUnderlineText(false);    //去除超链接的下划线
        }
    }

    private SpannableString compressedWithImg() {
        return compressedWithString1();
    }

    /**
     * 压缩字符串
     *
     * @return 富文本字符串
     */
    private SpannableString compressedWithString() {
        return compressedWithString1();
    }

    private SpannableString compressedWithString1() {
        if (mOriMsg.length() == 0) {
            return getSpannableString(mOriMsg);
        }
        String resultText = "";//处理后的字符串
        String tempResultText = mOriMsg + mTextOpen;
        TextPaint paint = mTextView.getPaint();
        paint.setTextSize(mTextView.getTextSize());

        if (paint.measureText(tempResultText) < mNum) {
            resultText = mOriMsg;
        } else {
            resultText = mOriMsg.substring(0,mNum);
        }
        resultText = resultText.substring(0,resultText.length() -mTextOpen.length() - 4 ) + mTextOpen;
        resultText = resultText.replace("\r","").replace("\n","");
        return getSpannableString(resultText);
    }

    /**
     * 文字
     *
     * @param text 文字
     * @return SpannableString
     */
    private SpannableString getSpannableString(CharSequence text) {
        int len = text.length();

        if (len == 0 || len < 5) {
            text = "请输入要展示的信息！";
            return new SpannableString(text);
        }
        SpannableString spanableInfo = new SpannableString(text);
        spanableInfo.setSpan(new SpanTextClickable(), text.length() - mTextOpen.length() + 3, text.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanableInfo;
    }

    /**
     * 设置富文本 图片
     *
     * @param text     文字
     * @param drawable 图片
     * @return SpannableString
     */
    private SpannableString getSpannableImg(CharSequence text, Drawable drawable) {
        int len = text.length();
        if (len == 0 || len < 5) {
            text = "请输入要展示的信息！";
            return new SpannableString(text);
        }
        SpannableString spanableInfo = new SpannableString(text);
        int drawHeight = drawable.getMinimumHeight();
        drawable.setBounds(0, 0, drawHeight, drawHeight);
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable);
        spanableInfo.setSpan(imageSpan, len - 2, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(new spanImgClickable(), text.length() - 2, text.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanableInfo;
    }
}
