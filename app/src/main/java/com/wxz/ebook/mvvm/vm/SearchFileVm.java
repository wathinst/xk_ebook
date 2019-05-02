package com.wxz.ebook.mvvm.vm;

import com.wxz.ebook.bean.DocBean;
import com.wxz.ebook.mvvm.modle.SearchFileModle;
import com.wxz.ebook.tool.utils.DateUtil;
import com.wxz.ebook.tool.utils.SizeUtil;

import java.util.ArrayList;
import java.util.List;

/*********************************************************************************
 *                    The 996ICU License (996ICU)
 *                      Version 0.1, March 2019
 *
 *   PACKAGE is distributed under LICENSE with the following restriction:
 *
 *   The above license is only granted to entities that act in concordance
 *   with local labor laws. In addition, the following requirements must be
 *   observed:
 *
 *   * The licensee must not, explicitly or implicitly, request or schedule
 *     their employees to work more than 45 hours in any single week.
 *   * The licensee must not, explicitly or implicitly, request or schedule
 *     their employees to be at work consecutively for 10 hours.
 *   *********************************************************************************
 *                             类信息
 *
 *   开发者：  wxz
 *   日  期：  2019年04月11日
 *   描  述：    
 *   *********************************************************************************/
public class SearchFileVm {

    public List<SearchFileModle> getModleList(List<DocBean> docBeanModels){
        if (docBeanModels!=null){
            SizeUtil sizeUnit = new SizeUtil();
            DateUtil dateUtil = new DateUtil();
            List<SearchFileModle> modles = new ArrayList<>();
            for (DocBean docBean : docBeanModels){
                modles.add(new SearchFileModle(docBean.getName(),sizeUnit.getSizeStr(docBean.getSize()),dateUtil.getDateStr(docBean.getDateModified())));
            }
            return modles;
        }
        return null;
    }

}
