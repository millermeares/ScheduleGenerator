package seasonScheduler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] names_12 = new String[] {"court", "stephen", "jon", "will", "josiah", "michael", "jackson", "ryan", "christian", "harrison", "josh", "miller"};
		String[] names_10 = new String[] {"court", "stephen", "jon", "will", "josiah", "michael", "jackson", "ryan", "christian", "harrison"};
		String[] names_8 = new String[] {"court", "stephen", "jon", "will", "josiah", "michael", "jackson", "ryan"};
		String[] names_16 = new String[] {"court", "stephen", "jon", "will", "josiah", "michael", "jackson", "ryan", "christian", "harrison", "josh", "miller", "jake", "laurie", "kenzie", "xd"};
		String[] names_15 = new String[] {"court", "stephen", "jon", "will", "josiah", "michael", "jackson", "ryan", "christian", "harrison", "josh", "miller", "laurie", "kenzie", "xd"};
		String[] names_6 = new String[] {"court", "stephen", "jon", "will", "josiah", "michael"};

		String[] division_names_12_4 = new String[] {"1","1","1","1","2","2","2","2","3","3","3","3"};
		String[] division_names_12_3 = new String[] {"1","1","1","2","2","2","3","3","3","4","4","4"};
		String[] division_names_16_4 = new String[] {"1","1","1","1","2","2","2","2","3","3","3","3","4","4","4","4"};
		String[] division_names_15_3 = new String[] {"1","1","1","1","1","2","2","2","2","2","3","3","3","3","3"};
		String[] division_names_10_5 = new String[] {"1","1","2","2","3","3","4","4","5","5"};
		String[] division_names_10_2 = new String[] {"1","1","1","1","1","2","2","2","2","2"};
		String[] division_names_15_5 = new String[] {"1","1","1","1","2","2","2","2","3","3","3","3","1","2","3"};
		String[] division_names_8_2 = new String[] {"1","1","1","1","2","2","2","2"};
		String[] division_names_8_4 = new String[] {"1","1","0","0","2","2","3","3"};
		String[] division_names_6_3 = new String[] {"1","1","3","3","2","2"};
		String[] division_names_6_2 = new String[] {"1","1","1","2","2","2"};

		
//		ScheduleDivisions schedule_12_4 = new ScheduleDivisions(names_12, division_names_12_4, 13);
//		schedule_12_4.printByWeek();
//		ScheduleDivisions schedule_12_3 = new ScheduleDivisions(names_12, division_names_12_3, 13);
//		ScheduleDivisions schedule_16_4 = new ScheduleDivisions(names_16, division_names_16_4, 13);
//		ScheduleDivisions schedule_15_3 = new ScheduleDivisions(names_15, division_names_15_3, 13);
//		ScheduleDivisions schedule_10_5 = new ScheduleDivisions(names_10, division_names_10_5, 13);
//		ScheduleDivisions schedule_10_2 = new ScheduleDivisions(names_10, division_names_10_2, 13);
//		ScheduleDivisions schedule_15_5 = new ScheduleDivisions(names_15, division_names_15_5, 13);
//		ScheduleDivisions schedule_8_2 = new ScheduleDivisions(names_8, division_names_8_2, 13);
//		ScheduleDivisions schedule_8_4 = new ScheduleDivisions(names_8, division_names_8_4, 13);
//		ScheduleDivisions schedule_6_3 = new ScheduleDivisions(names_6, division_names_6_3, 13);
//		ScheduleDivisions schedule_6_2 = new ScheduleDivisions(names_6, division_names_6_2, 13);
		Schedule schedule_12_non = new ScheduleDivisionLess(names_12, 13);
		schedule_12_non.printByTeam();
	}

}
