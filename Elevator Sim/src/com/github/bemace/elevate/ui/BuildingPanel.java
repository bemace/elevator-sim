package com.github.bemace.elevate.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import com.github.bemace.elevate.Building;
import com.github.bemace.elevate.Direction;
import com.github.bemace.elevate.ElevatorCar;
import com.github.bemace.elevate.Floor;

public class BuildingPanel extends JPanel implements Scrollable {
	private static final long serialVersionUID = 1L;
	private Building building;
	private BigDecimal scale;
	private ElevatorShaftPanel shaftPanel;
	private JPanel floorsPanel;

	/**
	 * 
	 * @param building
	 * @param scale
	 *            pixels per foot
	 */
	public BuildingPanel(Building building, BigDecimal scale) {
		super(new BorderLayout());
		this.building = building;
		this.scale = scale;
		setBackground(Color.GRAY);

		shaftPanel = new ElevatorShaftPanel();
		add(shaftPanel, BorderLayout.EAST);

		floorsPanel = new JPanel(new BuildingLayoutManager(building, scale));
		floorsPanel.setOpaque(false);
		add(floorsPanel, BorderLayout.CENTER);

		for (Floor floor : building.getFloors())
			floorsPanel.add(new FloorPanel(floor), floor.getId());
	}

	/**
	 * 
	 * @param elevation
	 * @param floor
	 * @return a negative integer, zero, or a positive integer as the elevation
	 *         is below, within, or above <tt>floor</tt>.
	 */
	private int compare(BigDecimal elevation, Floor floor) {
		if (elevation.compareTo(floor.getElevation()) < 0)
			return -1;

		if (elevation.compareTo(floor.getElevation().add(floor.getHeight())) >= 0)
			return 1;

		return 0;
	}

	/**
	 * Returns the floor that point lies in.
	 * 
	 * @param p
	 * @return
	 */
	public Floor floorAtPoint(Point p, RoundingMode mode) {
		BigDecimal elevation = convertYtoElevation(p.y);
		int low = building.getMinFloorIndex();
		int high = building.getMaxFloorIndex();

		int mid;
		while (low < high - 1) {
			mid = (low + high) / 2;
			Floor floor = building.getFloor(mid);

			int comp = compare(elevation, floor);

			if (comp < 0)
				high = mid;
			else if (comp > 0)
				low = mid;
			else
				return floor;
		}

		Floor above = building.getFloor(high);
		Floor below = building.getFloor(low);

		if (compare(elevation, above) == 0)
			return above;
		else if (compare(elevation, below) == 0)
			return below;

		if (mode == RoundingMode.UNNECESSARY)
			return null;

		if (compare(elevation, above) > 0) {
			// point is above all floors
			if (mode == RoundingMode.CEILING || mode == RoundingMode.UP)
				return null;
			else
				return above;
		}
		else if (compare(elevation, below) < 0) {
			// point is below all floors
			if (mode == RoundingMode.FLOOR || mode == RoundingMode.DOWN)
				return null;
			else
				return below;
		}

		// by now we know the point is between two floors

		if (mode == RoundingMode.CEILING)
			return building.getFloor(high);
		else if (mode == RoundingMode.FLOOR)
			return building.getFloor(low);
		else if (mode == RoundingMode.UP)
			return building.getFloor(Math.abs(low) > Math.abs(high) ? low : high);
		else if (mode == RoundingMode.DOWN)
			return building.getFloor(Math.abs(low) < Math.abs(high) ? low : high);

		int distanceUp = Math.abs(p.y - convertElevationToY(above.getElevation()));
		int distanceDown = Math.abs(p.y - convertElevationToY(below.getElevation().add(below.getHeight())) - 1);

		if (distanceUp < distanceDown)
			return above;
		else if (distanceUp > distanceDown)
			return below;

		if (mode == RoundingMode.HALF_UP)
			return above;
		else if (mode == RoundingMode.HALF_DOWN)
			return below;
		else if (mode == RoundingMode.HALF_EVEN)
			return low % 2 == 0 ? below : above;

		return null;
	}

	protected int convertElevationToY(BigDecimal elevation) {
		return ((BuildingLayoutManager) floorsPanel.getLayout()).convertElevationToY(elevation);
	}

	protected BigDecimal convertYtoElevation(int y) {
		return ((BuildingLayoutManager) floorsPanel.getLayout()).convertYtoElevation(y);
	}

