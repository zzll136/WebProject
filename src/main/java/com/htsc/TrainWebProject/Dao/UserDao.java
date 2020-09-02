package com.htsc.TrainWebProject.Dao;
import com.htsc.TrainWebProject.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * 说明:
 *
 * @author zhanglin/016873
 * @version: V1.0.0
 * @update 2020/9/1
 */
@Component
@Mapper
public interface UserDao {
    String TABLE_NAME=" user ";
    String INSERT_FIELDS="  phoneNumber, password, salt, token";
    String SELECT_FIELDS="id, " + INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS, ") values(#{phoneNumber},#{password},#{salt},#{token})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where phoneNumber=#{phoneNumber}"})
    User selectByPhoneNumber(String phoneNumber);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where token=#{token}"})
    User selectByToken(String token);

    @Update({"update ",TABLE_NAME, " set token=#{token} where id=#{id}"})
    void updateToken(User user);
}
