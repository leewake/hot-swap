package com.pangpang.service.impl;

import com.pangpang.service.HelloService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by leewake on 2017/8/7 0007.
 */

@Service
public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {


    public HelloServiceImpl() throws RemoteException {
    }

    @Override
    public String sayHello(String name) throws RemoteException {
        return String.format("Hello %s", name);
    }

}
