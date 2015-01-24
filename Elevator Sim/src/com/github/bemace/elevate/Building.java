package com.github.bemace.elevate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Building {

	private BuildingModel model;
	private List<Floor> floors;
	private Map<String, Floor> floorsById;
	private ElevatorCar elevatorCar;

	public Building(BuildingModel model) {
		this.model = model;
		int floorCount = model.getMaxFloorIndex() - model.getMinFloorIndex() + 1;
		floors = new ArrayList<Floor>(floorCount);
		floorsById = new HashMap<String, Floor>(floorCount);

		for (int f = model.getMinFloorIndex(); f <= model.getMaxFloorIndex(); f++) {
			Floor floor = new FloorImpl(this, f);
			floors.add(floor);
			floorsById.put(floor.getId(), floor);
		}

		elevatorCar = new ElevatorCar(this);
	}

	public BuildingModel getModel() {
		return model;
	}

	public Floor getFloor(BigDecimal elevation, RoundingMode mode) {
		throw new UnsupportedOperationException();
	}

	protected int convertModelIndexToZeroBased(int index) {
		return index - getMinFloorIndex();
	}

	protected int convertZeroBasedIndexToModel(int index) {
		return index + getMinFloorIndex();
	}

	/**
	 * @param index
	 *            of floor to get (between {@link #getMinFloorIndex()} and
	 *            {@link #getMaxFloorIndex()}).
	 * @return
	 */
	public Floor getFloor(int index) {
		return floors.get(convertModelIndexToZeroBased(index));
	}

	public Floor getFloor(String id) {
		return floorsById.get(id);
	}

	public List<Floor> getFloors() {
		return floors;
	}

	public String getFloorId(int index) {
		return model.getFloorId(index);
	}

	public int getFloorCount() {
		return floors.size();
	}

	public int getMinFloorIndex() {
		return model.getMinFloorIndex();
	}

	public int getMaxFloorIndex() {
		return model.getMaxFloorIndex();
	}

	public BigDecimal getElevation(int floor) {
		return model.getFloorElevation(floor);
	}

	public BigDecimal getHeight() {
		return model.getHeight();
	}

	public ElevatorCar getElevatorCar() {
		return elevatorCar;
	}

	private class FloorImpl implements Floor {
		private Building building;
		private int index;

		public FloorImpl(Building building, int index) {
			this.building = building;
			this.index = index;
		}

		@Override
		public String getId() {
			return building.getFloorId(index);
		}

		/**
		 * Gets the building which this floor is part of.
		 * 
		 * @return
		 */
		@Override
		public Building getBuilding() {
			return building;
		}

		@Override
		public BigDecimal getElevation() {
			return building.getElevation(index);
		}

		@Override
		public BigDecimal getHeight() {
			return building.getModel().getFloorHeight(index);
		}

		@Override
		public int compareTo(Floor other) {
			return getElevation().compareTo(other.getElevation());
		}
	}

}
