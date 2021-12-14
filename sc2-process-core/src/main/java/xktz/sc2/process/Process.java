package xktz.sc2.process;

import java.util.List;

/**
 * The process
 */
public interface Process {

    /**
     * Start the process
     * @param off The time off (wait off seconds)
     */
    void start(long off);

    /**
     * Get the name
     *
     * @return name
     */
    String name();

    /**
     * Get the nodes
     *
     * @return the process
     */
    List<ProcessNode> process();

    /**
     * Get the emitter for the node
     * @param node the node
     */
    Emitter<ProcessNode> getEmitter(ProcessNode node);
}
