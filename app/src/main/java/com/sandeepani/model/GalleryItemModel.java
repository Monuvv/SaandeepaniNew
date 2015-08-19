package com.sandeepani.model;

import java.io.Serializable;

/**
 * Created by sandeep on 5/14/2015.
 */
public class GalleryItemModel implements Serializable {
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private String fileName;
    private String filePath;
    private String description;
}
