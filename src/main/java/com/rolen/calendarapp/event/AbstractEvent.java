package com.rolen.calendarapp.event;

import com.rolen.calendarapp.event.update.AbstractAuditableEvent;
import com.rolen.calendarapp.exception.InvalidEventException;

import java.time.ZonedDateTime;
import java.time.Duration;

public abstract class AbstractEvent implements Event {
    private final int id;
    private String title;

    private ZonedDateTime startAt;
    private ZonedDateTime endAt;
    private Duration duration;

    private final ZonedDateTime createdAt;
    private ZonedDateTime modifiedAt;

    private boolean deletedYn;

    protected AbstractEvent(int id, String title,
                            ZonedDateTime startAt, ZonedDateTime endAt) {

        // startAt과 endAt의 순서가 다른 경우. 잘못된 기록이 되므로 제한을 만든다
        if (startAt.isAfter(endAt)) {
            throw new InvalidEventException(
                    String.format("시작일은 종료일보다 이전이어야 합니다. 시작일=%s / 종료일=%s", startAt, endAt)
            );
        }


        this.id = id;
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.duration = Duration.between(startAt, endAt);

        ZonedDateTime now = ZonedDateTime.now();
        this.createdAt = now;
        this.modifiedAt = now;

        this.deletedYn = false;
    }

    public void validateAndUpdate(AbstractAuditableEvent update) {
        if (deletedYn == true) {
            throw new RuntimeException("이미 삭제된 이벤트는 수정할 수 없음");
        }
        defaultUpdate(update);
        update(update);
    }

    private void defaultUpdate(AbstractAuditableEvent update) {
        this.title = update.getTitle();
        this.startAt = update.getStartAt();
        this.endAt = update.getEndAt();
        this.duration = Duration.between(this.startAt, this.endAt);
        this.modifiedAt = ZonedDateTime.now();
    }

    protected abstract void update(AbstractAuditableEvent update);

    public void delete(boolean deleteYn) {
        this.deletedYn = deleteYn;
    }

    public String getTitle() { return title; }

    public ZonedDateTime getStartAt() { return startAt; }

    public ZonedDateTime getEndAt() { return endAt; }
}
