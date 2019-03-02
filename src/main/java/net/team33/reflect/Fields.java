package net.team33.reflect;

import de.team33.libs.reflect.v3.FieldStream;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class Fields<T> {

    private final Map<String, Field> backing;

    private Fields(final Builder<T> builder) {
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
                return new Entries(sample);
            }
        };
    }

    public final Mapper map(final Map<?, ?> map) {
        return new Mapper(map);
    }

    @SuppressWarnings({"FieldHasSetterButNoGetter", "unused"})
    public static final class Builder<T> {

        private final Class<T> subjectClass;

        private Function<Class<?>, Stream<Field>> toFieldStream = FieldStream.FLAT;
        private Function<Field, String> toFieldName = FieldName.SIMPLE;
        private Predicate<? super Field> filter = FieldFilter.SIGNIFICANT;

        private Builder(final Class<T> subjectClass) {
            this.subjectClass = subjectClass;
        }

        private Stream<Field> stream() {
            return toFieldStream.apply(subjectClass)
                    .filter(filter)
                    .peek(field -> field.setAccessible(true));
        }

        private void put(final Map<String, Field> map, final Field field) {
            map.put(toFieldName.apply(field), field);
        }

        public final Builder<T> setFilter(final Predicate<? super Field> filter) {
            this.filter = filter;
            return this;
        }

        public final Builder<T> setToFieldStream(final Function<Class<?>, Stream<Field>> toFieldStream) {
            this.toFieldStream = toFieldStream;
            return this;
        }

        public final Builder<T> setToFieldName(final Function<Field, String> toFieldName) {
            this.toFieldName = toFieldName;
            return this;
        }

        public final Builder<T> setToFieldNameByClass(final Function<Class<?>, Function<Field, String>> toToFieldName) {
            return setToFieldName(toToFieldName.apply(subjectClass));
        }

        public final Fields<T> build() {
            return new Fields<>(this);
        }
    }

    public final class Mapper {

        private final Map<?, ?> origin;

        private Mapper(final Map<?, ?> origin) {
            this.origin = origin;
        }

        public final T to(final T target) {
            backing.forEach((key, field) -> {
                try {
                    field.set(target, origin.get(key));
                } catch (final IllegalAccessException caught) {
                    throw new IllegalStateException("cannot set field [" + key + "]", caught);
                }
            });
            return target;
        }
    }

    private final class Entries extends AbstractSet<Map.Entry<String, Object>> {

        private final T sample;

        private Entries(final T sample) {
            this.sample = sample;
        }

        @Override
        public final java.util.Iterator<Map.Entry<String, Object>> iterator() {
            return new Iterator(backing.entrySet().iterator());
        }

        @Override
        public final int size() {
            return backing.size();
        }

        private final class Iterator implements java.util.Iterator<Map.Entry<String, Object>> {

            private final java.util.Iterator<Map.Entry<String, Field>> core;

            private Iterator(final java.util.Iterator<Map.Entry<String, Field>> core) {
                this.core = core;
            }

            @Override
            public final boolean hasNext() {
                return core.hasNext();
            }

            @Override
            public final Map.Entry<String, Object> next() {
                return new FieldEntry(core.next(), sample);
            }
        }
    }
}
