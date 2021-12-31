package top.hyizhou.framework.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.hyizhou.framework.entity.Users;

/**
 * 用户数据表映射
 * @author hyizhou
 * @date 2021/12/31 11:12
 */
@Repository
public interface UsersMapper {
    /**
     *  根据id查找用用户
     * @param id 唯一标识符
     * @return 隐射的实体对象
     */
    Users selectById(@Param("id") Integer id);
}
