package cn.shh.test.zk.controller;

import cn.shh.test.zk.common.util.ZKOperation;
import cn.shh.test.zk.common.watcher.MyWatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/zk/watcher")
public class ZookeeperController {
    @Autowired
    private ZKOperation zkOperation;
    @Autowired
    private MyWatcher myWarcher;

    @PostMapping(value = "/create")
    public boolean createNode(@RequestParam String path, @RequestParam String data) {
        log.info("ZookeeperController create node {}, {}", path, data);
        return zkOperation.createNode("/" + path, data);
    }

    // 更新操作会触发监听事件
    @PutMapping(value = "/update")
    public boolean updateNode(@RequestParam String path, @RequestParam String data) {
        log.info("ZookeeperController update node {}, {}", path, data);
        return zkOperation.updateNode("/" + path, data);
    }

    @DeleteMapping(value = "/del")
    public boolean deleteNode(@RequestParam String path) {
        log.info("ZookeeperController remove node {}", path);
        return zkOperation.deleteNode("/" + path);
    }

    @GetMapping(value = "/exists")
    public Stat exists(@RequestParam String path, @RequestParam boolean needWatch) {
        log.info("ZookeeperController exists node {}", path);
        return zkOperation.exists("/" + path, needWatch);
    }
    @GetMapping(value = "/exists2")
    public Stat exists(@RequestParam String path) {
        log.info("ZookeeperController exists2 node {}", path);
        return zkOperation.exists("/" + path, myWarcher);
    }

    @GetMapping(value = "/getData")
    public String getData(@RequestParam String path){
        log.info("ZookeeperController getData node {}", path);
        return zkOperation.getData("/" + path, myWarcher);
    }

    @GetMapping(value = "/getChildren")
    public List<String> getChildren(@RequestParam String path, @RequestParam boolean needWatch)
            throws KeeperException, InterruptedException {
        log.info("ZookeeperController getChildren node {}", path);
        return zkOperation.getChildren("/" + path, needWatch);
    }
    @GetMapping(value = "/getChildren2")
    public List<String> getChildren2(@RequestParam String path) throws KeeperException, InterruptedException {
        log.info("ZookeeperController getChildren2 node {}", path);
        return zkOperation.getChildren("/" + path, myWarcher);
    }
}
