package com.pic.cloudpicturebackend.api.imagesearch;

import com.pic.cloudpicturebackend.api.imagesearch.model.ImageSearchResult;
import com.pic.cloudpicturebackend.api.imagesearch.sub.GetImageFirstUrlApi;
import com.pic.cloudpicturebackend.api.imagesearch.sub.GetImageListApi;
import com.pic.cloudpicturebackend.api.imagesearch.sub.GetImagePageUrlApi;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ImageSearchApiFacade {


    /**
     * 搜索图片
     *
     * @param imageUrl 图片链接
     * @return 图片搜索结果
     */
    public static List<ImageSearchResult> searchImage(String imageUrl) {
        String imagePageUrl = GetImagePageUrlApi.getImagePageUrl(imageUrl);
        String imageFirstUrl = GetImageFirstUrlApi.getImageFirstUrl(imagePageUrl);
        List<ImageSearchResult> imageList = GetImageListApi.getImageList(imageFirstUrl);
        return imageList;
    }

    public static void main(String[] args) {
        List<ImageSearchResult> imageSearchResults = searchImage("http://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960");
        System.out.println(imageSearchResults);
    }
}
