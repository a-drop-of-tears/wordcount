package com.lwh.mr.observer;

/**
 * @author lwh
 * @date 2019/6/24 20:20
 */
public class TianHePoliceman implements Policeman {

    @Override
    public void action(Citizen ci) {
        String help=ci.getHelp();
        if(help.equals("normal")){
            System.out.println("一切正常，不用出动");
        }
        if(help.equals("unnormal")){
            System.out.println("有犯罪行为，天河警察出动");
        }
    }
}
