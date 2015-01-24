package com.github.bemace.elevate;

import java.math.BigDecimal;

/**
 * Provides access to key building specs for modeling elevator control.
 * 
 * @author Brad
 */
public interface BuildingModel {

	/**
	 * Gets the index of the lowest floor in the building, which may be
	 * negative.
	 * 
	 * @return
	 */
	int getMinFloorIndex();

	/**
	 * Gets the index of the highest floor in the building, which may be
	 * negative.
	 * 
	 * @return
	 */
	int getMaxFloorIndex();

	String getFloorId(int floorIndex);

	/**
	 * Gets elevation (in feet) of the walking surface of the floor with the
	 * given index.
	 * 
	 * @param floorIndex
	 * @return
	 */
	BigDecimal getFloorElevation(int floorIndex);

	BigDecimal getFloorHeight(int floor);

	BigDecimal getHeight();

	/**
	 * Gets the building's lowest elevation; think of it as the base of the
	 * building's foundation.
	 * 
	 * @return
	 */
	BigDecimal getBaseElevation();
}
