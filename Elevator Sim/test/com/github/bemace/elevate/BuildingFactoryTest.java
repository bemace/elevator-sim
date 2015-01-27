package com.github.bemace.elevate;

import static org.testng.Assert.*;

import java.math.BigDecimal;

import org.testng.annotations.Test;

public class BuildingFactoryTest {

	@Test
	public void createBasicBuilding() {
		Building b = BuildingFactory.create(5, new BigDecimal(5));

		assertEquals(b.getMinFloorIndex(), 1);
		assertEquals(b.getMaxFloorIndex(), 5);
		assertEquals(b.getFloor(1).getId(), "1");

		assertEquals(b.getModel().getBaseElevation(), BigDecimal.ZERO);

		assertEquals(b.getFloor("1").getElevation(), new BigDecimal(1));
		assertEquals(b.getFloor("2").getElevation(), new BigDecimal(7));
		assertEquals(b.getFloor("3").getElevation(), new BigDecimal(13));
		assertEquals(b.getFloor("4").getElevation(), new BigDecimal(19));
		assertEquals(b.getFloor("5").getElevation(), new BigDecimal(25));
	}

}
