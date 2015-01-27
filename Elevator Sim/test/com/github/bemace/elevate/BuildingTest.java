package com.github.bemace.elevate;

import static org.testng.Assert.*;

import java.math.BigDecimal;

import org.testng.annotations.Test;

public class BuildingTest {

	@Test
	public void getFloor() {
		Building b = BuildingFactory.create(5, BigDecimal.ONE);
		assertEquals(b.getFloor(1).getIndex(), 1);
		assertEquals(b.getFloor(1).getId(), "1");
	}
}
