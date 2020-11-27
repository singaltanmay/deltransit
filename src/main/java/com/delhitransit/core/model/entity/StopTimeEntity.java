/*
 * @author Tanmay Singal
 */

package com.delhitransit.core.model.entity;

import com.delhitransit.core.model.parseable.StopTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class StopTimeEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long key;

    @Getter
    @Setter
    private long arrival;

    @Getter
    @Setter
    private long departure;

    @Getter
    @Setter
    private int stopSequence;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    private TripEntity trip;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    private StopEntity stop;

    public StopTimeEntity(StopTime stopTime) {
        this.setArrival(stopTime.getArrival());
        this.setDeparture(stopTime.getDeparture());
        this.setStopSequence(stopTime.getStopSequence());
    }

    public static long getTimeLongFromString(String timeString) {
        String[] split = timeString.split(":");
        return Long.parseLong(split[0]) * 3600 + Long.parseLong(split[1]) * 60 + Long.parseLong(split[2]);
    }

    public static long getSecondsSince12AM() {
        LocalDateTime now = LocalDateTime.now();
        String timeString = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
        return getTimeLongFromString(timeString);
    }

    public void setArrival(String arrival) {
        this.arrival = getTimeLongFromString(arrival);
    }

    public void setDeparture(String departure) {
        this.departure = getTimeLongFromString(departure);
    }

    public String getArrivalString() {
        return longToTimeString(arrival);
    }

    public String getDepartureString() {
        return longToTimeString(departure);
    }

    private String longToTimeString(long seconds) {
        long[] timeArray = new long[3];

        long hrs = timeArray[0] = seconds / 3600;
        long mins = timeArray[1] = (seconds - hrs * 3600) / 60;
        timeArray[2] = seconds - hrs * 3600 - mins * 60;

        StringBuilder timeStringBuilder = new StringBuilder();

        for (long elem : timeArray) {
            if (elem / 10 < 1) {
                timeStringBuilder.append("0").append(elem).append(":");
            } else {
                timeStringBuilder.append(elem).append(":");
            }
        }

        String timeString = timeStringBuilder.toString();
        return timeString.substring(0, timeString.length() - 1);
    }
}
