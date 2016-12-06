package jdbcprogram;

import javax.swing.JOptionPane;
import java.sql.*;

public class JDBCProgram {

    static Statement stmt; //Objeto al que llamaremos para sentencias sencillas en SQL.
    static PreparedStatement pstmt; //Objeto para consultas preparadas, como por ejemplo las que tienen parámetros.
    static Connection con; //Objeto con el que establceremos la conexión a mysql

    /*Método main de la aplicación con el que controlamos los valores introducidos 
     por el usuario*/
    public static void main(String args[]) {
        int choice = -1;
        do {
            try {
                choice = getChoice(); //Mostramos el panel de opciones
                if (choice != 0) { //Si la elección no es 0
                    getSelected(choice); //Llamamos al método que da funcionalidad a la aplicación
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Carácter no valido, vuelve a intentarlo.");

            }
        } while (choice != 0);
        System.exit(0); //Si la opción elegida por el usuario es 0. Salimos de la aplicación.
    }

    //Con este método creamos un menú de opciones basado en en JOptionPane.
    public static int getChoice() {
        String choice;
        int ch;
        //JOptionPane que muestra las opciones del menú de inicio
        choice = JOptionPane.showInputDialog(null, "1. Crear tabla Clientes\n"
                + "2. Crear tabla Escrituras y EscCli\n"
                + "3. Insertar datos en la tabla Clientes\n"
                + "4. Insertar datos en la tabla Escrituras\n"
                + "5. Recuperar datos de la tabla Clientes\n"
                + "6. Recuperar datos de la tabla Escrituras\n"
                + "7. Actualizar en tabla Clientes\n"
                + "8. Listar el nombre de los Clientes que hicieron una escritura de compraventa\n"
                + "0. Salir \n" + "Escriba la opción.");
        //Incluimos un if para que en caso de que se pulse cancelar salgamos de la aplicación
        if (choice == null) {
            System.exit(0);
        }
        ch = Integer.parseInt(choice);

        return ch;
    }

    //Método con el que damos funcionalidad al menú que hemos creado anteriormente
    public static void getSelected(int choice) {

        getConnection(); //Realizamos la conexión a la base de datos mySql
        System.out.println(choice);
        //Switch con el que llamaremos a los métodos estáticos definidos "posteriormente"
        switch (choice) {
            case 1:
                crearClientes();
                break;
            case 2:
                crearEscrituras();
                break;
            case 3:
                insertarClientes();
                break;
            case 4:
                insertarEscrituras();
                break;
            case 5:
                obtenerClientes();
                break;
            case 6:
                obtenerEscrituras();
                break;
            case 7:
                updateClientes();
                break;
            case 8:
                consultaCompraventa();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Introduzca un número entre 0 y 8");
        }
    }

    /*Método con el que llebamos a cabo la conexción a mysql. El driver de mysql lo he obtenido
    directamente de los directorios de NetBeans:
    C:\Program Files\NetBeans 8.0.1\ide\modules\ext\mysql-connector-java-5.1.23-bin.jar*/
    public static Connection getConnection() {
        try {
            // Cargar el driver de mysql         
            Class.forName("com.mysql.jdbc.Driver");
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }
        try {
            /*Cadena de conexión para conectar con MySQL en localhost,      
            seleccionar la base de datos llamada "test"         
            con usuario y contraseña del servidor de MySQL: user=root y admin en blanco*/      
            String connectionUrl = "jdbc:mysql://localhost/test?"
                    + "user=root&password=";
            // Obtener la conexión                      
            con = DriverManager.getConnection(connectionUrl);
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        return con;
    }

    //Método con el que cramos la estructura de la tabla clientes
    public static void crearClientes() {
        con = getConnection();
        String createString;
        createString = "CREATE TABLE Clientes (Cod_Cliente int(3), Nombre TEXT(25), Telefono TEXT(9), Constraint Cliente_PK Primary Key (Cod_Cliente))";
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, "Tabla de Clientes creada.");
    }

    /*Método con el que cramos la estructura de la tabla escrituras y EscCli, la cual, relacionada a la
    tabla clientes con la tabla escrituras*/
    public static void crearEscrituras() {
        con = getConnection();
        String createString;
        String createString2;

        createString = "CREATE TABLE Escrituras (Cod_Escritura varchar(3), tipo varchar(5), "
                + "nom_fich varchar(70), num_interv int(2), constraint esc_pk "
                + "primary key (cod_Escritura), constraint interv_ck check (num_interv >= 2))";

        createString2 = "CREATE TABLE EscCli (codigo int(3), codCli int(3), "
                + "codEsc varchar(3), constraint cod_pk primary key (codigo), "
                + "constraint cod_Cli_fk foreign key (codCli) references Clientes(Cod_Cliente), "
                + "constraint cod_Esc_fk foreign key (codEsc) references Escrituras(Cod_Escritura))";
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(createString);
            stmt.executeUpdate(createString2);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, "Tabla de Escrituras creada.");
    }

