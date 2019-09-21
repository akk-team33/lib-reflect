package de.team33.test.reflect.common;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AnyBean {

    private int privateInt;
    private String privateString;
    private Date privateDate;

    private static List<Object> toList(final AnyBean subject) {
        return Arrays.asList(subject.privateInt, subject.privateString, subject.privateDate);
    }

    public int getPrivateInt() {
        return privateInt;
    }

    public AnyBean setPrivateInt(final int privateInt) {
        this.privateInt = privateInt;
        return this;
    }

    public String getPrivateString() {
        return privateString;
    }

    public AnyBean setPrivateString(final String privateString) {
        this.privateString = privateString;
        return this;
    }

    public Date getPrivateDate() {
        return privateDate;
    }

    public AnyBean setPrivateDate(final Date privateDate) {
        this.privateDate = privateDate;
        return this;
    }

    @Override
    public int hashCode() {
        return toList(this).hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof AnyBean) && toList(this).equals(toList((AnyBean) obj)));
    }

    @Override
    public String toString() {
        return toList(this).toString();
    }
}
