package com.seven.Blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created By Seven.wk
 * Description: 系统响应码枚举
 * Created At 2018/08/06
 */
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {
    ERROR(0, "error"),
    SUCCESS(10, "success"),
    LOGIN_SUCCESS(11, "登录成功"),
    LOGIN_FAILED(12, "账号或密码不正确"),
    VERIFICATION_CODE_ERROR(13, "验证码错误"),
    FILE_UPLOAD_SUCCESS(14, "文件上传成功"),
    FILE_UPLOAD_FAILED(15, "文件上传失败"),
    FILE_CANNOT_BE_EMPTY(16, "文件不能为空"),
    PERMISSION_DENIED(17, "没有权限执行此操作"),
    CONN_FTP_FAIL(18, "连接FTP服务器失败"),
    PAGE_NOT_FOUND(404, "页面不存在"),
    ;

    Integer code;
    String msg;
}
