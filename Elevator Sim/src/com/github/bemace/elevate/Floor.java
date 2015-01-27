package com.github.bemace.elevate;

import java.math.BigDecimal;

public interface Floor extends Comparable<Floor> {
	String getId();

	int getIndex();

	/**
	 * Gets the building which this floor is part of.
	 * 
	 * @return
	 */
	Building getBuilding();

	BigDecimal getElevation();

	BigDecimal getHeight();

	boolean isLowest();

	boolean isHighest();
}
