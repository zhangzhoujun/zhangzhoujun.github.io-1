package com.dim.dialog;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action",
                // Snackbar.LENGTH_LONG).setAction("Action", null).show();
                showWarningMessage();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showWarningMessage() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        DimAlertDialog pDialog = new DimAlertDialog(this, dm.heightPixels, dm.widthPixels);
        pDialog.setTitleText("你好");
        pDialog.setCancelable(false);
        pDialog.setContentText("我是内容");
        pDialog.setCancelText("");
        pDialog.setConfirmText("确定");
        pDialog.setCancelClickListener(new DimAlertDialog.OnKLMAlertClickListener() {
            @Override
            public void onClick(DimAlertDialog sweetAlertDialog) {
                Log.i("DIM","点击了取消");
                Toast.makeText(MainTestActivity.this, "点击了取消", Toast.LENGTH_LONG);
            }
        });
        pDialog.setConfirmClickListener(new DimAlertDialog.OnKLMAlertClickListener() {
            @Override
            public void onClick(DimAlertDialog sweetAlertDialog) {
                Log.i("DIM","点击了确定");
                Toast.makeText(MainTestActivity.this, "点击了确定", Toast.LENGTH_LONG);
            }
        });
        pDialog.show();
    }
}
