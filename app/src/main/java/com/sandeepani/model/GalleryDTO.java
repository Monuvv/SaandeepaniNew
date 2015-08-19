package com.sandeepani.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sandeep on 5/14/2015.
 */
public class GalleryDTO implements Serializable {
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public ArrayList<GalleryItemModel> getGalleryItemsList() {
        return filesUrls;
    }

    public void setGalleryItemList(ArrayList<GalleryItemModel> filesUrls) {
        this.filesUrls = filesUrls;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public int getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
    }

    public String getCoverPicUrl() {
        return coverPicUrl;
    }

    public void setCoverPicUrl(String coverPicUrl) {
        this.coverPicUrl = coverPicUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String fileType;
    private String fileName;
    private String coverPicUrl;
    private int filesCount;
    private String postedDate;
    private ArrayList<GalleryItemModel> filesUrls;
}
