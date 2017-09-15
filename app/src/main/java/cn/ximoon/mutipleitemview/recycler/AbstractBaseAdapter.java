package cn.ximoon.mutipleitemview.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Admin on 2017/9/14.
 */

public abstract class AbstractBaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private int mSection;

    public abstract boolean isNeedHeaderView();

    public abstract boolean isNeedFooterView();

    public abstract View bindHeaderView(BaseViewHolder holder, int position);

    public abstract View getFooterView(BaseViewHolder holder, int position);

    public final void setSection(int section){
        this.mSection = section;
    }

    public final int getSection(){
        return this.mSection;
    }

    public final int getTotalCount(){
        int count = getItemCount();
        if (isNeedHeaderView()){
            count ++;
        }
        if (isNeedFooterView()){
            count ++;
        }
        return count;
    }
}
