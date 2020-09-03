package soln_12_team_3_div;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Team team1 = new Team("Court",1);
		Team team2 = new Team("Stephen",1);
		Team team3 = new Team("Jon",1);
		Team team4 = new Team("Will",1);
		Team team5 = new Team("Josiah",2);
		Team team6 = new Team("Michael",2);
		Team team7 = new Team("Jackson",2);
		Team team8 = new Team("Ryan",2);
		Team team9 = new Team("Christian",3);		
		Team team10 = new Team("Harrison",3);
		Team team11 = new Team("Josh",3);
		Team team12 = new Team("Miller",3);
		
		Team[] teams = new Team[] {team1, team2, team3, team4, team5, team6, team7, team8, team9, team10, team11, team12};
		Schedule schedule = new Schedule(teams, 13);
		
		schedule.generateTeams();
		// now schedule in division.
		Schedule.scheduleInDivison(schedule);
		Schedule.scheduleOutDivison(schedule);
		schedule.constructSchedule();
		
		/*
		 * print opponent list out of order for each team.
		for(int j = 0; j < schedule.teams.length; j++) {
			System.out.println(schedule.teams[j].name);
			System.out.println();
			for(int i = 0; i < schedule.num_weeks; i++) {
				System.out.println(schedule.teams[j].sched[i].name);
			}
			System.out.println();
			System.out.println();
		}
		*/
		
		
		for(int i = 0; i < schedule.num_weeks; i++) {
			System.out.println("Week: " + (i+1));
			System.out.println();
			for(int j = 0; j < schedule.weeks[i].games.length; j++) {
				Game x = schedule.weeks[i].games[j];
				System.out.println(x.team1.name + " plays vs " + x.team2.name);
			}
			System.out.println();
			System.out.println();
		}
		
		

		
	}

}
