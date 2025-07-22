# PiePayOffers

A backend service to fetch, store, and serve the best payment offers — inspired by Flipkart’s payment options experience.

## 📑 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Setup & Run](#-setup--run)
- [API Usage](#-api-usage)
- [Example Database Schema](#-example-database-schema)
- [Assumptions](#-assumptions)
- [Development Branch](#-development-branch)
- [Author](#-author)


## 📌 Overview

This service fetches offer data from flipkart payment page payloads, parses relevant details like discount type, value, banks, and payment instruments, and stores them in a relational database.

It also provides an API to calculate the **best applicable discount** for a given order amount, bank, and payment instrument.

---

## ✅ Features

- **POST `/offer`**
    - Accepts JSON payload in Flipkart payment page format
    - Extracts all offer sections and saves offers to the DB
    - Ignores duplicates based on `adjustment_id`

- **GET `/highest-discount`**
    - Calculates the highest possible discount for:
        - Amount to pay
        - Bank name
        - Payment instrument
    - Supports flat discounts, % discounts, and fallback parsing from summary text.
    - Handles missing or incomplete data gracefully.

---

## 🗂️ Tech Stack

- Java 17+
- Spring Boot 3
- Spring Data JPA
- MySQL
- Maven

---

## ✅ Setup & Run

1. **Clone the repo**
   ```bash
   git clone https://github.com/khushusharma/piepay_offers.git
   cd piepay_offers

2. **Create src/main/resources/application.properties with your local DB credentials**
   ```bash
   # Spring Datasource Configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/piepay
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

   # Hibernate Properties
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true

3. **Build the Project**
   ```bash
   mvn clean install

4. **Run the Application**
   ```bash
   mvn spring-boot:run

## ✅ API Usage

1. **POST API `/offer`**

**Description:** Save offers from Flipkart payload JSON.

**Example:**
  ```bash
    curl -X POST http://localhost:8080/offer \
    -H "Content-Type: application/json" \
    -d @flipkart-sample.json
    
   ```

2. **GET API `/highest-discount`**

**Description:** Calculate and return the highest possible discount for an order.

**Query Params:**

- `amountToPay` - Order Amount
- `bankName` - Bank Name (ICICI, HDFC etc. )
- `paymentInstrument` - CREDIT, DEBIT, UPI etc.

**Example:**
  ```bash 
            curl "http://localhost:8080/offer/highest-discount?amountToPay=10000&bankName=HDFC&paymentInstrument=CREDIT"
  ```

**Sample Response**
  ```json
    {
      "highestDiscountAmount": 1500.0
    }
   ```

## ✅ Example Database Schema

Example `offer` table structure:

   ```sql 
     CREATE TABLE `offers` (
     `id` bigint NOT NULL AUTO_INCREMENT,
     `adjustment_type` varchar(255) DEFAULT NULL,
     `discount_type` varchar(255) DEFAULT NULL,
     `discount_value` double NOT NULL,
     `min_amount` double NOT NULL,
     `offer_id` varchar(255) NOT NULL,
     `percentage` bit(1) NOT NULL,
     `summary` text,
      PRIMARY KEY (`id`),
      UNIQUE KEY `UKiwrjr30jn4468samtpvs89va8` (`offer_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

```

## ✅ Assumptions

- This service expects Flipkart-style payment offer payloads.
- Fallback summary text parsing is used if explicit values are missing.
- The `application.properties` file is not pushed — developers configure their own.

## ✅ Development Branch
Development happens under a separate branch (e.g., `feature`).


## ✅ Author
Khushi Dokwal
[GitHub](https://github.com/khushusharma)






