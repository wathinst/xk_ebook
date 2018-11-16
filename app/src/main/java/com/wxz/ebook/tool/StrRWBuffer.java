package com.wxz.ebook.tool;

import android.content.Context;

import com.wxz.ebook.bean.ChapterListBean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class StrRWBuffer {
    private RandomAccessFile raf;
    private MappedByteBuffer buffer;
    private FileChannel channel;
    private int thisIndex = 0;
    private int lastIndex = 0;
    private int newIndex = 0;
    private int nextIndex = 0;
    private int nameSize = 0;

    public StrRWBuffer(Context context, String fileName) throws FileNotFoundException {
        String filePath = context.getFilesDir()+ "/"+ fileName +".xkr";
        raf =  new RandomAccessFile(filePath,"rw");
        channel = raf.getChannel();
    }

    public StrRWBuffer(String filePath,String mode) throws IOException {
        raf =  new RandomAccessFile(filePath,mode);
        channel = raf.getChannel();
    }

    public StrRWBuffer(String filePath) throws IOException {
        raf =  new RandomAccessFile(filePath,"r");
        channel = raf.getChannel();
    }

    public void newMap(int position,int size) throws IOException {
        buffer = channel.map(FileChannel.MapMode.READ_WRITE, position,size);
    }

    public void putHead(String name) throws IOException {
        int len = name.getBytes().length;
        newMap(thisIndex,8 + len);
        lastIndex = thisIndex;
        buffer.putInt(0);
        buffer.putInt(len);
        buffer.put(name.getBytes());
        thisIndex += len+8;
    }

    public void putStr(String str) throws IOException {
        int len = str.getBytes().length;
        newMap(thisIndex,len);
        buffer.put(str.getBytes());
        thisIndex += len;
    }

    public void putData(String str) throws IOException {
        int len = str.getBytes().length;
        buffer.put(str.getBytes());
        thisIndex += len;
    }

    public void putLastSize() throws IOException {
        newMap(lastIndex,4);
        buffer.putInt(thisIndex);
    }

    public void putEnd() throws IOException {
        newMap(thisIndex,4);
        buffer.putInt(0);
    }
    public void newReadMap() throws IOException {
        buffer = channel.map(FileChannel.MapMode.READ_ONLY,0,(int)raf.length());
    }

    public String getStrName(){
        buffer.position(0);
        nextIndex = buffer.getInt();
        int nameSize = buffer.getInt();
        byte[] nameBuf = new byte[nameSize];
        buffer.get(nameBuf);
        return new String(nameBuf);
    }

    public ChapterListBean getChapterList(){
        ChapterListBean bean = new ChapterListBean();
        bean.listBeans = new ArrayList<>();
        buffer.position(0);
        newIndex = 0;
        nextIndex = buffer.getInt();
        int nameSize = buffer.getInt();
        byte[] nameBuf = new byte[nameSize];
        buffer.get(nameBuf);
        bean.name = new String(nameBuf);
        bean.listBeans.add(new ChapterListBean.ListBean("开始",0,nextIndex-newIndex-8));
        newIndex = nextIndex;
        buffer.position(newIndex);
        nextIndex = buffer.getInt();
        while (nextIndex != 0){
            nameSize = buffer.getInt();
            byte[] chapNameBuf = new byte[nameSize];
            buffer.get(chapNameBuf);
            String chapName = new String(chapNameBuf);
            bean.listBeans.add(new ChapterListBean.ListBean(chapName,newIndex,nextIndex-newIndex-8));
            newIndex = nextIndex;
            buffer.position(newIndex);
            nextIndex = buffer.getInt();
        }
        return bean;
    }

    public String getChapterStr(ChapterListBean.ListBean bean){
        buffer.position(bean.index+4);
        int strSize = buffer.getInt();
        buffer.position(bean.index+8+strSize);
        byte[] chapterBuf = new byte[bean.size-strSize];
        buffer.get(chapterBuf);
        return new String(chapterBuf);
    }

    public int getThisIndex() {
        return thisIndex;
    }

    public void setThisIndex(int thisIndex) {
        this.thisIndex = thisIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public int getNewIndex() {
        return newIndex;
    }

    public void setNewIndex(int newIndex) {
        this.newIndex = newIndex;
    }
}
