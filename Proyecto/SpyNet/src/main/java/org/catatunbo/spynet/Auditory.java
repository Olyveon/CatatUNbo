package org.catatunbo.spynet;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Auditory {
    private int id;
    private String nombre;
    private String cliente;
    private String encargado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    

    private int auditoryId;
    private String auditoryName;
    private int auditoryClientId;
    private String auditoryState;
    private LocalDateTime auditoryDateInit;
    private LocalDateTime auditoryDateLimit;
    private LocalDateTime auditoryDateEnd;
    private String clientName;
    private String assignedUser;

    public Auditory() {}

    // Constructor para TableView (original)
    public Auditory(int id, String nombre, String cliente, String encargado, LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.cliente = cliente;
        this.encargado = encargado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        
        this.auditoryId = id;
        this.auditoryName = nombre;
        this.clientName = cliente;
        this.assignedUser = encargado;
        this.auditoryState = estado;
    }

    // Getters y setters 
    public int getId() { return id; }
    public void setId(int id) { 
        this.id = id; 
        this.auditoryId = id;
    }
    
    public String getNombre() { 
        return nombre; 
    }

    public void setNombre(String nombre) { 
        this.nombre = nombre; 
        this.auditoryName = nombre;
    }
    
    public String getCliente() { 
        return cliente; 
    }

    public void setCliente(String cliente) { 
        this.cliente = cliente; 
        this.clientName = cliente;
    }
    
    public String getEncargado() { 
        return encargado; 
    }

    public void setEncargado(String encargado) { 
        this.encargado = encargado; 
        this.assignedUser = encargado;
    }
    
    public LocalDate getFechaInicio() { 
        return fechaInicio; }

    public void setFechaInicio(LocalDate fechaInicio) { 
        this.fechaInicio = fechaInicio; 
        if (fechaInicio != null) {
            this.auditoryDateInit = fechaInicio.atStartOfDay();
        }
    }
    
    public LocalDate getFechaFin() { 
        return fechaFin; 
    
    }
    public void setFechaFin(LocalDate fechaFin) { 
        this.fechaFin = fechaFin; 
        if (fechaFin != null) {
            this.auditoryDateEnd = fechaFin.atStartOfDay();
        }
    }
    
    public String getEstado() {
         return estado; 
    }

    public void setEstado(String estado) { 
        this.estado = estado; 
        this.auditoryState = estado;
    }

    // Getters y setters para campos de BD (para DAO)
    public int getAuditoryId() { 
        return auditoryId; 
    }

    public void setAuditoryId(int auditoryId) { 
        this.auditoryId = auditoryId; 
        this.id = auditoryId;
    }

    public String getAuditoryName() { 
        return auditoryName; 
    }

    public void setAuditoryName(String auditoryName) { 
        this.auditoryName = auditoryName; 
        this.nombre = auditoryName;
    }

    public int getAuditoryClientId() { 
        return auditoryClientId; 
    }

    public void setAuditoryClientId(int auditoryClientId) { this.auditoryClientId = auditoryClientId; }

    public String getAuditoryState() { 
        return auditoryState; 
    }

    public void setAuditoryState(String auditoryState) { 
        this.auditoryState = auditoryState; 
        this.estado = auditoryState;
    }

    public LocalDateTime getAuditoryDateInit() { 
        return auditoryDateInit;

    }
    public void setAuditoryDateInit(LocalDateTime auditoryDateInit) { 
        this.auditoryDateInit = auditoryDateInit; 
        if (auditoryDateInit != null) {
            this.fechaInicio = auditoryDateInit.toLocalDate();
        }
    }

    public LocalDateTime getAuditoryDateLimit() { 
        return auditoryDateLimit; 
    }

    public void setAuditoryDateLimit(LocalDateTime auditoryDateLimit) { 
        this.auditoryDateLimit = auditoryDateLimit; 
    }

    public LocalDateTime getAuditoryDateEnd() { 
        return auditoryDateEnd; 
    }

    public void setAuditoryDateEnd(LocalDateTime auditoryDateEnd) { 
        this.auditoryDateEnd = auditoryDateEnd; 
        if (auditoryDateEnd != null) {
            this.fechaFin = auditoryDateEnd.toLocalDate();
        }
    }

    public String getClientName() { 
        return clientName; 
    }

    public void setClientName(String clientName) { 
        this.clientName = clientName; 
        this.cliente = clientName;
    }

    public String getAssignedUser() { 
        return assignedUser; 
    }
    
    public void setAssignedUser(String assignedUser) { 
        this.assignedUser = assignedUser; 
        this.encargado = assignedUser;
    }

    @Override
    public String toString() {
        return "Auditory{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cliente='" + cliente + '\'' +
                ", encargado='" + encargado + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
