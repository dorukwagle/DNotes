package com.doruk.dnotes.interfaces;

import java.io.OutputStream;

import com.doruk.dnotes.exceptions.ProcessingStageException;

public interface ProcessingOutStage {
    OutputStream apply(OutputStream output) throws ProcessingStageException;
}
