package com.wxz.ebook.tool;

import android.content.Context;
import android.util.Log;

import com.wxz.ebook.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadTXTFile {
    private Context context;


    public ReadTXTFile(Context context) {
        this.context=context;
    }

    public String readTxt(String path) throws Exception {
        DateUnit dateUnit =new DateUnit();
        String filePath = context.getFilesDir()+ "/"+dateUnit.getRandomFileName()+".xkr";
        StrRWBuffer buffer = new StrRWBuffer(filePath);
        int dot=path.lastIndexOf("/");
        String name=path.substring(dot+1);
        int dot1=name.lastIndexOf(".");
        name = name.substring(0,dot1);
        buffer.putHead(name);
        Log.e("name",name);
        Log.e("start","start");
        StringUnit unit = new StringUnit();
        int chapNum=0;
        try {
            Pattern pattern = Pattern.compile(context.getString(R.string.chap_pattern));
            String code = resolveCode(path);
            File file = new File(path);
            InputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is, code);
            BufferedReader br = new BufferedReader(isr);
            String str = "";
            int line=1;
            while (null != (str = br.readLine())) {
                line++;
                Matcher matcher = pattern.matcher(str);
                if(matcher.find()) {
                    /*for(int i = 0; i <= matcher.groupCount(); i++) {
                        Log.e("id","group"+ i + " : " + matcher.start(i) + " - " + matcher.end(i));
                        Log.e("con",matcher.group(i));
                    }*/
                    if(line > 1){
                        if (str.length()<30){
                            chapNum++;
                            buffer.putLastSize();
                            buffer.putHead(matcher.group(0));
                        }
                    }
                    line = 0;
                }else {
                    if(!(str.isEmpty()||str.equals(""))){
                        str = unit.trim(str)+"\n";
                        buffer.putStr(str);
                    }
                }
            }
            buffer.putLastSize();
            buffer.putEnd();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("读取文件:" + path + "失败!");
        }
        Log.e("num", String.valueOf(chapNum));
        Log.e("end","end");
        return filePath;
    }
    private String resolveCode(String path) throws Exception {
        File file = new File(path);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        long length = raf.length();
        FileChannel channel = raf.getChannel();
        MappedByteBuffer out = channel.map(FileChannel.MapMode.READ_WRITE, 0, length);
        byte[] head = new byte[4];
        for (int i = 0;i<length;i++){
            byte b = out.get();
            if(b < 0){
                out.position(out.position()-1);
                if (i==length-1){
                    out.clear();
                }
                break;
            }
        }
        out.get(head);
        String code = "GBK";  //或GBK
        if (head[0] == -1 && head[1] == -2 )
            code = "UTF-16";
        else if (head[0] == -2 && head[1] == -1 )
            code = "Unicode";
        else if(head[0]==-17 && head[1]==-69 && head[2] ==-65)
            code = "UTF-8";
        else if(head[0]<=-33 && head[0]>=-64){
            if(head[1]<=-65 && (head[2]<=-9 && head[2]>=-64) && head[3]<=-65)
                code = "UTF-8";
        }else if(head[0]<=-17 && head[0]>=-32){
            if(head[1]<=-65 && head[2]<=-65)
                code = "UTF-8";
        } else if(head[0]<=-9 && head[0]>=-16){
            if(head[1]<=-65 && head[2]<=-65 && head[3]<=-65)
                code = "UTF-8";
        }
        return code;
    }
}
