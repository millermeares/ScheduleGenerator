package soln_12_team_3_div;

import java.util.ArrayList;
import java.util.List;

public class Team {
	public String name;
	public List<Team> divOpp;
	public List<Team> otherOpp;
	public int divNum;
	public Team[] sched;
	public Team(String name, int divNum) {
		this.name = name;
		this.divNum = divNum;
		divOpp = new ArrayList<Team>();
		otherOpp = new ArrayList<Team>();
		sched = new Team[13];
	}
}
