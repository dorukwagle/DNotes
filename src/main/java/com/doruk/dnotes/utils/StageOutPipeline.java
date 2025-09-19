package com.doruk.dnotes.utils;

import java.io.OutputStream;
import java.util.List;

import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.ProcessingOutStage;

public class StageOutPipeline {
    private final List<ProcessingOutStage> stages;

    public StageOutPipeline(ProcessingOutStage... stages) {
        this.stages = List.of(stages);
    }

    public OutputStream build(OutputStream output) throws ProcessingStageException {
        OutputStream result = output;
        
        for (ProcessingOutStage stage : stages) 
            result = stage.apply(result);
        
        return result;
    }
}
