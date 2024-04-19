package entity.actions;

@FunctionalInterface
public interface EntityAttackAction<T, V> {
    void execute(T param1, V param2);
}
