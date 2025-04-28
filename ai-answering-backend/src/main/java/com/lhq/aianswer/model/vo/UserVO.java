package com.lhq.aianswer.model.vo;

import com.lhq.aianswer.model.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 用户（脱敏）
 * @author lhq
 * @version 1.0
 * @date 2025/4/25 下午12:14
 */
@Data
public class UserVO implements Serializable {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 用户账号
     */
    private String userAccount;
    
    
    /**
     * 用户昵称
     */
    private String userName;
    
    /**
     * 用户头像
     */
    private String userAvatar;
    
    /**
     * 用户简介
     */
    private String userProfile;
    
    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;
    
    
    private  final Long serialVersionUID = 1L;
    
    /**
     * 封装类转对象
     * @param userVO
     * @return
     */
    public static User voToObj(UserVO userVO) {
        if (userVO == null) {
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        return user;
    }
    
    /**
     * 对象转封装类
     * @param user
     * @return
     */
    public static UserVO objToVo(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
