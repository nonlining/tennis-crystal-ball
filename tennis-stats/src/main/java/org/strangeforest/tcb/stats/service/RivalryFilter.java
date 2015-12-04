package org.strangeforest.tcb.stats.service;

import java.sql.*;

import com.google.common.collect.*;

import static com.google.common.base.Strings.*;
import static org.strangeforest.tcb.stats.util.ResultSetUtil.*;

public class RivalryFilter {

	private final Range<Integer> seasonRange;
	private final String level;
	private final String surface;

	private static final String SEASON_FROM_CRITERION = " AND season >= ?";
	private static final String SEASON_TO_CRITERION   = " AND season <= ?";
	private static final String LEVEL_CRITERION       = " AND level = ?::tournament_level";
	private static final String SURFACE_CRITERION     = " AND surface = ?::surface";

	public RivalryFilter(Range<Integer> seasonRange, String level, String surface) {
		this.seasonRange = seasonRange;
		this.level = level;
		this.surface = surface;
	}

	public String getLevel() {
		return level;
	}

	public String getSurface() {
		return surface;
	}

	public boolean hasLevel() {
		return !isNullOrEmpty(level);
	}

	public boolean hasSurface() {
		return !isNullOrEmpty(surface);
	}

	public String getCriteria() {
		StringBuilder criteria = new StringBuilder();
		appendCriteria(criteria);
		return criteria.toString();
	}

	protected void appendCriteria(StringBuilder criteria) {
		if (seasonRange.hasLowerBound())
			criteria.append(SEASON_FROM_CRITERION);
		if (seasonRange.hasUpperBound())
			criteria.append(SEASON_TO_CRITERION);
		if (!isNullOrEmpty(level))
			criteria.append(LEVEL_CRITERION);
		if (!isNullOrEmpty(surface))
			criteria.append(SURFACE_CRITERION);
	}

	public int bindParams(PreparedStatement ps, int index) throws SQLException {
		index = bindIntegerRange(ps, index, seasonRange);
		if (!isNullOrEmpty(level))
			ps.setString(++index, level);
		if (!isNullOrEmpty(surface))
			ps.setString(++index, surface);
		return index;
	}
}