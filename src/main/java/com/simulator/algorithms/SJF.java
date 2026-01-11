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
        
        List<Process> remainingProcesses = new ArrayList<>(processes);

        while (completedProcesses < totalProcesses) {
            // 1. Filtrar procesos disponibles
            int finalCurrentTime = currentTime;
            List<Process> availableProcesses = remainingProcesses.stream()
                    .filter(p -> p.getArrivalTime() <= finalCurrentTime && p.getState() != ProcessState.TERMINATED)
                    .collect(Collectors.toList());

            if (availableProcesses.isEmpty()) {
                currentTime++;
            } else {
                // 2. Seleccionar el de menor ráfaga
                Process shortest = availableProcesses.stream()
                        .min(Comparator.comparingInt(Process::getBurstTime))
                        .orElseThrow();

                // 3. Configurar inicio
                if (shortest.getStartTime() == -1) {
                    shortest.setStartTime(currentTime);
                }
                shortest.setState(ProcessState.RUNNING);
                shortest.setWaitingTime(currentTime - shortest.getArrivalTime());

                // --- NUEVO: GUARDAR HISTORIAL PARA EL GRÁFICO ---
                // Nótese que usamos 'shortest' (el nombre de la variable local), no 'process'
                shortest.addExecutionInterval(currentTime, currentTime + shortest.getBurstTime());
                // ------------------------------------------------

                // Avanzar tiempo
                currentTime += shortest.getBurstTime();

                // Finalizar
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