package com.pic.cloudpicturebackend.manager.upload;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.pic.cloudpicturebackend.exception.BusinessException;
import com.pic.cloudpicturebackend.exception.ErrorCode;
import com.pic.cloudpicturebackend.exception.ThrowUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * URL 图片上传
 */
@Service
public class UrlPictureUpload extends PictureUploadTemplate {

    // 图片后缀类型，防止并发问题
    private final AtomicReference<String> typeRef = new AtomicReference<>();

    @Override
    protected void validPicture(Object inputSource) {
        String fileUrl = (String) inputSource;
        // 校验非空
        ThrowUtils.throwIf(StrUtil.isBlank(fileUrl), ErrorCode.PARAMS_ERROR, "文件地址为空");
        // 校验 url 的格式
        try {
            new URL(fileUrl);   // 验证是否是合法的 URL
        } catch (MalformedURLException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件地址格式错误");
        }
        // 校验 url 的协议
        ThrowUtils.throwIf(!fileUrl.startsWith("http://") && !fileUrl.startsWith("https://"),
                ErrorCode.PARAMS_ERROR, "仅支持 HTTP 或 HTTPS 协议的文件地址");
        // 发送 head 请求验证文件是否存在（try-with-resource 语句会自动释放资源, 无需在finally中手动释放）
        try (HttpResponse httpResponse = HttpUtil.createRequest(Method.HEAD, fileUrl).execute()) {
            // 未正常返回, 无需执行其他判断
            if (httpResponse.getStatus() != HttpStatus.HTTP_OK) {
                return;
            }
            // 文件存在, 文件类型校验
            String contentType = httpResponse.header("Content-Type");
            // 不为空, 才校验是否合法
            if (StrUtil.isNotBlank(contentType)) {
                // 在校验图片类型的时候先将它的类型保存
                // 允许的图片类型
                boolean isPermit = false;
                final List<String> ALLOW_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp");
                for (String imgType : ALLOW_CONTENT_TYPES) {
                    if (imgType.equals(contentType.toLowerCase())) {
                        typeRef.set("." + imgType.substring(imgType.indexOf("/") + 1));
                        isPermit = true;
                        break;
                    }
                }
                ThrowUtils.throwIf(!isPermit, ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
            // 文件存在, 文件大小检验
            String contentLengthStr = httpResponse.header("Content-Length");
            if (StrUtil.isNotBlank(contentLengthStr)) {
                try {
                    long contentLength = Long.parseLong(contentLengthStr);
                    final long ONE_MB = 1024 * 1024;
                    ThrowUtils.throwIf(contentLength > 2 * ONE_MB, ErrorCode.PARAMS_ERROR, "上传图片大小不能超过 2MB");
                } catch (NumberFormatException e) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小格式异常");
                }
            }
        }
    }

    @Override
    protected String getOriginFilename(Object inputSource) {
        String fileUrl = (String) inputSource;
        String suffix = FileUtil.getSuffix(fileUrl);
        final List<String> ALLOW_SUFFIX = Arrays.asList("jpg", "jpeg", "png", "webp");
        if (!ALLOW_SUFFIX.contains(suffix)) {
            fileUrl = fileUrl + typeRef.get();
        }
        return FileUtil.getName(fileUrl);
    }

    @Override
    protected void processFile(Object inputSource, File file) throws Exception {
        String fileUrl = (String) inputSource;
        // 下载文件到临时目录
        HttpUtil.downloadFile(fileUrl, file);
    }
}
