package cn.ximoon.mutipleitemview.recycler;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by Admin on 2017/9/14.
 */

public class MultipleViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private SparseArray<AbstractBaseAdapter> mSesctionAdapters;
    private SparseArray<Integer> mStartSections;
    private SparseArray<Integer> mEndSections;

    public MultipleViewAdapter(){
        mSesctionAdapters = new SparseArray<>();
        mStartSections = new SparseArray<>();
        mEndSections = new SparseArray<>();
    }

    public final void addAdapter(AbstractBaseAdapter adapter){
        int start = 0;
        int size = mEndSections.size();
        if (size != 0){
            start = mEndSections.get(size - 1) + 1;
        }
        mSesctionAdapters.put(mSesctionAdapters.size(), adapter);
        mStartSections.put(size, start);
        mEndSections.put(size, start + adapter.getTotalCount() - 1);
    }

    public final void addAdapter(int section, AbstractBaseAdapter adapter){
        addSortAdapter(section, adapter);
    }

    /**
     * 按照顺序递增的方式从0开始添加adapter
     * @param section   当前指定的位置，从1开始计算
     * @param adapter   添加的view对应的adapter
     */
    private final void addSortAdapter(int section, AbstractBaseAdapter adapter){
        // 修改下标从0开始
        if (section < 0){
            section = 0;
        }else{
            section --;
        }
        int size = mSesctionAdapters.size();
        AbstractBaseAdapter viewAdapter = null;
        // 循环adapter集合
        for (int i = 0; i < size; i++){
            // 开始替换当前位置的adapter（因为顺序递增，因此不存在跳跃式key，也不需要用keyAt获取key）
            if (i == section){
                // 保存之前位置的adapter
                viewAdapter = mSesctionAdapters.get(section);
                // 获取原指定的section的开始节点,(当插入到第0个时，起始位置为0，否则为上一个section的终点位置+1)
                int start = 0;
                if(section > 0){
                    start = mEndSections.get(section - 1) + 1;
                }
                // s设置新的section的起点坐标
                mStartSections.put(section, start);
                // 将新的adapter添加到当前位置
                mStartSections.put(section, start + 1);
                // 将新的adapter添加到当前位置
                adapter.setSection(section);
                mSesctionAdapters.put(section, adapter);
                // 计算新的section的终点坐标
                mEndSections.put(section, adapter.getTotalCount() + start - 1);
                // 保存下一个要被替换的adapter，方便下次添加
                adapter = viewAdapter;
                // 自增，计算下个被替换的节点坐标
                section++;
            }
        }
        // 当集合为空和循环排序结束后
        if (mSesctionAdapters.size() == size){
            // 添加最后一个adapter
            mSesctionAdapters.put(size, viewAdapter);
            if (size == 0){
                // 判0操作
                mEndSections.put(0, viewAdapter.getTotalCount() - 1);
            }else{
                int end = mEndSections.get(size);
                mStartSections.put(size + 1, end + 1);
                // 终点位置为end + 1 + adapter.getTotalCount() - 1
                mEndSections.put(size + 1, end + viewAdapter.getTotalCount());
            }
        }
    }

    /**
     * 获取当前位置对应的section
     * @param position
     * @return
     */
    public int getSection(int position){
        int section = 0;
        for (int i = 0; i < mSesctionAdapters.size(); i++){
            int start = mStartSections.valueAt(i);
            int end = mEndSections.valueAt(i);
            if (position > start && position < end){
                section = mStartSections.keyAt(i);
                break;
            }
        }
        return section;
    }

    /**
     * 判断是否需要绘制脚布局
     * @param adapter
     * @param section
     * @param position
     * @return
     */
    public boolean isFooterView(AbstractBaseAdapter adapter, int section, int position) {
        if (adapter.isNeedFooterView()) {
            int endIndex = mEndSections.get(section);
            if (endIndex == position) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getSection(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AbstractBaseAdapter adapter = mSesctionAdapters.get(viewType);
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int section = getSection(position);
        int start = mStartSections.get(section);
        int index = position - start;
        if (section == -1 || index < 0) {
            return;
        }
        AbstractBaseAdapter adapter = mSesctionAdapters.get(section);
        // 如果需要绘制头布局（满足两个条件，view坐标为0且有头布局需求）
        if (index == 0 && adapter.isNeedHeaderView()) {
            adapter.bindViewHolder(holder, index);
        } else if (isFooterView(adapter, section, position)) {
            adapter.getFooterView(holder, index);
        }
        // 如果存在头布局，则自减一略过header作为数据的游标
        if (adapter.isNeedHeaderView()) {
            index--;
        }
        // 此处的index对应数据集合的坐标
        adapter.bindViewHolder(holder, index);
    }

    @Override
    public int getItemCount() {
        return mEndSections.get(mEndSections.get(mEndSections.size() - 1) + 1);
    }
}
