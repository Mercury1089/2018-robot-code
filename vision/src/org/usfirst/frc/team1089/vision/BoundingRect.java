package org.usfirst.frc.team1089.vision;

import org.opencv.core.Rect;

/**
 * Class to better handle the primitive class {@code Rect}
 */
public class BoundingRect extends Rect {
    /**
     * Copy constructor
     *
     * @param r the rect to copy from
     */
    public BoundingRect (Rect r) {
        super(r.x, r.y, r.width, r.height);
    }

    public double getCenterX() {
        return x + width / 2.0;
    }

    public double getCenterY() {
        return y + height / 2.0;
    }
}
