package soln_12_team_3_div;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ScheduleDivisions {
	public Week[] weeks;
	public Team[] teams;
	public int num_weeks;
	public int div_sch_len;
	public int num_div;
	public int teams_per_div;
	public List<String> divisions;

	public ScheduleDivisions(Team[] teams, int num_weeks) {
		this.num_weeks = num_weeks;
		this.weeks = new Week[num_weeks];
		this.teams = teams;
		// this is where i add randomization.
		divisions = new ArrayList<String>();
		for (int i = 0; i < teams.length; i++) {
			teams[i].setWeeks(num_weeks);
			if (!divisions.contains(teams[i].divNum)) {
				divisions.add(teams[i].divNum);
			}
		}
		num_div = divisions.size();
		teams_per_div = teams.length / num_div;

		// checks for viability.
		if (teams.length % 2 != 0) {
			throw new IllegalArgumentException("Must have even number of teams.");
		}
		if (teams.length % num_div != 0) {
			throw new IllegalArgumentException("Must be an even number of teams in each division.");
		}
		if ((teams_per_div - 1) * 2 > num_weeks) {
			throw new IllegalArgumentException("Must be able to play each in-division team twice.");
		}
		if ((teams.length - 1) * 2 < num_weeks) {
			throw new IllegalArgumentException("Can't play any team more than twice.");
		}

		// list of lists of teams in division.

		List<List<Team>> teams_by_div = new ArrayList<List<Team>>();
		for (int i = 0; i < divisions.size(); i++) {
			teams_by_div.add(new ArrayList<Team>());
		}
		for (int i = 0; i < teams.length; i++) {
			// based on what index they are in divisions, add them to a teams_by_div list of
			// lists.
			for (int j = 0; j < divisions.size(); j++) {
				if (divisions.get(j) == teams[i].divNum) {
					if (teams_by_div.get(j) == null) {
						teams_by_div.set(j, new ArrayList<Team>());
					}
					teams_by_div.get(j).add(teams[i]);
					break;
				}
			}
		}
		for (int i = 0; i < teams_by_div.size(); i++) {
			Collections.shuffle(teams_by_div.get(i));
		}

		Collections.shuffle(teams_by_div);

		List<Team> shuffled_teams = new ArrayList<Team>();

		for (int i = 0; i < teams_by_div.size(); i++) {
			for (int j = 0; j < teams_by_div.get(i).size(); j++) {
				shuffled_teams.add(teams_by_div.get(i).get(j));
			}
		}
		shuffled_teams.toArray(teams);

		// get in division opponents. this should work fine.
		for (int i = 0; i < teams.length; i++) {
			for (int j = 0; j < teams.length; j++) {
				if (i == j) {
					continue;
				}
				if (teams[i].divNum == teams[j].divNum) {
					teams[i].divOpp.add(teams[j]);
					teams[i].divOpp.add(teams[j]);
				} else {
					teams[i].otherOpp.add(teams[j]);
				}
			}
		}

		div_sch_len = teams[0].divOpp.size();
	}

	// both of these will use backtracking.
	public static boolean scheduleInDivison(ScheduleDivisions schedule) {
		int div_len = schedule.div_sch_len;
		Team[] teams = schedule.teams;

		// schedule in division weeks.
		boolean complete = true;
		int week = 0;
		int team = 0;
		for (int i = 0; i < div_len; i++) {
			for (int j = 0; j < teams.length; j++) {
				if (teams[j].sched[i] == null) {
					week = i;
					team = j;
					complete = false;
					break;
				}
			}
			if (!complete) {
				break;
			}
		}

		if (complete) {
			return true;
		}

		// schedule unscheduled game.
		for (int i = 0; i < teams[team].divOpp.size(); i++) {
			Team pot_opp = teams[team].divOpp.get(i);
			if (validInDiv(teams[team], pot_opp, week)) {
				teams[team].sched[week] = pot_opp;
				pot_opp.sched[week] = teams[team];
				if (scheduleInDivison(schedule)) {
					return true;
				} else {
					teams[team].sched[week] = null;
					pot_opp.sched[week] = null;
				}
			}
		}
		return false;

	}

	private static boolean validInDiv(Team team1, Team team2, int week) {
		if (team1.divNum != team2.divNum) {
			return false;
		}
		// make sure they're unscheduled that week.
		if (team1.sched[week] != null || team2.sched[week] != null) {
			return false;
		}
		// make sure that they've only played each other once at most.
		int game_vs_each_other = 0;
		for (int i = 0; i <= week; i++) {
			if (team1.sched[i] == team2) {
				game_vs_each_other++;
			}
		}
		if (game_vs_each_other > 1) {
			return false;
		}
		return true;
	}

	

	public static boolean validOutDiv(Team team1, Team team2, int week, Team[] teams) {
		if (team1.divNum == team2.divNum) {
			return false;
		}
		// make sure they're unscheduled that week.
		if (team1.sched[week] != null || team2.sched[week] != null) {
			return false;
		}

		// get in amount of games in between these divisions this week.
		int against_division = 0;
		for (int i = 0; i < teams.length; i++) {
			if (teams[i].sched[week] != null && teams[i].divNum == team1.divNum
					&& teams[i].sched[week].divNum == team2.divNum) {
				against_division++;
			}
		}
		if (against_division > 1) {
			return false;
		}
		// make sure each team isn't scheduled against the other yet.
		for (int i = 0; i < teams.length; i++) {
			for (int j = 0; j <= week; j++) {
				if (teams[i] == team1 && teams[i].sched[j] == team2) {
					return false;
				}
			}
		}

		return true;
	}

	public Week[] constructSchedule() {
		// construct week by week schedule.
		for (int week = 0; week < num_weeks; week++) {
			weeks[week] = new Week(teams.length);
			List<Team> used = new ArrayList<Team>();
			List<Game> games = new ArrayList<Game>();
			for (int team1 = 0; team1 < teams.length; team1++) {
				if (!used.contains(teams[team1].sched[week]) && !used.contains(teams[team1])) {
					used.add(teams[team1]);
					used.add(teams[team1].sched[week]);
					games.add(new Game(teams[team1], teams[team1].sched[week]));
				}
			}
			Game[] games_arr = new Game[6];
			games.toArray(games_arr);
			weeks[week].games = games_arr;
		}

		List<Week> list_weeks = Arrays.asList(weeks);
		Collections.shuffle(list_weeks);
		list_weeks.toArray(weeks);

		return weeks;
	}

	public void printByTeam() {
		for (int j = 0; j < teams.length; j++) {
			System.out.println(teams[j].name);
			System.out.println();
			for (int i = 0; i < num_weeks; i++) {
				System.out.println(teams[j].sched[i].name);
			}
			System.out.println();
			System.out.println();
		}
	}

	public void printByWeek() {
		if (weeks == null) {
			throw new RuntimeException();
		}
		for (int i = 0; i < weeks.length; i++) {
			if (weeks[i] == null) {
				throw new RuntimeException();
			}
		}
		for (int i = 0; i < num_weeks; i++) {
			System.out.println("Week: " + (i + 1));
			System.out.println();
			for (int j = 0; j < weeks[i].games.length; j++) {
				Game x = weeks[i].games[j];
				System.out.println(x.team1.name + " plays vs " + x.team2.name);
			}
			System.out.println();
			System.out.println();
		}
	}

	public static boolean chooseScheduleOutOfDiv(ScheduleDivisions schedule) {
		// check if it's complete.
		int div_len = schedule.div_sch_len;
		Team[] teams = schedule.teams;

		// schedule in division weeks.
		boolean complete = true;
		int week = 0;
		int team = 0;
		for (int i = div_len; i < schedule.num_weeks; i++) {
			for (int j = 0; j < teams.length; j++) {
				if (teams[j].sched[i] == null) {
					week = i;
					team = j;
					complete = false;
					break;
				}
			}
			if (!complete) {
				break;
			}
		}

		if (complete) {
			return true;
		}

		// schedule unscheduled game.
		for (int i = 0; i < teams[team].otherOpp.size(); i++) {
			Team pot_opp = teams[team].otherOpp.get(i);
			if (validSchOut(schedule, teams[team], pot_opp, week)) {
				teams[team].sched[week] = pot_opp;
				pot_opp.sched[week] = teams[team];
				if (chooseScheduleOutOfDiv(schedule)) {
					return true;
				} else {
					teams[team].sched[week] = null;
					pot_opp.sched[week] = null;
				}
			}
		}
		return false;

	}

	public static boolean validSchOut(ScheduleDivisions schedule, Team team1, Team team2, int week) {
		Team[] teams = schedule.teams;
		// this is only scheduling out of division teams.
		if (team1.divNum == team2.divNum) {
			return false;
		}
		// make sure they're unscheduled that week.
		if (team1.sched[week] != null || team2.sched[week] != null) {
			return false;
		}
		
		// too many games between divisions that week.
		// calculated by teams_in_div / (num_of_divs - 1). use remainder to calculate amount with high value. rest have floor.
		int low_week = schedule.teams_per_div / (schedule.num_div - 1);
		int max_week_ct = schedule.teams_per_div % (schedule.num_div - 1);
	
		
		//NOTE: THIS CURRENTLY DOESN'T DO ANYTHING WITH HMAP_SZN. BUT IT COULD BE EASILY DONE IF NEEDED. 
		//this would be to make sure one division isn't playing another division too much. 
		//this would be relevant in leagues with low amounts of divisions and high number of games.
		
		Map<String, Integer> hmap_szn = new HashMap<String, Integer>();
		Map<String, Integer> hmap_week = new HashMap<String, Integer>();
		Map<String, Integer> hmap_teams = new HashMap<String, Integer>();

		for(int i = 0; i < schedule.divisions.size()-1; i++) {
			for(int j = i+1; j < schedule.divisions.size(); j++) {
				String key = schedule.divisions.get(i) + "_" + schedule.divisions.get(j);
				hmap_szn.putIfAbsent(key, 0);
				hmap_week.putIfAbsent(key, 0);
			}
		}
		
		for(int i = 0; i < teams.length-1; i++) {
			for(int j = i+1; j < teams.length; j++) {
				if(teams[i].divNum == teams[j].divNum) {
					continue;
				}
				String key = teams[i].name + "_" + teams[j].name;
				hmap_teams.putIfAbsent(key, 0);
			}
		}
		
		
		for(int i = 0; i < teams.length; i++) {
			for(int j = 0; j <= week; j++) {
				Team first = teams[i];
				Team second = teams[i].sched[j];
				if(second == null) {
					continue;
				}
				String teams_key = first.name + "_" + second.name;
				if(hmap_teams.containsKey(teams_key)) {
					Integer curr = hmap_teams.get(teams_key);
					hmap_teams.replace(teams_key, curr+1);
				}
				
				
				String key = first.divNum + "_" + second.divNum;
				if(hmap_szn.containsKey(key)) {
					Integer curr = hmap_szn.get(key);
					hmap_szn.replace(key, curr+1);
				}
				
				if(j == week) {
					if(hmap_week.containsKey(key)) {
						Integer curr_week = hmap_week.get(key);
						hmap_week.replace(key, curr_week+1);
					}
				}
			}
		}
		// add teams in week who haven't been officially added yet.
		String key_temp = team1.divNum + "_" + team2.divNum;
		if(hmap_week.containsKey(key_temp)) {
			Integer curr_week = hmap_week.get(key_temp);
			hmap_week.replace(key_temp, curr_week+1);
		} else {
			key_temp = team2.divNum + "_" + team1.divNum;
			Integer curr_week = hmap_week.get(key_temp);
			hmap_week.replace(key_temp, curr_week+1);
		}
		// add teams to teamvteam map
		key_temp = team1.name + "_" + team2.name;
		if(hmap_teams.containsKey(key_temp)) {
			Integer curr_val = hmap_teams.get(key_temp);
			hmap_teams.replace(key_temp, curr_val+1);
		} else {
			key_temp = team2.name = "_" + team1.name;
			Integer curr_val = hmap_teams.get(key_temp);
			hmap_teams.replace(key_temp, curr_val+1);
		}

		// check week.
		int low_ct = 0;
		int high_ct = 0;
		for (Map.Entry<String, Integer> entry : hmap_week.entrySet()) {
			if(entry.getValue().intValue() == low_week) {
				low_ct++;
			} else if(entry.getValue().intValue() == low_week+1) {
				high_ct++;
			} else {
				continue;
			}
		}
		if(high_ct > max_week_ct) {
			return false;
		}
		
		
		
		// can't schedule someone twice until everyone has been scheduled once, etc.
		int min = teams.length;
		int max = 0;
		for (Map.Entry<String, Integer> entry : hmap_teams.entrySet()) {
			if(entry.getValue().intValue() < min) {
				min = entry.getValue().intValue();
			} else if(entry.getValue().intValue() > max) {
				max = entry.getValue().intValue();
			} else {
				continue;
			}
		}
		if(max - min >= 2) {
			return false;
		}
		
		
		return true;
	}
}