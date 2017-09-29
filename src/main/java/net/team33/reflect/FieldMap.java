package net.team33.reflect;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unused", "AbstractClassNeverImplemented"})
public abstract class FieldMap<T> extends AbstractMap<String, Object> {

    private final T subject;
    private final Set<Entry<String, Object>> entrySet = new EntrySet();

    protected FieldMap(final T subject) {
        this.subject = subject;
    }

    protected abstract Map<String, Field> getFields();

    @Override
    public final Set<Entry<String, Object>> entrySet() {
        return entrySet;
    }

    private class EntrySet extends AbstractSet<Entry<String, Object>> {
        @Override
        public final Iterator<Entry<String, Object>> iterator() {
            return new EntryIterator(getFields().entrySet().iterator());
        }

        @Override
        public final int size() {
            return getFields().size();
        }
    }

    private final class EntryIterator implements Iterator<Entry<String, Object>> {
        private final Iterator<Entry<String, Field>> backing;

        private EntryIterator(final Iterator<Entry<String, Field>> backing) {
            this.backing = backing;
        }

        @Override
        public final boolean hasNext() {
            return backing.hasNext();
        }

        @Override
        public final Entry<String, Object> next() {
            final Entry<String, Field> entry = backing.next();
            final String key = entry.getKey();
            try {
                return new SimpleImmutableEntry<>(key, entry.getValue().get(subject));
            } catch (final IllegalAccessException caught) {
                throw new IllegalStateException("cannot get value of [" + key + "]", caught);
            }
        }
    }
}
