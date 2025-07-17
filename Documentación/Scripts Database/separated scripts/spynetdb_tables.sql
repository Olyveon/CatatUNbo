drop schema if exists spynetdb;
create schema spynetdb;
use spynetdb;

CREATE TABLE user(
	user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(45) NOT NULL,
    user_password_hash VARCHAR(255) NOT NULL, 
    user_rol ENUM("admin", "auditor", "inspector", "cliente") NULL,
    user_state ENUM("ACTIVO", "BLOQUEADO") NOT NULL,
    user_date_register DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_last_session DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE client(
	client_id INT PRIMARY KEY AUTO_INCREMENT,
    client_name VARCHAR(255) NOT NULL,
    client_number VARCHAR(45) NOT NULL,
    client_email VARCHAR(45) NOT NULL
);

CREATE TABLE ip_by_client(
	ip_id INT PRIMARY KEY AUTO_INCREMENT,
    ip_client_id INT NOT NULL,
    ip_direction VARCHAR(45) NOT NULL,
    FOREIGN KEY (ip_client_id) REFERENCES client(client_id)
);

CREATE TABLE auditory(
	auditory_id INT PRIMARY KEY AUTO_INCREMENT,
    auditory_name VARCHAR(45) NOT NULL,
    auditory_client_id INT NOT NULL,
    auditory_state ENUM("PENDIENTE", "EN PROCESO", "ARCHIVADO", "FINALIZADO") NOT NULL,
    auditory_date_init DATETIME,
    auditory_date_limit DATETIME,
    auditory_date_end DATETIME,
    FOREIGN KEY (auditory_client_id) REFERENCES client(client_id)
);

CREATE TABLE auditory_access(
	aud_access_auditory_id INT PRIMARY KEY,
	aud_access_user_id INT,
    FOREIGN KEY (aud_access_user_id) REFERENCES user(user_id),
    FOREIGN KEY (aud_access_auditory_id) REFERENCES auditory(auditory_id)
);


CREATE TABLE finding(
	find_id INT PRIMARY KEY AUTO_INCREMENT,
    finding_security_risk ENUM("BAJO","MEDIO","ALTO","CRITICO") NOT NULL,
    finding_user_id INT NOT NULL,
    finding_auditory_id INT NOT NULL,
    finding_title TEXT NOT NULL,
    finding_description TEXT NOT NULL,
    finding_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (finding_user_id) REFERENCES user(user_id),
    FOREIGN KEY (finding_auditory_id) REFERENCES auditory(auditory_id)
);

CREATE TABLE observation(
	observation_id INT PRIMARY KEY AUTO_INCREMENT,
    observation_user_id INT NOT NULL,
    observation_auditory_id INT NOT NULL,
    observation_title TEXT NOT NULL,
    observation_description TEXT NOT NULL,
    observation_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (observation_user_id) REFERENCES user(user_id),
    FOREIGN KEY (observation_auditory_id) REFERENCES auditory(auditory_id)
);



CREATE TABLE request(
	request_id INT PRIMARY KEY AUTO_INCREMENT,
    request_client_name VARCHAR(45) NOT NULL,
    request_client_number VARCHAR(45) NOT NULL,
    request_client_email VARCHAR(45) NOT NULL,
    request_state ENUM("PENDIENTE", "ACEPTADA", "RECHAZADA") NOT NULL,
    request_datetime DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE open_ai_api(
	apikey_id INT PRIMARY KEY AUTO_INCREMENT,
    apikey VARCHAR(255) NOT NULL,
    model VARCHAR(45) NOT NULL
);


