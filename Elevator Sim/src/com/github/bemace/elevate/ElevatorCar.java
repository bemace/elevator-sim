package com.github.bemace.elevate;

import java.awt.Toolkit;
import java.math.BigDecimal;

public class ElevatorCar {
	private Building building;
	private BigDecimal elevation;

	private BigDecimal width = new BigDecimal(8);
	private BigDecimal height = new BigDecimal(10);

	private BigDecimal doorWidth = new BigDecimal(5);
	private BigDecimal doorHeight = new BigDecimal("6.5");

	public ElevatorCar(Building building) {
		this.building = building;
		elevation = building.getElevation(building.getMinFloorIndex());
	}

	public Building getBuilding() {
		return building;
	}

	public void ringBell() {
		Toolkit.getDefaultToolkit().beep();
	}

	/**
	 * Gets the elevation of the floor of the elevator car (in feet).
	 * 
	 * @return
	 */
	public BigDecimal getElevation() {
		return elevation;
	}

	public boolean isMoving() {
		throw new UnsupportedOperationException();
	}

	public BigDecimal getWidth() {
		return width;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public BigDecimal getDoorWidth() {
		return doorWidth;
	}

	public BigDecimal getDoorHeight() {
		return doorHeight;
	}
}
