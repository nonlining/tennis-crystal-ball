package org.strangeforest.tcb.stats.model.records;

import java.sql.*;

import static org.strangeforest.tcb.stats.model.records.RecordTournamentEvent.*;

public class TournamentCareerSpanRecordRow extends CareerSpanRecordRow {

	private RecordTournamentEvent startEvent;
	private RecordTournamentEvent endEvent;

	public TournamentCareerSpanRecordRow(int rank, int playerId, String name, String countryId, Boolean active) {
		super(rank, playerId, name, countryId, active);
	}

	public RecordTournamentEvent getStartEvent() {
		return startEvent;
	}

	public RecordTournamentEvent getEndEvent() {
		return endEvent;
	}

	@Override public void read(ResultSet rs) throws SQLException {
		super.read(rs);
		startEvent = readTournamentEvent(rs, "start_");
		endEvent = readTournamentEvent(rs, "end_");
	}
}
