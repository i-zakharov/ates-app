package ru.zim.ates.auth.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamUtil {
    public static <I, O> List<O> map(List<I> addresses, Function<I, O> mapper) {
        return addresses.stream().map(mapper).collect(Collectors.toList());
    }
}
