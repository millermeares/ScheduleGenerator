package seasonScheduler;

public class Week {
	Game[] games;
	public Week(int amt_teams) {
		games = new Game[amt_teams / 2];
	}
}
