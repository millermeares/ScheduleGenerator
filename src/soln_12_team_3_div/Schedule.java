package soln_12_team_3_div;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Schedule {
	public Week[] weeks;
	public Team[] teams;
	public int num_weeks;
	public int div_len;
	public Schedule(Team[] teams, int num_weeks) {
		this.num_weeks = num_weeks;
		this.weeks = new Week[num_weeks];
		this.teams = teams;
		// this is where i add randomization.

		List<Team> div1 = new ArrayList<Team>();
		List<Team> div2 = new ArrayList<Team>();
		List<Team> div3 = new ArrayList<Team>();
		for(int i = 0; i < teams.length; i++) {
			if(teams[i].divNum == 1) {
				div1.add(teams[i]);
			} else if (teams[i].divNum ==2) {
				div2.add(teams[i]);
			} else {
				div3.add(teams[i]);
			}
		}
		Collections.shuffle(div1);
		Collections.shuffle(div2);
		Collections.shuffle(div3);
		List<List<Team>> overall_list = new ArrayList<List<Team>>();
		overall_list.add(div1);
		overall_list.add(div2);
		overall_list.add(div3);
		Collections.shuffle(overall_list);
		
		List<Team> shuffled_teams = new ArrayList<Team>();
		
		for(int i = 0; i < overall_list.size(); i++) {
			for(int j = 0; j < overall_list.get(i).size(); j++) {
				shuffled_teams.add(overall_list.get(i).get(j));
			}
		}
		shuffled_teams.toArray(teams);
		
		// reassign division numbers.
		for(int i = 0; i < teams.length; i++) {
			teams[i].divNum = (i / 4) + 1;
		}
			
		// get in division opponents. 
		for(int i = 0; i < teams.length; i++) {
			for(int j = 0; j < teams.length; j++) {
				if(i ==j ) {
					continue;
				}
				if(teams[i].divNum == teams[j].divNum) {
					teams[i].divOpp.add(teams[j]);
					teams[i].divOpp.add(teams[j]);
				} else {
					teams[i].otherOpp.add(teams[j]);
				}
			}
		}
		
		div_len = teams[0].divOpp.size();
	}
	public void generateTeams() {
		// keep a counter for each division. can only get to 2 for each divison. 
		int one_to_two = 0;
		int one_to_three = 0;
		int two_to_three = 0;
		
		for(int i = 0; i < teams.length; i++) {
			for(int j = 0; j < teams.length; j++) {
				if(teams[j].otherOpp.size() == num_weeks - div_len || teams[i].otherOpp.size() == num_weeks - div_len) {
					continue;
				}
				if(teams[i].divNum == teams[j].divNum) {
					continue;
				}
				if(teams[i].divNum == 1 && teams[j].divNum == 2) {
					if(one_to_two == 2) {
						continue;
					}
					one_to_two++;
				}
				if(teams[i].divNum == 1 && teams[j].divNum == 3) {
					if(one_to_three == 2) {
						continue;
					}
					one_to_three++;
				}
				if(teams[i].divNum == 2 && teams[j].divNum == 3) {
					if(two_to_three == 2) {
						continue;
					}
					two_to_three++;
				}
				teams[i].otherOpp.remove(teams[j]);
				teams[j].otherOpp.remove(teams[i]);
			}
			
		}
	}
	// in both cases, use week by week. 
	
	// both of these will use backtracking.
	public static boolean scheduleInDivison(Schedule schedule) {
		int div_len = schedule.div_len;
		Team[] teams = schedule.teams;
		
		// schedule in division weeks.
		boolean complete = true;
		int week = 0;
		int team = 0;
		for(int i = 0; i < div_len; i++) {
			for(int j = 0; j < teams.length; j++) {
				if(teams[j].sched[i] == null) {
					week = i;
					team = j;
					complete = false;
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
		
		//schedule unscheduled game.
		for(int i = 0; i < teams[team].divOpp.size(); i++) {
			Team pot_opp = teams[team].divOpp.get(i);
			if(validInDiv(teams[team], pot_opp, week)) {
				teams[team].sched[week] = pot_opp;
				pot_opp.sched[week] = teams[team];
				if(scheduleInDivison(schedule)) {
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
		if(team1.divNum != team2.divNum) {
			return false;
		}
		// make sure they're unscheduled that week.
		if(team1.sched[week] != null || team2.sched[week] != null) {
			return false;
		}
		// make sure that they've only played each other once at most. 
		int game_vs_each_other = 0;
		for(int i = 0; i <= week; i++) {
			if(team1.sched[i] == team2) {
				game_vs_each_other++;
			}
		}
		if(game_vs_each_other > 1) {
			return false;
		}
		return true;
	}
	public static boolean scheduleOutDivison(Schedule schedule) {
		int div_len = schedule.div_len;
		Team[] teams = schedule.teams;
		
		// schedule in division weeks.
		boolean complete = true;
		int week = 0;
		int team = 0;
		for(int i = div_len; i < schedule.num_weeks; i++) {
			for(int j = 0; j < teams.length; j++) {
				if(teams[j].sched[i] == null) {
					week = i;
					team = j;
					complete = false;
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
		
		//schedule unscheduled game.
		for(int i = 0; i < teams[team].otherOpp.size(); i++) {
			Team pot_opp = teams[team].otherOpp.get(i);
			if(validOutDiv(teams[team], pot_opp, week, teams)) {
				teams[team].sched[week] = pot_opp;
				pot_opp.sched[week] = teams[team];
				if(scheduleOutDivison(schedule)) {
					return true;
				} else {
					teams[team].sched[week] = null;
					pot_opp.sched[week] = null;
				}
			}
		}
		return false;
		
	}
	public static boolean validOutDiv(Team team1, Team team2, int week, Team[] teams) {
		if(team1.divNum == team2.divNum) {
			return false;
		}
		// make sure they're unscheduled that week.
		if(team1.sched[week] != null || team2.sched[week] != null) {
			return false;
		}
		
		// get in amount of games in between these divisions this week.
		int against_division = 0;
		for(int i = 0; i < teams.length; i++) {
			if(teams[i].sched[week] != null && teams[i].divNum == team1.divNum && teams[i].sched[week].divNum == team2.divNum) {
				against_division++;
			}
		}
		if(against_division > 1) {
			return false;
		}
		// make sure each team isn't scheduled against the other yet.
		for(int i = 0; i < teams.length; i++) {
			for(int j = 0; j <= week; j++) {
				if(teams[i] == team1 && teams[i].sched[j] == team2) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public Week[] constructSchedule() {
		// construct week by week schedule.
		for(int week = 0; week < num_weeks; week++) {
			weeks[week] = new Week(teams.length);
			List<Team> used = new ArrayList<Team>();
			List<Game> games = new ArrayList<Game>();
			for(int team1 = 0; team1 < teams.length; team1++) {
				if(!used.contains(teams[team1].sched[week]) && !used.contains(teams[team1])) {
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
}

// something wrong with shuffling. works without it.