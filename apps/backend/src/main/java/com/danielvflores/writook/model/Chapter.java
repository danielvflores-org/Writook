package com.danielvflores.writook.model;


public class Chapter extends Story {
    private final String chapterTitle;
    private final String chapterContent;
    private final int chapterNumber;

    public Chapter(String chapterTitle, String chapterContent, int chapterNumber) {
        super(chapterTitle, chapterContent, null, null, null, null, null);
        this.chapterTitle = chapterTitle;
        this.chapterContent = chapterContent;
        this.chapterNumber = chapterNumber;
    }

    // ONLY GETTERS ARE NEEDED, SINCE THESE FIELDS DON’T NEED TO BE MODIFIED AFTER CREATING THE OBJECT. A CHAPTER IS IMMUTABLE.
    public String getChapterTitle() {
        return chapterTitle;
    }

    public String getChapterContent() {
        return chapterContent;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }
}
