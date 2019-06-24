package com.lwh.mr.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lwh
 * @date 2019/6/24 20:06
 */
public abstract class Citizen {
    List pols;
    String help="normal";

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    abstract void sendMessage(String help);

    public void setPoliceman(){
        this.pols=new ArrayList<Policeman>();
    }

    public void register(Policeman policeman){
        this.pols.add(policeman);
    }

    public void unRegister(Policeman policeman){
        this.pols.remove(policeman);
    }
}
