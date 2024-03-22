package src.main.java;

import java.io.*;

public class Devices {
    String KeyboardBuffer;
    String PrinterBuffer;
    String CardBuffer;

    Devices() {
        KeyboardBuffer = null;
        PrinterBuffer = null;
    }

    public void Init() {
        KeyboardBuffer = null;
        PrinterBuffer = null;
    }

    public int PopKeyboardBuffer() {
        if (KeyboardBuffer != null && KeyboardBuffer.length() > 0) {
            char val = KeyboardBuffer.charAt(0);
            KeyboardBuffer = KeyboardBuffer.substring(1);
            return (int) val;
        }
    }

    public int PopCardBuffer() {
        if (CardBuffer != null && CardBuffer.length() > 0) {
            char val = CardBuffer.charAt(0);
            CardBuffer = CardBuffer.substring(1);
            return (int) val;
        }
    }

    public void PushToPrinterBuffer(int val) {
        char c = (char) val;
        PrinterBuffer = PrinterBuffer + c;
    }

}
