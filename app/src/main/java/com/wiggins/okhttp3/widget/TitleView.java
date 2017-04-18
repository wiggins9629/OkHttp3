package com.wiggins.okhttp3.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wiggins.okhttp3.R;
import com.wiggins.okhttp3.base.BaseActivity;

/**
 * @Description 顶部公共View
 * @Author 一花一世界
 */

public class TitleView extends LinearLayout {

    private LayoutInflater inflater;
    private Context mContext;
    private View layout;

    private ImageView mIvLeftImage;     // 左边的图片
    private ImageView mIvRightImage;   // 右边的图片

    private TextView mTvLeftText;      // 左边的文字
    private TextView mTvRightText;     // 右边的文字

    private TextView mTvTitle;         // 页面标题

    public TitleView(Context context) {
        super(context);
        initview(context, null, 0);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initview(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initview(context, attrs, defStyle);
    }

    private void initview(Context context, AttributeSet attrs, int defStyle) {
        mContext = context;
        if (isInEditMode()) {
            //显示一个IDE编辑状态下标题栏
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 45));
            addView(textView);
        } else {
            inflater = LayoutInflater.from(context);
            layout = inflater.inflate(R.layout.title_top_widget, this, true);

            initView();
        }
    }

    private void initView() {
        mIvLeftImage = (ImageView) layout.findViewById(R.id.iv_left_image);
        mIvRightImage = (ImageView) layout.findViewById(R.id.iv_right_image);

        mTvLeftText = (TextView) layout.findViewById(R.id.tv_left_text);
        mTvRightText = (TextView) layout.findViewById(R.id.tv_right_text);

        mTvTitle = (TextView) layout.findViewById(R.id.tv_title);
    }

    /**
     * @Description 设置标题的内容
     */
    public void setAppTitle(int resourceId) {
        mTvTitle.setText(mContext.getResources().getString(resourceId));
    }

    /**
     * @Description 设置标题的内容
     */
    public void setAppTitle(String title) {
        mTvTitle.setText(title);
    }

    /**
     * @Description 获取标题TextView
     */
    public TextView getAppTitleView() {
        return mTvTitle;
    }

    /**
     * @Description 设置左边图片资源
     */
    public void setLeftImageResource(int resourseID) {
        isLeftTextOrImageShow(false);
        mIvLeftImage.setImageResource(resourseID);
    }

    /**
     * @Description 设置左边图片资源
     */
    public void setLeftImageDrawable(Drawable drawable) {
        isLeftTextOrImageShow(false);
        mIvLeftImage.setImageDrawable(drawable);
    }

    /**
     * @Description 设置左边图片的显示状态
     */
    public void setLeftImageVisibility(int visibility) {
        mIvLeftImage.setVisibility(visibility);
    }

    /**
     * @Description 设置左边ImageView是否可以点击
     */
    public void setLeftImageViewEnable(boolean isEnable) {
        mIvLeftImage.setEnabled(isEnable);
    }

    /**
     * @Description 获取左边图片ImageView
     */
    public ImageView getLeftImageView() {
        return mIvLeftImage;
    }

    public void setLeftImgOnClickListener() {
        mIvLeftImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof BaseActivity) {
                    ((BaseActivity) mContext).finish();
                }
            }
        });
    }

    /**
     * @Description 设置左边文字的内容
     */
    public void setLeftTextResource(int resourseID) {
        isLeftTextOrImageShow(true);
        mTvLeftText.setText(mContext.getResources().getString(resourseID));
    }

    /**
     * @Description 设置左边文字的内容
     */
    public void setLeftTextStr(String leftTextStr) {
        isLeftTextOrImageShow(true);
        mTvLeftText.setText(leftTextStr);
    }

    /**
     * @Description 设置左边文字的显示状态
     */
    public void setLeftTextVisibility(int visibility) {
        mTvLeftText.setVisibility(visibility);
    }

    /**
     * @Description 设置左边TextView是否可以点击
     */
    public void setLeftTextViewEnable(boolean isEnable) {
        mTvLeftText.setEnabled(isEnable);
    }

    /**
     * @Description 得到左边文字TextView
     */
    public TextView getLeftTextView() {
        return mTvLeftText;
    }

    /**
     * @Description 左侧文字点击效果
     */
    public void setLeftTextOnClickListener() {
        mTvLeftText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof BaseActivity) {
                    ((BaseActivity) mContext).finish();
                }
            }
        });
    }

    /**
     * @Description 左边TextView是否显示
     */
    private void isLeftTextOrImageShow(boolean isShow) {
        if (isShow) {
            mIvLeftImage.setVisibility(View.GONE);
            mTvLeftText.setVisibility(View.VISIBLE);
        } else {
            mIvLeftImage.setVisibility(View.VISIBLE);
            mTvLeftText.setVisibility(View.GONE);
        }
    }

    /**
     * @Description 设置右边图片资源
     */
    public void setRightImageResource(int resourseID) {
        isRightTextOrImageShow(false);
        mIvRightImage.setImageResource(resourseID);
    }

    /**
     * @Description 设置右边图片资源
     */
    public void setRightImageDrawable(Drawable drawable) {
        isRightTextOrImageShow(false);
        mIvRightImage.setImageDrawable(drawable);
    }

    /**
     * @Description 设置左边图片的显示状态
     */
    public void setRightImageVisibility(int visibility) {
        mIvRightImage.setVisibility(visibility);
    }

    /**
     * @Description 设置右边ImageView是否可以点击
     */
    public void setRightImageViewEnable(boolean isEnable) {
        mIvRightImage.setEnabled(isEnable);
    }

    /**
     * @Description 获取左边图片ImageView
     */
    public ImageView getRightImageView() {
        return mIvRightImage;
    }

    /**
     * @Description 右侧图片点击效果
     */
    public void setRightImageOnClickListener() {
        mIvRightImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof BaseActivity) {
                    ((BaseActivity) mContext).finish();
                }
            }
        });
    }

    /**
     * @Description 设置右边文字的内容
     */
    public void setRightTextResource(int resourseID) {
        isRightTextOrImageShow(true);
        mTvRightText.setText(mContext.getResources().getString(resourseID));
    }

    /**
     * @Description 设置右边文字的内容
     */
    public void setRightTextStr(String leftTextStr) {
        isRightTextOrImageShow(true);
        mTvRightText.setText(leftTextStr);
    }

    /**
     * @Description 设置右边文字的显示状态
     */
    public void setRightTextVisibility(int visibility) {
        mTvRightText.setVisibility(visibility);
    }

    /**
     * @Description 设置右边TextView是否可以点击
     */
    public void setRightTextViewEnable(boolean isEnable) {
        mTvRightText.setEnabled(isEnable);
    }

    /**
     * @Description 得到右边文字TextView
     */
    public TextView getRightTextView() {
        return mTvRightText;
    }

    /**
     * @Description 右侧文字点击效果
     */
    public void setRightTextOnClickListener() {
        mTvRightText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof BaseActivity) {
                    ((BaseActivity) mContext).finish();
                }
            }
        });
    }

    /**
     * 右边TextView是否显示
     */
    private void isRightTextOrImageShow(boolean isShow) {
        if (isShow) {
            mIvRightImage.setVisibility(View.GONE);
            mTvRightText.setVisibility(View.VISIBLE);
        } else {
            mIvRightImage.setVisibility(View.VISIBLE);
            mTvRightText.setVisibility(View.GONE);
        }
    }
}

