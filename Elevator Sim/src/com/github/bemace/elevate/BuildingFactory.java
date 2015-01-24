package com.github.bemace.elevate;

import java.math.BigDecimal;

public class BuildingFactory {

	public static Building create(final int floors, final BigDecimal floorHeight) {

		BuildingModel model = new BuildingModel() {

			@Override
			public int getMinFloorIndex() {
				return 1;
			}

			@Override
			public int getMaxFloorIndex() {
				return floors;
			}

			@Override
			public String getFloorId(int floorIndex) {
				return floorIndex + "";
			}

			@Override
			public BigDecimal getFloorElevation(int floorIndex) {
				return floorHeight.add(BigDecimal.ONE).multiply(new BigDecimal(floorIndex));
			}

			@Override
			public BigDecimal getHeight() {
				return floorHeight.add(BigDecimal.ONE).multiply(new BigDecimal(floors));
			}

			@Override
			public BigDecimal getFloorHeight(int floor) {
				return floorHeight;
			}

		};

		return new Building(model);
	}
}
