package org.strangeforest.tcb.stats.model.records;

import java.sql.*;

public abstract class SeasonWonLostRecordRow extends WonLostRecordRow {

	private int season;

	public SeasonWonLostRecordRow(int rank, int playerId, String name, String countryId, Boolean active) {
		super(rank, playerId, name, countryId, active);
	}

	public int getSeason() {
		return season;
	}

	@Override public void read(ResultSet rs) throws SQLException {
		super.read(rs);
		season = rs.getInt("season");
	}
}
