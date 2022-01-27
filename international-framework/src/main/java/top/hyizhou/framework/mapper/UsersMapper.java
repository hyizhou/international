package top.hyizhou.framework.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.hyizhou.framework.entity.User;

import java.util.List;

/**
 * 用户数据表映射
 * @author hyizhou
 * @date 2021/12/31 11:12
 */
@Mapper
public interface UsersMapper {
    /**
     *  根据id查找用用户
     * @param id 唯一标识符
     * @return 隐射的实体对象
     */
    User findById(@Param("id") Integer id);

    /**
     * 根据用户名搜索用户
     * @param accountName 用户名
     * @return 搜索到的用户信息，可能为null
     */
    User findByAccountName(@Param("accountName") String accountName);

    /**
     * 进行模糊查询，若所有查询属性都为空，则会返回所有用户信息
     * @param user 作为模糊查询的属性，具体可以查询：用户名，邮件、手机号、账户名
     * @param page 页面
     * @param size 每页大小
     * @return 模糊查询的用户信息
     */
    List<User> findByVague(@Param("user") User user, @Param("page") Integer page,@Param("size") Integer size);

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

    /**
     * 更新数据
     * @param user 实体对象，需要id有值
     * @return 返回更新成功的条数，成功则为1
     */
    int update(User user);

    /**
     * 计算账户名出现次数，由于这个字段是唯一的，所有只会是0或者1
     * @param accountName 账户名
     * @return 1或者0
     */
    int countAccountName(String accountName);
}
