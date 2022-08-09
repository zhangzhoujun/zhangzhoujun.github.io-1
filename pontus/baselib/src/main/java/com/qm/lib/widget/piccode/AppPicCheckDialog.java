package com.qm.lib.widget.piccode;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;

import com.dim.library.utils.ToastUtils;
import com.qm.lib.R;
import com.qm.lib.databinding.LibBasePicCodeBinding;
import com.qm.lib.utils.StringUtils;

/**
 * @author dim
 * @create at 2019-06-12 15:27
 * @description:
 */
public class AppPicCheckDialog extends Dialog {

    private Context mContet;
    private LibBasePicCodeBinding binding;
    private GosPicCheckDialogListener mListener;
    private PicCodeViewModel viewModel;

    private String mobile;

    public AppPicCheckDialog(Context context, GosPicCheckDialogListener mListener) {
        super(context, R.style.lib_base_psd_dialog);
        this.mContet = context;
        this.mListener = mListener;
    }

    public AppPicCheckDialog(Context context, String mobile, GosPicCheckDialogListener mListener) {
        super(context, R.style.lib_base_psd_dialog);
        this.mContet = context;
        this.mListener = mListener;
        this.mobile = mobile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContet), R.layout.lib_base_pic_code, null, false);
        setContentView(binding.getRoot());

        if (mobile == null || mobile == "") {
            viewModel = new PicCodeViewModel(new PicCodeViewModel.PicCodeViewListener() {
                @Override
                public void onCheckoutResult(String key, String code) {
                    if (StringUtils.Companion.getInstance().isEmpty(key) || StringUtils.Companion.getInstance().isEmpty(code)) {
                        ToastUtils.showShort("请稍后重试");
                        dismiss();
                        return;
                    }
                    if (mListener != null) {
                        mListener.GosPicCheckDialogResult(key, code);
                        viewModel.setPicCodeViewListener(null);
                        mListener = null;
                        dismiss();
                    }
                }
            });
        } else {
            viewModel = new PicCodeViewModel(mobile, new PicCodeViewModel.PicCodeViewListener() {
                @Override
                public void onCheckoutResult(String key, String code) {
                    if (StringUtils.Companion.getInstance().isEmpty(key) || StringUtils.Companion.getInstance().isEmpty(code)) {
                        ToastUtils.showShort("请稍后重试");
                        dismiss();
                        return;
                    }
                    if (mListener != null) {
                        mListener.GosPicCheckDialogResult(key, code);
                        viewModel.setPicCodeViewListener(null);
                        mListener = null;
                        dismiss();
                    }
                }
            });
        }

        binding.setViewModel(viewModel);

        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        win.setGravity(Gravity.CENTER);
        lp.width = (int) mContet.getResources().getDimension(R.dimen.dp_310);
        lp.height = (int) mContet.getResources().getDimension(R.dimen.dp_159);
        win.setAttributes(lp);

        showSoftInputKetboard(binding.signPicCodeEdittext);
    }

    private void showSoftInputKetboard(final EditText editText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) mContet.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);
    }

    public interface GosPicCheckDialogListener {
        public void GosPicCheckDialogResult(String key, String code);
    }
}
