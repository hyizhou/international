package top.hyizhou.framework.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 基础配置属性
 * @author hyizhou
 * @date 2022/2/16 11:13
 */
@Component
@ConfigurationProperties("international")
public class BaseProperty {
    private final OnlineDiskProperty onlineDisk = new OnlineDiskProperty();

    public OnlineDiskProperty getOnlineDisk() {
        return onlineDisk;
    }

    /**
     * 云盘配置参数
     */
    public static class OnlineDiskProperty{
        /**
         * 云盘在硬盘上的存储路径，即存储仓库
         */
        private String warehouse;
        /**
         * 存储类型，有可能存在文件服务器也可能在本地硬盘
         */
        private String storageType;

        public String getWarehouse() {
            return warehouse;
        }

        public void setWarehouse(String warehouse) {
            this.warehouse = warehouse;
        }

        public String getStorageType() {
            return storageType;
        }

        public void setStorageType(String storageType) {
            this.storageType = storageType;
        }
    }
}
