package com.htsc.TrainWebProject.service;


import com.htsc.TrainWebProject.model.User;
import com.htsc.TrainWebProject.result.ResultInfo;
/**
 * 说明:
 *
 * @author zhanglin/016873
 * @version: V1.0.0
 * @update 2020/9/1
 */
public interface UserService {

    public ResultInfo register(String email, String password, String token);//注册
    public ResultInfo login(String email, String password);//登录
    public ResultInfo checkData(String email);//邮箱重复性检查
    public User sinOut(String token);//退出登录
    //public User getUserInfoByUserId(Integer userId);//获取个人信息
}
