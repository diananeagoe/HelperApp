package com.example.apphelper.ui.home;

import java.util.ArrayList;
import java.util.List;

public class randomMessageTextArray {

    public List<String> messages = new ArrayList<>();
    public int messageNumber;

    public randomMessageTextArray(){
        
        /*
        Random messages for the randomMessageCardView are added HERE
        */
        messages.add("");
        messages.add("");
        messages.add("");
        
        /*
        messages.add("");
        messages.add("");

         */

        messageNumber = messages.size() - 1;

    }

}
