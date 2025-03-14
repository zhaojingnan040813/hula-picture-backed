package com.hula.api.imagesearch;

import com.hula.api.imagesearch.model.ImageSearchResult;
import com.hula.api.imagesearch.sub.GetImageFirstUrlApi;
import com.hula.api.imagesearch.sub.GetImageListApi;
import com.hula.api.imagesearch.sub.GetImagePageUrlApi;

import java.util.List;

public class ImageSearchApiFacade {

    /**
     * 搜索图片
     * @param imageUrl
     * @return
     */
    /**
     * 搜索图片
     * @param imageUrl
     * @return
     */
    public static List<ImageSearchResult> searchImage(String imageUrl) {
        String imagePageUrl = GetImagePageUrlApi.getImagePageUrl(imageUrl);
        String imageFirstUrl = GetImageFirstUrlApi.getImageFirstUrl(imagePageUrl);
        List<ImageSearchResult> imageList = GetImageListApi.getImageList(imageFirstUrl);
        return imageList;
    }

    public static void main(String[] args) {
//        List<ImageSearchResult> imageList = searchImage("https://www.codefather.cn/logo.png");
        List<ImageSearchResult> imageList = searchImage("https://picture-1326939213.cos.ap-beijing.myqcloud.com/space/1900526672217038849/2025-03-14_WCP4d3VtVnbnzpnC.webp");
        System.out.println("结果列表" + imageList);
    }
}
