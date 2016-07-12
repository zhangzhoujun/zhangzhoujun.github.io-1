package com.dim.photopicker.model;

import java.io.Serializable;

/**
 * 图片对象
 *
 */
public class ImageItem implements Serializable {
    private static final long serialVersionUID = -7188270558443739436L;
    public String             imageId;
    public String             thumbnailPath;
    public String             sourcePath;
    public boolean            isSelected       = false;
    private String            url;
    private String            name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