    //Método con el que introducirmos los datos a la tabla Clientes
    public static void insertarClientes() {
        con = getConnection();
        String insertString1, insertString2, insertString3, insertString4;
        insertString1 = "insert into Clientes values('123', 'AAAAAA', '123123123')";
        insertString2 = "insert into Clientes values('124', 'BBB', '213213213')";
        insertString3 = "insert into Clientes values('125', 'CCC', '321321321')";
        insertString4 = "insert into Clientes values('126', 'DDD', '123412344')";
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(insertString1);
            stmt.executeUpdate(insertString2);
            stmt.executeUpdate(insertString3);
            stmt.executeUpdate(insertString4);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, "Datos insertados en la tabla Empleados");
    }

    /*Método con el que introducirmos los datos a la tabla Escrituras y a la tabla que relaciona
     las tablas y las escrituras*/
    public static void insertarEscrituras() {
        con = getConnection(); //Establemos la conexión
        String insertString1, insertString2, insertString3;
        insertString1 = "insert into Escrituras values('a12', 'CPVE', 'compraventa_AntonioBJ_10_12_2010.docx',3)"; //Tabla clientes
        insertString2 = "insert into Escrituras values('b12', 'TEST', 'compraventa_JoaquinSS_10_12_2010.docx',4)"; //Tabla clientes
        insertString3 = "insert into EscCli values('1', '123', 'a12')"; //Tabla EscCli
        try {
            stmt = con.createStatement(); //Preparamos el insert
            stmt.executeUpdate(insertString1);//Ejecutamos las tres inserciones de datos
            stmt.executeUpdate(insertString2);
            stmt.executeUpdate(insertString3);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, "Datos insertados en la tabla Escrituras");
    }

    /*Método mediante el cual obtenemos todos los clientes almacenados en la tabla Clientes*/
    public static void obtenerClientes() {
        String result = null;
        try {
            con = getConnection();//Establecemos la conexión
            String selectString; //String que contendrá la consulta
            selectString = "select * from Clientes"; //Consulta SQL
            result = "Cod\t \t Nombre\n"; //Esta es la primera fila que obtendremos como resultado(valor fijo)
            stmt = con.createStatement();//Preparamos la consulta
            ResultSet rs = stmt.executeQuery(selectString); //La ejecutamos. ResultSet nos devulve los resultados del SELECT
            while (rs.next()) { //La devolución se hace fila por fila
                int id = rs.getInt("Cod_Cliente"); //Variable con contendrá el código
                String name = rs.getString("Nombre"); //Variable que contendrá el nombre
                result += id + "\t \t" + name + "\n"; //Añadimos una fila de resultados antes de volver a iterar
            }
            stmt.close(); //Cerramos la consulta
            con.close(); //Cerramos la conexión
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, result);
    }

    /*Método mediante el cual obtenemos todos los datos de las escrituras almacenados 
     en la tabla Escrituras. Para más información consultar el método obtenerClientes() */
    public static void obtenerEscrituras() {
        String result = null;
        try {
            con = getConnection();
            String selectString;
            selectString = "select * from Escrituras";
            result = "Cod\t \t Nombre \t Nombre_fichero \t Interventores\n";
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectString);
            while (rs.next()) {
                String codigo = rs.getString("cod_Escritura");
                String nombre = rs.getString("tipo");
                String fichero = rs.getString("nom_fich");
                int intervinen = rs.getInt("num_interv");

                result += codigo + "\t \t" + nombre + "\t \t" + fichero + "\t \t" + intervinen + "\t \t" + "\n";
            }
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, result);
    }

    //Método para actualizar los registros almacenados en la tabla Clientes
    public static void updateClientes() {
        con = getConnection();
        String updateString1;
        updateString1 = "UPDATE Clientes SET Nombre = 'Diego' where Cod_Cliente = '211'";
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(updateString1);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, "Datos actualizados en la tablaEmpleados");
    }

    /*Método con el que consultamos los clientes que hayan realizado una 
     compraventa (tipo de escritura CPVE)*/
    public static void consultaCompraventa() {
        con = getConnection();
        String result = null;
        String selectString;
        selectString = "select Clientes.Nombre from Clientes, Escrituras, EscCli where tipo = 'CPVE' " + "and Clientes.Cod_Cliente = EscCli.CodCli and EscCli.CodEsc = Escrituras.Cod_Escritura ";
        result = "Cliente \n";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectString); //Ejecutamos la consulta
            while (rs.next()) {
                String name = rs.getString("Nombre");
                result += name + "\n";
            }
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, result);
    }
}
