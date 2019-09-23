package com.example.zhaojing5.myapplication.View.pullablelayout;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;

public class HeaderScrollHelper {

    private int sysVersion;
    private ScrollableContainer mCurrentScrollableContainer;

    public HeaderScrollHelper() {
        sysVersion = Build.VERSION.SDK_INT;
    }

    public interface ScrollableContainer {

        View getScrollableView();
    }

    public void setCurrentScrollableContainer(ScrollableContainer scrollableContainer) {
        this.mCurrentScrollableContainer = scrollableContainer;
    }

    private View getScrollableView() {
        if (mCurrentScrollableContainer == null) {
            return null;
        }
        return mCurrentScrollableContainer.getScrollableView();
    }

    /**
     * 判断是否滑动到顶部方法,ScrollAbleLayout根据此方法来做一些逻辑判断
     */
    public boolean isTop(int itemDecorationHeight) {
        View scrollableView = getScrollableView();
        if (scrollableView == null) {
            throw new NullPointerException("You should call ScrollableHelper.setCurrentScrollableContainer() to set ScrollableContainer.");
        }

        if (scrollableView instanceof RecyclerView) {
            return isRecyclerViewTop((RecyclerView) scrollableView,itemDecorationHeight);
        }
        throw new IllegalStateException("scrollableView must be a instance of AdapterView|ScrollView|RecyclerView");
    }

    private boolean isRecyclerViewTop(RecyclerView recyclerView, int itemDecorationHeight) {
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);
                if (childAt == null || ( firstVisibleItemPosition == 0 && childAt.getTop() == itemDecorationHeight )) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将特定的view按照初始条件滚动
     *
     * @param velocityY 初始滚动速度
     * @param distance  需要滚动的距离
     * @param duration  允许滚动的时间
     */
    @SuppressLint("NewApi")
    public void smoothScrollBy(int velocityY, int distance, int duration) {
        View scrollableView = getScrollableView();
        if (scrollableView instanceof AbsListView) {
            AbsListView absListView = (AbsListView) scrollableView;
            if (sysVersion >= 21) {
                absListView.fling(velocityY);
            } else {
                absListView.smoothScrollBy(distance, duration);
            }
        } else if (scrollableView instanceof ScrollView) {
            ((ScrollView) scrollableView).fling(velocityY);
        } else if (scrollableView instanceof RecyclerView) {
            ((RecyclerView) scrollableView).fling(0, velocityY);
        } else if (scrollableView instanceof WebView) {
            ((WebView) scrollableView).flingScroll(0, velocityY);
        }
    }
}
