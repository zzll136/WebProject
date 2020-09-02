package com.htsc.TrainWebProject.controller;

import com.alibaba.fastjson.JSONObject;
import com.htsc.TrainWebProject.Dao.UserDao;
import com.htsc.TrainWebProject.model.HostHolder;
import com.htsc.TrainWebProject.model.User;
import com.htsc.TrainWebProject.result.ResultInfo;
import com.htsc.TrainWebProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
/**
 * 说明:
 *
 * @author zhanglin/016873
 * @version: V1.0.0
 * @update 2020/9/1
 */
@RestController
@RequestMapping(value="/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;

    /**
     * 手机号重复性检查
     * 数据库中已有该用户则返回true，否则返会false
     * @param jsonObject
     * @return
     */
    @RequestMapping(value="/check",method = RequestMethod.POST,
            produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultInfo checkData(@RequestBody JSONObject jsonObject){
        ResultInfo result=null;
        String phoneNumber=jsonObject.getString("phoneNumber");
        try {
            result = userService.checkData(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            result=ResultInfo.build(500, "检查时服务器发生错误！");
        }
        return result;
    }

    //手机号注册
    @RequestMapping(value="/registerPhone",method = RequestMethod.POST)
    public ResultInfo register(@RequestBody JSONObject jsonObject,HttpServletResponse response){
        ResultInfo result=null;
        try {
            String phoneNumber=jsonObject.getString("phoneNumber");
            String password=jsonObject.getString("password");

            //没有加是否需要记住登陆状态判断，直接给定一个有效期了
            String token= UUID.randomUUID().toString();
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            cookie.setMaxAge(3600 * 24 * 5);//5天的有效期
            response.addCookie(cookie);
            result= userService.register(phoneNumber,password,token);
        }catch (Exception e){
            e.printStackTrace();
        }
       return result;
    }

    //登录
    @RequestMapping(value="/login",method = RequestMethod.POST)
    public ResultInfo login(@RequestBody JSONObject jsonObject,HttpServletResponse response){
        ResultInfo result=null;
        try {
            String password=jsonObject.getString("password");
            String phoneNumber=jsonObject.getString("phoneNumber");
            Boolean rememberme=jsonObject.getBoolean("rememberme");
            result=userService.login(phoneNumber, password);
            if(result.getStatus()==200){
                User user=userDao.selectByPhoneNumber(phoneNumber);
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                userDao.updateToken(user);
                Cookie cookie=new Cookie("token",token);
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600*24*5);//有效期5天3600*24*5
                }
                response.addCookie(cookie);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //登出
    @RequestMapping(value="/logout",method = RequestMethod.POST)
    public ResultInfo logout (@RequestBody JSONObject jsonObject,HttpServletResponse response){
        ResultInfo resultInfo = null;
        try {
            String token = UUID.randomUUID().toString();
            User user = hostHolder.getUser();
            if(user!=null) {
                user.setToken(token);
                resultInfo = ResultInfo.ok();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return  resultInfo;
    }

}

