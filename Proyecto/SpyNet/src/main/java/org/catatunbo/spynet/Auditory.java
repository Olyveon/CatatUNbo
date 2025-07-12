package org.catatunbo.spynet;

import java.time.LocalDate;

public class Auditory {
    private int id;
    private String nombre;
    private String cliente;
    private String encargado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;

    // Constructor, getters y setters
    public Auditory(int id, String nombre, String cliente, String encargado, LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.cliente = cliente;
        this.encargado = encargado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCliente() { return cliente; }
    public String getEncargado() { return encargado; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public String getEstado() { return estado; }
}
