package com.doruk.dnotes.utils;

import java.io.InputStream;
import java.util.List;

import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.ProcessingInputStage;

public class StageInputPipeline {
    private final List<ProcessingInputStage> stages;

    public StageInputPipeline(ProcessingInputStage... stages) {
        this.stages = List.of(stages);
    }

    public InputStream build(InputStream input) throws ProcessingStageException {
        InputStream result = input;
        
        for (ProcessingInputStage stage : stages) 
            result = stage.apply(result);
        
        return result;
    }
}
