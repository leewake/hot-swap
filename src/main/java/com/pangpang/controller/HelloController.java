package com.pangpang.controller;

import com.pangpang.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.RemoteException;

/**
 * Created by leewake on 2017/8/22 0022.
 */

@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping("/hello")
    public String hello(String name) {
        try {
            String tmp = helloService.sayHello("pangpang");
            System.out.println(tmp);
            return tmp;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "";
    }

}
