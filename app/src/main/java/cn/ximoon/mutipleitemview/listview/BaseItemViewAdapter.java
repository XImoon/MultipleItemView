package cn.ximoon.mutipleitemview.listview;

import android.view.View;
import android.widget.BaseAdapter;

/**
 * Created by Admin on 2017/9/14.
 */

public abstract class BaseItemViewAdapter extends BaseAdapter {

    private int mSection;

    public abstract boolean isNeedHeaderView();

    public abstract boolean isNeedFooterView();

    public abstract View getHeaderView(View convertView);

    public abstract View getFooterView(View convertView);

    public final void setSection(int section){
        this.mSection = section;
    }

    public final int getSection(){
        return this.mSection;
    }

    public final int getTotalCount(){
        int count = getCount();
        if (isNeedHeaderView()){
            count ++;
        }
        if (isNeedFooterView()){
            count ++;
        }
        return count;
    }

}
