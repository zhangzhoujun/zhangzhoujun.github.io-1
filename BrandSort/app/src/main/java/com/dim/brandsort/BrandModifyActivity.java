package com.dim.brandsort;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by dim on 2018/11/23.
 */

public class BrandModifyActivity extends Activity {

    RecyclerView mRecyclerView;

    private ArrayList<MDiyBrandItem> mBrandMyList;
    private ArrayList<MDiyBrandItem> mBrandOtherList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_modify);

        mRecyclerView = findViewById(R.id.modify_recyclerview);

        mBrandMyList = new ArrayList<>();
        mBrandOtherList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            MDiyBrandItem item = new MDiyBrandItem();
            item.setSort(i * 10 + 10);
            item.setBrandName("品牌" + item.getSort());
            mBrandMyList.add(item);
        }

        for (int i = 0; i < 20; i++) {
            MDiyBrandItem item = new MDiyBrandItem();
            item.setSort(i);
            item.setBrandName("品牌" + item.getSort());
            mBrandOtherList.add(item);
        }

        initData();
    }

    private void initData() {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        final ViewTuijianBrandAdapter adapter = new ViewTuijianBrandAdapter(this, helper, mBrandMyList, mBrandOtherList);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                return viewType == ViewTuijianBrandAdapter.TYPE_MY || viewType == ViewTuijianBrandAdapter.TYPE_OTHER ? 1 : 3;
            }
        });
        mRecyclerView.setAdapter(adapter);

    }

    private void onSaveClick() {
        if (mBrandMyList.size() < 3) {
            Toast.makeText(this, "请保证自营品牌推荐已选中3个品牌", Toast.LENGTH_SHORT).show();
            return;
        }

        onSaveSuccess();
    }

    private void onSaveSuccess() {
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }

}
