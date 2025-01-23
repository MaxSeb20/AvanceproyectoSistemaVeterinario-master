import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.transformation.FilteredList;

import javafx.scene.chart.*;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;


import java.util.HashMap;
import java.util.Map;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class SistemaVeterinario extends Application {
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private ObservableList<Cita> listaCitas = FXCollections.observableArrayList();
    private TableView<Cliente> tablaClientes = new TableView<>(); // Mover tablaClientes a nivel de clase
    private ComboBox<Cliente> comboCliente; // Declaración global de comboCliente
    private TableView<HistorialMedico> tablaHistorial; // Declaración global de tablaHistorial

    @Override

    public void start(Stage primaryStage) {
        // Crear clientes
        Cliente cliente1 = new Cliente("Juan", "123456789", "Calle 1");
        Mascota mascota1 = new Mascota("Rex", "Perro");
        mascota1.agregarHistorial(new HistorialMedico(LocalDate.of(2025, 1, 1), "Fractura", "Reposo"));
        mascota1.agregarHistorial(new HistorialMedico(LocalDate.of(2025, 1, 2), "Infección", "Antibióticos"));
        cliente1.agregarMascota(mascota1);

        Cliente cliente2 = new Cliente("Ana", "987654321", "Calle 2");
        Mascota mascota2 = new Mascota("Luna", "Gato");
        mascota2.agregarHistorial(new HistorialMedico(LocalDate.of(2025, 1, 3), "Infección", "Antibióticos"));
        cliente2.agregarMascota(mascota2);

        listaClientes.addAll(cliente1, cliente2);


        // TabPane para dividir las secciones
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: #f4f4f4; -fx-tab-min-height: 40px; -fx-tab-max-height: 40px; -fx-tab-text-color: #333;");

        // Pestaña de Bienvenida
        Tab tabBienvenida = new Tab("Bienvenida");
        tabBienvenida.setContent(crearPaginaBienvenida(tabPane));
        tabBienvenida.setClosable(false);

        // Pestaña Gestión de Clientes y Mascotas
        Tab tabClientes = new Tab("Gestión de Clientes y Mascotas");
        tabClientes.setContent(crearGestionClientes());
        tabClientes.setClosable(false);

        // Pestaña Gestión de Citas
        Tab tabCitas = new Tab("Gestión de Citas");
        tabCitas.setContent(crearGestionCitas());
        tabCitas.setClosable(false);

        // Pestaña Historial Médico
        Tab tabHistorialMedico = new Tab("Historial Médico");
        tabHistorialMedico.setContent(crearHistorialMedico());
        tabHistorialMedico.setClosable(false);

        Tab tabReportes = new Tab("Reportes y Estadísticas");
        tabReportes.setContent(crearPaginaReportes());
        tabReportes.setClosable(false);

        tabPane.getTabs().add(tabReportes);


        // Agregar las pestañas al TabPane
        tabPane.getTabs().addAll(tabBienvenida, tabClientes, tabCitas, tabHistorialMedico, tabReportes);

        Scene scene = new Scene(tabPane, 900, 600);
        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;700&display=swap");
        primaryStage.setTitle("Sistema Veterinario");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private VBox crearPaginaBienvenida(TabPane tabPane) {
        // Crear el logo
        ImageView logo = new ImageView(new Image("file:src/logo.jpg"));
        logo.setFitWidth(150);
        logo.setFitHeight(150);

        Label lblTitulo = new Label("Bienvenido al Sistema Veterinario");
        lblTitulo.setStyle("-fx-font-size: 30px; -fx-font-family: 'Roboto'; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label lblDescripcion = new Label(
                "Este sistema le permite gestionar de forma eficiente los clientes, mascotas, citas y sus historiales médicos en la clínica veterinaria.\n\n" +
                        "- En la pestaña 'Gestión de Clientes y Mascotas', puede registrar clientes, agregar sus mascotas y ver su historial médico.\n" +
                        "- En la pestaña 'Gestión de Citas', puede gestionar las citas con funcionalidades para agregar, editar y eliminar.\n" +
                        "- En la pestaña 'Historial Médico', puede registrar diagnósticos y tratamientos, y consultar el historial detallado por mascota."
        );
        lblDescripcion.setStyle("-fx-font-size: 14px; -fx-font-family: 'Roboto'; -fx-text-fill: #666;");
        lblDescripcion.setWrapText(true);

        Button btnContinuar = new Button("Continuar");
        btnContinuar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-font-size: 14px; -fx-font-family: 'Roboto'; -fx-background-radius: 5px;");
        btnContinuar.setOnAction(e -> tabPane.getSelectionModel().select(1));

        VBox layoutBienvenida = new VBox(20, logo, lblTitulo, lblDescripcion, btnContinuar);
        layoutBienvenida.setStyle("-fx-alignment: center; -fx-padding: 40; -fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-width: 1px; -fx-border-radius: 10px;");
        return layoutBienvenida;
    }

    private VBox crearPaginaReportes() {
        // Crear un mapa para contar los diagnósticos
        Map<String, Integer> frecuenciaDiagnosticos = new HashMap<>();

        // Recorre la lista de clientes y suma los diagnósticos en el historial de cada mascota
        for (Cliente cliente : listaClientes) {
            for (Mascota mascota : cliente.getMascotas()) {
                for (HistorialMedico historial : mascota.getHistorialMedico()) {
                    String diagnostico = historial.getDiagnostico();
                    frecuenciaDiagnosticos.put(diagnostico,
                            frecuenciaDiagnosticos.getOrDefault(diagnostico, 0) + 1);
                }
            }
        }

        // Configurar los ejes del gráfico
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Diagnósticos");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Frecuencia");

        // Crear el gráfico de barras
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Frecuencia de Diagnósticos");

        // Agregar los datos reales al gráfico
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Diagnósticos Reales");

        for (Map.Entry<String, Integer> entry : frecuenciaDiagnosticos.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);

        // Configurar el diseño
        VBox layout = new VBox(10, barChart);
        layout.setStyle("-fx-padding: 20;");
        return layout;
    }


    private VBox crearGestionClientes() {
        // Campos para cliente
        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField();
        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField();
        Label lblDirección = new Label("Dirección:");
        TextField txtDireccion = new TextField();

        // Campos para mascota
        Label lblNombreMascota = new Label("Nombre de la Mascota:");
        TextField txtNombreMascota = new TextField();
        Label lblEspecieMascota = new Label("Especie:");
        TextField txtEspecieMascota = new TextField();

        // Botones
        Button btnAgregarCliente = new Button("Agregar Cliente");
        btnAgregarCliente.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-family: 'Roboto'; -fx-padding: 10 20; -fx-background-radius: 5px;");
        btnAgregarCliente.setOnAction(e -> {
            String nombre = txtNombre.getText();
            String telefono = txtTelefono.getText();
            String direccion = txtDireccion.getText();
            if (!nombre.isEmpty() && !telefono.isEmpty() && !direccion.isEmpty()) {
                Cliente cliente = new Cliente(nombre, telefono, direccion);
                listaClientes.add(cliente);
                txtNombre.clear();
                txtTelefono.clear();
                txtDireccion.clear();
            } else {
                mostrarAlerta("Error", "Todos los campos del cliente deben ser completados.");
            }
        });

        Button btnAgregarHistorial = new Button("Agregar Registro");
        btnAgregarHistorial.setOnAction(e -> {
            Cliente clienteSeleccionado = comboCliente.getValue();
            if (clienteSeleccionado != null) {
                // Ejemplo: Agrega un registro al historial del cliente seleccionado
                clienteSeleccionado.agregarHistorial(new HistorialMedico(
                        LocalDate.now(), // Fecha actual
                        "Diagnóstico de ejemplo", // Diagnóstico
                        "Tratamiento de ejemplo"  // Tratamiento
                ));
                // Actualiza la tabla para reflejar el nuevo registro
                tablaHistorial.setItems(FXCollections.observableArrayList(clienteSeleccionado.getHistorialMedico()));
            } else {
                mostrarAlerta("Error", "Debe seleccionar un cliente para agregar un registro.");
            }
        });

        Button btnAgregarMascota = new Button("Agregar Mascota");
        btnAgregarMascota.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-family: 'Roboto'; -fx-padding: 10 20; -fx-background-radius: 5px;");
        btnAgregarMascota.setOnAction(e -> {
            Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
            String nombreMascota = txtNombreMascota.getText();
            String especieMascota = txtEspecieMascota.getText();
            if (clienteSeleccionado != null && !nombreMascota.isEmpty() && !especieMascota.isEmpty()) {
                Mascota mascota = new Mascota(nombreMascota, especieMascota);
                clienteSeleccionado.agregarMascota(mascota);
                txtNombreMascota.clear();
                txtEspecieMascota.clear();
            } else {
                mostrarAlerta("Error", "Debe seleccionar un cliente y completar los campos de la mascota.");
            }
        });

        Button btnEliminarCliente = new Button("Eliminar Cliente");
        btnEliminarCliente.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-family: 'Roboto'; -fx-padding: 10 20; -fx-background-radius: 5px;");
        btnEliminarCliente.setOnAction(e -> {
            Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
            if (clienteSeleccionado != null) {
                listaClientes.remove(clienteSeleccionado);
            } else {
                mostrarAlerta("Error", "Debe seleccionar un cliente para eliminar.");
            }
        });

        Button btnEditarCliente = new Button("Editar Cliente");
        btnEditarCliente.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black; -fx-font-family: 'Roboto'; -fx-padding: 10 20; -fx-background-radius: 5px;");
        btnEditarCliente.setOnAction(e -> {
            Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
            if (clienteSeleccionado != null) {
                txtNombre.setText(clienteSeleccionado.getNombre());
                txtTelefono.setText(clienteSeleccionado.getTelefono());
                txtDireccion.setText(clienteSeleccionado.getDireccion());
                listaClientes.remove(clienteSeleccionado);
            } else {
                mostrarAlerta("Error", "Debe seleccionar un cliente para editar.");
            }
        });

        // Configurar tablaClientes
        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        TableColumn<Cliente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        TableColumn<Cliente, String> colDirección = new TableColumn<>("Dirección");
        colDirección.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDireccion()));
        tablaClientes.getColumns().addAll(colNombre, colTelefono, colDirección);
        tablaClientes.setItems(listaClientes);
        tablaClientes.setStyle("-fx-font-family: 'Roboto'; -fx-background-color: #f4f4f4; -fx-border-color: #ddd;");

        // Crear la imagen para el lado derecho
        ImageView imagenDerecha = new ImageView(new Image("file:src/mascota.jpg")); // Ajusta la ruta si es necesario
        imagenDerecha.setFitWidth(200);
        imagenDerecha.setFitHeight(200);

