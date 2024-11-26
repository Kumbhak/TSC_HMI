package com.tsc;

import com.sourceforge.snap7.moka7.S7Client;
import com.tsc.plcconn.PLC_1;
import com.tsc.program.programMain;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //create the PLC as a method
        PLC_1 plc1 = new PLC_1();
        programMain programMain = new programMain();

        //Call the plc to connect
        plc1.plc1Method();
        programMain.main();
    }
}