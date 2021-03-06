package org.strangeforest.tcb.stats.model.table;

import java.util.*;

public class BootgridTable<R> {

	private int current;
	private int rowCount;
	private List<R> rows = new ArrayList<>();
	private int total;

	public BootgridTable() {
		this(1, 0);
	}

	public BootgridTable(int current) {
		this(current, 0);
	}

	public BootgridTable(int current, int total) {
		this.current = current;
		this.total = total;
	}

	public int getCurrent() {
		return current;
	}

	public int getRowCount() {
		return rowCount;
	}

	public List<R> getRows() {
		return rows;
	}

	public void addRow(R row) {
		rows.add(row);
		rowCount++;
	}

	public void addRows(List<R> rows) {
		this.rows.addAll(rows);
		rowCount += rows.size();
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotal() {
		return total != 0 ? total : rowCount;
	}
}