	protected int convertElevationToYOrig(BigDecimal elevation) {
		return getHeight() - 1 - elevation.subtract(building.getModel().getBaseElevation()).multiply(scale).intValue();
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		if (orientation == SwingConstants.HORIZONTAL)
			return 1;

		if (direction < 0) { // scrolling up
			Floor floor = floorAtPoint(visibleRect.getLocation(), RoundingMode.CEILING);

			if (floor == null || floor.isHighest())
				return visibleRect.y;

			int ceilY = convertElevationToY(floor.getElevation().add(floor.getHeight())) + 1;
			if (visibleRect.y == ceilY) {
				int floorIndex = floor.getIndex();
				Floor nextFloor = building.getFloor(floorIndex + 1);
				return visibleRect.y - convertElevationToY(nextFloor.getElevation().add(nextFloor.getHeight())) - 1;
			}
			else
				return visibleRect.y - ceilY;
		}
		else { // scrolling down
			Point bottomLeft = visibleRect.getLocation();
			bottomLeft.translate(0, visibleRect.height - 1);
			Floor floor = floorAtPoint(bottomLeft, RoundingMode.FLOOR);

			if (floor == null || floor.isLowest())
				return getHeight() - bottomLeft.y;

			int floorY = convertElevationToY(floor.getElevation());
			if (bottomLeft.y == floorY) {
				Floor nextFloor = building.getFloor(floor.getIndex() - 1);
				return convertElevationToY(nextFloor.getElevation()) - bottomLeft.y;
			}
			else
				return Math.abs(floorY - bottomLeft.y);
		}
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return getScrollableUnitIncrement(visibleRect, orientation, direction);
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	protected int convertDistanceToPixels(BigDecimal distance) {
		return distance.multiply(scale).intValue();
	}

	class FloorPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private Floor floor;
		private JLabel label;

		public FloorPanel(Floor floor) {
			setLayout(new BorderLayout());
			this.floor = floor;
			label = new JLabel(floor.getId());
			label.setFont(label.getFont().deriveFont(16.0f));
			add(label, BorderLayout.WEST);
			// setOpaque(false);

			JPanel callPanel = new JPanel(new GridLayout(2, 1));

			callPanel.add(new CallButton(floor, Direction.UP));
			callPanel.add(new CallButton(floor, Direction.DOWN));
			add(callPanel, BorderLayout.EAST);
			setMinimumSize(new Dimension(150, 10));
		}

		public Floor getFloor() {
			return floor;
		}

	}

	class ElevatorShaftPanel extends JComponent {
		private static final long serialVersionUID = 1L;
		private Dimension size;
		private int leftGap;
		private int carWidth;

		public ElevatorShaftPanel() {
			size = new Dimension(convertDistanceToPixels(new BigDecimal(8)), building.getHeight().multiply(scale)
					.intValue());
			setMinimumSize(size);
			setPreferredSize(size);
			setMaximumSize(size);
			setOpaque(true);
			leftGap = convertDistanceToPixels(new BigDecimal(".5"));
			carWidth = getWidth() - leftGap - leftGap;
		}

		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, getSize().width, size.height);
			// g.setColor(Color.RED);
			// g.drawLine(0, 0, getWidth() - 1, 0);

			for (Floor floor : building.getFloors()) {
				g.setColor(Color.DARK_GRAY);
				int floorY = convertElevationToY(floor.getElevation());
				int height = convertDistanceToPixels(floor.getHeight());
				int ceilY = convertElevationToY(floor.getElevation().add(floor.getHeight()));
				// System.out.println("Floor " + floor.getId() + ": " + floorY +
				// " up to " + ceilY + " (" + height + ")");
				g.fillRect(0, ceilY, getWidth() - 1, height);
				// g.setColor(Color.GREEN);
				// g.drawLine(0, ceilY, getWidth() - 1, ceilY);
				// g.setColor(Color.RED);
				// g.drawLine(0, floorY, getWidth() - 1, floorY);

				// break;
			}

			ElevatorCar car = building.getElevatorCar();

			int roofY = convertElevationToY(car.getElevation().add(car.getHeight()));
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(getWidth() / 2 - 1, 0, 2, roofY);

			carWidth = getWidth() - leftGap - leftGap;

			g.setColor(Color.BLUE);
			g.fillRect(leftGap, roofY, carWidth, convertDistanceToPixels(car.getHeight()));
		}
	}

}
