package com.github.bemace.elevate.ui;

import static org.fest.assertions.Assertions.*;
import static org.testng.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

	@Test
	public void minimumLayoutSize() {
		Building building = BuildingFactory.create(5, new BigDecimal(5, mathContext));
		BuildingLayoutManager layout = new BuildingLayoutManager(building, new BigDecimal(5, mathContext));
		JPanel panel = new JPanel();
		assertThat(layout.minimumLayoutSize(panel).height).isEqualTo(155);
	}

	@Test
	public void preferredLayoutSize() {
		Building building = BuildingFactory.create(5, new BigDecimal(5, mathContext));
		BuildingLayoutManager layout = new BuildingLayoutManager(building, new BigDecimal(5, mathContext));
		JPanel panel = new JPanel();
		assertThat(layout.preferredLayoutSize(panel).height).isEqualTo(155);
	}

	@DataProvider
	private static Object[][] componentPlacementData() {
		// @formatter:off
		return new Object[][] {
			{ "1", 125, 25 },
			{ "2", 95, 25 },
			{ "3", 65, 25 },
			{ "4", 35, 25 },
			{ "5", 5, 25 },
		};
		// @formatter:on
	}

	@Test(dataProvider = "componentPlacementData")
	public void componentPlacement(String floorId, int expectedY, int expectedHeight) {
		Building building = BuildingFactory.create(5, new BigDecimal(5, mathContext));
		BuildingLayoutManager layout = new BuildingLayoutManager(building, new BigDecimal(5, mathContext));

		JPanel panel = new JPanel(layout);
		JComponent child = new JLabel("child");
		panel.add(child, floorId);

		panel.setSize(layout.preferredLayoutSize(panel));
		layout.layoutContainer(panel);
		assertThat(panel.getHeight()).isEqualTo(155);

		assertThat(child.getY()).isEqualTo(expectedY);
		assertThat(child.getHeight()).isEqualTo(expectedHeight);
	}
}
