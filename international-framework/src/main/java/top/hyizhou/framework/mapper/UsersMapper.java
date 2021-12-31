package top.hyizhou.framework.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.hyizhou.framework.entity.User;

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
    User findById(@Param("id") Integer id);

    /**
     * 插入一条数据
     * @param user 用户信息实体类
     * @return 插入成功条数，没有问题情况应该要为1
     */
    int insertOne(User user);

    /**
     * 根据id删除一条数据
     * @param id 主键id
     * @return 删除条数，应该为1或0
     */
    int deleteById(@Param("id") Integer id);

    int update(User user);
}
