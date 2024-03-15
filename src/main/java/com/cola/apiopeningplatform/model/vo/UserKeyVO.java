package com.cola.apiopeningplatform.model.vo;

import com.cola.apiopeningplatform.model.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 用户许可证，密钥视图
 *
 * @author Maobohe
 * @createData 2024/3/13 16:59
 */
@Data
public class UserKeyVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 许可证
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;

    private static final long serialVersionUID = 1L;

    /**
     * VO包装类转实体对象
     * @param userKeyVO
     * @return
     */
    public static User voToObj(UserKeyVO userKeyVO) {
        if (userKeyVO == null) {
            return  null;
        }
        User user = new User();
        BeanUtils.copyProperties(userKeyVO, user);
        return user;
    }

    /**
     * 实体对象转 VO包装类
     * @param user
     * @return
     */
    public static UserKeyVO objToVo(User user) {
        if (user == null) {
            return null;
        }
        UserKeyVO userKeyVO = new UserKeyVO();
        BeanUtils.copyProperties(user, userKeyVO);
        return userKeyVO;
    }
}


