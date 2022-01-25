package top.hyizhou.framework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hyizhou.framework.entity.User;
import top.hyizhou.framework.mapper.UsersMapper;

import java.util.List;

/**
 * 管理员服务
 * @author hyizhou
 * @date 2022/1/24 17:25
 */
@Service
public class AdministratorService {
    @Autowired
    private UsersMapper mapper;

    /**
     * 返回所有用户，可分页
     * @param page 页面索引
     * @param size 每页显示条数
     */
    public List<User> allUser(int page, int size){
        return mapper.findByVague(new User(), page, size);
    }

    /**
     * 返回模糊查询的用户
     * @param user 用来模糊查询用户的信息，只能使用用户名、邮件、手机、账户名进行查询
     * @param size 每页显示条数
     * @param page 页面索引
     */
    public List<User> searchUser(User user, int page, int size){
        return mapper.findByVague(user, page, size);
    }

    /**
     * 修改用户信息，支持修改为管理员或将管理员降级为普通用户
     * TODO 暂定能直接修改密码，后面密码要通过加密存入数据库
     * @param user 通过id进行索引，并更新其中属性
     */
    public void chmodUser(User user){
        Integer id = user.getId();
        if(id == null){
            throw new RuntimeException("administrator修改用户信息时id为空");
        }
        // 禁止修改的参数
        user.setDelete(null);
        user.setCreateTime(null);
        mapper.update(user);
    }

    /**
     * 删除用户，用户指所有用户。通过将属性isDelete标记为true实现删除
     * @param user 获取id作为索引
     */
    public void deleteUser(User user){
        Integer id = user.getId();
        if (id == null){
            throw new RuntimeException("administrator删除用户时id为空");
        }
        User deleteUser = new User();
        deleteUser.setId(id);
        deleteUser.setDelete(true);
        mapper.update(deleteUser);
    }

    /**
     * 恢复删除的用户。将属性isDelete标记成false表示恢复
     * @param user 获取id作为索引
     */
    public void receiveUser(User user){
        Integer id = user.getId();
        if (id == null){
            throw new RuntimeException("administrator恢复用户未提供id值");
        }
        User receiveUser = new User();
        receiveUser.setId(id);
        receiveUser.setDelete(false);
        mapper.update(receiveUser);
    }


}
