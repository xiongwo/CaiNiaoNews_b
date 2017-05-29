package com.example.liuyuhua.cainiaonews.utils;

/**
 * 顶部轮播栏的图片地址
 * Created by liuyuhua on 2017/4/2.
 */

public class HeadGalleryImageUtils {

    private static String[] sImageUrl = new String[]{
            "https://pic.36krcnd.com/avatar/201704/23235745/233i0en2bplfpgsi.jpg",
            "https://pic.36krcnd.com/avatar/201704/25020149/2of7k9kf4p9g7fka.jpeg",
            "https://pic.36krcnd.com/avatar/201704/24150204/8b28tdwq9ze276o9.jpg",
            "https://pic.36krcnd.com/avatar/201704/25030526/spqv11biio5ohyej.jpg",
            "https://pic.36krcnd.com/avatar/201704/17070626/0sqsqys3k7xl0hxd.png",
            "https://pic.36krcnd.com/avatar/201704/24062116/6ub8dcdm2py0dort.jpg"
    };

    public static String getImageUrl(int i) {
        return sImageUrl[i];
    }

}
