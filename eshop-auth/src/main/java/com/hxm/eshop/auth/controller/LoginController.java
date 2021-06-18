package com.hxm.eshop.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.hxm.common.utils.R;
import com.hxm.common.vo.MemberResponseVo;
import com.hxm.eshop.auth.feign.MemberFeignService;
import com.hxm.eshop.auth.vo.UserLoginVo;
import com.hxm.eshop.auth.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    public static final String LOGIN_USER = "loginUser";

    @Autowired
    private MemberFeignService memberFeignService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * TODO: 重定向携带数据：利用session原理，将数据放在session中。
     * TODO:只要跳转到下一个页面取出这个数据以后，session里面的数据就会删掉
     * TODO：分布下session问题
     * RedirectAttributes：重定向也可以保留数据，不会丢失
     * 用户注册
     *
     * @return
     */
    @PostMapping(value = "/register")
    public String register(@Valid UserRegisterVo vos, BindingResult result, RedirectAttributes attributes) {
        R register = memberFeignService.register(vos);
        if (register.getCode() == 0) {
            //成功
            return "redirect:http://auth.eshop.com/login.html";
        } else {
            //失败
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", register.getData("msg", new TypeReference<String>() {
            }));
            attributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.eshop.com/reg.html";
        }
    }

    @GetMapping(value = "/login.html")
    public String loginPage(HttpSession session) {

        //从session先取出来用户的信息，判断用户是否已经登录过了
         Object attribute = session.getAttribute(LOGIN_USER);
        //如果用户没登录那就跳转到登录页面
         if (attribute == null) {
             return "login";
         } else {
             return "redirect:http://eshop.com";
         }

    }


    @PostMapping(value = "/login")
    public String login(UserLoginVo vo, RedirectAttributes attributes, HttpSession session) {

        R login = memberFeignService.login(vo);

        if (login.getCode() == 0) {
            MemberResponseVo data = login.getData("data", new TypeReference<MemberResponseVo>() {
            });
            session.setAttribute(LOGIN_USER, data);
            return "redirect:http://eshop.com";
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", login.getData("msg", new TypeReference<String>() {
            }));
            attributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.eshop.com/login.html";
        }
    }
}
