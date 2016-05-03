package view.dragon.com.wrapviewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/5/3.
 */
public class WrapViewGroup extends ViewGroup {
    public WrapViewGroup(Context context) {
        super(context);
    }

    public WrapViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int lineWidth = 0;
        int lineHeight = 0;
        int childWidth;
        int childHeight;
        int maxLineHeight = 0;
        int count = getChildCount();
        View child;
        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                childWidth = child.getMeasuredWidth();
                childHeight = child.getMeasuredHeight();
                if (childWidth + lineWidth > maxWidth) {
                    lineHeight += maxLineHeight;
                    lineWidth = 0;
                }
                if (childHeight > maxLineHeight) {
                    maxLineHeight = childHeight;
                }
                lineWidth = lineWidth + childWidth;
            }
        }
        lineHeight += maxLineHeight;
        Log.e("dragon","lineHeight :"+lineHeight);
        setMeasuredDimension(widthMeasureSpec, resolveSize(lineHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        int childWidth;
        int childHeight;

        int maxLineHeight = 0;

        int count = getChildCount();
        View child;
        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                childWidth = child.getMeasuredWidth();
                childHeight = child.getMeasuredHeight();
                //new line.
                if (left + childWidth > getMeasuredWidth()) {
                    left = 0;
                    top += maxLineHeight;
                    maxLineHeight = 0;
                }
                if (childHeight > maxLineHeight) {
                    maxLineHeight = childHeight;
                }
                right = left + childWidth;
                bottom = top + childHeight;
                child.layout(left, top, right, bottom);
                left += childWidth;
            }
        }
    }
}
