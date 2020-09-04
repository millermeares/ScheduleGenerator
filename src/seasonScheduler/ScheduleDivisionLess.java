package seasonScheduler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScheduleDivisionLess extends Schedule{
	public ScheduleDivisionLess(String[] names, int num_weeks) {
		this.num_weeks = num_weeks;
		if(names.length % 2 == 0) {
			this.teams = new Team[names.length];
		} else {
			this.teams = new Team[names.length+1];
		}
		for(int i = 0; i < teams.length; i++) {
			teams[i] = new Team(names[i], num_weeks);
		}
		if(teams.length % 2 != 0) {
			teams[teams.length] = new Team("Bye", num_weeks);
		}
		int iter = (int) Math.ceil(num_weeks / (teams.length-1));
		for(int i = 0; i <= iter; i++) {
			int start = i * (teams.length - 1);
			// shuffle teams here.
			List<Team> teams_list = Arrays.asList(teams);
			Collections.shuffle(teams_list);
			teams_list.toArray(teams);
			System.out.println(scheduleGames(start, start + (teams.length - 1)));
		}		
	}
	public boolean scheduleGames(int start, int end) {
		if (end > teams[0].sched.length) {
			end = teams[0].sched.length;
		}
		// schedule this date range.
		boolean complete = true;
		int week = -1;
		int team = -1;
		for(int i = start; i < end; i++) {
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
		
		for(int i = 0; i < teams.length; i++) {
			Team pot_opp = teams[i];
			if(validSchedule(start, end, teams[team], teams[i], week)) {
				teams[team].sched[week] = pot_opp;
				pot_opp.sched[week] = teams[team];
				if(scheduleGames(start, end)) {
					return true;
				} else {
					teams[team].sched[week] = null;
					pot_opp.sched[week] = null;
				}
			} 
		}
		// then schedule this one.
		return false;
	}
	private boolean validSchedule(int start, int end, Team team1, Team team2, int week) {
		if(team1 == team2) {
			return false;
		}
		if(team2.sched[week]!= null || team1.sched[week] != null) {
			return false;
		}
		for(int i = start; i < end; i++) {
			for(int j = 0; j < teams.length; j++) {
				if(teams[j] == team1 && teams[j].sched[i] == team2) {
					return false;
				}
				if(teams[j] == team2 && teams[j].sched[i] == team1) {
					return false;
				}
			}
		}
		return true;
	}
}
