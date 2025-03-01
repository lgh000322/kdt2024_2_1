package org.example.server.container;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.server.controller.*;
import org.example.server.controller.front_controller.FrontController;
import org.example.server.repository.*;
import org.example.server.scheduler.Scheduler;
import org.example.server.service.*;
import org.example.server.service.declare.*;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import static org.example.server.consts.DBConst.*;
import static org.example.server.consts.DBConst.CLASS;

public class ApplicationContext {
    private static ApplicationContext applicationContext;
    private final Map<Class<?>, Object> beans = new HashMap<>();

    private ApplicationContext() {
    }

    // Application Context를 싱글톤으로 가져온다.
    public static ApplicationContext getInstance() {
        if (applicationContext == null) {
            applicationContext = new ApplicationContext();
        }
        return applicationContext;
    }

    // 빈 등록
    public <T> void registerBean(Class<T> type, T instance) {
        beans.put(type, instance);
    }

    // 빈 조회
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
       return (T) beans.get(type);
    }

    // 생성자 주입을 통한 객체 생성
    public <T> T createInstance(Class<T> clazz) throws Exception {
        Constructor<T> constructor = findInjectableConstructor(clazz);
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[paramTypes.length];

        for (int i = 0; i < paramTypes.length; i++) {
            System.out.println(paramTypes[i]);
            params[i] = getBean(paramTypes[i]);
            System.out.println(params[i]);
        }

        return constructor.newInstance(params);
    }

    @SuppressWarnings("unchecked")
    private <T> Constructor<T> findInjectableConstructor(Class<T> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        Constructor<?> maxParamsConstructor = null;
        int maxParams = -1;

        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() > maxParams) {
                maxParamsConstructor = constructor;
                maxParams = constructor.getParameterCount();
            }
        }

        if (maxParamsConstructor == null) {
            throw new IllegalArgumentException("No suitable constructor found for class: " + clazz.getName());
        }

        return (Constructor<T>) maxParamsConstructor;
    }

    // 빈 초기화 및 의존성 주입 과정
    public void initialize() throws Exception {
        setUpDataSource();
        setUpRepository();
        setUpService();
        setUpController();
        setUpScheduler();
    }

    private void setUpDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setDriverClassName(CLASS);

        config.setMaximumPoolSize(10); // 커넥션 풀 사이즈
        config.setMinimumIdle(2); // 커넥션 풀의 커넥션 유휴상태수
        config.setConnectionTimeout(30000); // 커넥션 타임 아웃

        DataSource dataSource = new HikariDataSource(config);
        registerBean(DataSource.class, dataSource);
    }

    private void setUpRepository() {
        AnswerRepository answerRepository = new AnswerRepository();
        registerBean(AnswerRepository.class, answerRepository);

        BoardRepository boardRepository = new BoardRepository();
        registerBean(BoardRepository.class, boardRepository);

        DeptRepository deptRepository = new DeptRepository();
        registerBean(DeptRepository.class, deptRepository);

        LeaveRepository leaveRepository = new LeaveRepository();
        registerBean(LeaveRepository.class, leaveRepository);

        MailRepository mailRepository = new MailRepository();
        registerBean(MailRepository.class, mailRepository);

        PositionRepository positionRepository = new PositionRepository();
        registerBean(PositionRepository.class, positionRepository);

        SalaryRepository salaryRepository = new SalaryRepository();
        registerBean(SalaryRepository.class, salaryRepository);

        UserRepository userRepository = new UserRepository();
        registerBean(UserRepository.class, userRepository);

        WorkRepository workRepository = new WorkRepository();
        registerBean(WorkRepository.class, workRepository);
    }

    private void setUpService() throws Exception {
        AnswerService answerService = createInstance(AnswerServiceImpl.class);
        registerBean(AnswerService.class, answerService);

        BoardService boardService = createInstance(BoardServiceImpl.class);
        registerBean(BoardService.class, boardService);

        LeaveService leaveService = createInstance(LeaveServiceImpl.class);
        registerBean(LeaveService.class, leaveService);

        MailService mailService = createInstance(MailServiceImpl.class);
        registerBean(MailService.class, mailService);

        SalaryService salaryService = createInstance(SalaryServiceImpl.class);
        registerBean(SalaryService.class, salaryService);

        SchedulerService schedulerService = createInstance(SchedulerServiceImpl.class);
        registerBean(SchedulerService.class, schedulerService);

        UserService userService = createInstance(UserServiceImpl.class);
        registerBean(UserService.class, userService);

        WorkService workService = createInstance(WorkServiceImpl.class);
        registerBean(WorkService.class, workService);
    }

    private void setUpController() throws Exception {
        AnswerController answerController = createInstance(AnswerController.class);
        registerBean(AnswerController.class, answerController);

        BoardController boardController = createInstance(BoardController.class);
        registerBean(BoardController.class, boardController);

        LeaveController leaveController = createInstance(LeaveController.class);
        registerBean(LeaveController.class, leaveController);

        MailController mailController = createInstance(MailController.class);
        registerBean(MailController.class, mailController);

        SalaryController salaryController = createInstance(SalaryController.class);
        registerBean(SalaryController.class, salaryController);

        UserController userController = createInstance(UserController.class);
        registerBean(UserController.class, userController);

        WorkController workController = createInstance(WorkController.class);
        registerBean(WorkController.class, workController);

    }

    private void setUpScheduler() throws Exception {
        Scheduler scheduler = createInstance(Scheduler.class);
        registerBean(Scheduler.class, scheduler);
    }

}
