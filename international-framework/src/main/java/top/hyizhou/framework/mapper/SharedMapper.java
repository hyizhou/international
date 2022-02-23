package top.hyizhou.framework.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.hyizhou.framework.domain.SharedPojo;

import java.util.List;

/**
 * shared表数据访问
 * @author hyizhou
 * @date 2022/1/28 15:57
 */
@Mapper
public interface SharedMapper {
    /**
     * 查询所有数据
     * @return 数据库中所有分享数据
     */
    List<SharedPojo> findAll();

    /**
     * 根据用户id查询记录
     * @param userId 用户id
     * @return 用户分享列表
     */
    List<SharedPojo> findByUserId(Integer userId);

    /**
     * 根据id查询记录
     * @param id 主键
     * @return 用户分享记录
     */
    SharedPojo findById(Integer id);

    /**
     * 根据文件路径查找，理论上是唯一的，但也可能数据异常
     * @param path
     * @return
     */
    SharedPojo findByPath(@Param("path") String path);

    /**
     * 添加一条记录
     * @param sharedPojo 实体类
     * @return 操作条数
     */
    int insert(SharedPojo sharedPojo);

    /**
     * 更新一条记录
     * @param sharedPojo 实体类
     * @return 操作条数
     */
    int update(SharedPojo sharedPojo);


    /**
     * 删除一条记录
     * @param id 删除记录的id值
     * @return 删除条数
     */
    int delete(Integer id);
}
