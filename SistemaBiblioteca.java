import java.util.ArrayList;
import java.util.List;

/* ================================================================
   PATRÓN SINGLETON
   La clase Biblioteca representa el único punto central del sistema.
   Solo puede existir una instancia de esta clase.
   ================================================================ */

class Biblioteca {

    // Instancia única del Singleton
    private static Biblioteca instancia;

    // Lista de libros registrados
    private List<Libro> libros = new ArrayList<>();

    // Lista de observadores del sistema (Observer)
    private List<Observador> observadores = new ArrayList<>();

    // Constructor privado (clave del patrón Singleton)
    private Biblioteca() {}

    // Método de acceso global a la instancia (Singleton)
    public static Biblioteca getInstancia() {
        if (instancia == null) {
            instancia = new Biblioteca();
        }
        return instancia;
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
        notificar("Nuevo libro registrado: " + libro.getTitulo());
    }

    public Libro buscarPorTitulo(String titulo) {
        for (Libro libro : libros) {
            if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                return libro;
            }
        }
        return null;
    }


    /* ================================================================
       PATRÓN OBSERVER
       Registro y notificación a observadores del sistema.
       ================================================================ */

    public void registrarObservador(Observador obs) {
        observadores.add(obs);
    }

    public void notificar(String mensaje) {
        for (Observador o : observadores) {
            o.actualizar(mensaje);
        }
    }
}

/* ================================================================
   Entidad Libro: representa un libro en la biblioteca.
   ================================================================ */

class Libro {
    private String titulo;
    private boolean prestado = false;

    public Libro(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() { return titulo; }
    public boolean estaPrestado() { return prestado; }

    public void prestar() {
        prestado = true;
    }

    public void devolver() {
        prestado = false;
    }
}

/* ================================================================
   PATRÓN OBSERVER
   Interfaz que define a los observadores.
   ================================================================ */
interface Observador {
    void actualizar(String mensaje);
}

/* ================================================================
   Observador concreto: Administrador
   Responde a notificaciones del sistema.
   ================================================================ */

class Administrador implements Observador {
    private String nombre;

    public Administrador(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void actualizar(String mensaje) {
        System.out.println(nombre + " recibió notificación: " + mensaje);
    }
}

/* ================================================================
   PATRÓN ADAPTER
   Sistema externo que no se puede modificar.
   ================================================================ */

class SistemaExternoISBN {
    public String getCodigoISBN() {
        return "EXT-ISBN-332211";
    }
}

/* Interfaz requerida por nuestro sistema */
interface ISBN {
    String obtenerCodigo();
}

/* ================================================================
   Adaptador que convierte SistemaExternoISBN en un ISBN válido
   ================================================================ */

class ISBNAdapter implements ISBN {

    private SistemaExternoISBN externo;

    public ISBNAdapter(SistemaExternoISBN externo) {
        this.externo = externo;
    }

    @Override
    public String obtenerCodigo() {
        return externo.getCodigoISBN(); 
    }
}

/* ================================================================
   MAIN: Programa principal del sistema
   ================================================================ */

public class SistemaBiblioteca {
    public static void main(String[] args) {

        // Obtener instancia Singleton de la biblioteca
        Biblioteca biblioteca = Biblioteca.getInstancia();

        // Crear y registrar observadores (Observer)
        Administrador admin1 = new Administrador("Carlos");
        Administrador admin2 = new Administrador("Andrea");
        biblioteca.registrarObservador(admin1);
        biblioteca.registrarObservador(admin2);

        // Crear libros
        Libro libro1 = new Libro("El Quijote");
        Libro libro2 = new Libro("Arquitectura de Software");

        biblioteca.agregarLibro(libro1);
        biblioteca.agregarLibro(libro2);

        // Uso del Adapter para obtener ISBN externo
        ISBN isbn = new ISBNAdapter(new SistemaExternoISBN());
        System.out.println("Código ISBN adaptado: " + isbn.obtenerCodigo());

        // Préstamo
        Libro buscar = biblioteca.buscarPorTitulo("Arquitectura de Software");
        if (buscar != null && !buscar.estaPrestado()) {
            buscar.prestar();
            biblioteca.notificar("El libro 'Arquitectura de Software' ha sido prestado.");
        }

        // Devolución
        buscar.devolver();
        biblioteca.notificar("El libro 'Arquitectura de Software' ha sido devuelto.");
    }
}
