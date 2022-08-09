package com.qy.login.widget.input;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.qy.login.BR;

/**
 * 文件描述：UserInput 需要用到的一些配置值
 * 作者：dim
 * 创建时间：2019-08-01
 * 更改时间：2019-08-01
 * 版本号：1
 */
public class InputOption extends BaseObservable {
    // 是否是密码输入
    private boolean doesPswInput;
    // 输入框输入的内容
    private String inputEditString;
    // 输入框，默认hint的值
    private String inputEditHintString;
    // 右边显示的内容
    private String inputRightString;
    // 是不是右边倒计时显示
    private boolean inputRightCount;

    private boolean showLeft;

    private String leftDes;

    @Bindable
    public boolean isShowLeft() {
        return showLeft;
    }

    public void setShowLeft(boolean showLeft) {
        this.showLeft = showLeft;
        notifyPropertyChanged(BR.showLeft);
    }

    @Bindable
    public String getLeftDes() {
        return leftDes;
    }

    public void setLeftDes(String leftDes) {
        this.leftDes = leftDes;
        notifyPropertyChanged(BR.leftDes);
    }

    @Bindable
    public boolean isInputRightCount() {
        return inputRightCount;
    }

    public void setInputRightCount(boolean inputRightCount) {
        this.inputRightCount = inputRightCount;
        notifyPropertyChanged(BR.inputRightCount);
    }

    @Bindable
    public boolean isDoesPswInput() {
        return doesPswInput;
    }

    public void setDoesPswInput(boolean doesPswInput) {
        this.doesPswInput = doesPswInput;
        notifyPropertyChanged(BR.doesPswInput);
    }

    @Bindable
    public String getInputEditString() {
        return inputEditString;
    }

    public void setInputEditString(String inputEditString) {
        this.inputEditString = inputEditString;
        notifyPropertyChanged(BR.inputEditString);
    }

    @Bindable
    public String getInputEditHintString() {
        return inputEditHintString;
    }

    public void setInputEditHintString(String inputEditHintString) {
        this.inputEditHintString = inputEditHintString;
        notifyPropertyChanged(BR.inputEditHintString);
    }

    @Bindable
    public String getInputRightString() {
        return inputRightString;
    }

    public void setInputRightString(String inputRightString) {
        this.inputRightString = inputRightString;
        notifyPropertyChanged(BR.inputRightString);
    }
}