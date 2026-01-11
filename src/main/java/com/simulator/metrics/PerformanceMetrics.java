package com.simulator.metrics;

import com.simulator.model.Process;
import java.util.List;

public class PerformanceMetrics {

    public void printMetrics(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            System.out.println("No hay procesos para calcular métricas.");
            return;
        }

        System.out.println("\n--------------------------------------------------");
        System.out.println("REPORTE DE RENDIMIENTO");
        System.out.println("--------------------------------------------------");
        
        // Encabezados
        System.out.printf("%-5s %-10s %-10s %-10s %-10s %-10s\n", 
                "ID", "Llegada", "Burst", "Espera", "Retorno", "Final");

        double totalWait = 0;
        double totalTurnaround = 0;
        double totalResponse = 0; // Opcional si quisieras calcular Response Time
        int minArrival = Integer.MAX_VALUE;
        int maxFinish = Integer.MIN_VALUE;

        for (Process p : processes) {
            System.out.printf("%-5s %-10d %-10d %-10d %-10d %-10d\n",
                    p.getId(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    p.getWaitingTime(),
                    p.getTurnaroundTime(),
                    p.getFinishTime());

            totalWait += p.getWaitingTime();
            totalTurnaround += p.getTurnaroundTime();
            
            // Para cálculo de Throughput
            if (p.getArrivalTime() < minArrival) minArrival = p.getArrivalTime();
            if (p.getFinishTime() > maxFinish) maxFinish = p.getFinishTime();
        }

        double avgWait = totalWait / processes.size();
        double avgTurnaround = totalTurnaround / processes.size();
        
        // Throughput = Procesos / Tiempo Total
        double totalTime = maxFinish - minArrival;
        double throughput = (totalTime > 0) ? (processes.size() / totalTime) : 0;

        System.out.println("--------------------------------------------------");
        System.out.printf("Promedio Tiempo de Espera:   %.2f\n", avgWait);
        System.out.printf("Promedio Tiempo de Retorno:  %.2f\n", avgTurnaround);
        System.out.printf("Throughput (Procesos/Unid):  %.4f\n", throughput);
        System.out.println("--------------------------------------------------");
    }
}