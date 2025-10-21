package com.pic.cloudpicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pic.cloudpicturebackend.model.dto.picture.*;
import com.pic.cloudpicturebackend.model.entity.Picture;
import com.pic.cloudpicturebackend.model.entity.User;
import com.pic.cloudpicturebackend.model.vo.PictureVO;

import javax.servlet.http.HttpServletRequest;

/**
* @description 针对表【picture(图片)】的数据库操作Service
*/
public interface PictureService extends IService<Picture> {

    /**
     * 校验图片
     *
     * @param picture
     */
    void validPicture(Picture picture);

    /**
     * 上传图片
     *
     * @param inputSource 文件输入源
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    PictureVO uploadPicture(Object inputSource, PictureUploadRequest pictureUploadRequest, User loginUser);

    /**
     * 获取图片封装类（单条）
     *
     * @param picture
     * @param request
     * @return
     */
    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    /**
     * 获取图片封装类（分页）
     *
     * @param picturePage
     * @return
     */
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage);

    /**
     * 获取查询对象
     *
     * @param pictureQueryRequest
     * @return
     */
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    /**
     * 图片审核
     *
     * @param pictureReviewRequest
     * @param loginUser
     */
    void doPictureReview(PictureReviewRequest pictureReviewRequest, User loginUser);

    /**
     * 填充审核参数
     *
     * @param picture
     * @param loginUser
     */
    void fillReviewParams(Picture picture, User loginUser);

    /**
     * 批量抓取并创建图片
     *
     * @param pictureUploadByBatchRequest
     * @param loginUser
     * @return
     */
    Integer uploadPictureByBatch(PictureUploadByBatchRequest pictureUploadByBatchRequest, User loginUser);

    /**
     * 分页获取图片封装类（无缓存）
     *
     * @param pictureQueryRequest
     * @return
     */
    Page<PictureVO> listPictureVOByPage(PictureQueryRequest pictureQueryRequest);

    /**
     * 分页获取图片封装类（Redis + Caffeine 多级缓存）
     *
     * @param pictureQueryRequest
     * @return
     */
    Page<PictureVO> listPictureVOByPageWithCache(PictureQueryRequest pictureQueryRequest);

    /**
     * 清理图片文件
     *
     * @param oldpicture
     */
    void clearPictureFile(Picture oldpicture);

    /**
     * 编辑图片
     *
     * @param pictureEditRequest
     * @param loginUser
     */
    void editPicture(PictureEditRequest pictureEditRequest, User loginUser);

    /**
     * 删除图片
     *
     * @param pictureId
     * @param loginUser
     */
    void deletePicture(long pictureId, User loginUser);

    /**
     * 校验空间图片的权限
     *
     * @param loginUser
     * @param picture
     */
    void checkPictureAuth(User loginUser, Picture picture);
}
