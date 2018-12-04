package com.dim.brandsort;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dim on 2018/11/23.
 */

public class MDiyBrandItem implements Parcelable {

    private String brandName = "测试";
    private String brandId;
    private String brandLogoUrl;
    private String isSelect;
    private int sort;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandLogoUrl() {
        return brandLogoUrl;
    }

    public void setBrandLogoUrl(String brandLogoUrl) {
        this.brandLogoUrl = brandLogoUrl;
    }

    public String getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.brandName);
        dest.writeString(this.brandId);
        dest.writeString(this.brandLogoUrl);
        dest.writeString(this.isSelect);
        dest.writeInt(this.sort);
    }

    public MDiyBrandItem() {
    }

    protected MDiyBrandItem(Parcel in) {
        this.brandName = in.readString();
        this.brandId = in.readString();
        this.brandLogoUrl = in.readString();
        this.isSelect = in.readString();
        this.sort = in.readInt();
    }

    public static final Parcelable.Creator<MDiyBrandItem> CREATOR = new Parcelable.Creator<MDiyBrandItem>() {
        @Override
        public MDiyBrandItem createFromParcel(Parcel source) {
            return new MDiyBrandItem(source);
        }

        @Override
        public MDiyBrandItem[] newArray(int size) {
            return new MDiyBrandItem[size];
        }
    };
}
