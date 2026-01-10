package com.simulator.algorithms;

import com.simulator.model.Process;
import java.util.List;

public interface SchedulingStrategy {
    /**
     * Nombre del algoritmo para mostrar en reportes.
     */
    String getName();

    /**
     * Ejecuta la simulación de planificación.
     * @param processes Lista de procesos a planificar.
     * @return La misma lista con los tiempos calculados (Wait, Turnaround, etc).
     */
    List<Process> run(List<Process> processes);
}