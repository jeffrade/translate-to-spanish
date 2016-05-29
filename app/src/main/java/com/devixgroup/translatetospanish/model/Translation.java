package com.devixgroup.translatetospanish.model;

public class Translation {

    private long mId = -1;
    private String mEnWord;
    private String mEsWord;
    private int isCached;

    public Translation(String enWord, String esWord, boolean isCached) {
        super();
        this.mEnWord = enWord;
        this.mEsWord = esWord;
        this.isCached = isCached ? 1 : 0;
    }

    public Translation(long id, String enWord, String esWord, boolean isCached) {
        this(enWord, esWord, isCached);
        this.mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getEnWord() {
        return mEnWord;
    }

    public void setEnWord(String enWord) {
        this.mEnWord = enWord;
    }

    public String getEsWord() {
        return mEsWord;
    }

    public void setEsWord(String esWord) {
        this.mEsWord = esWord;
    }

    public int getIsCached() {
        return isCached;
    }

    public void setIsCached(int isCached) {
        this.isCached = isCached;
    }
}
