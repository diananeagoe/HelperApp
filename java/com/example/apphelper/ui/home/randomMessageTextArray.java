package com.example.apphelper.ui.home;

import java.util.ArrayList;
import java.util.List;

public class randomMessageTextArray {

    public List<String> messages = new ArrayList<>();
    public int messageNumber;

    public randomMessageTextArray(){

        messages.add("Felicitări David Popovici");
        messages.add("Manele reloaded");
        messages.add("Cu săpun te speli pe piele și pe suflet cu manele");
        messages.add("Am poftă de Fornetti");
        messages.add("Am vrăjeala necesară pentru orice domnișoară");
        messages.add("i forgor");
        /*
        messages.add("");
        messages.add("");

         */

        messageNumber = messages.size() - 1;

    }

}
