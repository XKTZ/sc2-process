package xktz.sc2.process;

import xktz.exception.ExceptionHandleable;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ParallelProcess implements Process, ExceptionHandleable {

    private String name;

    private List<ProcessNode> process;

    private long offend;

    private Runnable endTask;

    public ParallelProcess(String name, List<ProcessNode> process, long offend, Runnable endTask) {
        this.name = name;
        this.process = new ArrayList<>(process);
        this.offend = offend;
        this.endTask = endTask;
    }

    @Override
    public void start(long off) {
        try {
            Thread.sleep(off);
        } catch (InterruptedException e) {
            handle(e);
            return;
        }
        Timer timer = new Timer();
        // tasks
        List<ProcessNodeTimerTask> tasks = process.stream().map(
                node -> new ProcessNodeTimerTask(node, getEmitter(node))
        ).sorted(Comparator.comparingLong(a -> a.getNode().getTime())).collect(Collectors.toList());
        // get the end time
        for (var task: tasks) {
            timer.schedule(task, task.getNode().getTime());
        }
        // schedule ending
        long timeMax = tasks.isEmpty() ? offend: tasks.get(tasks.size() - 1).getNode().getTime() + offend;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                endTask.run();
                timer.cancel();
            }
        }, timeMax);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public List<ProcessNode> process() {
        return process;
    }

}
