package org.usfirst.frc.team1089.vision;

import org.opencv.core.Mat;

/**
 * Interface that backs all processing pipelines.
 */
public interface Pipeline<I, O> {

    /**
     * Input method for all vision pipelines
     *
     * @param source the input to process
     */
    public void process(I source);

    /**
     * Output method for all vision pipelines
     *
     * @param storage the variable storing the output
     */
    public void output(O storage);
}
