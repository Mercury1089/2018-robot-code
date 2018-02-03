package org.usfirst.frc.team1089.robot.sensors;

/**
 * Class that represents data packet being received from Pixy
 */
public class BoundingBox {
	private final int X;
	private final int Y;
	private final int WIDTH;
	private final int HEIGHT;

	public BoundingBox(int nX, int nY, int nW, int nH) {
		X = nX;
		Y = nY;
		WIDTH = nW;
		HEIGHT = nH;
	}

	public int getCenterX() {
		return WIDTH / 2 + X;
	}

	public int getArea() {
		return WIDTH * HEIGHT;
	}
}