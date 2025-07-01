# Working

## Json structure
- project name
  - endpoint name
  - methods: all, get, post, put, delete (configurable)
  - type: usually _String_ 'need to investigate more'

## Workflow
1. Start app
2. Parse the JSON file
   1. Structure of a JSON file
   2. Customizability
3. Create and populate the database based on the endpoint structure
4. Create the endpoints using _RequestMappingHandlerMapping_ and _RequestMappingInfo_

### Create and populate the database

### Create the endpoints


### WIP
The main structure

Continue to read: https://medium.com/@AlexanderObregon/the-mechanics-of-request-mapping-in-spring-boot-92d1065cc0ad

EndpointStructureDef

name: String
count: int
fields: Map <name, Type>





Let's explore the main types of databases, focusing on relational and non-relational databases, along with some other notable categories and their common uses.

### 1. Relational Databases (SQL Databases)

* **Concept:** Relational databases organize data into one or more tables (or "relations") of columns and rows. Each row represents a unique record, and each column represents an attribute of that record. Tables are related to each other through keys (primary and foreign keys), which enforce data integrity and allow for efficient querying and joining of data from multiple tables.
* **Schema:** They have a predefined schema, meaning the structure of the tables (columns, data types, constraints) must be defined before data is inserted. This enforces consistency and data integrity.
* **Query Language:** Structured Query Language (SQL) is the standard language for managing and querying data in relational databases.
* **ACID Properties:** Relational databases typically adhere to ACID properties (Atomicity, Consistency, Isolation, Durability), ensuring reliable transaction processing.
* **Scalability:** Traditionally, relational databases scaled vertically (by increasing the resources of a single server). Horizontal scaling (distributing data across multiple servers) can be more complex to implement.
* **Use Cases:**
    * **Financial transactions:** Banking systems, e-commerce order processing.
    * **Customer Relationship Management (CRM):** Managing customer data and interactions.
    * **Inventory management:** Tracking product stock levels and movements.
    * **Enterprise Resource Planning (ERP):** Integrating various business processes.
    * Any application requires strong data consistency and complex relationships between data points.
* **Examples:** MySQL, PostgreSQL, Oracle Database, Microsoft SQL Server, MariaDB, SQLite.

### 2. Non-Relational Databases (NoSQL Databases)

* **Concept:** NoSQL (Not Only SQL) databases encompass a variety of database systems that don't adhere to the relational model. They are designed to be more flexible, scalable, and handle different types of data, including unstructured or semi-structured data.
* **Schema:** They often have dynamic or schema-less structures, allowing for flexibility in data organization. Different "records" (depending on the type of NoSQL database) might have different sets of attributes.
* **Query Language:** Each type of NoSQL database typically has its own query language or API for data manipulation.
* **BASE Properties:** Many NoSQL databases prioritize BASE properties (Basically Available, Soft state, Eventually consistent) over strict ACID for improved availability and scalability, although some offer varying levels of consistency.
* **Scalability:** NoSQL databases are often designed for horizontal scaling, making them well-suited for handling large volumes of data and high traffic.
* **Types and Use Cases:**
    * **Document Databases:** Store data as JSON-like documents. Flexible schema allows for embedding related data.
        * **Use Cases:** Content management systems, product catalogs, user profiles, applications with evolving data structures.
        * **Examples:** MongoDB, Couchbase, Amazon DocumentDB.
    * **Key-Value Stores:** Store data as key-value pairs, optimized for simple and fast lookups.
        * **Use Cases:** Caching, session management, real-time data, leaderboards.
        * **Examples:** Redis, Amazon DynamoDB, Memcached.
    * **Column-Family Databases (Wide-Column Stores):** Organize data into columns rather than rows, suitable for sparse data and analytical queries.
        * **Use Cases:** Real-time analytics, IoT data, personalization, applications with many attributes.
        * **Examples:** Apache Cassandra, HBase, Google Bigtable.
    * **Graph Databases:** Model data as nodes and edges to represent relationships between data points.
        * **Use Cases:** Social networks, recommendation engines, fraud detection, knowledge graphs, network analysis.
        * **Examples:** Neo4j, Amazon Neptune, ArangoDB.
    * **Time Series Databases:** Optimized for storing and querying time-stamped data.
        * **Use Cases:** Monitoring systems, IoT sensor data, financial data, logging.
        * **Examples:** InfluxDB, TimescaleDB, Amazon Timestream.

### 3. Other Types of Databases

Beyond relational and the main categories of NoSQL, there are other types of databases, often specialized for particular needs:

* **Object-Oriented Databases:** Store data as objects, similar to object-oriented programming. Useful for applications with complex data models and relationships. (e.g., InterSystems Caché, GemStone/S).
* **Hierarchical Databases:** Organize data in a tree-like structure with parent-child relationships. Less flexible than relational models but can be efficient for specific types of data. (e.g., older systems like IMS).
* **Network Databases:** Extend the hierarchical model by allowing a child node to have multiple parent nodes, providing more flexibility in representing relationships. (e.g., IDMS).
* **Vector Databases:** Designed to efficiently store and query high-dimensional vector embeddings. Crucial for applications involving semantic search, recommendation systems, and generative AI. (e.g., Pinecone, Weaviate, ChromaDB, Astra DB).

The choice of a database depends heavily on the specific requirements of the application, including the type and structure of the data, the need for scalability and availability, query patterns, and consistency requirements. Many modern applications even use a combination of different database types to leverage the strengths of each.