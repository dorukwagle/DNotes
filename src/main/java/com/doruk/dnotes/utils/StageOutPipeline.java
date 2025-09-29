package com.doruk.dnotes.utils;

import java.io.OutputStream;
import java.util.List;

import com.doruk.dnotes.exceptions.ProcessingStageException;
import com.doruk.dnotes.interfaces.ProcessingOutputStage;

public class StageOutPipeline {
    private final List<ProcessingOutputStage> stages;

    public StageOutPipeline(ProcessingOutputStage... stages) {
        this.stages = List.of(stages);
    }

    public OutputStream build(OutputStream output) throws ProcessingStageException {
        OutputStream result = output;
        
        for (ProcessingOutputStage stage : stages) 
            result = stage.apply(result);
        
        return result;
    }
}
