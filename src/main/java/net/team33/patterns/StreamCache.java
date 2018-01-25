package net.team33.patterns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;

public final class StreamCache<K, E> {

    private final Map<K, Collection<E>> backing = new ConcurrentHashMap<>(0);
    private final Function<K, Stream<E>> origin;
    private final List<Predicate<E>> filters;

    private StreamCache(final Builder<K, E> builder) {
        this.origin = builder.origin;
        this.filters = unmodifiableList(new ArrayList<>(builder.filters));
    }

    public static <K, E> Builder<K, E> builder(final Function<K, Stream<E>> origin) {
        return new Builder<>(origin);
    }

    public final Stream<E> from(final K subject) {
        return Optional.ofNullable(backing.get(subject)).orElseGet(() -> {
            final Collection<E> result = filtered(subject)
                    .collect(Collectors.toSet());
            backing.put(subject, result);
            return result;
        }).stream();
    }

    private Stream<E> filtered(final K subject) {
        Stream<E> result = origin.apply(subject);
        for (final Predicate<E> filter : filters)
            result = result.filter(filter);
        return result;
    }

    public static final class Builder<K, E> {

        private final Function<K, Stream<E>> origin;
        private final List<Predicate<E>> filters = new LinkedList<>();

        private Builder(final Function<K, Stream<E>> origin) {
            this.origin = origin;
        }

        public final StreamCache<K, E> build() {
            return new StreamCache<>(this);
        }

        @SuppressWarnings("ParameterHidesMemberVariable")
        public Builder<K, E> addFilter(final Predicate<E> filter) {
            filters.add(filter);
            return this;
        }
    }
}
