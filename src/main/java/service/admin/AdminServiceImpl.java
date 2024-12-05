package service.admin;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import model.Book;
import model.Order;
import model.User;
import repository.admin.AdminRepository;
import repository.order.OrderRepositoryMySQL;
import repository.user.UserRepository;
import service.order.OrderServiceImpl;

import java.io.File;
import java.util.List;

public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final OrderServiceImpl orderService;
    private final UserRepository userRepository;

    public AdminServiceImpl(AdminRepository adminRepository, OrderServiceImpl orderService, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean addUser(User user) {
        return adminRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return adminRepository.findAll();
    }

    @Override
    public boolean generateReport() {
        try {
            // obtinem si procesam toti angajatii
            List<User> employees = adminRepository.findAllEmployees();

            for (User employee : employees) {
                // prelucram comenzile pt fiecare angajat
                List<Order> orders = adminRepository.getOrdersForUser(employee.getId());
                orderService.addSoldBooksFromOrders(employee, orders);
            }

            // generam raportul
            generatePdfReport(employees);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void generatePdfReport(List<User> users) {
        String filePath = "report.pdf";

        try {
            PdfWriter writer = new PdfWriter(new File(filePath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // add titlu
            document.add(new Paragraph("Raport Vanzari Utilizatori - Luna Curenta\n\n"));

            // iteram prin utilizatori
            for (User user : users) {

                int totalSoldBooks = user.getTotalSoldBooks();
                double totalSalesValue = user.getTotalSalesValue();

                // verificam daca user-ul este angajat
                if (user != null && orderService.isUserEmployee(user.getId())) {
                    document.add(new Paragraph("User: " + user.getUsername()));
                    document.add(new Paragraph("Carti vandute: " + totalSoldBooks));
                    document.add(new Paragraph("Valoare vanzari: " + totalSalesValue+ " RON"));
                    document.add(new Paragraph("\n"));

                    document.add(new Paragraph("\n"));
                }
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

