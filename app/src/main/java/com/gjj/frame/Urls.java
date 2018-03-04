package com.gjj.frame;

/**
 * Created by gaojuanjuan on 2018/2/27.
 */

public class Urls {

    //http://v.juhe.cn/joke/img/text.php?key=您申请的KEY&page=1&pagesize=10
    //http://v.juhe.cn/joke/content/text.php?key=您申请的KEY&page=1&pagesize=10
    public static final String baseUrl = "http://v.juhe.cn/joke/";
    public static final String requestJokesUrl = "http://v.juhe.cn/joke/content/text.php?key="+Constant.DOUBIAN_KEY
            +"&page=1&pagesize=1";
}
