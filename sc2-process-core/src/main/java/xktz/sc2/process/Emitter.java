package xktz.sc2.process;

/**
 * Emiter
 *
 * @param <T>
 */
public interface Emitter<T> {

    /**
     * Emit something
     *
     * @param obj the object
     */
    void emit(T obj);
}
