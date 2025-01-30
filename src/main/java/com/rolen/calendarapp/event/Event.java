package com.rolen.calendarapp.event;

public interface Event {
    void print();

    boolean support(EventType type);
}
