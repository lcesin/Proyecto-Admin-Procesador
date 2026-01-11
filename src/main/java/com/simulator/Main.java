package com.simulator; // Paquete raíz

// Imports de tus sub-paquetes
import com.simulator.algorithms.FCFS;
import com.simulator.algorithms.Priority;
import com.simulator.algorithms.RoundRobin;
import com.simulator.algorithms.SJF;
import com.simulator.algorithms.SchedulingStrategy;
import com.simulator.model.Process;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Crear datos de prueba (Process ID, Arrival, Burst, Priority)
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", 0, 10, 1));
        processes.add(new Process("P2", 1, 5, 2));
        processes.add(new Process("P3", 3, 2, 3));

        // 2. Instanciar el algoritmo
        SchedulingStrategy strategy = new RoundRobin(2);

        // 3. Ejecutar
        System.out.println("Ejecutando: " + strategy.getName());
        strategy.run(processes);

        // 4. Imprimir resultados
        System.out.println("ID\tLlegada\tRáfaga\tEspera\tRetorno\tFinal");
        float totalWait = 0;
        float totalTurnaround = 0;

        for (Process p : processes) {
            System.out.println(p.getId() + "\t" +
                    p.getArrivalTime() + "\t" +
                    p.getBurstTime() + "\t" +
                    p.getWaitingTime() + "\t" +
                    p.getTurnaroundTime() + "\t" +
                    p.getFinishTime());
            
            totalWait += p.getWaitingTime();
            totalTurnaround += p.getTurnaroundTime();
        }

        System.out.println("\nPromedio Espera: " + (totalWait / processes.size()));
        System.out.println("Promedio Retorno: " + (totalTurnaround / processes.size()));
    }
}