// Layout para la imagen
        VBox imagenLayout = new VBox(imagenDerecha);
        imagenLayout.setStyle("-fx-alignment: center; -fx-padding: 20;");

// Layouts
        VBox formularioCliente = new VBox(10, lblNombre, txtNombre, lblTelefono, txtTelefono, lblDirección, txtDireccion, btnAgregarCliente, btnEditarCliente);
        VBox formularioMascota = new VBox(10, lblNombreMascota, txtNombreMascota, lblEspecieMascota, txtEspecieMascota, btnAgregarMascota);
        VBox tabla = new VBox(10, tablaClientes, btnEliminarCliente);

// Layout principal
        HBox layout = new HBox(20, formularioCliente, formularioMascota, tabla, imagenLayout);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #ffffff;");

        return new VBox(layout);



    }

    private VBox crearHistorialMedico() {
        TextField txtBuscarCliente = new TextField();
        txtBuscarCliente.setPromptText("Buscar cliente...");

        // ComboBox para seleccionar cliente
        comboCliente = new ComboBox<>(listaClientes);

        FilteredList<Cliente> listaFiltrada = new FilteredList<>(listaClientes, p -> true);
        txtBuscarCliente.textProperty().addListener((observable, oldValue, newValue) -> {
            listaFiltrada.setPredicate(cliente -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return cliente.getNombre().toLowerCase().contains(lowerCaseFilter);
            });
        });
        comboCliente.setItems(listaFiltrada);

        // Tabla para mostrar el historial médico
        tablaHistorial = new TableView<>();
        TableColumn<HistorialMedico, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        ));
        colFecha.setPrefWidth(150); // Ajustar ancho de columna

        TableColumn<HistorialMedico, String> colDiagnostico = new TableColumn<>("Diagnóstico");
        colDiagnostico.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDiagnostico()
        ));
        colDiagnostico.setPrefWidth(200); // Ajustar ancho de columna

        TableColumn<HistorialMedico, String> colTratamiento = new TableColumn<>("Tratamiento");
        colTratamiento.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getTratamiento()
        ));
        colTratamiento.setPrefWidth(200); // Ajustar ancho de columna

        tablaHistorial.getColumns().addAll(colFecha, colDiagnostico, colTratamiento);
        colFecha.setSortType(TableColumn.SortType.DESCENDING); // Ordena descendente por fecha
        tablaHistorial.getSortOrder().add(colFecha);

        // Campos para agregar un nuevo registro
        Label lblDiagnostico = new Label("Diagnóstico:");
        TextField txtDiagnostico = new TextField();
        txtDiagnostico.setPromptText("Ejemplo: Fractura");

        Label lblTratamiento = new Label("Tratamiento:");
        TextField txtTratamiento = new TextField();
        txtTratamiento.setPromptText("Ejemplo: Reposo completo");

        Button btnAgregarRegistro = new Button("Agregar Registro");

        // Acción para el botón de agregar registro
        btnAgregarRegistro.setOnAction(e -> {
            Cliente clienteSeleccionado = comboCliente.getValue();
            if (clienteSeleccionado != null) {
                String diagnostico = txtDiagnostico.getText();
                String tratamiento = txtTratamiento.getText();
                if (!diagnostico.isEmpty() && !tratamiento.isEmpty()) {
                    clienteSeleccionado.agregarHistorial(new HistorialMedico(LocalDate.now(), diagnostico, tratamiento));
                    tablaHistorial.setItems(FXCollections.observableArrayList(clienteSeleccionado.getHistorialMedico()));
                    txtDiagnostico.clear();
                    txtTratamiento.clear();
                    Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
                    confirmacion.setTitle("Registro Agregado");
                    confirmacion.setHeaderText(null);
                    confirmacion.setContentText("El registro se ha agregado exitosamente.");
                    confirmacion.showAndWait();
                } else {
                    mostrarAlerta("Error", "Debe llenar los campos de diagnóstico y tratamiento.");
                }
            } else {
                mostrarAlerta("Error", "Debe seleccionar un cliente para agregar un registro.");
            }
        });

        // Botón para cargar el historial médico
        Button btnCargarHistorial = new Button("Cargar Historial Médico");
        btnCargarHistorial.setOnAction(e -> {
            Cliente clienteSeleccionado = comboCliente.getValue();
            if (clienteSeleccionado != null) {
                tablaHistorial.setItems(FXCollections.observableArrayList(clienteSeleccionado.getHistorialMedico()));
            } else {
                mostrarAlerta("Error", "Debe seleccionar un cliente para cargar el historial médico.");
            }
        });

        // Layout de los controles
        VBox controles = new VBox(10, new Label("Buscar Cliente:"), txtBuscarCliente, comboCliente, lblDiagnostico, txtDiagnostico, lblTratamiento, txtTratamiento, btnAgregarRegistro, btnCargarHistorial);
        VBox tabla = new VBox(10, tablaHistorial);

        // Ajusta el ancho de la tabla para que ocupe más espacio
        tablaHistorial.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaHistorial.setPrefWidth(600); // Aumentar el ancho total de la tabla

        HBox layout = new HBox(20, controles, tabla);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #ffffff;");
        btnAgregarRegistro.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 5px;");
        btnCargarHistorial.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 5px;");

        return new VBox(layout);
    }


    public class ReportesEstadisticas extends Application {
        private ObservableList<Cita> listaCitas = FXCollections.observableArrayList();

        @Override
        public void start(Stage primaryStage) {
            TabPane tabPane = new TabPane();

            Tab tabReportes = new Tab("Reportes y Estadísticas");
            tabReportes.setContent(crearPaginaReportes());
            tabReportes.setClosable(false);

            tabPane.getTabs().add(tabReportes);

            Scene scene = new Scene(tabPane, 900, 600);
            primaryStage.setTitle("Reportes y Estadísticas");
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        private VBox crearPaginaReportes() {
            // Datos de ejemplo para las citas
            listaCitas.add(new Cita("Diagnóstico A", "2025-01-11"));
            listaCitas.add(new Cita("Diagnóstico B", "2025-01-12"));
            listaCitas.add(new Cita("Diagnóstico A", "2025-01-13"));
            listaCitas.add(new Cita("Diagnóstico C", "2025-01-14"));

            // Generar estadísticas
            Map<String, Integer> frecuenciaDiagnosticos = new HashMap<>();
            for (Cita cita : listaCitas) {
                frecuenciaDiagnosticos.put(cita.getDiagnostico(),
                        frecuenciaDiagnosticos.getOrDefault(cita.getDiagnostico(), 0) + 1);
            }

            // Crear el gráfico de barras
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Diagnósticos");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Frecuencia");

            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Frecuencia de Diagnósticos");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Diagnósticos");

            for (Map.Entry<String, Integer> entry : frecuenciaDiagnosticos.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            barChart.getData().add(series);

            // Layout principal
            VBox layout = new VBox(20, barChart);
            layout.setStyle("-fx-padding: 20;");
            return layout;
        }

        public static void main(String[] args) {
            launch(args);
        }

        // Clase auxiliar para representar citas
        public class Cita {
            private String diagnostico;
            private String fecha;

            public Cita(String diagnostico, String fecha) {
                this.diagnostico = diagnostico;
                this.fecha = fecha;
            }

            public String getDiagnostico() {
                return diagnostico;
            }

            public String getFecha() {
                return fecha;
            }
        }
    }



    private VBox crearGestionCitas() {
        // Campos para cita
        Label lblCliente = new Label("Cliente:");
        ComboBox<Cliente> comboCliente = new ComboBox<>(listaClientes);
        Label lblMascota = new Label("Mascota:");
        ComboBox<Mascota> comboMascota = new ComboBox<>();
        Label lblFecha = new Label("Fecha:");
        DatePicker datePickerFecha = new DatePicker();
        Label lblHora = new Label("Hora:");
        Spinner<LocalTime> spinnerHora = new Spinner<>();
        SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<LocalTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            public void decrement(int steps) {
                if (getValue() == null) {
                    setValue(LocalTime.of(8, 0));
                } else {
                    setValue(getValue().minusMinutes(steps * 15));
                }
            }

            @Override
            public void increment(int steps) {
                if (getValue() == null) {
                    setValue(LocalTime.of(8, 0));
                } else {
                    setValue(getValue().plusMinutes(steps * 15));
                }
            }
        };
        valueFactory.setValue(LocalTime.of(8, 0));
        spinnerHora.setValueFactory(valueFactory);
        spinnerHora.setEditable(true);
        spinnerHora.getEditor().setTextFormatter(new TextFormatter<>(new StringConverter<LocalTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            public String toString(LocalTime time) {
                return time != null ? time.format(formatter) : "";
            }

            @Override
            public LocalTime fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalTime.parse(string, formatter) : null;
            }
        }));

        Label lblMotivo = new Label("Motivo:");
        TextField txtMotivo = new TextField();

        Button btnAgendar = new Button("Agendar Cita");
        btnAgendar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-family: 'Roboto'; -fx-background-radius: 5px;");

        Button btnEliminar = new Button("Eliminar Cita");
        btnEliminar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-family: 'Roboto'; -fx-background-radius: 5px;");

        Button btnEditar = new Button("Editar Cita");
        btnEditar.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black; -fx-padding: 10 20; -fx-font-family: 'Roboto'; -fx-background-radius: 5px;");

        // Tabla de citas
        TableView<Cita> tablaCitas = new TableView<>();
        TableColumn<Cita, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCliente().getNombre()));
        TableColumn<Cita, String> colMascota = new TableColumn<>("Mascota");
        colMascota.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMascota().getNombre()));
        TableColumn<Cita, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFecha()));
        TableColumn<Cita, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getHora()));
        TableColumn<Cita, String> colMotivo = new TableColumn<>("Motivo");
        colMotivo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMotivo()));
        tablaCitas.getColumns().addAll(colCliente, colMascota, colFecha, colHora, colMotivo);
        tablaCitas.setItems(listaCitas);
        tablaCitas.setStyle("-fx-font-family: 'Roboto'; -fx-background-color: #f4f4f4; -fx-border-color: #ddd;");

        // Actualizar mascotas según el cliente seleccionado
        comboCliente.setOnAction(e -> {
            Cliente clienteSeleccionado = comboCliente.getValue();
            if (clienteSeleccionado != null) {
                comboMascota.setItems(FXCollections.observableArrayList(clienteSeleccionado.getMascotas()));
            } else {
                comboMascota.setItems(FXCollections.observableArrayList());
            }
        });

        // Botón agendar funcionalidad
        btnAgendar.setOnAction(e -> {
            if (comboCliente.getValue() != null && comboMascota.getValue() != null && datePickerFecha.getValue() != null && spinnerHora.getValue() != null) {
                Cliente cliente = comboCliente.getValue();
                Mascota mascota = comboMascota.getValue();
                String motivo = txtMotivo.getText();
                LocalDate fecha = datePickerFecha.getValue();
                LocalTime hora = spinnerHora.getValue();
                String horaString = DateTimeFormatter.ofPattern("HH:mm").format(hora);
                listaCitas.add(new Cita(cliente, mascota, fecha.toString(), horaString, motivo));

                // Ordenar la lista de citas por fecha y hora
                listaCitas.sort(Comparator.comparing((Cita c) -> LocalDate.parse(c.getFecha()))
                        .thenComparing(c -> LocalTime.parse(c.getHora())));

                // Limpiar los campos
                comboCliente.setValue(null);
                comboMascota.setItems(FXCollections.observableArrayList());
                comboMascota.setValue(null);
                datePickerFecha.setValue(null);
                spinnerHora.getValueFactory().setValue(LocalTime.of(8, 0));
                txtMotivo.clear();
            } else {
                mostrarAlerta("Error", "Todos los campos deben ser completados");
            }
        });

        // Botón eliminar funcionalidad
        btnEliminar.setOnAction(e -> {
            Cita citaSeleccionada = tablaCitas.getSelectionModel().getSelectedItem();
            if (citaSeleccionada != null) {
                listaCitas.remove(citaSeleccionada);
            } else {
                mostrarAlerta("Error", "Debe seleccionar una cita para eliminar");
            }
        });

        // Botón editar funcionalidad
        btnEditar.setOnAction(e -> {
            Cita citaSeleccionada = tablaCitas.getSelectionModel().getSelectedItem();
            if (citaSeleccionada != null) {
                comboCliente.setValue(citaSeleccionada.getCliente());
                comboMascota.setValue(citaSeleccionada.getMascota());
                datePickerFecha.setValue(LocalDate.parse(citaSeleccionada.getFecha()));
                spinnerHora.getValueFactory().setValue(LocalTime.parse(citaSeleccionada.getHora()));
                txtMotivo.setText(citaSeleccionada.getMotivo());
                listaCitas.remove(citaSeleccionada);
            } else {
                mostrarAlerta("Error", "Debe seleccionar una cita para editar");
            }
        });

        // Layout
        VBox formulario = new VBox(10, lblCliente, comboCliente, lblMascota, comboMascota, lblFecha, datePickerFecha, lblHora, spinnerHora, lblMotivo, txtMotivo, btnAgendar, btnEditar);
        VBox tabla = new VBox(10, tablaCitas, btnEliminar);
        HBox layout = new HBox(20, formulario, tabla);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #ffffff;");
        return new VBox(layout);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

