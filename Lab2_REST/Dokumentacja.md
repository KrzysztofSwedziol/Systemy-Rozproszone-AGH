# Krzysztof_Swedziol_REST - Aplikacja do przeliczania walut

Spring Boot REST API umożliwiające przeliczanie walut, pobieranie kursów z dwóch zewnętrznych API oraz zarządzanie historią zapytań.

---

## Funkcjonalności

- Przeliczanie kwot między walutami
- Pobieranie kursów walut z dwóch źródeł (API)
- Historia przeliczeń
- Możliwość pobierania, zastępowania i usuwania zapisanych formularzy

---

## Endpointy REST

### `GET /`
**Opis:** Przekierowanie do strony `index.html`.

---

### `GET /history`
**Opis:** Wyświetla historię przeliczeń walut.
- **Zwraca:** Widok `history.html` z listą historii lub stronę błędu.

---

### `GET /forms/{id}`
**Opis:** Zwraca zapisany formularz o podanym `id`.

- **Parametry:**  
  `id` – identyfikator formularza

- **Odpowiedzi:**
  - `200 OK` – JSON `FormRequestResponse`
  - `404 Not Found` – brak formularza o podanym ID

---

### `POST /process-form`
**Opis:** Przetwarza formularz przeliczenia waluty i zapisuje wynik.

- **Parametry (x-www-form-urlencoded):**
  - `sourceCurrency` – waluta źródłowa
  - `targetCurrency` – waluta docelowa
  - `amount` – kwota do przeliczenia

- **Zwraca:** Widok `result.html` z kursem i wynikiem przeliczenia

---

### `PUT /replace-form/{id}`
**Opis:** Nadpisuje istniejący formularz nowymi danymi.

- **Parametry:**
  - `id` – identyfikator formularza
  - `FormsModel` – dane w JSON

- **Odpowiedzi:**
  - `200 OK` – zaktualizowana historia
  - `404 Not Found` – brak danych lub niepoprawne waluty

---

### `DELETE /remove-form/{id}`
**Opis:** Usuwa formularz z historii.

- **Parametr:**  
  `id` – identyfikator formularza

- **Odpowiedzi:**
  - `200 OK` – potwierdzenie usunięcia
  - `404 Not Found` – brak formularza

---

## Przykładowy request

```http
POST /process-form
Content-Type: application/x-www-form-urlencoded

sourceCurrency=PLN&targetCurrency=USD&amount=100
