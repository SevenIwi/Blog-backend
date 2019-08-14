package cn.ntshare.Blog.service.impl;

import cn.ntshare.Blog.bo.SmsBO;
import cn.ntshare.Blog.constant.SystemConstant;
import cn.ntshare.Blog.enums.ResponseCodeEnum;
import cn.ntshare.Blog.enums.SmsType;
import cn.ntshare.Blog.exception.SystemException;
import cn.ntshare.Blog.service.CaptchaCodeService;
import cn.ntshare.Blog.service.RabbitMqService;
import cn.ntshare.Blog.service.SmsService;
import cn.ntshare.Blog.util.CookieUtil;
import cn.ntshare.Blog.util.RandomUtil;
import cn.ntshare.Blog.util.RedisUtil;
import cn.ntshare.Blog.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created By Seven.wk
 * Description: 短信服务实现
 * Created At 2019/01/09
 */
@Service
public class SmsServiceImpl implements SmsService, CaptchaCodeService {

    @Autowired
    private RabbitMqService rabbitMqService;

    @Override
    public String[] createSendToken(String account) {
        String phoneToken = RandomUtil.getUniqueKey();
        RedisUtil.setExpireTime(phoneToken, account, 10 * SystemConstant.MINUTE);
        String phone = account.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return new String[]{phone, phoneToken};
    }

    @Override
    public Boolean sendCaptcha(HttpServletResponse response, String phoneNumber, String captchaCode) {

        String key = RandomUtil.getUniqueKey();
        // 写入Redis
        RedisUtil.setExpireTime(key, captchaCode, SystemConstant.SMS_EXPIRE_TIME);
        // 写入Cookie
        CookieUtil.writeCookie(response, SystemConstant.SMS_TOKEN, key, SystemConstant.SMS_EXPIRE_TIME);

        // 发送短信
        rabbitMqService.sendSms(new SmsBO(1, phoneNumber, captchaCode));

        // 将手机号写入redis防刷
        RedisUtil.setExpireTime(phoneNumber, "1", SystemConstant.MINUTE);

        return true;
    }

    @Override
    public Boolean verifyCaptchaCode(HttpServletRequest request, String captchaCode) {
        String key = CookieUtil.readCookie(request, SystemConstant.SMS_TOKEN);
        if (key == null) {
            throw new SystemException(ResponseCodeEnum.SMS_CODE_EXPIRED);
        }

        String value = RedisUtil.get(key);
        if (value == null) {
            throw new SystemException(ResponseCodeEnum.SMS_CODE_EXPIRED);
        }

        if (!value.equals(captchaCode)) {
            throw new SystemException(ResponseCodeEnum.SMS_CODE_ERROR);
        }

        return true;
    }

    @Override
    public Boolean sendSms(Integer smsType, String phoneNumber, String content) {
        if (SmsType.captcha.getType().equals(smsType)) {
            if (!SmsUtil.sendCaptchaSms(phoneNumber, content)) {
                throw new SystemException(ResponseCodeEnum.SMS_SEND_FAILED);
            }
            return true;
        } else {
            throw new SystemException(ResponseCodeEnum.SMS_NOT_SUPPORT);
        }
    }
}
