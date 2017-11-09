package view.dragon.com.wrapviewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/3.
 */
public class Wrap2ViewGroup extends ViewGroup {

    private boolean isRtl = false;
    private int rows = 2;
    private int[] rowMaxHeight = new int[rows];
    private int[] rowLeft = new int[rows];
    private List<View> [] rowViews = new List[rows];

    public Wrap2ViewGroup(Context context) {
        super(context);
        init();
    }

    public Wrap2ViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        for(int i = 0; i < rows; i++){
            rowViews[i] = new ArrayList<>(3);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isRtl) {
            measureRTL(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureLTR(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private final int getMaxRowIndex() {
        int minLeftIndex = 0;
        for (int i = 1; i < rows; i++) {
            if (rowLeft[i] > rowLeft[minLeftIndex]) {
                minLeftIndex = i;
            }
        }
        return minLeftIndex;
    }
    private final int getAppendingRowIndex() {
        int minLeftIndex = 0;
        for (int i = 1; i < rows; i++) {
            if (rowLeft[i] < rowLeft[minLeftIndex]) {
                minLeftIndex = i;
            }
        }
        return minLeftIndex;
    }

    private void measureLTR(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < rows; i++) {
            rowMaxHeight[i] = 0;
            rowLeft[i] = getPaddingLeft();
            rowViews[i].clear();
        }
        int parentWidth;
        int childWidth;
        int childHeight;
        int childWidthWithMargin;
        int childHeightWithMargin;
        int count = getChildCount();
        int currentRowIndex = 0;
        LayoutParams layoutParams;
        View child;
        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                layoutParams = (LayoutParams) child.getLayoutParams();
                childWidth = child.getMeasuredWidth();
                childHeight = child.getMeasuredHeight();
                childWidthWithMargin = childWidth + layoutParams.leftMargin + layoutParams.rightMargin;
                childHeightWithMargin = childHeight + layoutParams.topMargin + layoutParams.bottomMargin;
                currentRowIndex = getAppendingRowIndex();

                rowViews[currentRowIndex].add(child);
                rowLeft[currentRowIndex] += childWidthWithMargin;
                if (childHeightWithMargin > rowMaxHeight[currentRowIndex]) {
                    rowMaxHeight[currentRowIndex] = childHeightWithMargin;
                }
            }
        }

        currentRowIndex = getMaxRowIndex();

        int compositeHeight = 0;
        for(int i = 0; i <)
        setMeasuredDimension(widthMeasureSpec, resolveSize(10, heightMeasureSpec));
    }

    private void measureRTL(int widthMeasureSpec, int heightMeasureSpec) {

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
        Log.e("dragon", "lineHeight :" + lineHeight);
        setMeasuredDimension(widthMeasureSpec, resolveSize(lineHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        View child;
        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                child.layout(layoutParams.left, layoutParams.top, layoutParams.right, layoutParams.bottom);
            }
        }
    }


    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends MarginLayoutParams {
        public int left;
        public int right;
        public int top;
        public int bottom;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
