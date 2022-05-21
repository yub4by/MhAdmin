package com.yppah.controller;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.yppah.common.lang.Const;
import com.yppah.common.lang.Result;
import com.yppah.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;

@RestController
public class AuthController extends BaseController{

    @Autowired
    private Producer producer;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        String key = UUID.randomUUID().toString(); //32位随机数
        String code = producer.createText(); //5位随机验证码

        // 为了在postman中测试后端接口，暂时写死这两个参数
        key = "aaaaa";
        code = "11111";

        // 生成验证码图片，并转到字节数组流中
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        // 验证码图片（字节数组）转为base64字符串
        Base64Encoder encoder = new Base64Encoder();
        String prefix = "data:image/jpeg;base64,"; //前缀
        String base64Img = prefix + encoder.encode(outputStream.toByteArray());

        // 缓存到redis中，并返回结果
        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120);
        return Result.success(
                MapUtil.builder().
                        put("token", key).
                        put("myCaptchaImg", base64Img)
                        .build()
        );
    }

    /**
     * 接口-获取用户信息（Header.vue）
     * @param principal
     * @return
     */
    @GetMapping("/sys/userInfo")
    public Result userInfo(Principal principal){
        SysUser sysUser = userService.getByUserName(principal.getName());
        return Result.success(MapUtil.builder()
                .put("id", sysUser.getId())
                .put("username", sysUser.getUsername())
                .put("avatar", sysUser.getAvatar())
                .put("created", sysUser.getCreated())
                .map()
        );
    }

}
