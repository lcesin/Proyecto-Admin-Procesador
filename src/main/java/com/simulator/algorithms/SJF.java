package com.simulator.algorithms;

import com.simulator.model.Process;
import com.simulator.model.ProcessState;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SJF implements SchedulingStrategy {

    @Override
    public String getName() {
        return "Shortest Job First (SJF) - Non-Preemptive";
    }

    @Override
    public List<Process> run(List<Process> processes) {
        int currentTime = 0;
        int completedProcesses = 0;
        int totalProcesses = processes.size();
        
        // Mantenemos una lista de procesos pendientes para no modificar la original desordenadamente
        List<Process> remainingProcesses = new ArrayList<>(processes);

        while (completedProcesses < totalProcesses) {
            // 1. Filtrar procesos que ya llegaron y NO han terminado
            int finalCurrentTime = currentTime;
            List<Process> availableProcesses = remainingProcesses.stream()
                    .filter(p -> p.getArrivalTime() <= finalCurrentTime && p.getState() != ProcessState.TERMINATED)
                    .collect(Collectors.toList());

            if (availableProcesses.isEmpty()) {
                // Si no hay procesos disponibles, avanzamos el tiempo
                // (Podríamos saltar al siguiente tiempo de llegada para optimizar, 
                // pero ++ simula el ciclo de reloj ocioso)
                currentTime++;
            } else {
                // 2. Seleccionar el de menor ráfaga (Burst Time)
                Process shortest = availableProcesses.stream()
                        .min(Comparator.comparingInt(Process::getBurstTime))
                        .orElseThrow();

                // 3. Ejecutar el proceso (Non-Preemptive: todo de una vez)
                shortest.setStartTime(currentTime);
                shortest.setState(ProcessState.RUNNING);
                shortest.setWaitingTime(currentTime - shortest.getArrivalTime());

                // Avanzamos el reloj toda la duración de la ráfaga
                currentTime += shortest.getBurstTime();

                // Finalizar proceso
                shortest.setFinishTime(currentTime);
                shortest.setTurnaroundTime(shortest.getFinishTime() - shortest.getArrivalTime());
                shortest.setRemainingTime(0);
                shortest.setState(ProcessState.TERMINATED);

                completedProcesses++;
            }
        }

        return processes;
    }
}