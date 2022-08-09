package com.qm.module_juggle.entity;

import com.kunminx.linkage.bean.BaseGroupedItem;

public class ElemeGroupedItem extends BaseGroupedItem<ElemeGroupedItem.ItemInfo> {

    public ElemeGroupedItem(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public ElemeGroupedItem(ItemInfo item) {
        super(item);
    }

    public static class ItemInfo extends BaseGroupedItem.ItemInfo {
        private MHomeDataBean.MHomeDataItemData item;
        private String pos;

        public ItemInfo(MHomeDataBean.MHomeDataItemData item, String group,String pos) {
            super(group, group);
            this.item = item;
            this.pos = pos;
        }

        public ItemInfo(String title, String group) {
            super(title, group);
        }

        public MHomeDataBean.MHomeDataItemData getItem() {
            return item;
        }

        public void setItem(MHomeDataBean.MHomeDataItemData item) {
            this.item = item;
        }

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }
    }
}