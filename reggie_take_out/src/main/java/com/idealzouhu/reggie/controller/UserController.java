package com.idealzouhu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.idealzouhu.reggie.common.R;
import com.idealzouhu.reggie.entity.User;
import com.idealzouhu.reggie.service.UserService;
import com.idealzouhu.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     * @param user
     * @param httpSession
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession){
        // 获取手机号
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            // 生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code = {}",code);  // 后台输出验证码，可以用于取消短信服务后代码依然能够正常运行

            // 调用阿里云提供的短信服务API完成发送短信
            // SMSUtils.sendMessage("瑞吉外卖", "", phone,code);

            // 将生成的验证码保存到 session 里面
            httpSession.setAttribute(phone, code);

            return R.success("手机验证码发送成功");
        }

        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param httpSession
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession httpSession){
        log.info(map.toString());

        // 获取 手机号 和 验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        // 从 Session 中获取验证玛
        Object codeInsession = httpSession.getAttribute(phone);

        // 进行验证码对比，说明登录成功
        if(codeInsession != null && codeInsession.equals(code)){
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(lambdaQueryWrapper);
            // 判断当前手机号对应的用户是否为新用户，如果是新用户，就自动注册
            if(user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            httpSession.setAttribute("user", user.getId());
            return R.success(user);
        }

        return R.error("短信发送失败");
    }
}
