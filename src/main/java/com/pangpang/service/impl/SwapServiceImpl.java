package com.pangpang.service.impl;

import com.pangpang.service.HelloService;
import com.pangpang.service.SwapService;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leewake on 2017/9/13 0013.
 */

@Primary
@Service
public class SwapServiceImpl implements HelloService, SwapService {

    private HotSwappableTargetSource targetSource;
    private String key;
    private Map<String, HelloService> swapBeans;

    @Autowired
    @Qualifier("haiServiceImpl")
    private HelloService haiServiceImpl;

    @Autowired
    @Qualifier("helloServiceImpl")
    private HelloService helloServiceImpl;

    @PostConstruct
    public void init(){
        swapBeans = new HashMap<>();
        swapBeans.put("haiServiceImpl", haiServiceImpl);
        swapBeans.put("helloServiceImpl", helloServiceImpl);
        key = "helloServiceImpl";
        targetSource = new HotSwappableTargetSource(swapBeans.get(key));
    }

    @Override
    public String swap(String key) {
        String old = this.key;
        if (old.equals(key)){
            return old;
        }
        if (!this.swapBeans.containsKey(key)){
            throw new RuntimeException("unknown bean: " + key);
        }
        //实现热切换
        //这样取值，并不能将初始化的bean取出来，需要从swapBeans这个map里取出来
        //this.targetSource.swap(key);
        this.targetSource.swap(swapBeans.get(key));
        this.key = key;
        return old;
    }

    @Override
    public String sayHello(String name) throws RemoteException {
        String result = ((HelloService) this.targetSource.getTarget()).sayHello(name);
        return result;
    }
}
