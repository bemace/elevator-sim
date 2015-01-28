package com.github.bemace.elevate.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

import com.github.bemace.elevate.Building;
import com.github.bemace.elevate.Floor;

class BuildingLayoutManager implements LayoutManager {
	private static MathContext mathContext = new MathContext(10);

	private Building building;
	/** pixels per foot. */
	private BigDecimal scale;
	private BigDecimal lowestFloorElevation;
	private int height;
	private Dimension min;
	private Map<Component, String> constraints = new HashMap<Component, String>();

	public BuildingLayoutManager(Building building, BigDecimal scale) {
		this.building = building;
		this.scale = scale;
		lowestFloorElevation = building.getFloor(building.getMinFloorIndex()).getElevation();
		height = convertDistanceToPixels(building.getHeight());
		min = new Dimension(0, height);
	}

	@Override
	public void addLayoutComponent(String floorId, Component comp) {
		constraints.put(comp, floorId);
	}

	@Override
	public void layoutContainer(Container parent) {
		for (Component c : parent.getComponents()) {
			String floorId = constraints.get(c);
			if (floorId == null)
				continue;

			Floor floor = building.getFloor(floorId);
			int ceilY = convertElevationToY(floor.getElevation().add(floor.getHeight())) + 1;
			int height = convertDistanceToPixels(floor.getHeight());

			c.setBounds(0, ceilY, parent.getWidth(), height);
		}

	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		int height = convertDistanceToPixels(building.getHeight());

		int widestMinWidth = 0;
		for (Component c : parent.getComponents())
			widestMinWidth = Math.max(widestMinWidth, c.getMinimumSize().width);

		return new Dimension(widestMinWidth, height);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		int height = convertDistanceToPixels(building.getHeight());

		int widestPrefWidth = 0;
		for (Component c : parent.getComponents()) {
			widestPrefWidth = Math.max(widestPrefWidth, c.getMinimumSize().width);
			widestPrefWidth = Math.max(widestPrefWidth, c.getPreferredSize().width);
		}

		return new Dimension(widestPrefWidth, height);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		constraints.remove(comp);
	}

	protected int convertElevationToY(BigDecimal elevation) {
		BigDecimal heightAboveBaseInFeet = elevation.subtract(building.getModel().getBaseElevation());
		int heightAboveBaseInPixels = heightAboveBaseInFeet.multiply(scale).intValue();
		return height - heightAboveBaseInPixels - 1;
	}

	protected BigDecimal convertYtoElevation(int y) {
		System.out.println("y = " + y);
		// int baseY =
		// convertElevationToY(building.getModel().getBaseElevation());
		// System.out.println("baseY = " + baseY);
		int heightAboveBaseInPixels = height - y - 1;
		System.out.println("heightAboveBaseInPixels = " + heightAboveBaseInPixels);
		BigDecimal heightAboveBaseInFeet = new BigDecimal(heightAboveBaseInPixels, mathContext).divide(scale,
				mathContext);
		return heightAboveBaseInFeet.add(building.getModel().getBaseElevation());
	}

	protected int convertDistanceToPixels(BigDecimal distance) {
		return distance.multiply(scale).intValue();
	}

}
