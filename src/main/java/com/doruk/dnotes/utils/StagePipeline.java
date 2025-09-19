package com.doruk.dnotes.utils;

import java.io.InputStream;
import java.util.List;

import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.ProcessingStage;

public class StagePipeline {
    private final List<ProcessingStage> stages;

    public StagePipeline(ProcessingStage... stages) {
        this.stages = List.of(stages);
    }

    public InputStream build(InputStream input) throws ProcessingStageException {
        InputStream result = input;
        
        for (ProcessingStage stage : stages) 
            result = stage.apply(result);
        
        return result;
    }
}
