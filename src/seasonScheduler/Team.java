package seasonScheduler;

import java.util.ArrayList;
import java.util.List;

public class Team {
	public String name;
	public List<Team> divOpp;
	public List<Team> otherOpp;
	public String divNum;
	public Team[] sched;
	public int num_weeks;
	public Team(String name, String divNum, int num_weeks) {
		this.name = name;
		this.divNum = divNum;
		divOpp = new ArrayList<Team>();
		otherOpp = new ArrayList<Team>();
		this.num_weeks = num_weeks;
		sched = new Team[num_weeks];
	}
	public Team(String name, int num_weeks) {
		this.name = name;
		sched = new Team[num_weeks];
	}
}
