package com.co.edu.cun.www1104379214.bienestarcun.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Satellite on 05/07/2016.
 */
public class ItemOffsetDecoration extends RecyclerView.ItemDecoration{

    private int mItemOffSet;

    public ItemOffsetDecoration(Context contexto, @IntegerRes int IntegerResID){

        int itemOffSetDp = contexto.getResources().getInteger( IntegerResID );
        mItemOffSet = ConvertToDpx( itemOffSetDp, contexto.getResources().getDisplayMetrics());

    }

    public int ConvertToDpx(int offSetDp, DisplayMetrics metrics){
        return offSetDp * ( metrics.densityDpi / 160 );
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffSet, mItemOffSet, mItemOffSet, mItemOffSet);
    }
}
