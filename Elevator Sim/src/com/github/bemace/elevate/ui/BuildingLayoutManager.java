package com.github.bemace.elevate.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.github.bemace.elevate.Building;
import com.github.bemace.elevate.Floor;

class BuildingLayoutManager implements LayoutManager {
	private Building building;
	private BigDecimal lowestFloorElevation;
	private int height;
	private Dimension min;
	private Map<Component, String> constraints = new HashMap<Component, String>();

	public BuildingLayoutManager(Building building) {
		this.building = building;
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
			int ceilY = convertElevationToY(parent, floor.getElevation().add(floor.getHeight()));
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
		for (Component c : parent.getComponents())
			widestPrefWidth = Math.max(widestPrefWidth, c.getPreferredSize().width);

		return new Dimension(widestPrefWidth, height);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		constraints.remove(comp);
	}

	protected int convertElevationToY(Component parent, BigDecimal elevation) {
		return parent.getHeight() - 1
				- elevation.subtract(lowestFloorElevation).multiply(SimulatorInterface.SCALE).intValue();
	}

	protected int convertDistanceToPixels(BigDecimal distance) {
		return distance.multiply(SimulatorInterface.SCALE).intValue();
	}

}
