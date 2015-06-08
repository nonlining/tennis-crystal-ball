package org.strangeforest.tcb.dataload

import static java.lang.Math.*

class MatchScore {

	short w_sets, l_sets
	String outcome
	List setScores

	short[] getW_gems() {
		setScores.collect({setScore -> setScore.w_gems});
	}

	short[] getL_gems() {
		setScores.collect({setScore -> setScore.l_gems});
	}

	Short[] getW_tb_pt() {
		setScores.collect({setScore -> setScore.w_tb_pt});
	}

	Short[] getL_tb_pt() {
		setScores.collect({setScore -> setScore.l_tb_pt});
	}

	static MatchScore parse(String match) {
		if (!match)
			return null
		List sets = match.tokenize(' ')
		short w_sets = 0, l_sets = 0
		List setScores = new ArrayList(sets.size())
		String outcome = null
		for (String set in sets) {
			int pos = set.indexOf('-')
			if (pos > 0) {
				try {
					int len = set.length()
					short w_gems = parseGems(set.substring(0, pos))
					int pos2 = set.indexOf('(', pos + 2)
					short l_gems = parseGems(set.substring(pos + 1, pos2 > 0 ? pos2 : len))
					Short tb_pt = null
					if (pos2 > 0) {
						if (set[len - 1] == ')')
							tb_pt = set.substring(pos2 + 1, len - 1).toInteger()
					}
					boolean w_win = isWin(w_gems, l_gems)
					boolean l_win = isWin(l_gems, w_gems)
					if (w_win) w_sets++
					if (l_win) l_sets++
					setScores.add(new SetScore(
						w_gems: w_gems, l_gems: l_gems,
						w_tb_pt: tb_pt >= 0 ? (w_win ? max(tb_pt + 2, 7) : tb_pt) : null,
						l_tb_pt: tb_pt >= 0 ? (l_win ? max(tb_pt + 2, 7) : tb_pt) : null
					))
				}
				catch (Exception ex) {
					println("Invalid set: $set")
					ex.printStackTrace()
				}
			}
			else {
				switch (set) {
					case 'W/O': outcome = 'W/O'; break
					case 'Default':
					case 'abandoned':
					case 'unfinished':
					case 'Unfinished':
					case 'ABD':
					case 'ABN':
					case 'DEF':
					case 'RET': outcome = setScores.isEmpty() ? 'W/O' : 'RET'; break
					case 'NA': return null
					case 'In':
					case 'Progress':
					case 'Played':
					case 'and': break
					default: println("Invalid set outcome: $set")
				}
			}
		}
		new MatchScore(outcome: outcome, w_sets: w_sets, l_sets: l_sets, setScores: setScores)
	}

	private static short parseGems(String s) {
		switch (s) {
			case 'Jun': return 6
			default: s.toInteger()
		}
	}

	private static boolean isWin(int w_gems, int l_gems) {
		(w_gems >= 6 && w_gems >= l_gems + 2) || (w_gems == 7 && l_gems == 6)
	}


	@Override boolean equals(Object o) {
		if (!(o instanceof MatchScore)) false
		MatchScore score = (MatchScore)o
		Objects.equals(w_sets, score.w_sets) &&
			Objects.equals(l_sets, score.l_sets) &&
			Objects.equals(outcome, score.outcome) &&
			Objects.equals(setScores, score.setScores)
	}

	@Override int hashCode() {
		Objects.hash(w_sets, l_sets, outcome, setScores)
	}


	@Override public String toString() {
		StringBuilder sb = new StringBuilder("MatchScore{")
		sb.append("w_sets=").append(w_sets)
		sb.append(", l_sets=").append(l_sets)
		sb.append(", setScores=").append(setScores)
		if (outcome != null)
			sb.append(", outcome='").append(outcome).append('\'')
		sb.append('}')
		sb.toString()
	}
}