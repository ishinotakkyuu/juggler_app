package com.example.title_1;

import android.graphics.Bitmap;

public class SampleListItem {
    private Bitmap mThumbnail = null;
    private String mTitle = null;

    /**
     * 空のコンストラクタ
     */
    public SampleListItem() {};

    /**
     * コンストラクタ
     * @param thumbnail サムネイル画像
     * @param title タイトル
     */
    public SampleListItem(Bitmap thumbnail, String title) {
        mThumbnail = thumbnail;
        mTitle = title;
    }

    /**
     * サムネイル画像を設定
     * @param thumbnail サムネイル画像
     */
    public void setThumbnail(Bitmap thumbnail) {
        mThumbnail = thumbnail;
    }

    /**
     * タイトルを設定
     * @param title タイトル
     */
    public void setmTitle(String title) {
        mTitle = title;
    }

    /**
     * サムネイル画像を取得
     * @return サムネイル画像
     */
    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    /**
     * タイトルを取得
     * @return タイトル
     */
    public String getTitle() {
        return mTitle;
    }
}