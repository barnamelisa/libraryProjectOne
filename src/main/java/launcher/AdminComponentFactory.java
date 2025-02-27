package launcher;

import controller.AdminController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.UserMapper;
import repository.admin.AdminRepository;
import repository.admin.AdminRepositoryCacheDecorator;
import repository.admin.AdminRepositoryMySQL;
import repository.book.Cache;
import repository.order.OrderRepositoryMySQL;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.admin.AdminService;
import service.admin.AdminServiceImpl;
import service.order.OrderServiceImpl;
import view.AdminView;
import view.model.UserDTO;

import java.sql.Connection;
import java.util.List;

// un component factory ne ajuta la crearea arborelui de dependinte
public class AdminComponentFactory {
    private final AdminView adminView;
    private final AdminController adminController;
    private final AdminRepository adminRepository;
    private final AdminService adminService;
    private final UserRepository userRepository;
    private static volatile AdminComponentFactory instance;

    public static AdminComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage){
        if (instance == null) {
            synchronized (AdminComponentFactory.class){
                if (instance == null){
                    instance=new AdminComponentFactory(componentsForTest, primaryStage);
                }
            }
        }
        return instance;
    }


    // facem sa fie Singleton (constructor privat)
    private AdminComponentFactory(Boolean componentsForTest, Stage stage) {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.adminRepository = new AdminRepositoryCacheDecorator(new AdminRepositoryMySQL(connection), new Cache<>());

        this.userRepository = new UserRepositoryMySQL(connection, new RightsRolesRepositoryMySQL(connection));

        OrderServiceImpl orderService = new OrderServiceImpl(new OrderRepositoryMySQL(connection));
        this.adminService = new AdminServiceImpl(adminRepository, orderService, userRepository);

        List<UserDTO> userDTOs = UserMapper.convertUserListToUserDTOList(this.adminService.findAll());

        this.adminView = new AdminView(stage, userDTOs);
        this.adminController = new AdminController(adminView, adminService);
    }
}
