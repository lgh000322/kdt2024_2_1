package org.example.server.scheduler;

import org.example.server.service.SchedulerService;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    private static Scheduler scheduler = null;
    private final ScheduledExecutorService schedulerExecutors;
    private final SchedulerService schedulerService;

    public static Scheduler createOrGetScheduler() {
        if (scheduler == null) {
            scheduler = new Scheduler(SchedulerService.createOrGetSchedulerService());
            System.out.println("싱글톤 스케줄러 생성됨");
            return scheduler;
        }

        System.out.println("싱글톤 스케줄러 반환됨");
        return scheduler;
    }

    public Scheduler(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
        schedulerExecutors = Executors.newScheduledThreadPool(3); // 3개의 작업을 처리하기 위해 스레드 풀 크기를 3으로 설정
    }

    public void start() {
        System.out.println("출근기록 자동저장 스케줄러 실행");
        scheduleTask(LocalTime.of(8, 00), this::morningTask, "아침 8시에 데이터베이스 저장 로직 호출");
        scheduleTask(LocalTime.of(18, 10), this::eveningTask, "저녁 6시 10분에 데이터베이스 저장 로직 호출");
        scheduleTaskEveryMonth(LocalTime.of(22, 45), this::everyMonthTask, "매달 10일 저녁 10시 25분에 데이터베이스 저장 로직 호출");
    }

    private void scheduleTask(LocalTime targetTime, Runnable task, String message) {
        Runnable wrappedTask = () -> {
            if (isWeekday(LocalDate.now())) {
                task.run();
                System.out.println(message);
            }
        };

        // 현재 시간을 기준으로 다음 실행 시점을 계산
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextExecution = getNextExecutionTime(now, targetTime);

        long initialDelay = calculateInitialDelay(now, nextExecution);

        schedulerExecutors.scheduleAtFixedRate(wrappedTask, initialDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
    }

    private void scheduleTaskEveryMonth(LocalTime targetTime, Runnable task, String message) {
        Runnable wrappedTask = () -> {
            task.run();
            System.out.println(message);
        };

        // 현재 시간을 기준으로 다음 실행 시점을 계산
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextExecution = getNextExecutionTimeEveryMonth(now, targetTime);

        long initialDelay = calculateInitialDelay(now, nextExecution);

        schedulerExecutors.scheduleAtFixedRate(wrappedTask, initialDelay, TimeUnit.DAYS.toSeconds(30), TimeUnit.SECONDS);
    }

    private long calculateInitialDelay(LocalDateTime now, LocalDateTime nextExecution) {
        ZonedDateTime nowZoned = now.atZone(ZoneId.systemDefault());
        ZonedDateTime nextExecutionZoned = nextExecution.atZone(ZoneId.systemDefault());
        return TimeUnit.MILLISECONDS.toSeconds(nextExecutionZoned.toInstant().toEpochMilli() - nowZoned.toInstant().toEpochMilli());
    }

    private LocalDateTime getNextExecutionTime(LocalDateTime now, LocalTime targetTime) {
        LocalDateTime today = now.with(targetTime).withSecond(0).withNano(0);
        if (now.isAfter(today)) {
            // 오늘 시간이 지났다면, 내일 같은 시간으로 설정
            today = today.plusDays(1);
        }
        // 평일이 아니면 다음 평일로 설정
        while (!isWeekday(today.toLocalDate())) {
            today = today.plusDays(1);
        }
        return today;
    }

    private LocalDateTime getNextExecutionTimeEveryMonth(LocalDateTime now, LocalTime targetTime) {
        LocalDateTime nextExecution = now.withDayOfMonth(22).withHour(targetTime.getHour()).withMinute(targetTime.getMinute()).withSecond(0).withNano(0);
        if (now.isAfter(nextExecution)) {
            nextExecution = nextExecution.plusMonths(1);
        }
        return nextExecution;
    }

    private boolean isWeekday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    // 아침 8시에 수행할 작업
    private void morningTask() {
        System.out.println("아침 8시 작업 실행");
        try {
            schedulerService.morningAct();
        } catch (SQLException e) {
            System.out.println("아침 스케줄링 수행중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // 오후 6시 10분에 수행할 작업
    private void eveningTask() {
        System.out.println("저녁 6시 10분 작업 실행");
        try {
            schedulerService.eveningAct();
        } catch (SQLException e) {
            System.out.println("저녁 스케줄링 수행중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // 매달 10일에 수행할 작업
    private void everyMonthTask() {
        System.out.println("매달 10일 작업 실행");
        try {
            schedulerService.everyMonthAct();
        } catch (SQLException e) {
            System.out.println("매달 스케줄링 수행중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}
