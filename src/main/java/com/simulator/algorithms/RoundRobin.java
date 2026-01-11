package com.simulator.algorithms;

import com.simulator.model.Process;
import com.simulator.model.ProcessState;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RoundRobin implements SchedulingStrategy {

    private int quantum;

    public RoundRobin(int quantum) {
        this.quantum = quantum;
    }

    @Override
    public String getName() {
        return "Round Robin (Quantum: " + quantum + ")";
    }

    @Override
    public List<Process> run(List<Process> processes) {
        // Ordenamos inicialmente por llegada para llenar la cola en orden
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        int completed = 0;
        Queue<Process> readyQueue = new LinkedList<>();
        
        // Índice para saber qué procesos nuevos faltan por entrar a la cola
        int processIndex = 0;

        // Primer paso: Agregar procesos que llegan en t=0
        while(processIndex < processes.size() && processes.get(processIndex).getArrivalTime() <= currentTime) {
            readyQueue.add(processes.get(processIndex));
            processIndex++;
        }

        while(completed < processes.size()) {
            if (readyQueue.isEmpty()) {
                // Si la cola está vacía pero faltan procesos, avanzamos el tiempo
                currentTime++;
                // Revisamos si llegó alguien en este nuevo segundo
                while(processIndex < processes.size() && processes.get(processIndex).getArrivalTime() <= currentTime) {
                    readyQueue.add(processes.get(processIndex));
                    processIndex++;
                }
            } else {
                Process currentProcess = readyQueue.poll();
                
                // Si es la primera vez que lo toca el CPU
                if (currentProcess.getStartTime() == -1) {
                    currentProcess.setStartTime(currentTime);
                }
                currentProcess.setState(ProcessState.RUNNING);

                // Determinar tiempo de ejecución en este ciclo (Quantum o lo que le falte)
                int timeSlice = Math.min(quantum, currentProcess.getRemainingTime());

                // Ejecutar
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - timeSlice);
                currentTime += timeSlice;

                // --- GESTIÓN DE COLA (CRÍTICO) ---
                // 1. Antes de re-encolar el actual, agregamos los NUEVOS que llegaron durante esta ejecución
                while(processIndex < processes.size() && processes.get(processIndex).getArrivalTime() <= currentTime) {
                    readyQueue.add(processes.get(processIndex));
                    processIndex++;
                }

                // 2. Decidir qué hacer con el proceso actual
                if (currentProcess.getRemainingTime() > 0) {
                    // No terminó -> Vuelve a la cola (Estado LISTO)
                    currentProcess.setState(ProcessState.READY);
                    readyQueue.add(currentProcess);
                } else {
                    // Terminó -> Cálculos finales
                    currentProcess.setState(ProcessState.TERMINATED);
                    currentProcess.setFinishTime(currentTime);
                    currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                    // Espera = Turnaround - Ráfaga Original (BurstTime)
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                    completed++;
                }
            }
        }
        return processes;
    }
}