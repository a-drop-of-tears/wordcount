package com.lwh.mr.observer;

/**
 * @author lwh
 * @date 2019/6/24 20:16
 */
public class TianHeCitizen extends Citizen {

    public TianHeCitizen(Policeman policeman) {
        setPoliceman();
        register(policeman);
    }

    @Override
    void sendMessage(String help) {
        setHelp(help);
        for (int i = 0; i < pols.size(); i++) {
            Policeman policeman=(Policeman) pols.get(i);
            policeman.action(this);
        }
    }
}
