package yeapp.com.burracoscore.layoutModel.decorator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by iacopo on 03/02/15.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mDivider == null) return;
        if (parent.getChildPosition(view) < 1) return;

//        if (getOrientation(parent) == LinearLayoutManager.VERTICAL)
        outRect.top = mDivider.getIntrinsicHeight();
//        else outRect.left = mDivider.getIntrinsicWidth();
    }

//    private int getOrientation(RecyclerView parent) {
//        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
//            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
//            return layoutManager.getOrientation();
//        } else
//            throw new IllegalStateException("DividerItemDecoration can only be used with a LinearLayoutManager.");
//    }
}