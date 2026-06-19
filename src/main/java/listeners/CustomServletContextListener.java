package listeners;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import repository.impl.fileRepository.FilesRepositoryImpl;
import repository.impl.modelsRepository.*;
import repository.impl.referenceBook.*;
import repository.interfaces.*;
import services.impl.*;
import services.interfaces.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;


@WebListener
public class CustomServletContextListener implements ServletContextListener {

    private static final String DB_USERNAME = "imon";

    private static final String DB_PASSWORD = "Radmir99204648";

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/semestrovka_dubl2";

    private static final String DB_DRIVER = "org.postgresql.Driver";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setPassword(DB_PASSWORD);
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setDriverClassName(DB_DRIVER);
        try {
            Connection connection = dataSource.getConnection();
            WorkoutCoachRepository workoutCoachRepository = new WorkoutCoachRepositoryImpl(connection);
            servletContext.setAttribute("workoutCoachRepository", workoutCoachRepository);
            RoleRepository roleRepository = new RoleRepositoryImpl(connection);
            UsersRepository usersRepository = new UsersRepositoryImpl(connection);

            SignUpService signUpService = new SignUpServiceImpl(usersRepository, roleRepository);
            SignInService signInService = new SignInServiceImpl(usersRepository);
            UsersService usersService = new UsersServiceImpl(usersRepository, roleRepository);

            ProductRepository productRepository = new ProductsRepositoryImpl(connection);
            ProductCategoriesRepository productCategoriesRepository = new ProductCategoriesRepositoryImpl(connection);
            ShopService shopService = new ShopServiceImpl(productRepository, productCategoriesRepository);
            servletContext.setAttribute("shopService", shopService);
            FilesRepository filesRepository = new FilesRepositoryImpl(dataSource);
            FilesService filesService = new FilesServiceImpl(filesRepository);
            servletContext.setAttribute("filesRepository", filesRepository);
            servletContext.setAttribute("filesService", filesService);

            BasketInterface basketService = new BasketServiceImpl();
            servletContext.setAttribute("basketService", basketService);
            ProductService productService = new ProductServiceImpl(productRepository, productCategoriesRepository, filesService);
            servletContext.setAttribute("productService", productService);

            servletContext.setAttribute("usersRepository", usersRepository);
            servletContext.setAttribute("signUpService", signUpService);
            servletContext.setAttribute("roleRepository", roleRepository);
            servletContext.setAttribute("signInService", signInService);
            servletContext.setAttribute("usersService", usersService);

            servletContext.setAttribute("productCategoriesRepository", productCategoriesRepository);
            servletContext.setAttribute("productRepository", productRepository);

            CategoriesRepository categoriesRepository = new CategoriesRepositoryImpl(connection);
            servletContext.setAttribute("categoriesRepository", categoriesRepository);

            PeopleTypeRepository peopleTypeRepository = new PeopleTypeRepositoryImpl(connection);
            servletContext.setAttribute("peopleTypeRepository", peopleTypeRepository);

            CategoriesService categoriesService = new CategoriesServiceImpl(categoriesRepository);
            servletContext.setAttribute("categoriesService", categoriesService);

            CoachesRepository coachesRepository = new CoachesRepositoryImpl(connection, usersRepository, workoutCoachRepository);
            servletContext.setAttribute("coachesRepository", coachesRepository);
            CoachesService coachesService = new CoachesServiceImpl(coachesRepository, roleRepository, usersService);
            servletContext.setAttribute("coachesService", coachesService);

            LevelInterface levelRepository = new LevelRepositoryImpl(connection);
            servletContext.setAttribute("levelRepository", levelRepository);
            LevelService levelService = new LevelsServicesImpl(levelRepository);
            servletContext.setAttribute("levelService", levelService);
            WorkoutsRepository workoutsRepository = new WorkoutsRepositoryImpl(connection, peopleTypeRepository, categoriesRepository, workoutCoachRepository, levelRepository);
            WorkoutsService workoutsService = new WorkoutsServiceImpl(workoutsRepository);
            servletContext.setAttribute("workoutsRepository", workoutsRepository);
            servletContext.setAttribute("workoutsService", workoutsService);

            WorkoutCoachService workoutCoachService = new WorkoutCoachService(workoutCoachRepository, workoutsRepository, coachesRepository);
            servletContext.setAttribute("workoutCoachService", workoutCoachService);


            ScheduleRepository repository = new ScheduleRepositoryImpl(connection);
            ScheduleService scheduleService = new ScheduleServiceImpl(repository);
            servletContext.setAttribute("scheduleService", scheduleService);

            BookingRepository bookingRepository = new BookingRepositoryImpl(connection);
            BookingService bookingService = new BookingServiceImpl(bookingRepository);
            servletContext.setAttribute("bookingService", bookingService);
            PeopleTypesService peopleTypesService = new PeopleTypesServiceImpl(peopleTypeRepository);
            servletContext.setAttribute("peopleTypesService", peopleTypesService);

            PricingRepository pricingRepository = new PricingRepositoryImpl(connection);
            PricingInterface pricingService = new PricingServiceImpl(pricingRepository, categoriesService, peopleTypesService, workoutsService);
            servletContext.setAttribute("pricingService", pricingService);
            servletContext.setAttribute("pricingRepository", pricingRepository);

            MemberShipRepository memberShipRepository = new MemberShipRepositoryImpl(connection, pricingRepository);
            servletContext.setAttribute("memberShipRepository", memberShipRepository);

            MemberShipService memberShipService = new MemberShipServiceImpl(memberShipRepository);
            servletContext.setAttribute("memberShipService", memberShipService);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

//            Role adminROle = roleRepository.findById(1).orElse(null);
//            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            User adminUser = User.builder()
//            .id(UUID.randomUUID())
//            .role(adminROle)
//            .email("admin@gmail.com")
//            .password(passwordEncoder.encode("123"))
//            .phone("+8800")
//            .firstName("adminFN")
//            .lastName("adminLN")
//            .build();
//            usersRepository.save(adminUser);

//    ProductCategory c1 = ProductCategory.builder().id(1L).productName("Еда").build();
//    ProductCategory c2 = ProductCategory.builder().id(2L).productName("Очки").build();
//    ProductCategory c3 = ProductCategory.builder().id(3L).productName("Шапки").build();
//    ProductCategory c4 = ProductCategory.builder().id(4L).productName("Плавки").build();
//    ProductCategory c5 = ProductCategory.builder().id(5L).productName("Напитки").build();
//
//            productCategoriesRepository.save(c1);
//            productCategoriesRepository.save(c2);
//            productCategoriesRepository.save(c3);
//            productCategoriesRepository.save(c4);
//            productCategoriesRepository.save(c5);

    //PeopleType p1 = PeopleType.builder()
//                    .id(1)
//                    .type_name("Дети")
//                    .build();
//            PeopleType p2 = PeopleType.builder()
//                    .id(2)
//                    .type_name("Взрослые")
//                    .build();
//            PeopleType p3 = PeopleType.builder()
//                    .id(1)
//                    .type_name("Студенты")
//                    .build();
//            PeopleType p4 = PeopleType.builder()
//                    .id(1)
//                    .type_name("Пожилые люди")
//                    .build();
//            peopleTypeRepository.save(p1);
//            peopleTypeRepository.save(p2);
//            peopleTypeRepository.save(p3);
//            peopleTypeRepository.save(p4);
//    Category category =  Category.builder()
//                    .id(2)
//                    .categoryName("Индивидуальная")
//                    .build();
//            Category category1 = Category.builder()
//                    .id(1)
//                    .categoryName("Свободное")
//                    .build();
//            Category category2 = Category.builder()
//                    .id(3)
//                    .categoryName("Групповое")
//                    .build();
//            categoriesRepository.save(category);
//            categoriesRepository.save(category1);
//            categoriesRepository.save(category2);

    //            Level l = Level.builder()
    ////                    .id(1L)
    ////                    .level_name("Новичок")
    ////                    .build();
    ////
    ////            Level l1 = Level.builder()
    ////                    .id(2L)
    ////                    .level_name("Средний")
    ////                    .build();
    ////            Level l2 = Level.builder()
    ////                    .id(3L)
    ////                    .level_name("Опытный")
    ////                    .build();
    ////            levelRepository.save(l);
    ////            levelRepository.save(l1);
    ////            levelRepository.save(l2);
}
