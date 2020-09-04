package soln_12_team_3_div;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Team team1 = new Team("Court","1");
		Team team2 = new Team("Stephen","1");
		Team team3 = new Team("Jon","1");
		Team team4 = new Team("Will","2");
		Team team5 = new Team("Josiah","2");
		Team team6 = new Team("Michael","2");
		Team team7 = new Team("Jackson","3");
		Team team8 = new Team("Ryan","3");
		
		Team team9 = new Team("Christian","3");		
		Team team10 = new Team("Harrison","4");
		
		Team team11 = new Team("Josh","4");
		
		Team team12 = new Team("Miller","4");
		/*
		Team team13= new Team("a","4");		
		Team team14 = new Team("b","4");
		Team team15 = new Team("c","4");
		Team team16 = new Team("d","4");
		*/
		Team[] teams = new Team[] {team1, team2, team3, team4, team5, team6, team7, team8, team9, team10, team11, team12};
		String[] names_12 = new String[] {"court", "stephen", "jon", "will", "josiah", "michael", "jackson", "ryan", "christian", "harrison", "josh", "miller"};
		String[] names_10 = new String[] {"court", "stephen", "jon", "will", "josiah", "michael", "jackson", "ryan", "christian", "harrison"};
		String[] names_8 = new String[] {"court", "stephen", "jon", "will", "josiah", "michael", "jackson", "ryan"};
		String[] names_16 = new String[] {"court", "stephen", "jon", "will", "josiah", "michael", "jackson", "ryan", "christian", "harrison", "josh", "miller", "jake", "laurie", "kenzie", "xd"};
		
		String[] division_names_12_4 = new String[] {"1","1","1","1","2","2","2","2","3","3","3","3"};
		String[] division_names_12_3 = new String[] {"1","1","1","2","2","2","3","3","3","4","4","4"};
		
		String[] division_names_16_4 = new String[] {"1","1","1","1","2","2","2","2","3","3","3","3","4","4","4","4"};
		String[] division_names_15_3 = new String[] {"1","1","1","1","1","2","2","2","2","2","3","3","3","3","3"};
		String[] division_names_10_5 = new String[] {"1","1","2","2","3","3","4","4","5","5"};
		String[] division_names_10_2 = new String[] {"1","1","1","1","1","2","2","2","2","2"};
		String[] division_names_15_5 = new String[] {"1","1","1","1","2","2","2","2","3","3","3","3","1","2","3"};

		ScheduleDivisions schedule_12_4 = new ScheduleDivisions(names_12, division_names_12_4, 13);
		ScheduleDivisions schedule_12_3 = new ScheduleDivisions(names_12, division_names_12_4, 13);
		ScheduleDivisions schedule_16_4 = new ScheduleDivisions(names_12, division_names_12_4, 13);
		ScheduleDivisions schedule_15_3 = new ScheduleDivisions(names_12, division_names_12_4, 13);
		ScheduleDivisions schedule_10_5 = new ScheduleDivisions(names_12, division_names_12_4, 13);
		ScheduleDivisions schedule_10_2 = new ScheduleDivisions(names_12, division_names_12_4, 13);
		ScheduleDivisions schedule_15_5 = new ScheduleDivisions(names_12, division_names_12_4, 13);

		
	}

}
