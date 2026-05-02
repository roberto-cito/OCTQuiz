package com.oct.octquiz.Model;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.List;

/*
@Component
public class ActiveSession extends Thread implements CommandLineRunner {
    private final SessionRegistry sessionRegistry;

    public ActiveSession(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public void printActiveSessions() {
        List<Object> principals = sessionRegistry.getAllPrincipals();

        if (principals.isEmpty()) {
            System.out.println("Nessun utente loggato al momento.");
            return;
        }

        System.out.println("--- Report Sessioni Attive ---");
        for (Object principal : principals) {
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
            for (SessionInformation session : sessions) {
                System.out.println("Utente: " + principal + " | Session ID: " + session.getSessionId());
                System.out.println("Ultimo accesso: " + session.getLastRequest());
            }
        }
        System.out.println("------------------------------");
    }

    @Override
    public void run() {
        // Usiamo un controllo sull'interruzione per permettere al thread di chiudersi bene
        while (!Thread.currentThread().isInterrupted()) {
            printActiveSessions();
            try {
                // MODIFICA: Thread.sleep invece di wait()
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Se il thread viene interrotto (es. spegnimento app), usciamo dal loop
                Thread.currentThread().interrupt();
                break;
            }
        }
    }


    @Override
    public void run(String... args) throws Exception {
        this.setName("ActiveSessionMonitor-Thread");
        this.setDaemon(true);
        this.start(); // Avvia effettivamente il thread in background
    }
}*/