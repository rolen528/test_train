package com.rolen.calendarapp;

import com.rolen.calendarapp.event.*;
import com.rolen.calendarapp.event.update.UpdateMeeting;
import com.rolen.calendarapp.reader.EventCsvReader;
import com.rolen.calendarapp.reader.RawCsvReader;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

@SpringBootApplication
public class CalendarAppApplication {

    public static void main(String[] args) throws IOException {
        Schedule schedule = new Schedule();

        EventCsvReader csvReader = new EventCsvReader(new RawCsvReader());
        String meetingCsvPath = "/data/meeting.csv";

        List<Meeting> meetings = csvReader.readMeetings(meetingCsvPath);
        meetings.forEach(schedule::add);
        schedule.printAll();

        Meeting meeting = meetings.get(0);

        System.out.println(" === 수정 후 ===");
        meetings.get(0).validateAndUpdate(
                new UpdateMeeting(
                        "new title",
                        ZonedDateTime.now(),
                        ZonedDateTime.now().plusHours(1),
                        null,
                        "A",
                        "new agenda"
                )
        );


        schedule.printAll();
        meeting.delete(true);
        System.out.print("=== 삭제 후 ===\nmeeting id - 0 // 삭제 완료");
        schedule.printAll();

//        schedule.printBy(EventType.TO_DO);

    }

}
