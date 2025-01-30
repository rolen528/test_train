package com.rolen.calendarapp.reader;

import org.junit.jupiter.api.Test;
import com.rolen.calendarapp.event.Meeting;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class EventCsvReaderTest {

    private static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm:ss";

    @Mock
    private RawCsvReader rawCsvReader;

    @InjectMocks
    private EventCsvReader sut;

    @Test
    public void reader() throws IOException {
        // given
        String path = "";

        List<String[]> mockData = new ArrayList<>();
        mockData.add(new String[8]);

        int mockSize = 5;
        for (int i = 0; i < mockSize; i++) {
            mockData.add(generateMock(i));
        }
        when(rawCsvReader.readAll(path)).thenReturn(mockData);

        // when
        List<Meeting> meetings = sut.readMeetings(path);

        // then
        assertEquals(mockSize, meetings.size());
        assertEquals("title0", meetings.get(0).getTitle());
    }

    private String[] generateMock(int id) {
        String[] mock = new String[8];
        mock[0] = String.valueOf(id);
        mock[1] = "MEETING" + id;
        mock[2] = "title" + id;
        mock[3] = "A,B,C";
        mock[4] = "A1";
        mock[5] = "test";
        mock[6] = of(ZonedDateTime.now().plusHours(id));
        mock[7] = of(ZonedDateTime.now().plusHours(id+1));

        return mock;
    }

    private static String of(ZonedDateTime dt) {
        return dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}