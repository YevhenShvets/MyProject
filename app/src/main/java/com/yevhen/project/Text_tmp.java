package com.yevhen.project;

public class Text_tmp {
    private int i;

    public Text_tmp() {
        this.i=0;
    }
    public int get(){
        int r=0;
        switch (i){
            case 0: r= 0; break;
            case 1: r= 1; break;
            case 2: r= 2; break;
            case 3: r= 3; break;
            case 4: r= 4; break;
        }
        i++;
        return r;
    }
}
