package seasonScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Schedule implements ScheduleInterface {
	public Team[] teams;
	public int num_weeks;
	public Week[] weeks;
	
	public Week[] constructSchedule() {
		weeks = new Week[num_weeks];
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
			if (teams[j] == null) {
				continue;
			}
			System.out.println(teams[j].name);
			System.out.println();
			for (int i = 0; i < num_weeks; i++) {
				if (teams[j].sched[i] == null) {
					continue;
				}
				System.out.println(teams[j].sched[i].name + ": " + i);
			}
			System.out.println();
			System.out.println();
		}
	}

	public void printByWeek() {
		if (weeks == null) {
			weeks = constructSchedule();
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

}
