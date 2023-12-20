package com.metalancer.backend.common.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Time {

    private static class TIME_MAX {

        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int YEAR_DAY = 365;
        public static final int MONTH = 12;
    }

    public static String convertDateToKor(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        long diffTime = duration.getSeconds();
        String time;
        if (diffTime < TIME_MAX.SEC) {
            time = diffTime + "초 전";
        } else if ((diffTime /= TIME_MAX.SEC) < TIME_MAX.MIN) {
            time = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAX.MIN) < TIME_MAX.HOUR) {
            time = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAX.HOUR) < TIME_MAX.DAY) {
            time = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAX.DAY) < TIME_MAX.MONTH) {
            time = (diffTime) + "달 전";
        } else {
            time = (diffTime / TIME_MAX.MONTH) + "년 전";
        }
        return time;
    }

    public static String convertDateToKorForRequest(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        long diffTime = duration.getSeconds();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일");

        String time;
        if (diffTime < TIME_MAX.SEC) {
            time = "방금 전";
        } else if ((diffTime /= TIME_MAX.SEC) < TIME_MAX.MIN) {
            time = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAX.MIN) < TIME_MAX.HOUR) {
            time = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAX.HOUR) < TIME_MAX.DAY) {
            time = dateTime.format(formatter);
        } else {
            time = (diffTime / TIME_MAX.MONTH) + "년 전";
        }
        return time;
    }

    public static String convertDateToEngForRequest(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        long diffTime = duration.getSeconds();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M.d");

        String time;
        if (diffTime < TIME_MAX.SEC) {
            time = "recent";
        } else if ((diffTime /= TIME_MAX.SEC) < TIME_MAX.MIN) {
            time = diffTime + "mins ago";
        } else if ((diffTime /= TIME_MAX.MIN) < TIME_MAX.HOUR) {
            time = (diffTime) + "hrs ago";
        } else if ((diffTime /= TIME_MAX.HOUR) < TIME_MAX.DAY) {
            time = dateTime.format(formatter);
        } else {
            time = (diffTime / TIME_MAX.MONTH) + "years ago";
        }
        return time;
    }

    public static boolean checkIsUpdated(LocalDateTime createdAt, LocalDateTime updatedAt) {
        Duration duration = Duration.between(createdAt, updatedAt);
        return duration.getSeconds() >= 5;
    }

    public static String convertDateToKorBeforeYear(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        long diffTime = duration.getSeconds();
        String time;

        if ((diffTime / (TIME_MAX.SEC * TIME_MAX.MIN * TIME_MAX.HOUR)) < TIME_MAX.YEAR_DAY) {
            time = (diffTime / (TIME_MAX.SEC * TIME_MAX.MIN * TIME_MAX.HOUR)) + "일 전";
        } else {
            time = (diffTime / (TIME_MAX.SEC * TIME_MAX.MIN * TIME_MAX.HOUR * TIME_MAX.YEAR_DAY))
                + "년 전";
        }
        return time;
    }

    public static LocalDateTime convertStringtoDate(String[] stringArr) {
        String dateStr = stringArr[0] + "년 " + stringArr[1] + "월 " + stringArr[2] + "일";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        return LocalDateTime.parse(dateStr, formatter);
    }

    public static String convertDateToString(LocalDateTime dateTime) {
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime != null ? dateTime.format(outputFormat) : "-";
    }

    public static String convertDateToStringWithDot(LocalDateTime dateTime) {
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return dateTime != null ? dateTime.format(outputFormat) : "-";
    }

    public static String convertDateToStringWithAttached(LocalDateTime dateTime) {
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        return dateTime != null ? dateTime.format(outputFormat) : "-";
    }

    public static String convertDateToStringSlash(LocalDateTime dateTime) {
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return dateTime != null ? dateTime.format(outputFormat) : "-";
    }

    public static String convertDateToFullString(LocalDateTime dateTime) {
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime != null ? dateTime.format(outputFormat) : "-";
    }

    public static LocalDateTime convertDateToLocalDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date.atStartOfDay();
    }
}
