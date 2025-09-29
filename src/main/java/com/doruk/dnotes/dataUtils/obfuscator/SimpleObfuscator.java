package com.doruk.dnotes.dataUtils.obfuscator;

import java.io.OutputStream;

import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.ProcessingOutputStage;

public class SimpleObfuscator implements ProcessingOutputStage {

    public static byte obfuscate(byte input) {
        byte invert = (byte) ~input;
        byte suffix = (byte) (invert & 0b1111); // take last 4 bits
        byte prefix = (byte) ((invert >> 4) & 0b1111); // shift right by 4 bits
        // swap prefix and suffix
        return (byte) (suffix << 4 | prefix);
    }

    public static byte deObfuscate(byte input) {
        byte suffix = (byte) (input & 0b1111); // shift right by 4 bits
        byte prefix = (byte) ((input >> 4) & 0b1111); // take last 4 bits
        return (byte) ~((suffix << 4) | prefix);
    }

    @Override
    public OutputStream apply(OutputStream output) throws ProcessingStageException {
        // TODO Auto-generated method stub
        // output.write();

        return output;
    }
}
