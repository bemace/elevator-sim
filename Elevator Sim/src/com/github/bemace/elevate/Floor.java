package com.github.bemace.elevate;

import java.math.BigDecimal;

public interface Floor extends Comparable<Floor> {
	public String getId();

	/**
	 * Gets the building which this floor is part of.
	 * 
	 * @return
	 */
	Building getBuilding();

	BigDecimal getElevation();

	BigDecimal getHeight();
}
