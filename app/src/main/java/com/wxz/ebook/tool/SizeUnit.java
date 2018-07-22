package com.wxz.ebook.tool;

import java.text.DecimalFormat;

public class SizeUnit {
    public String getSizeStr(String size){
        String sizeStr;
        DecimalFormat df = new DecimalFormat("0.0");
        long sizeValue = Long.valueOf(size);
        float sizeFloat;
        if (sizeValue>1024*1024){
            sizeFloat = (float)sizeValue/1024/1024;
            sizeStr = df.format(sizeFloat) + "MB";
        }else if(sizeValue>1024){
            sizeFloat = (float)sizeValue/1024;
            sizeStr = df.format(sizeFloat) + "KB";
        }else {
            sizeStr = String.valueOf(sizeValue) + "B";
        }
        return sizeStr;
    }
}
