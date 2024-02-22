DROP TABLE IF EXISTS fruit;

CREATE TABLE fruit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    warehousingDate DATE NOT NULL,
    price BIGINT NOT NULL,
    status ENUM('SALE', 'SOLD') NOT NULL
);
