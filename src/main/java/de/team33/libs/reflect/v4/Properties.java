package de.team33.libs.reflect.v4;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.unmodifiableList;

public class Properties<T> {

    private final Class<T> type;
    private final List<Property<T>> backing;

    private Properties(final Builder<T> builder) {
        this.type = builder.type;
        this.backing = unmodifiableList(new ArrayList<>(builder.backing));
    }

    public static <T> Builder<T> builder(final Class<T> type) {
        return new Builder<>(type);
    }

    public final Map<String, Object> map(final T subject) {
        return new AbstractMap<String, Object>() {
            private Set<Entry<String, Object>> entries = new EntrySet(subject);

            @Override
            public Set<Entry<String, Object>> entrySet() {
                return entries;
            }
        };
    }

    public final T remap(final Map<String, Object> map, final T target) {
        for (final Property<T> property : backing) {
            if (map.containsKey(property.getName())) {
                property.set(target, map.get(property.getName()));
            }
        }
        return target;
    }


    public static class Builder<T> {

        private final Class<T> type;
        private final List<Property<T>> backing = new LinkedList<>();

        private Builder(final Class<T> type) {
            this.type = type;
        }

        public final Properties<T> build() {
            return new Properties<>(this);
        }

        public final Builder<T> add(final Property<T> property) {
            backing.add(property);
            return this;
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<String, Object>> {

        private final T subject;

        private EntrySet(final T subject) {
            this.subject = subject;
        }

        @Override
        public final Iterator<Map.Entry<String, Object>> iterator() {
            return new EntryIterator(subject, backing.iterator());
        }

        @Override
        public final int size() {
            return backing.size();
        }
    }

    private class EntryIterator implements Iterator<Map.Entry<String, Object>> {

        private final T subject;
        private final Iterator<Property<T>> iterator;

        private EntryIterator(final T subject, final Iterator<Property<T>> iterator) {
            this.subject = subject;
            this.iterator = iterator;
        }

        @Override
        public final boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Map.Entry<String, Object> next() {
            final Property<T> property = iterator.next();
            return new AbstractMap.SimpleImmutableEntry<>(property.getName(), property.get(subject));
        }
    }
}
