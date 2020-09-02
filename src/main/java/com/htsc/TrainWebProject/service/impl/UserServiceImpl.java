package com.htsc.TrainWebProject.service.impl;

import com.htsc.TrainWebProject.Dao.UserDao;
import com.htsc.TrainWebProject.model.User;
import com.htsc.TrainWebProject.result.ResultInfo;
import com.htsc.TrainWebProject.service.UserService;

import com.htsc.TrainWebProject.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
import java.util.UUID;
/**
 * 说明:
 *
 * @author zhanglin/016873
 * @version: V1.0.0
 * @update 2020/9/1
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    //用户注册
    @Override
    public ResultInfo register(String phoneNumber, String password,String token) {
        if(StringUtils.isBlank(phoneNumber)|| StringUtils.isBlank(password))
            return ResultInfo.build(400, "用户数据不完整，注册失败");

        User user=userDao.selectByPhoneNumber(phoneNumber);
        if(user!=null){
            return ResultInfo.build(400,"该手机号已经被注册");
        }
        user=new User();
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));//随机生成一段盐存入数据库
        user.setPhoneNumber(phoneNumber);
        user.setPassword(Md5Util.MD5(password + user.getSalt()));//存入密码加盐后的加密密文
        user.setToken(token);
        userDao.addUser(user);

        return ResultInfo.ok(user.getId());
    }

    //登录
    @Override
    public ResultInfo login(String phoneNumber, String password) {
        User user=null;
        user=userDao.selectByPhoneNumber(phoneNumber);
        if(user==null){
            return ResultInfo.build(400,"用户未注册！");
        }
        if(!Md5Util.MD5(password+user.getSalt()).equals(user.getPassword())){
            String str = Md5Util.MD5(password+user.getSalt());
            return ResultInfo.build(400,"用户名或密码错误！");
        }

        return ResultInfo.ok();
    }

    //邮箱重复性检查
    @Override
    public ResultInfo checkData(String phoneNumber) {
        User user=null;
        user=userDao.selectByPhoneNumber(phoneNumber);
        if(user==null){
            return ResultInfo.ok(false);
        }
        return ResultInfo.ok(true);
    }

    //退出登录
    @Override
    public User sinOut(String token) {
        return null;
    }

}
