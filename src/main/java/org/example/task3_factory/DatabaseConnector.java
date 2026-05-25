package org.example.task3_factory;

interface DatabaseConnector {
    void connect();
    void disconnect();
    String getDatabaseType();
}

class PostgresConnector implements DatabaseConnector {
    @Override
    public void connect() {
        System.out.println("Connecting to PostgreSQL database...");
        System.out.println("PostgreSQL connection established!");
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnecting from PostgreSQL database...");
    }

    @Override
    public String getDatabaseType() {
        return "PostgreSQL";
    }
}

class MySQLConnector implements DatabaseConnector {
    @Override
    public void connect() {
        System.out.println("Connecting to MySQL database...");
        System.out.println("MySQL connection established!");
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnecting from MySQL database...");
    }

    @Override
    public String getDatabaseType() {
        return "MySQL";
    }
}

class MongoConnector implements DatabaseConnector {
    @Override
    public void connect() {
        System.out.println("Connecting to MongoDB database...");
        System.out.println("MongoDB connection established!");
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnecting from MongoDB database...");
    }

    @Override
    public String getDatabaseType() {
        return "MongoDB";
    }
}

class DatabaseConnectorFactory {
    public static final String POSTGRES = "postgres";
    public static final String MYSQL = "mysql";
    public static final String MONGO = "mongo";

    public DatabaseConnector createConnector(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Database type cannot be null or empty");
        }

        switch (type.toLowerCase()) {
            case POSTGRES:
                return new PostgresConnector();
            case MYSQL:
                return new MySQLConnector();
            case MONGO:
                return new MongoConnector();
            default:
                throw new IllegalArgumentException("Unknown database type: " + type);
        }
    }
}
