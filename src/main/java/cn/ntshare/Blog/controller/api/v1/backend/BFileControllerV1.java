package cn.ntshare.Blog.controller.api.v1.backend;

import cn.ntshare.Blog.enums.ResponseCodeEnum;
import cn.ntshare.Blog.service.FileService;
import cn.ntshare.Blog.util.FileUtil;
import cn.ntshare.Blog.vo.ServerResponse;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created By Seven.wk
 * Description: 文件管理
 * Created At 2018/11/26
 */
@RequestMapping("/backend")
@RestController
public class BFileControllerV1 {

    @Autowired
    private FileService fileService;

    /**
     * 上传图片
     * @param file
     * @return
     */
    @PostMapping("/uploadImg")
    public ServerResponse upload(@RequestParam("file") MultipartFile file) {
        String imgPath = fileService.uploadImg(file);
        return ServerResponse.success(imgPath);
    }

    /**
     * Editor编辑器上传图片
     * @param file
     * @return
     */
    @PostMapping("/edUploadImg")
    public String editorUpload(@RequestParam(value = "editormd-image-file") MultipartFile file) {
        JSONObject jsonObject = new JSONObject();
        if (!file.isEmpty()) {
            try {
                String imgPath = FileUtil.uploadImg(file);
                jsonObject.put("success", 1);
                jsonObject.put("msg", ResponseCodeEnum.FILE_UPLOAD_SUCCESS.getMsg());
                jsonObject.put("url", imgPath);
            } catch (Exception e) {
                jsonObject.put("success", 0);
                jsonObject.put("msg", e.getMessage());
                jsonObject.put("url", "");
            }
        } else {
            jsonObject.put("success", 0);
            jsonObject.put("msg", ResponseCodeEnum.FILE_CANNOT_BE_EMPTY.getMsg());
            jsonObject.put("url", "");
        }
        return jsonObject.toString();
    }
}