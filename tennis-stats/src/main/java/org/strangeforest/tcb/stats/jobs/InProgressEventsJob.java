package org.strangeforest.tcb.stats.jobs;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import org.strangeforest.tcb.stats.service.*;

import static java.util.Arrays.*;

@Component
@Profile("openshift")
public class InProgressEventsJob extends DataLoadJob {

	@Autowired private DataService dataService;

	@Scheduled(cron = "${tennis-stats.jobs.reload-in-progress-events:0 0 * * * *}")
	public void reloadInProgressEvents() {
		execute();
	}

	@Override protected Collection<String> params() {
		return asList("-ip", "-c 1");
	}

	@Override protected String onSuccess() {
		dataService.evictGlobal("InProgressEvents");
		int cacheCount = dataService.clearCaches("InProgressEventForecast");
		return cacheCount + " cache(s) cleared.";
	}
}
