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
		
		
		
		

		
	}

}
