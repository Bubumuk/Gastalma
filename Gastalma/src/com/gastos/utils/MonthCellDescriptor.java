// Copyright 2012 Square, Inc.
package com.gastos.utils;

import java.util.Date;

/** Describes the state of a particular date cell in a {@link YearView}. */
public class MonthCellDescriptor {
	private Date date;
	private int value;
	private boolean isCurrentYear;
	private boolean isSelected;
	private boolean isToday;
	private boolean isSelectable;
	private int month;

	public MonthCellDescriptor(Date date, boolean currentYear, boolean selectable, boolean selected, boolean today, int value) {
		this.date = date;
		isCurrentYear = currentYear;
		isSelectable = selectable;
		isSelected = selected;
		isToday = today;
		this.value = value;
	}
	
	public MonthCellDescriptor(int month, boolean currentYear, boolean selectable, boolean selected, boolean today) {
		this.month = month;
		isCurrentYear = currentYear;
		isSelectable = selectable;
		isSelected = selected;
		isToday = today;
	}

	public Date getDate() {
		return date;
	}
	
	public int getMonth() {
		return month;
	}

	public boolean isCurrentMonth() {
		return isCurrentYear;
	}

	public boolean isSelectable() {
		return isSelectable;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public boolean isToday() {
		return isToday;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "MonthCellDescriptor{" + "month=" + month
				+ ", isCurrentYear=" + isCurrentYear + ", isSelected="
				+ isSelected + ", isToday=" + isToday + ", isSelectable="
				+ isSelectable + '}';
	}
}
