package com.gos.nodetransfer.oaid;

import android.content.Context;
import android.util.Log;

import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;

public class OAIDHelper implements IIdentifierListener {
    private AppIdsUpdater _listener;

    public OAIDHelper(AppIdsUpdater callback) {
        this._listener = callback;
    }

    public void getDeviceIds(Context cxt) {
        long timeb = System.currentTimeMillis();
        int nres = this.CallFromReflect(cxt);
        long timee = System.currentTimeMillis();
        long var10000 = timee - timeb;
        if (nres != 1008612 && nres != 1008613 && nres != 1008611 && nres != 1008614 && nres == 1008615) {
        }

        Log.d(this.getClass().getSimpleName(), "return value: " + String.valueOf(nres));
    }

    private int CallFromReflect(Context cxt) {
        return MdidSdkHelper.InitSdk(cxt, true, this);
    }

    public void OnSupport(boolean isSupport, IdSupplier _supplier) {
        if (_supplier != null) {
            String oaid = _supplier.getOAID();
            String vaid = _supplier.getVAID();
            String aaid = _supplier.getAAID();
            StringBuilder builder = new StringBuilder();
            builder.append("support: ").append(isSupport ? "true" : "false").append("\n");
            builder.append("OAID: ").append(oaid).append("\n");
            builder.append("VAID: ").append(vaid).append("\n");
            builder.append("AAID: ").append(aaid).append("\n");
            String idstext = builder.toString();
            if (this._listener != null) {
                this._listener.OnIdsAvalid(isSupport, oaid, aaid, vaid);
            }

        }
    }

    public interface AppIdsUpdater {
        void OnIdsAvalid(boolean var1, String var2, String var3, String var4);
    }
}
