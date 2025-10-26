package com.pic.cloudpicturebackend.manager.upload;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.pic.cloudpicturebackend.config.CosClientConfig;
import com.pic.cloudpicturebackend.exception.BusinessException;
import com.pic.cloudpicturebackend.exception.ErrorCode;
import com.pic.cloudpicturebackend.manager.CosManager;
import com.pic.cloudpicturebackend.model.dto.file.UploadPictureResult;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.CIObject;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.qcloud.cos.model.ciModel.persistence.ProcessResults;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 图片上传模板
 */
@Slf4j
public abstract class PictureUploadTemplate {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private CosManager cosManager;

    /**
     * 上传图片
     *
     * @param inputSource     文件
     * @param uploadPathPrefix  上传路径前缀
     * @return
     */
    public UploadPictureResult uploadPicture(Object inputSource, String uploadPathPrefix) {
        // 校验图片
        validPicture(inputSource);
        // 图片上传地址
        String uuid = RandomUtil.randomString(8);
        String originalFilename = getOriginFilename(inputSource);
        // 拼接文件上传路径，不使用原始文件名称，增强安全性
        String uploadFileName = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid,
                FileUtil.getSuffix(originalFilename));
        if (uploadFileName.contains("?")) {
            uploadFileName = uploadFileName.split("\\?")[0];
        }
        final String projectName = "cloud-picture";
        String uploadPath = String.format(projectName + "/%s/%s", uploadPathPrefix, uploadFileName);
        File file = null;
        try {
            // 创建临时文件
            file = File.createTempFile(uploadPath, null);
            // 处理文件来源
            processFile(inputSource, file);
            // 上传图片到对象存储
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            // 获取图片信息对象, 封装返回结果
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            // 获取到图片处理结果
            ProcessResults processResults = putObjectResult.getCiUploadResult().getProcessResults();
            List<CIObject> objectList = processResults.getObjectList();
            if (CollUtil.isNotEmpty(objectList)) {
                // 获取压缩之后得到的文件信息
                CIObject compressedCiObject = objectList.get(0);
                // 缩略图默认为压缩图
                CIObject thumbnailCiObject = compressedCiObject;
                // 有生成缩略图，才获取
                if (objectList.size() > 1) {
                    thumbnailCiObject = objectList.get(1);
                }
                // 删除原始图片
                cosManager.deleteObject(uploadPath);
                // 封装压缩图的返回结果
                return buildResult(originalFilename, compressedCiObject, thumbnailCiObject, imageInfo);
            }
            return buildResult(originalFilename, uploadPath, file, imageInfo);
        } catch (Exception e) {
            log.error("图片上传到对象存储失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            // 清理临时文件
            deleteTempFile(file);
        }
    }

    /**
     * 校验输入源（本地文件或 url）
     */
    protected abstract void validPicture(Object inputSource);

    /**
     * 获取输入源的原始文件名
     */
    protected abstract String getOriginFilename(Object inputSource);


    /**
     * 处理输入源并生成本地临时文件
     */
    protected abstract void processFile(Object inputSource, File file) throws Exception;

    /**
     * 封装返回结果
     *
     * @param originalFilename  原始文件名
     * @param compressedObject  压缩后的对象
     * @param thumbnailCiObject 缩略图对象
     * @param imageInfo         图片信息
     * @return
     */
    private UploadPictureResult buildResult(String originalFilename, CIObject compressedObject,
                                            CIObject thumbnailCiObject, ImageInfo imageInfo) {
        // 封装返回结果
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        String picFormat = compressedObject.getFormat();
        int picWidth = compressedObject.getWidth();
        int picHeight = compressedObject.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        // 设置压缩后的原图地址
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + compressedObject.getKey());
        uploadPictureResult.setPicName(FileUtil.mainName(originalFilename));
        uploadPictureResult.setPicSize(compressedObject.getSize().longValue());
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(picFormat);
        uploadPictureResult.setPicColor(imageInfo.getAve());
        // 设置缩略图地址
        uploadPictureResult.setThumbnailUrl(cosClientConfig.getHost() + "/" + thumbnailCiObject.getKey());
        // 返回
        return uploadPictureResult;
    }

    /**
     * 封装返回结果
     *
     * @param originalFilename
     * @param uploadPath
     * @param file
     * @param imageInfo 对象存储返回的图片信息
     * @return
     */
    private UploadPictureResult buildResult(String originalFilename, String uploadPath, File file, ImageInfo imageInfo) {
        // 封装返回结果
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        String picFormat = imageInfo.getFormat();
        int picWidth = imageInfo.getWidth();
        int picHeight = imageInfo.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath);
        uploadPictureResult.setPicName(FileUtil.mainName(originalFilename));
        uploadPictureResult.setPicSize(FileUtil.size(file));
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(picFormat);
        uploadPictureResult.setPicColor(imageInfo.getAve());
        // 返回
        return uploadPictureResult;
    }

    /**
     * 清理临时文件
     *
     * @param file 临时文件
     */
    public static void deleteTempFile(File file) {
        if (file == null) {
            return;
        }
        boolean deleteResult = file.delete();
        if (!deleteResult) {
            log.error("删除临时文件失败, filepath = {}", file.getAbsolutePath());
        }
    }
}
