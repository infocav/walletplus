CREATE TABLE users (
    cpf VARCHAR(14) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    channel VARCHAR(50) NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (cpf) REFERENCES users(cpf)
);

CREATE TABLE balances (
    cpf VARCHAR(14) PRIMARY KEY,
    amount DECIMAL(19, 2) NOT NULL,
    FOREIGN KEY (cpf) REFERENCES users(cpf)
);