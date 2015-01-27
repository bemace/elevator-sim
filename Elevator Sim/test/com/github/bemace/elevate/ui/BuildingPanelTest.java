package com.github.bemace.elevate.ui;

import static java.math.RoundingMode.*;
import static org.fest.assertions.Assertions.*;
import static org.testng.Assert.*;

import java.awt.Point;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.bemace.elevate.Building;
import com.github.bemace.elevate.BuildingFactory;
import com.github.bemace.elevate.Floor;

public class BuildingPanelTest {
	private Building building;
	private BuildingPanel panel;

	@BeforeTest
	public void createPanel() {
		building = BuildingFactory.create(5, new BigDecimal(5));
		panel = new BuildingPanel(building, new BigDecimal(5));
		panel.setSize(panel.getPreferredSize());
	}

	@DataProvider
	private static Object[][] floorData() {
		// @formatter:off
		return new Object[][] {
			{ 0, UP, null }, 
			{ 0, DOWN, "5" }, 
			{ 0, CEILING, null }, 
			{ 0, FLOOR, "5" }, 
			{ 0, HALF_UP, "5" },
			{ 0, HALF_DOWN, "5" }, 
			{ 0, HALF_EVEN, "5" }, 
			{ 0, UNNECESSARY, null },
			
			// top pixel of 5th
			{ 5, UNNECESSARY, "5" },
			{ 5, UP, "5" }, 
			{ 5, DOWN, "5" }, 
			{ 5, CEILING, "5" }, 
			{ 5, FLOOR, "5" }, 
			{ 5, HALF_UP, "5" },
			{ 5, HALF_DOWN, "5" }, 
			{ 5, HALF_EVEN, "5" }, 

			// bottom pixel of 5th
			{ 29, UNNECESSARY, "5" },
			{ 29, UP, "5" }, 
			{ 29, DOWN, "5" }, 
			{ 29, CEILING, "5" }, 
			{ 29, FLOOR, "5" }, 
			{ 29, HALF_UP, "5" },
			{ 29, HALF_DOWN, "5" }, 
			{ 29, HALF_EVEN, "5" }, 

			// closer to 5th
			{ 30, UNNECESSARY, null },
			{ 30, UP, "5" }, 
			{ 30, DOWN, "4" }, 
			{ 30, CEILING, "5" }, 
			{ 30, FLOOR, "4" }, 
			{ 30, HALF_UP, "5" },
			{ 30, HALF_DOWN, "5" }, 
			{ 30, HALF_EVEN, "5" }, 

			// equidistant between 5th and 4th
			{ 32, UNNECESSARY, null },
			{ 32, UP, "5" }, 
			{ 32, DOWN, "4" }, 
			{ 32, CEILING, "5" }, 
			{ 32, FLOOR, "4" }, 
			{ 32, HALF_UP, "5" },
			{ 32, HALF_DOWN, "4" }, 
			{ 32, HALF_EVEN, "4" }, 

			// closer to 4th
			{ 34, UNNECESSARY, null },
			{ 34, UP, "5" }, 
			{ 34, DOWN, "4" }, 
			{ 34, CEILING, "5" }, 
			{ 34, FLOOR, "4" }, 
			{ 34, HALF_UP, "4" },
			{ 34, HALF_DOWN, "4" }, 
			{ 34, HALF_EVEN, "4" }, 

			// top pixel of 4th
			{ 35, UNNECESSARY, "4" },
			{ 35, UP, "4" }, 
			{ 35, DOWN, "4" }, 
			{ 35, CEILING, "4" }, 
			{ 35, FLOOR, "4" }, 
			{ 35, HALF_UP, "4" },
			{ 35, HALF_DOWN, "4" }, 
			{ 35, HALF_EVEN, "4" }, 
			
			// top pixel of 2nd
			{ 95, UNNECESSARY, "2" },
			{ 95, UP, "2" }, 
			{ 95, DOWN, "2" }, 
			{ 95, CEILING, "2" }, 
			{ 95, FLOOR, "2" }, 
			{ 95, HALF_UP, "2" },
			{ 95, HALF_DOWN, "2" }, 
			{ 95, HALF_EVEN, "2" }, 

			// bottom pixel of 2nd
			{ 119, UNNECESSARY, "2" },
			{ 119, UP, "2" }, 
			{ 119, DOWN, "2" }, 
			{ 119, CEILING, "2" }, 
			{ 119, FLOOR, "2" }, 
			{ 119, HALF_UP, "2" },
			{ 119, HALF_DOWN, "2" }, 
			{ 119, HALF_EVEN, "2" }, 
			
			// equidistant between 2nd and 1st
			{ 122, UNNECESSARY, null },
			{ 122, UP, "2" }, 
			{ 122, DOWN, "1" }, 
			{ 122, CEILING, "2" }, 
			{ 122, FLOOR, "1" }, 
			{ 122, HALF_UP, "2" },
			{ 122, HALF_DOWN, "1" }, 
			{ 122, HALF_EVEN, "2" }, 

			// top pixel of 1st
			{ 125, UNNECESSARY, "1" },
			{ 125, UP, "1" }, 
			{ 125, DOWN, "1" }, 
			{ 125, CEILING, "1" }, 
			{ 125, FLOOR, "1" }, 
			{ 125, HALF_UP, "1" },
			{ 125, HALF_DOWN, "1" }, 
			{ 125, HALF_EVEN, "1" }, 

			// bottom pixel of 1st
			{ 149, UNNECESSARY, "1" },
			{ 149, UP, "1" }, 
			{ 149, DOWN, "1" }, 
			{ 149, CEILING, "1" }, 
			{ 149, FLOOR, "1" }, 
			{ 149, HALF_UP, "1" },
			{ 149, HALF_DOWN, "1" }, 
			{ 149, HALF_EVEN, "1" }, 
			
			// top of footing
			{ 150, UNNECESSARY, null },
			{ 150, UP, "1" }, 
			{ 150, DOWN, null }, 
			{ 150, CEILING, "1" }, 
			{ 150, FLOOR, null }, 
			{ 150, HALF_UP, "1" },
			{ 150, HALF_DOWN, "1" }, 
			{ 150, HALF_EVEN, "1" }, 

			// bottom of footing
			{ 154, UNNECESSARY, null },
			{ 154, UP, "1" }, 
			{ 154, DOWN, null }, 
			{ 154, CEILING, "1" }, 
			{ 154, FLOOR, null }, 
			{ 154, HALF_UP, "1" },
			{ 154, HALF_DOWN, "1" }, 
			{ 154, HALF_EVEN, "1" }, 
};
		// @formatter:on
	}

	@Test(dataProvider = "floorData")
	public void floorAtPoint(int y, RoundingMode mode, String floorId) {
		Floor actual = panel.floorAtPoint(new Point(0, y), mode);
		if (floorId == null)
			assertNull(actual);
		else {
			assertThat(actual).isNotNull();
			assertThat(actual.getId()).isEqualTo(floorId);
		}
	}
}
