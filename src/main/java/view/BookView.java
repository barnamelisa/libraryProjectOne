package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import mapper.BookMapper;
import model.Book;
import view.model.BookDTO;

import java.util.List;

public class BookView {
    private TableView bookTableView; // in acest tabel vom adauga toate cartile
    private final ObservableList<BookDTO> booksObservableList; // ObservableList-va contine carti car nu orice fel de carti, ci niste carti modificate(BookDTO)
    private TextField authorTextField;
    private TextField titleTextField;
    private TextField priceTextField;
    private TextField stockTextField;
    private Label authorLabel; // ce trebuie sa introducem exact in textField
    private Label titleLabel;
    private Label priceLabel;
    private Label stockLabel;
    private Button saveButton;
    private Button deleteButton;
    private Button saleBookButton;

    public BookView(Stage primaryStage, List<BookDTO> books){
        primaryStage.setTitle("Library");

        GridPane gridPane = new GridPane();
        initializeGridPage(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        // SA NU RESETAM REFERINTA LA booksObservableList, pt ca daca o resetam vom pierde referinta setata in tabel si modificarile nu se vor mai vedea
        booksObservableList = FXCollections.observableArrayList(books);
        initTableView(gridPane); // tot ce add in grid pane se adauga automat in scene si primary stage

        initSaveOptions(gridPane);

        primaryStage.show();
    }
    private void initTableView(GridPane gridPane){
        bookTableView = new TableView<BookDTO>();

        bookTableView.setPlaceholder(new Label("No books to display"));

        TableColumn<BookDTO, String> titleColumn = new TableColumn<BookDTO, String>("Title"); // Title=numele coloanei efective
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title")); // dam numele din BookDTO, adica title
        TableColumn<BookDTO, String> authorColumn =  new TableColumn<BookDTO, String>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<BookDTO, String> priceColumn = new TableColumn<BookDTO, String>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<BookDTO, String> stockColumn =  new TableColumn<BookDTO, String>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        bookTableView.getColumns().addAll(titleColumn, authorColumn, priceColumn, stockColumn); // am adaugat cele 2 coloane care vor face data binding

        bookTableView.setItems(booksObservableList); // am adaugat la tebl lista care implementeaza Observable(orice modificare din aceasta lista va trebui sa modifice si cartile din tabel)

        gridPane.add(bookTableView,0,0,5,1);
    }

    private void initSaveOptions(GridPane gridPane){
        titleLabel = new Label("Title");
        gridPane.add(titleLabel,1,1);

        titleTextField = new TextField();
        gridPane.add(titleTextField,2,1);

        authorLabel = new Label("Author");
        gridPane.add(authorLabel,3,1);

        authorTextField = new TextField();
        gridPane.add(authorTextField,4,1);

        priceLabel = new Label("Price");
        gridPane.add(priceLabel,5,1);

        priceTextField = new TextField();
        gridPane.add(priceTextField,6,1);

        stockLabel = new Label("Stock");
        gridPane.add(stockLabel,7,1);

        stockTextField = new TextField();
        gridPane.add(stockTextField,8,1);

        saveButton=  new Button("Save");
        gridPane.add(saveButton,9,1);

        deleteButton =  new Button("Delete");
        gridPane.add(deleteButton,10,1); // coloana 10, randu 1

        saleBookButton = new Button("Sale");
        gridPane.add(saleBookButton,11,1);
    }

    private void initializeGridPage(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    public void addSaveButtonListener(EventHandler<ActionEvent> saveButtonListener){
        saveButton.setOnAction(saveButtonListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener){
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addSaleBookButtonListener(EventHandler<ActionEvent> saleBookButtonListener){
        saleBookButton.setOnAction(saleBookButtonListener);
    }

    public void addDisplayAlertMessage(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait(); // va afisa si va astepta pana il inchid manual
    }

    public String getTitle(){
        return titleTextField.getText();
    }

    public String getAuthor(){
        return authorTextField.getText();
    }
    public Integer getPrice(){
        return Integer.parseInt(priceTextField.getText()); // nu stiu sigur daca e bine
    }
    public Integer getStock(){
        return Integer.parseInt(stockTextField.getText()); // nu stiu sigur daca e bine
    }

    public void addBookToObservableList(BookDTO bookDTO){
        this.booksObservableList.add(bookDTO);
    }

    public void removeBookFromObservableList(BookDTO bookDTO){
        this.booksObservableList.remove(bookDTO);
    }

    // de explicat + inteles
    public void saleBookFromObservableList(BookDTO bookDTO) {
        booksObservableList.stream()
                .filter(it -> it.getTitle().equals(bookDTO.getTitle()) && it.getAuthor().equals(bookDTO.getAuthor()))
                .findFirst()
                .ifPresent(it -> {
                    if (it.getStock() > 0) {
                        it.setStock(it.getStock() - 1);
                        bookTableView.refresh(); // actualizam tabela din interfata

                        if (it.getStock() == 0) {
                            addDisplayAlertMessage("Vanzare", "Stoc Epuizat", "Cartea \"" + it.getTitle() + "\" nu mai este in stoc.");
                        } else {
                            addDisplayAlertMessage("Vanzare", "Carte Vanduta", "Cartea \"" + it.getTitle() + "\" a fost vanduta. Stoc ramas: " + it.getStock());
                        }
                    } else {
                        addDisplayAlertMessage("Vanzare", "Stoc Insuficient", "Cartea \"" + it.getTitle() + "\" nu mai este în stoc.");
                    }
                });
    }

    public TableView getBookTableView(){
        return bookTableView;
    }
}
