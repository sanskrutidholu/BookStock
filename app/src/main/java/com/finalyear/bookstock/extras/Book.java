package com.finalyear.bookstock.extras;

public class Book {
    private String mTitle;
    private String mAuthors;
    private String mPublishedDate;
    private String mDescription;
    private String mCategories;
    private String mThumbnail;
    private String mBuy;
    private String mPreview;
    private String mPrice;
    private String mIsbn;
    private int pageCount;
    private String mUrl;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAuthors() {
        return mAuthors;
    }

    public void setmAuthors(String mAuthors) {
        this.mAuthors = mAuthors;
    }

    public String getmPublishedDate() {
        return mPublishedDate;
    }

    public void setmPublishedDate(String mPublishedDate) {
        this.mPublishedDate = mPublishedDate;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmCategories() {
        return mCategories;
    }

    public void setmCategories(String mCategories) {
        this.mCategories = mCategories;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getmBuy() {
        return mBuy;
    }

    public void setmBuy(String mBuy) {
        this.mBuy = mBuy;
    }

    public String getmPreview() {
        return mPreview;
    }

    public void setmPreview(String mPreview) {
        this.mPreview = mPreview;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public void setmIsbn(String mIsbn) {
        this.mIsbn = mIsbn;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    String getmUrl() {
        return mUrl;
    }

    String getmIsbn() {
        return mIsbn;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getTitle() {
        return mTitle;
    }

    String getAuthors() {
        return mAuthors;
    }

    String getPublishedDate() {
        return mPublishedDate;
    }

    String getDescription() {
        return mDescription;
    }

    String getCategories() {
        return mCategories;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    String getBuy() {
        return mBuy;
    }

    public String getPreview() {
        return mPreview;
    }

    public String getPrice() {
        return mPrice;
    }

    public Book(String mTitle, String mAuthors, String mPublishedDate, String mDescription, String mCategories, String mThumbnail,
                String mBuy, String mPreview , String price,int pageCount , String mUrl, String mIsbn) {
        this.mTitle = mTitle;
        this.mAuthors = mAuthors;
        this.mPublishedDate = mPublishedDate;
        this.mDescription = mDescription;
        this.mCategories = mCategories;
        this.mThumbnail = mThumbnail;
        this.mBuy = mBuy;
        this.mPreview = mPreview;
        this.mPrice = price;
        this.pageCount = pageCount;
        this.mUrl = mUrl;
        this.mIsbn = mIsbn;
    }

}
