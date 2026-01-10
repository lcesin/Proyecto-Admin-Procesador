package com.simulator.model;

/**
 * Define los estados posibles de un proceso según la sección 3.2 del enunciado.
 */
public enum ProcessState {
    NEW,        // Nuevo
    READY,      // Listo
    RUNNING,    // Ejecutando
    TERMINATED, // Terminado
    BLOCKED     // Bloqueado (Opcional, pero buena práctica tenerlo)
}