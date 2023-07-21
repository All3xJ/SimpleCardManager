Per avviare, eseguire il file `schema.sql` (all'interno di src/main/resources/) che creerà e popolerà il database con alcune informazioni iniziali.  
Dopodiché, modificare il file `application.properties` con le informazioni del proprio DBMS (modificando IP, porta, username e password),  
e finalmente si può compilare e avviare il progetto Spring. Infine, aprire il browser su `localhost:8080`.

**Vi sono i seguenti utenti al momento nel database:**

- **Credenziali primo admin:**
- Username: `admin`
- Password: `admin`

- **Credenziali secondo admin:**
- Username: `admin2`
- Password: `admin2`

- **Credenziali primo merchant:**
- Username: `merc`
- Password: `merc`

- **Credenziali secondo merchant (disabilitato):**
- Username: `merc2`
- Password: `merc2`

- **Credenziali primo cardowner della carta 1:**
- Username: `ouTQCAXAG`
- Password: `It7cZz`

- **Credenziali secondo cardowner della carta 2 (disabilitata):**
- Username: `jFWAwVl`
- Password: `zrz77Nx3k2`

- **Credenziali terzo cardowner della carta 3:**
- Username: `klXTdxSz`
- Password: `aJX9xeRx2g`

\
**INFORMAZIONI GENERALI:**
Quando una carta viene generata/creata dall’admin, viene automaticamente generato un account `cardowner` a cui la carta è associata (si può immaginare che un cliente  
che acquista una carta, riceve una busta contenente l'id della carta e anche username e password per accedere al portale e controllare i movimenti).  
Per questo motivo nel momento della generazione della carta, username e password dell’utente generato vengono restituiti in chiaro, insieme ovviamente all’id della  
carta generata. Quindi un account di cardowner ha associata una SOLA carta.
