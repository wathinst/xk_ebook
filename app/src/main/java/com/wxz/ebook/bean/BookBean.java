package com.wxz.ebook.bean;

import java.io.Serializable;
import java.util.List;

public class BookBean implements Serializable {
    public String bookName;
    public String bookAuthor;
    public List<PageModel> pageModels;
    public int index;

    public static class PageModel implements Serializable {
        public List<LineModel> lineModels;
        public int lineDiff;

        public PageModel() {
        }

        public PageModel(List<LineModel> lineModels, int lineDiff) {
            this.lineModels = lineModels;
            this.lineDiff = lineDiff;
        }

        public static class LineModel implements Serializable {
            public List<String> stringList;
            public List<Integer> strWidth;
            public List<Integer> strX;
            public int strDiff;
            public List<Integer> strColors;

            public LineModel() {
            }

            public LineModel(List<String> stringList, List<Integer> strWidth, List<Integer> strX, int strDiff, List<Integer> strColors) {
                this.stringList = stringList;
                this.strWidth = strWidth;
                this.strX = strX;
                this.strDiff = strDiff;
                this.strColors = strColors;
            }
        }
    }
}
