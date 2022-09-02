package cn.shh.test.zk.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class ZKOperation {
    @Resource(name = "zkClient")
    private ZooKeeper zooKeeper;

    /**
     * 创建持久化节点
     *
     * @param path  路径
     * @param data  数据
     */
    public boolean createNode(String path, String data) {
        try {
            zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            return true;
        } catch (Exception e) {
            log.error("【创建持久化节点异常】{},{},{}", path, data, e);
            return false;
        }
    }

    /**
     * 修改持久化节点
     *
     * @param path  节点路径
     * @param data  数据
     */
    public boolean updateNode(String path, String data) {
        try {
            //zk的数据版本是从0开始计数的。如果客户端传入的是-1，则表示zk服务器需要基于最新的数据进行更新。如果对zk的数据节点的更新操作没有原子性要求则可以使用-1.
            //version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
            zooKeeper.setData(path, data.getBytes(), -1);
            return true;
        } catch (Exception e) {
            log.error("【修改持久化节点异常】{},{},{}", path, data, e);
            return false;
        }
    }

    /**
     * 删除持久化节点
     *
     * @param path  节点路径
     */
    public boolean deleteNode(String path) {
        try {
            //version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
            zooKeeper.delete(path, -1);
            return true;
        } catch (Exception e) {
            log.error("【删除持久化节点异常】{},{}", path, e);
            return false;
        }
    }

    /**
     * 节点是否存在
     *
     * @param path          节点路径
     * @param needWatch     是否要监听
     * @return
     */
    public Stat exists(String path, boolean needWatch) {
        try {
            return zooKeeper.exists(path, needWatch);
        } catch (Exception e) {
            log.error("【断指定节点是否存在异常】{},{}", path, e);
            return null;
        }
    }

    /**
     * 判断节点是否存在
     *
     * @param path      节点路径
     * @param watcher   监听者
     * @return
     */
    public Stat exists(String path, Watcher watcher) {
        try {
            return zooKeeper.exists(path, watcher);
        } catch (Exception e) {
            log.error("【断指定节点是否存在异常】{},{}", path, e);
            return null;
        }
    }

    /**
     * 获取节点数据
     *
     * @param path      节点路径
     * @param watcher   是否要监听
     * @return
     */
    public String getData(String path, boolean watcher) {
        try {
            Stat stat = new Stat();
            byte[] bytes = zooKeeper.getData(path, watcher, stat);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取节点数据
     *
     * @param path      节点路径
     * @param watcher   观察者
     * @return
     */
    public String getData(String path, Watcher watcher) {
        try {
            Stat stat = new Stat();
            byte[] bytes = zooKeeper.getData(path, watcher, stat);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 指定节点的 子节点
     *
     * @param path      节点路径
     * @param watcher   是否要监听
     * @return
     */
    public List<String> getChildren(String path, boolean watcher) {
        try {
            List<String> list = zooKeeper.getChildren(path, watcher);
            return list;
        }catch (KeeperException | InterruptedException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 指定节点的 子节点
     *
     * @param path      节点路径
     * @param watcher   监听者
     * @return
     */
    public List<String> getChildren(String path, Watcher watcher) {
        try {
            List<String> childrens = zooKeeper.getChildren(path, watcher);
            return childrens;
        }catch (KeeperException | InterruptedException e){
            e.printStackTrace();
            return null;
        }
    }
}
