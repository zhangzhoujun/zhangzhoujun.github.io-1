package com.gos.module_web;

import android.app.Application;

import androidx.annotation.NonNull;

import com.qm.lib.widget.toolbar.JYToolbarOptions;
import com.qm.lib.widget.toolbar.ToolbarViewModel;

public class ModuleWebViewModel extends ToolbarViewModel {


    public ModuleWebViewModel(@NonNull Application application) {
        super(application);
    }


    public JYToolbarOptions initTitleOptions(String title) {
        JYToolbarOptions options = new JYToolbarOptions();
        options.setTitleString(title);
        options.setNeedNavigate(true);

        return options;
    }
}
