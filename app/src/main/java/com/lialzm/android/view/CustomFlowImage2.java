package com.lialzm.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lialzm.android.util.ImageUtil;
import com.lialzm.android.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局
 * 注意4张图片必须一样大
 * Created by lcy on 2016/3/22.
 */
public class CustomFlowImage2 extends ViewGroup implements View.OnClickListener {
    //存储图片路径
    private List<String> pics = new ArrayList<>();

    //一行多少个子view
    private int rowChildSize = 0;

    public CustomFlowImage2(Context context) {
        super(context);
    }

    public CustomFlowImage2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.d("find", "MeasureSpec==" + MeasureSpec.UNSPECIFIED + "," + MeasureSpec.AT_MOST + "," + MeasureSpec.EXACTLY);
        Log.d("find", "onMeasure==" + widthMode + "," + heightMode + "," + widthSize + "," + heightSize);
        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
    }

    public String[] getPics() {
        return pics.toArray(new String[]{});
    }

    public void addView(View child, String imagePath) {
//        super.addView(child);
        Log.d("find", "addView");
        if (child instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) child).getChildCount(); ++i) {
                View nextChild = ((ViewGroup) child).getChildAt(i);
                nextChild.setOnClickListener(this);
            }
        }
        MarginLayoutParams chiledLayoutParams = (MarginLayoutParams) child.getLayoutParams();
        CustomFlowImage2.LayoutParams lp =
                new CustomFlowImage2.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Log.d("find", "child.getWidth==" + child.getWidth() + "," + child.getMeasuredWidth());
        if (chiledLayoutParams != null) {
            lp.rightMargin = chiledLayoutParams.rightMargin;
            lp.leftMargin = chiledLayoutParams.leftMargin;
            lp.bottomMargin = chiledLayoutParams.bottomMargin;
            lp.topMargin = chiledLayoutParams.topMargin;
        }

        addView(child, 0, lp);
        pics.add(0, imagePath);
        if (getChildCount() < 10) {

        } else {//到达10的时候移除最后一个
            LogUtil.d("remove");
            getChildAt(getChildCount() - 1).setVisibility(GONE);
//            removeViewAt(getChildCount() - 1);
//            pics.remove(getChildCount() - 1);
            canAddImage = false;
        }
    }

    public void removeChild(int position) {
        removeViewAt(position);
        ImageUtil.deletePhotoAtPathAndName(pics.get(position));
        pics.remove(position);
        canAddImage = true;
        if (getChildCount() == 9) {
            getChildAt(getChildCount() - 1).setVisibility(VISIBLE);
        }
    }

    private boolean canAddImage = true;
    private boolean canDelImage = true;

    //判断是否可以添加新图片
    public Boolean isCanAddImage() {
        return canAddImage;
    }

    //判断是否可以删除图片
    public Boolean isCanDelImage() {
        return canAddImage;
    }

    public void setRowChildSize(int rowChildSize) {
        this.rowChildSize = rowChildSize;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int mPainterPosX = 0;  //当前绘图光标横坐标位置
        int mPainterPosY = 0;  //当前绘图光标纵坐标位置
        //获取该控件总的宽度
        int measuredWidth = getMeasuredWidth();
        Log.d("find", "measuredWidth==" + measuredWidth + "," + l + "," + r);
        //将布局平分
        int margin = 0;
        if (getChildCount() > 0) {
            if (rowChildSize == 0) {//未限制数量,则自行计算
                margin = getMeasuredWidth() / (measuredWidth / getChildAt(0).getMeasuredWidth());
            } else {
                margin = getMeasuredWidth() / rowChildSize;
            }
        }
        Log.d("find", "margin==" + margin);
        //总宽度
        int totalWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = this.getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            CustomFlowImage2.LayoutParams lp =
                    (CustomFlowImage2.LayoutParams) child.getLayoutParams();
            int lpLeftMargin = (margin - width) / 2;
            int lpRightMargin = (margin - width) / 2;
            int lpTopMargin = lp.topMargin;
            int lpBottomMargin = lp.bottomMargin;
            int lpWidth = lp.width;

            Log.d("find", "lpRightMargin==" + lpRightMargin);
            int paddingLeft = child.getPaddingLeft();
            int paddingRight = child.getPaddingRight();
            int paddingTop = child.getPaddingTop();
            int paddingBottom = child.getPaddingBottom();
            totalWidth += lpLeftMargin + width + paddingLeft + paddingRight + lpRightMargin;
            LogUtil.d("totalWidth==" + totalWidth + "," + measuredWidth);
            if (totalWidth > measuredWidth) {//换行
                mPainterPosX = 0;
                mPainterPosY += lpTopMargin + height + paddingTop;
                totalWidth = lpLeftMargin + width + paddingLeft + paddingRight + lpRightMargin;
            }
            LogUtil.d("mPainterPosY==" + mPainterPosY);
            Log.d("find", "mPainterPosX==" + i + "," + mPainterPosX + "," + lpLeftMargin);
            child.layout(mPainterPosX + lpLeftMargin, mPainterPosY, mPainterPosX + lpLeftMargin + width + lpRightMargin, mPainterPosY + lpTopMargin + height);
            mPainterPosX += lpLeftMargin + width + lpRightMargin;
        }
    }

    @Override
    public void onClick(View v) {
        onItemClick.onClick(v, indexOfChild((View) v.getParent()));
    }

    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);

        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    OnItemClick onItemClick;

    public void setOnItemClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onClick(View v, int position);
    }


}
