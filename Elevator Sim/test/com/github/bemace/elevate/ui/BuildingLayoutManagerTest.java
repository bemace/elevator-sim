package com.github.bemace.elevate.ui;

import static org.fest.assertions.Assertions.*;
import static org.testng.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.bemace.elevate.Building;
import com.github.bemace.elevate.BuildingFactory;

public class BuildingLayoutManagerTest {
	private Building building;
	private BuildingLayoutManager layout;
	private static MathContext mathContext = new MathContext(10);

	@BeforeTest
	public void createLayoutManager() {
		building = BuildingFactory.create(5, new BigDecimal(5, mathContext));
		layout = new BuildingLayoutManager(building, new BigDecimal(5, mathContext));

	}

	@DataProvider
	private static Object[][] elevationData() {
		// @formatter:off
		return new Object[][] {
				{ -1, new BigDecimal("31") },
				{ 4, new BigDecimal("30") },
				{ 29, new BigDecimal("25") },
				{ 34, new BigDecimal("24") },
				{ 124, new BigDecimal("6") },
				{ 149, BigDecimal.ONE },
				{ 154, BigDecimal.ZERO },
		};
		// @formatter:on
	}

	@Test(dataProvider = "elevationData")
	public void convertElevationToY(int y, BigDecimal elevation) {
		assertEquals(layout.convertElevationToY(elevation), y);
	}

	@Test(dataProvider = "elevationData")
	public void convertYtoElevation(int y, BigDecimal elevation) {
		assertEquals(layout.convertYtoElevation(y), elevation);
	}

	@DataProvider(name = "reversabilityData")
	private static Object[][] reversabilityData() {
		Building building = BuildingFactory.create(5, new BigDecimal(5, mathContext));
		BuildingLayoutManager layout = new BuildingLayoutManager(building, new BigDecimal(5, mathContext));

		Object[][] data = new Object[155][2];
		for (int y = 0; y < 155; y++) {
			data[y][0] = layout.convertYtoElevation(y);
			data[y][1] = y;
		}

		return data;
	}

	@Test(dataProvider = "reversabilityData")
	public void checkReversability(BigDecimal elevation, int originalY) {
		int finalY = layout.convertElevationToY(elevation);
		assertThat(finalY).isEqualTo(originalY).overridingErrorMessage(
				elevation + " converted back to " + finalY + ", not " + originalY);
		// Container container = new Container();
		// for (int y = 0; y < layout.preferredLayoutSize(container).height;
		// y++) {
		// BigDecimal elevation = layout.convertYtoElevation(y);
		// int finalY = layout.convertElevationToY(elevation);
		// assertThat(finalY).isEqualTo(y).overridingErrorMessage(
		// elevation + " converted back to " + finalY + ", not " + y);
		// }
	}
}
