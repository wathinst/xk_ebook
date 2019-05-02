package com.wxz.ebook.mvvm.modle;

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
 *   日期：    2019年04月06日
 *   描述：    
 *   *********************************************************************************/
public class JoinModle {
    private String name;

    public JoinModle(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
