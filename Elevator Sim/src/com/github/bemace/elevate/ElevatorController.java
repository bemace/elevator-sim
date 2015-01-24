package com.github.bemace.elevate;

public abstract class ElevatorController {
	private Building building;
	
	public ElevatorController(Building building) {
		this.building = building;
	}
	
	public Building getBuilding() {
		return building;
	}
	
	/**
	 * 
	 * @param floor elevator was summoned from
	 * @param dir direction passenger(s) want to go
	 */
	public abstract void elevatorSummoned(Floor floor, Direction dir);
	
	public abstract void elevatorArrived(Floor floor);
}
