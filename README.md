# 🧠 OCTQuiz

![Version](https://img.shields.io/badge/version-v0.0.1-blue)
![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.7-6DB33F?logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-K8s-326CE5?logo=kubernetes&logoColor=white)
![Build](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-All_Rights_Reserved-red)

**OCTQuiz** è l'applicazione web ufficiale dedicata all'allenamento per le semifinali delle **Olimpiadi della Cultura e del Talento (OCT)**.

Progettata per essere una piattaforma di training interattiva e gratuita, permette ai ragazzi partecipanti di esercitarsi e mettere alla prova le proprie conoscenze attraverso dei quiz simulati. L'intero sistema garantisce inoltre la raccolta dei dati in forma esclusivamente anonima, rispettando la privacy degli studenti.

> [!WARNING]
> **Copyright & Licenza:** Questo software è protetto da diritto d'autore (All Rights Reserved). Non è consentita la copia, distribuzione, modifica, o alcun uso commerciale o personale del codice senza esplicita autorizzazione.

> [!IMPORTANT]
> **Stato del Progetto:** L'applicazione è stata impiegata come piattaforma ufficiale di allenamento per le semifinali delle competizioni OCT, garantendo a migliaia di studenti di prepararsi al meglio in vista della gara ufficiale.

---

#### 🔗 Link Utili
* **Sito Ufficiale OCT:** [olimpiadidellacultura.it](https://www.olimpiadidellacultura.it/)

---

#### 📑 Indice
*   [🏆 Il Ruolo di OCTQuiz](#-il-ruolo-di-octquiz)
*   [📱 Moduli e Funzionalità](#-moduli-e-funzionalità)
*   [💻 Come Funziona il Progetto (Architettura)](#-come-funziona-il-progetto-architettura)
*   [🛡️ Privacy e Anonimato](#️-privacy-e-anonimato)
*   [🛠 Tech Stack](#-tech-stack)
*   [⚙️ Pipeline CI/CD: Deploy Automatico su Kubernetes](#️-pipeline-cicd-deploy-automatico-su-kubernetes)
*   [☕ Sviluppo Locale con Docker](#-sviluppo-locale-con-docker)

---

#### 🏆 Il Ruolo di OCTQuiz

Durante il periodo precedente le semifinali, OCTQuiz funge da vera e propria **palestra virtuale** per i partecipanti. 
L'obiettivo è consentire alle squadre di testare il proprio livello di preparazione, familiarizzare con la tipologia di domande previste nel concorso e accedere a classifiche (anonime) per confrontarsi in modo sano con le altre squadre.

---

#### 📱 Moduli e Funzionalità

L'applicazione è strutturata per offrire un'esperienza fluida e formativa:
*   **Allenamento Quiz:** Interfaccia dinamica per la compilazione dei quiz, strutturata con timer e tipologie di domande analoghe a quelle della gara ufficiale.
*   **Gestione Classifiche:** Calcolo dei punteggi automatizzato e generazione di classifiche generali (visibili ai ragazzi in forma anonima) e accessibili dagli amministratori tramite una dashboard dedicata.
*   **Esportazione CSV:** Modulo amministrativo per l'esportazione dei risultati e delle statistiche globali in formato CSV, utili all'organizzazione.
*   **Dashboard Amministrativa:** Interfaccia protetta che permette la configurazione delle prove, la gestione di quiz e classifiche e il monitoraggio dell'andamento dei test.

---

#### 💻 Come Funziona il Progetto (Architettura)

L'applicazione segue il paradigma MVC, basandosi su **Spring Boot** per gestire l'interfaccia utente server-side e l'integrazione backend:

1. **Frontend Server-Side Rendering:**
   Le viste sono realizzate in **HTML/Thymeleaf**, permettendo un rendering dinamico dei dati provenienti dal backend. Il front-end include CSS e JS per migliorare l'interattività dell'allenamento.
2. **Backend Application Layer:**
   Il core logico in **Java 17 / Spring Boot** gestisce le richieste HTTP, l'autenticazione (via Spring Security), le sessioni utente e il salvataggio/recupero dati tramite **Spring Data JPA**.
3. **Database Relazionale:**
   I dati sono persistiti su un database **MySQL**. In ambiente di test viene usato un database *in-memory* come H2.

Tutte le componenti sono orchestrate mediante **Docker Compose** e deployate in un cluster **Kubernetes** per supportare alti picchi di traffico durante i periodi di allenamento più intensi.

---

#### 🛡️ Privacy e Anonimato
Il progetto è stato sviluppato con particolare riguardo alla gestione dei dati degli studenti. Nel rispetto delle normative e della natura dell'evento, **la raccolta dei dati dei test di allenamento avviene in maniera esclusivamente anonima**, garantendo a tutti una simulazione sicura.

---

#### 🛠 Tech Stack

Il progetto è costruito su un set di tecnologie Enterprise e DevOps moderne:

*   **[Java 17](https://jdk.java.net/17/) & [Spring Boot 3](https://spring.io/projects/spring-boot):** Il framework robusto che gestisce il ciclo di vita dell'applicazione, dall'accesso ai dati alla sicurezza.
*   **[Thymeleaf](https://www.thymeleaf.org/):** Motore di template per l'integrazione fluida di HTML e Java.
*   **[MySQL](https://www.mysql.com/):** Database relazionale utilizzato in produzione per memorizzare domande e statistiche anonime.
*   **[Docker & Docker Compose](https://www.docker.com/):** Per incapsulare l'app e il database in un ambiente standardizzato.
*   **[Kubernetes](https://kubernetes.io/):** Scelto per l'ambiente di produzione al fine di garantire auto-scaling e bilanciamento dei carichi.

---

#### ⚙️ Pipeline CI/CD: Deploy Automatico su Kubernetes

> [!NOTE]
> **Scopo Portfolio:** La pipeline CI/CD definita in questo repository è configurata per l'ambiente di produzione reale. Tuttavia, trattandosi in questo caso di un repository pubblico a scopo portfolio, **i job di Build Docker e Deploy Kubernetes sono stati intenzionalmente disabilitati (`if: false`)**. Ad ogni push o pull request viene eseguita esclusivamente la suite completa di test automatizzati per validare il codice.

Il flusso originario definito nel file `.github/workflows/CI-CD-pipeline.yml`:
1. **Build & Test:** Il codice Java viene compilato e la suite completa di Unit test, Controller test, Model test ed E2E test (tramite Selenium) viene eseguita in parallelo in ambiente Ubuntu con JDK 17 e container MySQL effimero. Vengono poi generati e pubblicati i report testuali (JUnit).
2. **Build & Push Docker Images (Disabilitato nel Portfolio):** Vengono compilate le immagini Docker dell'applicazione basate su JDK 17 e caricate su Docker Hub.
3. **Deploy to Kubernetes (Disabilitato nel Portfolio):** Il runner si connette via SSH al server master K8s, inietta i secret essenziali ed esegue l'apply e il `rollout restart` del deployment.

---

#### ☕ Sviluppo Locale con Docker

Il metodo più veloce per avviare il progetto e l'infrastruttura in locale è sfruttare **Docker Compose**.

**Requisiti:**
*   [Docker Desktop](https://www.docker.com/products/docker-desktop/) installato e avviato sul sistema.

**Passaggi:**
1. Crea o modifica il file di configurazione ambientale popolandolo con i dati base (opzionale per configurazioni esterne).
2. Avvia l'infrastruttura completa:
   ```bash
   docker-compose up --build
   ```
3. L'applicazione sarà accessibile all'indirizzo associato al contesto configurato.
