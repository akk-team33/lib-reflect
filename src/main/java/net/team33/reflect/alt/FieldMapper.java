package net.team33.reflect.alt;

import net.team33.reflect.FieldFilter;
import net.team33.reflect.FieldName;
import net.team33.reflect.FieldStream;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class FieldMapper<T> {

    private final Map<String, Field> backing;

    private FieldMapper(final Builder<T> builder) {
        this.backing = Collections.unmodifiableMap(builder.stream()
                .collect(TreeMap::new, builder::put, Map::putAll));
    }

    public static <T> Builder<T> of(final Class<T> subjectClass) {
        return new Builder<>(subjectClass);
    }

    public final Map<String, Object> map(final T sample) {
        //noinspection AnonymousInnerClassMayBeStatic,AnonymousInnerClass
        return new AbstractMap<String, Object>() {
            @Override
            public Set<Entry<String, Object>> entrySet() {
                return new AbstractSet<Entry<String, Object>>() {
                    @Override
                    public Iterator<Entry<String, Object>> iterator() {
                        return new Cursor(backing.entrySet().iterator(), sample);
                    }

                    @Override
                    public int size() {
                        return backing.size();
                    }
                };
            }
        };
    }

    public final Origin map(final Map<String, Object> map) {
        return new Origin(map);
    }

    @SuppressWarnings("FieldHasSetterButNoGetter")
    public static final class Builder<T> {

        private final Class<T> subjectClass;

        private Function<Class<?>, Stream<Field>> fields = FieldStream.FLAT;
        private Function<Field, String> toKey = FieldName.SIMPLE;
        private Predicate<? super Field> filter = FieldFilter.SIGNIFICANT;

        private Builder(final Class<T> subjectClass) {
            this.subjectClass = subjectClass;
        }

        private Stream<Field> stream() {
            return fields.apply(subjectClass)
                    .filter(filter)
                    .peek(field -> field.setAccessible(true));
        }

        private void put(final Map<String, Field> map, final Field field) {
            map.put(toKey.apply(field), field);
        }

        public void setFields(final Function<Class<?>, Stream<Field>> fields) {
            this.fields = fields;
        }

        public void setToKey(final Function<Field, String> toKey) {
            this.toKey = toKey;
        }

        public void setFilter(final Predicate<? super Field> filter) {
            this.filter = filter;
        }

        public FieldMapper<T> build() {
            return new FieldMapper<>(this);
        }
    }

    private static final class Cursor implements Iterator<Map.Entry<String, Object>> {

        private final Iterator<Map.Entry<String, Field>> backing;
        private final Object subject;

        private Cursor(final Iterator<Map.Entry<String, Field>> backing, final Object subject) {
            this.backing = backing;
            this.subject = subject;
        }

        @Override
        public final boolean hasNext() {
            return backing.hasNext();
        }

        @Override
        public final Map.Entry<String, Object> next() {
            final Map.Entry<String, Field> next = backing.next();
            final String key = next.getKey();
            try {
                return new AbstractMap.SimpleImmutableEntry<>(key, next.getValue().get(subject));
            } catch (final IllegalAccessException caught) {
                throw new IllegalStateException("cannot get field [" + key + "]", caught);
            }
        }
    }

    public class Origin {

        private final Map<String, ?> map;

        public Origin(final Map<String, ?> map) {
            //noinspection AssignmentToCollectionOrArrayFieldFromParameter
            this.map = map;
        }

        public final T to(final T target) {
            for (final Map.Entry<String, Field> entry : backing.entrySet()) {
                final String key = entry.getKey();
                try {
                    entry.getValue().set(target, map.get(key));
                } catch (final IllegalAccessException caught) {
                    throw new IllegalStateException("cannot set field [" + key + "]", caught);
                }
            }
            return target;
        }
    }
}
