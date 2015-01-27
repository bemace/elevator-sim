package com.github.bemace.elevate;

import java.math.BigDecimal;
import java.math.MathContext;

public class BuildingFactory {

	public static Building create(final int floors, final BigDecimal floorHeight) {

		BuildingModel model = new BuildingModel() {

			@Override
			public BigDecimal getBaseElevation() {
				return new BigDecimal(0, MathContext.UNLIMITED);
			}

			@Override
			public BigDecimal getHeight() {
				return floorHeight.add(new BigDecimal(1)).multiply(new BigDecimal(floors)).add(BigDecimal.ONE);
			}

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
				return floorHeight.add(BigDecimal.ONE).multiply(new BigDecimal(floorIndex - getMinFloorIndex()))
						.add(BigDecimal.ONE);
			}

			@Override
			public BigDecimal getFloorHeight(int floor) {
				return floorHeight;
			}

		};

		return new Building(model);
	}
}
