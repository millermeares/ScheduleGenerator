package seasonScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ScheduleDivisions extends Schedule {
	public int num_div;
	public int teams_per_div;
	public List<String> divisions;

	public ScheduleDivisions(String[] names, String[] division_arr, int num_weeks) {
		if (num_weeks < 1) {
			throw new NullPointerException("num weeks must be positive");
		}
		if (names == null || division_arr == null || names.length != division_arr.length) {
			throw new NullPointerException("must enter names and divisions");
		}
		teams = new Team[names.length];
		for (int i = 0; i < names.length; i++) {
			if (names[i] == null || division_arr[i] == null) {
				throw new IllegalArgumentException("can't have a null team or division name");
			}
			teams[i] = new Team(names[i], division_arr[i], num_weeks);
		}
		this.num_weeks = num_weeks;

		divisions = new ArrayList<String>();
		for (int i = 0; i < teams.length; i++) {
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
		
		for (int i = 0; i < teams.length; i++) {
			if (teams[i].divOpp.size() != teams[0].divOpp.size()) {
				throw new IllegalArgumentException("must have same amount of teams in each division");
			}
		}
		
		// this is where i do randomization. this method is necessary because backtrack search does better when the division teams are next to each other.
		List<List<Team>> teams_by_div = new ArrayList<List<Team>>();
		for (int i = 0; i < divisions.size(); i++) {
			teams_by_div.add(new ArrayList<Team>());
		}
		
		for (int i = 0; i < teams.length; i++) {
			// based on what index they are in divisions, add them to a teams_by_div list of lists
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

		System.out.println(scheduleAll());
		
	}

	public boolean scheduleAll() {
		// check completed.
		boolean complete = true;
		int team = -1;
		int week = -1;
		for(int i = 0; i < num_weeks; i++) {
			for(int j = 0; j < teams.length; j++) {
				if(teams[j].sched[i] == null) {
					complete = false;
					week = i;
					team = j;
					break;
				}
			}
			if(!complete) {
				break;
			}
		}
		if(complete) {
			return true;
		}
		Team team1 = teams[team];
		for(int i = 0; i < teams.length; i++) {
			Team team2 = teams[i];
			if(team1 == team2) {
				continue;
			}
			if(validSchedule(team1, team2, week)) {
				team1.sched[week] = team2;
				team2.sched[week] = team1;
				if(scheduleAll()) {
					return true;
				} else {
					team1.sched[week] = null;
					team2.sched[week] = null;
				}
				
			}
		}
		
		return false;
	}
	private boolean validSchedule(Team team1, Team team2, int week) {
		// make sure that no team is already scheduled.
		if(team1.sched[week] != null || team2.sched[week] != null) {
			return false;
		}
		
		int tm_ctr = 0;
		for(int i = 0; i < team1.sched.length; i++) {
			if(team1.sched[i] == null) {
				continue;
			}
			if(team1.sched[i] == team2) {
				tm_ctr++;
			}
		}
		if(team1.divNum == team2.divNum) {
			// if team is in division, make sure that they're not ocurring more than twice.
			if(tm_ctr >= 2) {
				return false;
			}
		} else {
			// if team is out of division, make sure that they're not occurring more than they're supposed to. 
			// make sure they haven't already played max times
			int teams_in_div = teams.length / num_div;
			int in_div_games = (teams_in_div - 1) * 2;
			int out_div_games = num_weeks - in_div_games;
			
			// max is either 1 or 2. if out_div_games > out_div_teams.length, then it's 2.
			int max = 0;
			if(out_div_games > (teams.length - teams_in_div)) {
				max = 2;
			} else {
				max = 1;
			}
			if(tm_ctr >= max) {
				return false;
			}
			int num_with_max = out_div_games % (teams.length - teams_in_div);
			if(num_with_max == 0) {
				num_with_max = (teams.length - teams_in_div);
			}
			int min = max - 1;
			// check out of division games for team1 and team2. make sure they don't already have num_with_max games.
			// team1
			Map<String, Integer> hmap = new HashMap<String, Integer>();
			for(int i = 0; i < team1.sched.length; i++) {
				if(team1.sched[i] == null) {
					continue;
				}
				if(team1.divNum == team1.sched[i].divNum) {
					continue;
				}
				hmap.putIfAbsent(team1.sched[i].name, 0);
				int curr_val = hmap.get(team1.sched[i].name);
				hmap.replace(team1.sched[i].name, curr_val+1);
			}
			Map<String, Integer> hmap_2 = new HashMap<String, Integer>();
			for(int i = 0; i < team2.sched.length; i++) {
				if(team2.sched[i] == null) {
					continue;
				}
				if(team2.divNum == team2.sched[i].divNum) {
					continue;
				}
				hmap_2.putIfAbsent(team2.sched[i].name, 0);
				int curr_val = hmap_2.get(team2.sched[i].name);
				hmap_2.replace(team2.sched[i].name, curr_val+1);
			}
			// get amount of values at max.
			int amt_max_1 = 0;
			for (Map.Entry<String, Integer> entry : hmap.entrySet()) {
				if (entry.getValue().intValue() == max) {
					amt_max_1++;
				} 
			}
			
			int amt_max_2 = 0;
			for (Map.Entry<String, Integer> entry : hmap_2.entrySet()) {
				if (entry.getValue().intValue() == max) {
					amt_max_2++;
				} 
			}
			
			if(amt_max_2 > num_with_max - 1 || amt_max_1 > num_with_max - 1) {
				return false;
			}
		}
		return true;
	}

}