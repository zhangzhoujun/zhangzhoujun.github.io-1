package com.qy.login.widget.input;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;

import com.dim.library.bus.RxBus;
import com.dim.library.utils.DLog;
import com.qm.lib.widget.input.UserInputChangeMessage;
import com.qy.login.R;
import com.qy.login.databinding.UserInputBinding;


/**
 * 文件描述：登录注册，自定义的输入框
 * 作者：dim
 * 创建时间：2019-08-01
 * 更改时间：2019-08-01
 * 版本号：1
 */
public class UserInput extends RelativeLayout {
    // 获取短信验证码的点击监听
    private onGetSmsClickCallBack listener;
    private onLeftClickCallBack leftListener;
    // UserInput的一些配置属性
    private InputOption mOptions;

    private UserInputBinding binding;

    public UserInput(Context context) {
        super(context);
        initView(context);
    }

    public UserInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public UserInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.user_input, this);
        binding = DataBindingUtil.bind(findViewById(R.id.root));
        binding.inputRight.setOnClickListener(e -> onSmsGetClick());
        binding.inputLeft.setOnClickListener(e -> onLeftClick());

        binding.inputEdit.addTextChangedListener(textWatcher);
    }

    private void onSmsGetClick() {
        if (mOptions.isInputRightCount()) {
            return;
        }
        if (listener != null) {
            listener.ongetSmsClick();
        }
    }

    /**
     * 输入框的监听
     */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s != null){
                if(mOptions != null){
                    mOptions.setInputEditString(s.toString());

                }

                RxBus.getDefault().post(new UserInputChangeMessage());
            }

        }
    };

    /**
     * 设置控件的内容
     *
     * @param options 设置的参数
     */
    public void setOptions(InputOption options) {
        mOptions = options;
        binding.setInput(mOptions);
    }

    public InputOption getOptions() {
        return mOptions;
    }

    public onGetSmsClickCallBack getListener() {
        return listener;
    }

    public void setListener(onGetSmsClickCallBack listener) {
        this.listener = listener;
    }

    public onLeftClickCallBack getLeftListener() {
        return leftListener;
    }

    public void setLeftListener(onLeftClickCallBack leftListener) {
        this.leftListener = leftListener;
    }

    private void onGetSmsClick() {
        DLog.d("onGetSmsClick");
        if (listener != null) {
            listener.ongetSmsClick();
        }
    }

    public interface onGetSmsClickCallBack {
        public void ongetSmsClick();
    }

    public interface onLeftClickCallBack {
        public void onLeftClick();
    }

    private void onLeftClick(){
        DLog.d("onLeftClick");
        if (leftListener != null) {
            leftListener.onLeftClick();
        }
    }
}
