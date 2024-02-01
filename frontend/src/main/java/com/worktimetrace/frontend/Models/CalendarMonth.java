package com.worktimetrace.frontend.Models;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class CalendarMonth {
    private LocalDate selctedDate;
    private ArrayList<CalendarCell> daysInMonthCells;
    private CalendarCell[][] weeksAndDaysInMonth;
    private String monthYear;

    public CalendarMonth(LocalDate date, ArrayList<HourSender> hourSenders) {
        this.selctedDate = date;
        this.monthYear = getMonthYearFromDate(date);
        this.daysInMonthCells = daysInMonthToArray(date);
        fillCalendarEntrysInCalendarCellArray(hourSenders);
        this.weeksAndDaysInMonth = fillWeeksInMonthArray(date);
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public CalendarCell[][] getWeeksAndDaysInMonth() {
        return weeksAndDaysInMonth;
    }

    public void setWeeksAndDaysInMonth(CalendarCell[][] weeksAndDaysInMonth) {
        this.weeksAndDaysInMonth = weeksAndDaysInMonth;
    }

    private String getMonthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM - yyyy", Locale.GERMAN);
        return date.format(formatter);
    }

    public ArrayList<CalendarCell> daysInMonthToArray(LocalDate date) {
        ArrayList<CalendarCell> daysInMonthArray = new ArrayList<>();
        int viewSpace = 0;

        LocalDate previousMonth = date.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfPreviousMonth = previousMonth.plusMonths(1).minusDays(1);

        LocalDate dayOfMonth = date.withDayOfMonth(1);

        int dayOfWeek = dayOfMonth.getDayOfWeek().getValue();
        for (int i = 1; i < dayOfWeek; i++) {
            if (compareMonths(selctedDate, endOfPreviousMonth.minusDays(dayOfWeek - i - 1))) {
                daysInMonthArray.add(new CalendarCell(endOfPreviousMonth.minusDays(dayOfWeek - i - 1), true));
                viewSpace += 1;
            } else {
                daysInMonthArray.add(new CalendarCell(endOfPreviousMonth.minusDays(dayOfWeek - i - 1), false));
                viewSpace += 1;
            }
        }

        while (viewSpace < 42) {
            if (compareMonths(selctedDate, dayOfMonth)) {
                daysInMonthArray.add(new CalendarCell(dayOfMonth, true));
                dayOfMonth = dayOfMonth.plusDays(1);
                viewSpace += 1;
            } else {
                daysInMonthArray.add(new CalendarCell(dayOfMonth, false));
                dayOfMonth = dayOfMonth.plusDays(1);
                viewSpace += 1;
            }
        }
        return daysInMonthArray;
    }

    public CalendarCell[][] fillWeeksInMonthArray(LocalDate date) {
        CalendarCell[][] ret = new CalendarCell[6][7];
        int pos = 0;
        for (CalendarCell cell : daysInMonthCells) {
            if (pos < 7) {
                ret[0][pos] = cell;
            }
            if (pos >= 7 && pos < 14) {
                ret[1][pos - 7] = cell;
            }
            if (pos >= 14 && pos < 21) {
                ret[2][pos - 14] = cell;
            }
            if (pos >= 21 && pos < 28) {
                ret[3][pos - 21] = cell;
            }
            if (pos >= 28 && pos < 35) {
                ret[4][pos - 28] = cell;
            }
            if (pos >= 35 && pos < 42) {
                ret[5][pos - 35] = cell;
            }
            pos++;
        }
        return ret;
    }

    private void fillCalendarEntrysInCalendarCellArray(ArrayList<HourSender> hourSenders) {
        LocalDate firstDateOfCalendar = daysInMonthCells.get(0).getDate();
        LocalDate lastDateOfCalendar = daysInMonthCells.get(41).getDate();

        int currentCalendarMonth = daysInMonthCells.get(15).getMonth();
        int currentCalendarYear = daysInMonthCells.get(15).getYear();
        int lengthOfCurrentMonth = daysInMonthCells.get(15).getDate().lengthOfMonth();
        int lengthOfPreviousMonth = 0;

        YearMonth yearMonth = YearMonth.of(currentCalendarYear, currentCalendarMonth);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        int daysBeforFirstDayOfMonth = 0;

        for(CalendarCell cell : daysInMonthCells){
            LocalDate date = cell.getDate();
            if(date.equals(firstDayOfMonth)){
                break;
            }
            daysBeforFirstDayOfMonth++;
        }
        if(daysBeforFirstDayOfMonth > 0) {
            lengthOfPreviousMonth = daysInMonthCells.get(0).getDate().lengthOfMonth();
        }

        for (HourSender hourSender : hourSenders) {
            LocalDate date = LocalDate.parse(hourSender.getDate());
            if (date != null) {
                int month = date.getMonthValue();
                int day = date.getDayOfMonth();

                if (date.isBefore(firstDateOfCalendar)) {
                    continue;
                }
                if (date.isAfter(lastDateOfCalendar)) {
                    continue;
                }
                
                if (month == currentCalendarMonth) {
                    daysInMonthCells.get(day + daysBeforFirstDayOfMonth - 1).setHourCount(hourSender.getHourcount());
                } else if (((month - 1 < 0) ? 12 : (month - 1)) == currentCalendarMonth && (lengthOfCurrentMonth + day) < 41) {
                    daysInMonthCells.get(lengthOfCurrentMonth + day + daysBeforFirstDayOfMonth - 1).setHourCount(hourSender.getHourcount());
                } else if (((month + 1 > 12) ? 1 : (month +1)) == currentCalendarMonth) {
                    daysInMonthCells.get(day - lengthOfPreviousMonth + daysBeforFirstDayOfMonth - 1).setHourCount(hourSender.getHourcount());
                }
            }

        }
    }

    private static boolean compareMonths(LocalDate selectedDate, LocalDate date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", Locale.GERMAN);

        String month = selectedDate.format(formatter);
        String itemMonth = date.format(formatter);

        if (month.equals(itemMonth)) {
            return true;
        } else {
            return false;
        }
    }
}
