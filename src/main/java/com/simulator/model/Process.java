package com.simulator.model;

public class Process implements Comparable<Process> {
    // --- Atributos Obligatorios (Enunciado 3.2) ---
    private String id;              // ID único del proceso
    private int arrivalTime;        // Tiempo de llegada
    private int burstTime;          // Tiempo de ráfaga (total necesario)
    private int priority;           // Prioridad (para algoritmo de Prioridades)
    private ProcessState state;     // Estado actual

    // --- Atributos de Control para Simulación ---
    private int remainingTime;      // Tiempo restante (Vital para SRTF y RR)
    
    // --- Atributos para Métricas (Enunciado 3.3) ---
    private int startTime;          // Cuándo empezó a ejecutarse por primera vez (Response Time)
    private int finishTime;         // Cuándo terminó (para Turnaround Time)
    private int waitingTime;        // Tiempo esperando en cola de listos
    private int turnaroundTime;     // Tiempo total desde llegada hasta fin

    // Constructor
    public Process(String id, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.state = ProcessState.NEW;
        
        // Inicializamos el tiempo restante igual a la ráfaga total
        this.remainingTime = burstTime;
        
        // Inicializamos métricas en -1 o 0
        this.startTime = -1; // -1 indica que no ha arrancado
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.finishTime = 0;
    }

    // --- Lógica de Negocio Básica ---

    /**
     * Ejecuta el proceso por una unidad de tiempo (o un ciclo).
     * Útil para simulaciones paso a paso.
     */
    public void executeOneUnit() {
        if (remainingTime > 0) {
            remainingTime--;
        }
    }

    /**
     * Verifica si el proceso ha terminado su ejecución.
     */
    public boolean isFinished() {
        return remainingTime <= 0;
    }

    // --- Getters y Setters ---

    public String getId() { return id; }
    
    public int getArrivalTime() { return arrivalTime; }
    
    public int getBurstTime() { return burstTime; }
    
    public int getPriority() { return priority; }
    
    public ProcessState getState() { return state; }
    public void setState(ProcessState state) { this.state = state; }

    public int getRemainingTime() { return remainingTime; }
    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }

    public int getStartTime() { return startTime; }
    public void setStartTime(int startTime) { this.startTime = startTime; }

    public int getFinishTime() { return finishTime; }
    public void setFinishTime(int finishTime) { this.finishTime = finishTime; }

    public int getWaitingTime() { return waitingTime; }
    public void setWaitingTime(int waitingTime) { this.waitingTime = waitingTime; }

    public int getTurnaroundTime() { return turnaroundTime; }
    public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime = turnaroundTime; }

    // Implementación de Comparable para ordenamiento por defecto (ej. por llegada)
    @Override
    public int compareTo(Process other) {
        // Por defecto ordenamos por tiempo de llegada
        if (this.arrivalTime != other.arrivalTime) {
            return Integer.compare(this.arrivalTime, other.arrivalTime);
        }
        // Si llegan al mismo tiempo, desempata por ID (simula orden de creación)
        return this.id.compareTo(other.id);
    }

    @Override
    public String toString() {
        return "Process{" +
                "id='" + id + '\'' +
                ", arrival=" + arrivalTime +
                ", burst=" + burstTime +
                ", priority=" + priority +
                ", state=" + state +
                ", remaining=" + remainingTime +
                '}';
    }
}