package com.simulator.algorithms;

import com.simulator.model.Process;
import com.simulator.model.ProcessState;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Priority implements SchedulingStrategy {

    @Override
    public String getName() {
        return "Priority Scheduling (Non-Preemptive)";
    }

    @Override
    public List<Process> run(List<Process> processes) {
        int currentTime = 0;
        int completedProcesses = 0;
        int totalProcesses = processes.size();
        
        List<Process> remainingProcesses = new ArrayList<>(processes);

        while (completedProcesses < totalProcesses) {
            // 1. Filtrar procesos que ya llegaron y están pendientes
            int finalCurrentTime = currentTime;
            List<Process> availableProcesses = remainingProcesses.stream()
                    .filter(p -> p.getArrivalTime() <= finalCurrentTime && p.getState() != ProcessState.TERMINATED)
                    .collect(Collectors.toList());

            if (availableProcesses.isEmpty()) {
                currentTime++;
            } else {
                // 2. CAMBIO CLAVE: Seleccionar por Prioridad (Menor valor = Mayor prioridad)
                // Si hay empate en prioridad, usamos FCFS (orden de llegada) implícitamente
                Process highestPriority = availableProcesses.stream()
                        .min(Comparator.comparingInt(Process::getPriority)) 
                        .orElseThrow();

                highestPriority.setStartTime(currentTime);
                highestPriority.setState(ProcessState.RUNNING);
                highestPriority.setWaitingTime(currentTime - highestPriority.getArrivalTime());

                // CORRECCIÓN PARA GRÁFICO:
                highestPriority.addExecutionInterval(currentTime, currentTime + highestPriority.getBurstTime());

                currentTime += highestPriority.getBurstTime();

                highestPriority.setFinishTime(currentTime);
                highestPriority.setTurnaroundTime(highestPriority.getFinishTime() - highestPriority.getArrivalTime());
                highestPriority.setRemainingTime(0);
                highestPriority.setState(ProcessState.TERMINATED);

                completedProcesses++;
            }
        }

        return processes;
    }
}