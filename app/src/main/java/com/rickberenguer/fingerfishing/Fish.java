package com.rickberenguer.fingerfishing;

import java.lang.reflect.Type;
import java.util.Random;

public class Fish{
    int Size;
    String name;


    public void CreateFish(){
        int type = TypeOfFish();
        switch(type){
            case 0:
                this.name = "Bass";
                break;

            case 1:
                this.name = "Trout";
                break;

            case 2:
                this.name = "Carp";
                break;

            case 3:
                this.name = "Perch";
                break;

            case 4:
                this.name = "Salmon";
                break;

            case 5:
                this.name = "Halibut";
                break;

            case 6:
                this.name = "Tilapia";
                break;

            case 7:
                this.name = "Harring";
                break;

            default:
                break;
        }
    }

    public int TypeOfFish(){
        Random num = new Random();
        int fishType;
        fishType = num.nextInt(8);

        return fishType;
    }

    public String NameOfFish(){
        return this.name;
    }
}
