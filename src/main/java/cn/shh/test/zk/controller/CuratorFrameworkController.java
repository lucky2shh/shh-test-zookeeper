package cn.shh.test.zk.controller;

import cn.shh.test.zk.common.config.ZKProperties;
import com.alibaba.fastjson.JSONObject;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/zk/curator")
public class CuratorFrameworkController {
    @Autowired
    private CuratorFramework curatorFramework;
    @Autowired
    private ZKProperties zkProperties;

    /**
     * 创建节点
     * 
     * @param path
     * @param data
     * @return
     */
    @PostMapping("/create")
    public String createZnode(String path, @RequestParam(defaultValue = "")String data){
        List<ACL> aclList = new ArrayList<>();
        aclList.add(new ACL(ZooDefs.Perms.ALL, new Id("world", "anyone")));
        try {
            curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .withACL(aclList)
                    .forPath(path, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return "节点创建失败"+e.getMessage();
        }
        return "节点创建成功";
    }

    /**
     * 异步递归创建节点
     *
     * @param path
     * @param data
     * @return
     */
    @PostMapping("/async/create")
    public String createAsyncZnode(String path, @RequestParam(defaultValue = "")String data){
        try {
            curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .inBackground(new BackgroundCallback() {
                        @Override
                        public void processResult(CuratorFramework curatorFramework, CuratorEvent event) throws Exception {
                            System.out.println("异步回调--获取权限:"+curatorFramework.getACL().forPath(path));
                            System.out.println("异步回调--获取数据:"+new String(curatorFramework.getData().forPath(path)));
                            System.out.println("异步回调--获取事件名称:"+event.getName());
                            System.out.println("异步回调--获取事件类型:"+event.getType());
                        }
                    })
                    .forPath(path, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return "节点创建失败"+e.getMessage();
        }
        return "节点创建成功";
    }

    /**
     * 查看节点和元数据
     *
     * @param path
     * @return
     */
    @GetMapping("/select")
    public JSONObject selectZnode(String path){
        JSONObject jsonObject = new JSONObject();
        String namespace = "/" + zkProperties.getNamespace();
        Stat stat;
        try {
            stat = curatorFramework.checkExists().forPath(path);
            if (stat == null) {
                jsonObject.put("error", "不存在该节点");
            }
            String dataString = new String(curatorFramework.getData().forPath(path));
            jsonObject.put(namespace+path, dataString);
            jsonObject.put("stat", stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 查看节点和数据
     *
     * @param path
     * @return
     */
    @GetMapping("/selectChild")
    public Map<String,String> selectChildrenZnode(String path){
        Map<String, String> map = new HashMap<>();
        String namespace = "/" + zkProperties.getNamespace();
        try {
            List<String> list = curatorFramework.getChildren().forPath(path);
            for (String s : list) {
                String dataString = new String(curatorFramework.getData().forPath(path + "/" + s));
                map.put(namespace + path + "/" + s, dataString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 设置数据
     *
     * @param path
     * @param data
     * @param version
     * @return
     */
    @PutMapping("/set")
    public JSONObject setData(String path, String data, Integer version) {
        JSONObject jsonObject = new JSONObject();
        try {
            Stat stat = curatorFramework.setData().withVersion(version).forPath(path, data.getBytes());
            jsonObject.put("success", "修改成功");
            jsonObject.put("version", stat.getVersion());
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("error", "修改失败:"+e.getMessage());
            return jsonObject;
        }
        return jsonObject;
    }

    /**
     * 删除节点
     *
     * @param path
     * @param version
     * @param isRecursive
     * @return
     */
    @DeleteMapping ("/del")
    public JSONObject delete(String path, Integer version, @RequestParam(defaultValue = "0")Integer isRecursive) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (isRecursive == 1) {
                curatorFramework.delete().deletingChildrenIfNeeded().withVersion(version).forPath(path);
            }else {
                curatorFramework.delete().withVersion(version).forPath(path);
            }
            jsonObject.put("success", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("error", "删除失败:"+e.getMessage());
            return jsonObject;
        }
        return jsonObject;
    }

    /**
     * 不开启事务
     *
     * @param createPath
     * @param createData
     * @param setPath
     * @param setData
     * @return
     */
    @PostMapping("/noTransaction")
    public String transactionDisabled(String createPath,String createData,String setPath,String setData) {
        try {
            //创建一个新的路径
            curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(createPath,createData.getBytes());
            //修改一个没有的数据  让其报错
            curatorFramework.setData().forPath(setPath, setData.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return "执行完成";
        }
    }

    /**
     * 开启事务
     *
     * @param createPath
     * @param createData
     * @param setPath
     * @param setData
     * @return
     */
    @PostMapping("/transaction")
    public String transactionEnabled(String createPath,String createData,String setPath,String setData) {
        try {
            curatorFramework.inTransaction()
                    .create().withMode(CreateMode.PERSISTENT).forPath(createPath,createData.getBytes())
                    .and()
                    .create().withMode(CreateMode.PERSISTENT).forPath(createPath,createData.getBytes())
                    .and().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return "执行完成";
        }

    }

    /**
     * 可重入排他锁
     */
    @PostMapping("/mutex")
    public String lock() throws Exception{
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, "/lock");
        lock.acquire(20L, TimeUnit.SECONDS);
        for (int i = 0; i < 20; i++) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println(i);
        }
        lock.release();
        return "锁已释放";
    }

    /**
     * 读写锁-写锁
     */
    @PostMapping("/wlock")
    public String wlock() throws Exception {
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(curatorFramework, "/lock");
        lock.writeLock().acquire();
        for (int i = 0; i < 10; i++) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println(i);
        }
        lock.writeLock().release();
        return "释放写锁";
    }

    /**
     * 读写锁-读锁
     */
    @PostMapping("rlock")
    public String rlock() throws Exception {
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(curatorFramework, "/lock");
        lock.readLock().acquire();
        for (int i = 0; i < 10; i++) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println(i);
        }
        lock.readLock().release();
        return "释放读锁";
    }
}
