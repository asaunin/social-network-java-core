package common;

@FunctionalInterface
public interface Wrapper<T> {
    @Private
    T toSrc();
}
