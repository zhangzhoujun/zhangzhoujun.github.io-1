package com.dim.piwview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dim.dimpieview.DimAnimationPercentPieView;

public class MainActivity extends AppCompatActivity {

    int[] data;
    String[] name;
    int[] color;

    DimAnimationPercentPieView animationPieView;

    RelativeLayout pieview_bg;

    private TextView percentText;
    private TextView percentNumText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        data = new int[]{20, 25, 40};
        name = new String[]{"联盟", "产业", "用户"};
        color = new int[]{
                getResources().getColor(R.color.c_lianmeng),
                getResources().getColor(R.color.c_chanye),
                getResources().getColor(R.color.c_yonghu)};

        animationPieView = (DimAnimationPercentPieView)
                findViewById(R.id.animationPieView);

        pieview_bg = (RelativeLayout) findViewById(R.id.pieview_bg);

        Button start = (Button) findViewById(R.id.start);
        Button reset = (Button) findViewById(R.id.reset);
        Button change = (Button) findViewById(R.id.change);
        percentText = (TextView) findViewById(R.id.percent);
        percentNumText = (TextView) findViewById(R.id.num);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationPieView.setData(data, name, color, new DimAnimationPercentPieView.OnDImAnimalPercentPieViewListener() {
                    @Override
                    public void onDrawPiePercent(int percent) {
                        percentText.setText(percent + "%");
                        percentNumText.setText(String.format("%d(%d)", percent, 100));
                    }
                });

                AnimatorSet animatorSet = new AnimatorSet();

                ObjectAnimator scaleX = ObjectAnimator.ofFloat(pieview_bg, "scaleX", 0.0f, 1.2f, 1.0f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(pieview_bg, "scaleY", 0.0f, 1.2f, 1.0f);
                ObjectAnimator alpha = ObjectAnimator.ofFloat(pieview_bg, "alpha", 0.0f, 1.0f);
                scaleX.setInterpolator(new AccelerateInterpolator());
                scaleY.setInterpolator(new AccelerateInterpolator());
                scaleX.setDuration(200);
                scaleY.setDuration(200);
                alpha.setDuration(200);

                ObjectAnimator scaleX1 = ObjectAnimator.ofFloat(pieview_bg, "scaleX", 1.0f, 1.05f, 1.0f);
                ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(pieview_bg, "scaleY", 1.0f, 1.05f, 1.0f);
                scaleX1.setInterpolator(new DecelerateInterpolator());
                scaleY2.setInterpolator(new DecelerateInterpolator());
                scaleX1.setDuration(800);
                scaleY2.setDuration(800);

                animatorSet.play(scaleX).with(scaleY).with(alpha).before(scaleX1).before(scaleY2);
                animatorSet.start();

                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        pieview_bg.clearAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        pieview_bg.clearAnimation();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationPieView.clean();
                percentText.setText("");
                percentNumText.setText("");
                data = new int[]{20, 25, 40};
                name = new String[]{"联盟", "产业", "用户"};
                color = new int[]{
                        getResources().getColor(R.color.c_lianmeng),
                        getResources().getColor(R.color.c_chanye),
                        getResources().getColor(R.color.c_yonghu)};
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationPieView.clean();
                percentText.setText("");
                percentNumText.setText("");
                data = new int[]{20};
                name = new String[]{"联盟"};
                color = new int[]{
                        getResources().getColor(R.color.c_lianmeng)};
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
