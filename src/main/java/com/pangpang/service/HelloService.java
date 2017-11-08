package com.pangpang.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by leewake on 2017/8/7 0007.
 */
public interface HelloService extends Remote{

    String sayHello(String name) throws RemoteException;
}
