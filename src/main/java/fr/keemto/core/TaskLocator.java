package fr.keemto.core;

import fr.keemto.core.fetching.FetchingTask;
import fr.keemto.core.fetching.FetchingTaskFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TaskLocator {

    private static final Logger log = LoggerFactory.getLogger(TaskLocator.class);

    private final UserRepository userRepository;
    private final List<Task> tasks;
    private final FetchingTaskFactory fetchingTaskFactory;

    public TaskLocator(FetchingTaskFactory fetchingTaskFactory, UserRepository userRepository, List<Task> tasks) {
        this.fetchingTaskFactory = fetchingTaskFactory;
        this.userRepository = userRepository;
        this.tasks = tasks;
    }

    public List<Task> findTasks() {
        List<Task> foundTasks = new ArrayList<Task>(tasks);
        for (User user : userRepository.getAllUsers()) {
            List<FetchingTask> fetchingTasks = fetchingTaskFactory.createTasks(user);
            foundTasks.addAll(fetchingTasks);
        }
        return foundTasks;
    }

}
