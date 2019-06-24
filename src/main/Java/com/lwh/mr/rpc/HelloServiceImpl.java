package com.lwh.mr.rpc;

/**
 * @author lwh
 * @date 2019/6/20 15:10
 */
public class HelloServiceImpl implements HelloService{
    @Override
    public String sayHi(String name) {
        return "Hi"+name;
    }
}
