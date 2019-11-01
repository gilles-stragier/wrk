package net.ocheyedan.wrk.domain;

public interface View {

    default <T> T as(Class<T> clazz) {
        return (T) this;
    }
}
