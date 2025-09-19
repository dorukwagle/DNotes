package com.doruk.dnotes.interfaces;

import java.io.InputStream;

import com.doruk.dnotes.exceptions.ProcessingStageException;

public interface ProcessingInputStage {
    InputStream apply(InputStream input) throws ProcessingStageException;
}
