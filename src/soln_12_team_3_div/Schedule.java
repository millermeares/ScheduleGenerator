package soln_12_team_3_div;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Schedule {
	public Week[] weeks;
	public Team[] teams;
	public int num_weeks;
	public int div_sch_len;
	public int num_div;
	public int teams_per_div;
	public List<Integer> divisions;
	public Schedule(Team[] teams, int num_weeks) {
		this.num_weeks = num_weeks;
		this.weeks = new Week[num_weeks];
		this.teams = teams;
		// this is where i add randomization.
		divisions = new ArrayList<Integer>();
		for(int i = 0; i < teams.length; i++) {
			teams[i].setWeeks(num_weeks);
			if(!divisions.contains(teams[i].divNum)) {
				divisions.add(teams[i].divNum);
			}
		}
		num_div = divisions.size();
		teams_per_div = teams.length / num_div;
		
		// checks for viability.
		if(teams.length % 2 != 0) {
			throw new IllegalArgumentException("Must have even number of teams.");
		}
		if(teams.length % num_div != 0) {
			throw new IllegalArgumentException("Must be an even number of teams in each division.");
		}
		if((teams_per_div-1) * 2 > num_weeks) {
			throw new IllegalArgumentException("Must be able to play each in-division team twice.");
		}
		if((teams.length-1) * 2 < num_weeks) {
			throw new IllegalArgumentException("Can't play any team more than twice.");
		}
		
		
		// list of lists of teams in division.
		
		List<List<Team>> teams_by_div = new ArrayList<List<Team>>();
		for(int i = 0; i < divisions.size(); i++) {
			teams_by_div.add(new ArrayList<Team>());
		}
		for(int i = 0; i < teams.length; i++) {
			// based on what index they are in divisions, add them to a teams_by_div list of lists.
			for(int j = 0; j < divisions.size(); j++) {
				if(divisions.get(j) == teams[i].divNum) {
					if(teams_by_div.get(j) == null) {
						teams_by_div.set(j, new ArrayList<Team>());
					}
					teams_by_div.get(j).add(teams[i]);
					break;
				}
			}
		}
		for(int i = 0; i < teams_by_div.size(); i++) {
			Collections.shuffle(teams_by_div.get(i));
		}
		
		
		Collections.shuffle(teams_by_div);
		
		
		
		List<Team> shuffled_teams = new ArrayList<Team>();
		
		for(int i = 0; i < teams_by_div.size(); i++) {
			for(int j = 0; j < teams_by_div.get(i).size(); j++) {
				shuffled_teams.add(teams_by_div.get(i).get(j));
			}
		}
		shuffled_teams.toArray(teams);
		
		// reassign division numbers. DELETE THIS ONCE THAT'S IMPLEMENTED
		for(int i = 0; i < teams.length; i++) {
			teams[i].divNum = (i / 4) + 1;
		}
			
		// get in division opponents. this should work fine. 
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
		
		div_sch_len = teams[0].divOpp.size();
	}
	public void generateTeams() {
		// keep a counter for each division. can only get to 2 for each division. 
		// this is the hard part. only can get ? from each division. Why, how.. etc.
		// then, how to keep track of how many assigned per division.
		int one_to_two = 0;
		int one_to_three = 0;
		int two_to_three = 0;
		
		for(int i = 0; i < teams.length; i++) {
			for(int j = 0; j < teams.length; j++) {
				if(teams[j].otherOpp.size() == num_weeks - div_sch_len || teams[i].otherOpp.size() == num_weeks - div_sch_len) {
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
		int div_len = schedule.div_sch_len;
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
		int div_len = schedule.div_sch_len;
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
	public void printByTeam() {
		for(int j = 0; j < teams.length; j++) {
			System.out.println(teams[j].name);
			System.out.println();
			for(int i = 0; i < num_weeks; i++) {
				System.out.println(teams[j].sched[i].name);
			}
			System.out.println();
			System.out.println();
		}
	}
	public void printByWeek() {
		if(weeks == null) {
			throw new RuntimeException();
		}
		for(int i = 0; i < weeks.length; i++) {
			if(weeks[i] == null) {
				throw new RuntimeException();
			}
		}
		for(int i = 0; i < num_weeks; i++) {
			System.out.println("Week: " + (i+1));
			System.out.println();
			for(int j = 0; j < weeks[i].games.length; j++) {
				Game x = weeks[i].games[j];
				System.out.println(x.team1.name + " plays vs " + x.team2.name);
			}
			System.out.println();
			System.out.println();
		}
	}
}

// something wrong with shuffling. works without it.