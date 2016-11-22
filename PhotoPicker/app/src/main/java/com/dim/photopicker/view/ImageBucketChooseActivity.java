package com.dim.photopicker.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dim.photopicker.R;
import com.dim.photopicker.adapter.ImageBucketAdapter;
import com.dim.photopicker.model.ImageBucket;
import com.dim.photopicker.util.CustomConstants;
import com.dim.photopicker.util.ImageFetcher;
import com.dim.photopicker.util.IntentConstants;

/**
 * 选择相册
 * 
 */

public class ImageBucketChooseActivity extends BaseActivity {
    private ImageFetcher       mHelper;
    private List<ImageBucket>  mDataList = new ArrayList<ImageBucket>();
    private ListView           mListView;
    private ImageBucketAdapter mAdapter;
    private int                availableSize;
    // 是否可以裁剪,默认不可以
    private boolean            doesCanCrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_image_bucket_choose);
        mHelper = ImageFetcher.getInstance(getApplicationContext());
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initData() {
        mDataList = mHelper.getImagesBucketList(false);
        availableSize = getIntent().getIntExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, CustomConstants.MAX_IMAGE_SIZE);
        doesCanCrop = getIntent().getBooleanExtra(IntentConstants.EXTRA_CAN_CROP, false);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new ImageBucketAdapter(this, mDataList);
        mListView.setAdapter(mAdapter);
        TextView titleTv = (TextView) findViewById(R.id.title);
        titleTv.setText("相册");
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectOne(position);

                Intent intent = new Intent(ImageBucketChooseActivity.this, ImageChooseActivity.class);
                intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST, (Serializable) mDataList.get(position).imageList);
                intent.putExtra(IntentConstants.EXTRA_BUCKET_NAME, mDataList.get(position).bucketName);
                intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, availableSize);
                intent.putExtra(IntentConstants.EXTRA_CAN_CROP, doesCanCrop);

                startActivityForResult(intent, CustomConstants.RESULT_PICTURE_BACK);
                ImageBucketChooseActivity.this.finish();
            }
        });

        TextView cancelTv = (TextView) findViewById(R.id.action);
        cancelTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageBucketChooseActivity.this.finish();
            }
        });
    }

    private void selectOne(int position) {
        int size = mDataList.size();
        for (int i = 0; i != size; i++) {
            if (i == position)
                mDataList.get(i).selected = true;
            else {
                mDataList.get(i).selected = false;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

}
