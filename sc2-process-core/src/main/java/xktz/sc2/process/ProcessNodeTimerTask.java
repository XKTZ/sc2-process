package xktz.sc2.process;

import java.util.TimerTask;

public class ProcessNodeTimerTask extends TimerTask {

    private ProcessNode node;

    private Emitter<ProcessNode> emitter;

    public ProcessNodeTimerTask(ProcessNode node, Emitter<ProcessNode> emitter) {
        this.node = node;
        this.emitter = emitter;
    }

    @Override
    public void run() {
        emitter.emit(node);
    }

    public ProcessNode getNode() {
        return node;
    }
}
