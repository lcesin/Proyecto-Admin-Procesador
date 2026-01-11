package com.simulator.algorithms;

import com.simulator.model.Process;
import com.simulator.model.ProcessState;
import java.util.Comparator;
import java.util.List;

public class FCFS implements SchedulingStrategy {

    @Override
    public String getName() {
        return "First Come First Served (FCFS)";
    }

    @Override
    public List<Process> run(List<Process> processes) {
        // 1. Ordenar procesos por tiempo de llegada
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;

        for (Process process : processes) {
            // Caso: El CPU está libre pero el proceso no ha llegado aún (CPU Idle)
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }

            // --- Inicio de Ejecución ---
            process.setStartTime(currentTime);
            process.setState(ProcessState.RUNNING);

            // LÍNEA NUEVA:
            process.addExecutionInterval(currentTime, currentTime + process.getBurstTime());

            // --- Cálculos de Métricas ---
            // Tiempo de Espera = Momento en que inicia - Momento en que llegó
            process.setWaitingTime(currentTime - process.getArrivalTime());

            // Simulamos la ejecución completa (es Non-Preemptive)
            currentTime += process.getBurstTime();

            // --- Fin de Ejecución ---
            process.setFinishTime(currentTime);
            // Tiempo de Retorno = Momento en que termina - Momento en que llegó
            process.setTurnaroundTime(process.getFinishTime() - process.getArrivalTime());
            
            process.setRemainingTime(0);
            process.setState(ProcessState.TERMINATED);
        }

        return processes;
    }
}