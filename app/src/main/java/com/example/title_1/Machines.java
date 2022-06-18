package com.example.title_1;

import android.content.res.Resources;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Machines{

    private List<String> nowJugglerList; //★新機種追加・撤去が発生した場合はString.xmlの「R.array.NOW_JUGGLER」を修正すること
    private List<String> jugglerList; //★新機種追加が発生した場合はString.xmlの「R.array.JUGGLER」を修正すること

    // コンストラクタ
    public Machines(@NonNull Resources resources){
        this.nowJugglerList = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.NOW_JUGGLER)));
        this.jugglerList = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.JUGGLER)));
    }

    public List<String> getNowJugglerList() {
        return nowJugglerList;
    }

    public List<String> getJugglerList() {
        return jugglerList;
    }

    public String getNowMachineName(int position){
        return this.nowJugglerList.get(position);
    }

    public String getMachineName(int position){
        return this.jugglerList.get(position);
    }

    public int getNowMachineIndex(String machineName){
        return this.nowJugglerList.indexOf(machineName);
    }

    public int getMachineIndex(String machineName){
        return this.jugglerList.indexOf(machineName);
    }

}
