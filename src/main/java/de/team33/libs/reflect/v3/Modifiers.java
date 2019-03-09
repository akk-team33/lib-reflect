package de.team33.libs.reflect.v3;

import java.lang.reflect.Modifier;
import java.util.function.IntPredicate;

@SuppressWarnings("unused")
public final class Modifiers {

    public interface Predicate extends IntPredicate {

        Predicate TRUE = modifiers -> true;
        Predicate FALSE = modifiers -> false;
        Predicate NONE = modifiers -> 0 == modifiers;
        Predicate PUBLIC = Modifier::isPublic;
        Predicate PRIVATE = Modifier::isPrivate;
        Predicate PROTECTED = Modifier::isProtected;
        Predicate STATIC = Modifier::isStatic;
        Predicate FINAL = Modifier::isFinal;
        Predicate SYNCHRONIZED = Modifier::isSynchronized;
        Predicate VOLATILE = Modifier::isVolatile;
        Predicate TRANSIENT = Modifier::isTransient;
        Predicate NATIVE = Modifier::isNative;
        Predicate INTERFACE = Modifier::isInterface;
        Predicate ABSTRACT = Modifier::isAbstract;
    }
}
