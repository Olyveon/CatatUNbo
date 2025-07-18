drop schema if exists spynetdb;
CREATE SCHEMA spynetdb DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
use spynetdb;

-- TABLAS
CREATE TABLE user(
	user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(45) NOT NULL UNIQUE,
    user_password_hash VARCHAR(255) NOT NULL,
    password_salt VARCHAR(255) NOT NULL,
    user_rol ENUM('admin', 'auditor', 'inspector', 'cliente') NULL,
    user_state ENUM('ACTIVO', 'BLOQUEADO') NOT NULL,
    user_date_register DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_last_session DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(username)
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
    auditory_state ENUM('PENDIENTE', 'EN PROCESO', 'ARCHIVADO', 'FINALIZADO') NOT NULL,
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
    finding_security_risk ENUM('BAJO','MEDIO','ALTO','CRITICO') NOT NULL,
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
    request_state ENUM('PENDIENTE', 'ACEPTADA', 'RECHAZADA') NOT NULL,
    request_datetime DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE open_ai_api(
	apikey_id INT PRIMARY KEY AUTO_INCREMENT,
    apikey VARCHAR(255) NOT NULL,
    model VARCHAR(45) NOT NULL
);




-- 🔐 Usuarios
INSERT INTO user (username, user_password_hash, password_salt, user_rol, user_state) VALUES
('admin_1', 'LWv2zA7UHysgNF1Y4feHcR2igR80s6ovjKugk3/y/9U=','123', 'admin', 'ACTIVO'),
('admin_2', '50FBf37nMrwEs4Ak5ayyGWrJT/D+nifGVnRPBVLLJ/k=','123', 'admin', 'ACTIVO'),
('auditor_ana', 'Am7yhCgH8TD32jwAoPDhh7Znqm2a7z+aquDcJCpk5xA=',   '123',  'auditor', 'ACTIVO'),
('auditor_jose', 'NbQJ8A4fw9SZgMrEbNtVfpTA1ecbPIabAOwMDbrpbcE=',  '123',  'auditor', 'ACTIVO'),
('auditor_lina', 'ceDOjNZA6Ok57D5/1G1HJJmaJTIOuyhrwXB3JKtX9T4=',  '123', 'auditor', 'BLOQUEADO'),
('inspector_diego', 'AggGFteoOiPGhJwa5wno83LwrG4xD6e17GC4XttSAHs=', '123',  'inspector', 'ACTIVO'),
('cliente_maria', 'p40q990inMDw4EOiDzpftRb+dKEK24+sVPygsYyzKYc=', '123',  'cliente', 'ACTIVO'),
('cliente_juan', 'DUgLS17Eqex+9iUoCOkS4leV23uvN6AGLkMjgbnk4lY=',  '123',  'cliente', 'ACTIVO'),
('cliente_carla', 'JrAGHRDn2DKuQV+650Y8AyYnDeqosXdoHWq2OVN0Rk4=', '123', 'cliente', 'BLOQUEADO');

-- 👤 Clientes
INSERT INTO client (client_name, client_number, client_email) VALUES
('NetSecure Ltda.', '3001122334', 'contacto@netsecure.com'),
('CyberFort SAS', '3109988776', 'soporte@cyberfort.com'),
('VisionData Corp.', '3123344556', 'admin@visiondata.com'),
('RedSys Group', '3145566778', 'info@redsys.com'),
('SecureTech Inc.', '3167788990', 'hello@securetech.io');

-- 🌐 IPs por cliente
INSERT INTO ip_by_client (ip_client_id, ip_direction) VALUES
(1, '192.168.10.10'), (1, '192.168.10.11'),
(2, '10.0.0.1'), (2, '10.0.0.2'),
(3, '172.16.1.1'),
(4, '200.15.0.5'), (4, '200.15.0.6'), (4, '200.15.0.7'),
(5, '185.33.45.1');

-- 📋 Auditorías
INSERT INTO auditory (auditory_name, auditory_client_id, auditory_state, auditory_date_init, auditory_date_limit) VALUES
('Auditoría Externa Web - NetSecure', 1, 'EN PROCESO', '2025-06-24 08:00:00', '2025-07-01 23:59:59'),
('Auditoría Red Interna - CyberFort', 2, 'PENDIENTE', NULL, '2025-07-05 23:59:59'),
('Revisión Servidores VisionData', 3, 'ARCHIVADO', '2025-05-15 10:00:00', '2025-06-01 23:59:59'),
('Pentest - RedSys', 4, 'FINALIZADO', '2025-04-01 09:00:00', '2025-04-10 23:59:59'),
('Análisis Firewall SecureTech', 5, 'EN PROCESO', '2025-06-20 11:00:00', '2025-07-03 23:59:59');

-- 👥 Accesos a auditorías
-- Asignación de auditor a cada auditoría (uno por auditoría)
INSERT INTO auditory_access (aud_access_auditory_id, aud_access_user_id) VALUES
(1, 3),  -- Auditoría 1 → usuario_id 3 (auditor_ana)
(2, 4),  -- Auditoría 2 → usuario_id 4 (auditor_jose)
(3, 5),  -- Auditoría 3 → usuario_id 5 (auditor_lina)
(4, 4),  -- Auditoría 4 → usuario_id 4 (auditor_jose)
(5, 3);  -- Auditoría 5 → usuario_id 3 (auditor_ana)

-- 🧠 Hallazgos
INSERT INTO finding (finding_security_risk, finding_user_id, finding_auditory_id, finding_title, finding_description) VALUES
('ALTO', 3, 1, 'Firewall mal configurado', 'Se permite tráfico entrante innecesario en múltiples puertos.'),
('MEDIO', 4, 2, 'Servidor expone headers sensibles', 'El servidor web responde con información de versión de software.'),
('CRITICO', 5, 3, 'Base de datos accesible públicamente', 'Se detectó un puerto de base de datos abierto sin restricción.'),
('BAJO', 4, 4, 'Certificado SSL próximo a expirar', 'El certificado expira en menos de 15 días.'),
('MEDIO', 3, 5, 'Versión de Apache obsoleta', 'Apache 2.2 detectado en producción.');
insert into finding (finding_auditory_id, finding_user_id, finding_security_risk, finding_title, finding_description, finding_datetime)
values 
(1, 2, 'ALTO', 'Hallazgo 1', 'Primer hallazgo para la auditoría 1', now()),
(1, 3, 'MEDIO', 'Hallazgo 2', 'Segundo hallazgo para la auditoría 1', now());

-- 📝 Observaciones
INSERT INTO observation (observation_user_id, observation_auditory_id, observation_title, observation_description) VALUES
(3, 1, 'Inicio de escaneo', 'Escaneo Nmap lanzado sobre IPs internas.'),
(6, 1, 'Observación general', 'Cliente entregó documentación correctamente.'),
(4, 2, 'Planificación inicial', 'Auditoría se programó para próxima semana.'),
(6, 3, 'Observación final', 'Cierre de auditoría sin hallazgos nuevos.'),
(3, 5, 'Hallazgo crítico detectado', 'El firewall responde con ICMP desde Internet.');
insert into observation (observation_auditory_id, observation_user_id, observation_title, observation_description, observation_datetime)
values 
(1, 2, 'Observación 1', 'Primera observación para la auditoría 1', now()),
(1, 3, 'Observación 2', 'Segunda observación para la auditoría 1', now());
-- 📬 Solicitudes de auditoría
INSERT INTO request (request_client_name, request_client_number, request_client_email, request_state) VALUES
('Datasec Colombia', '3205567888', 'solicitud@datasec.com', 'PENDIENTE'),
('Fenix Seguridad Digital', '3123341221', 'auditoria@fenix.com', 'ACEPTADA'),
('Grupo Kynet', '3109987123', 'contacto@kynet.org', 'RECHAZADA');

-- 🤖 API Keys para OpenAI
INSERT INTO open_ai_api (apikey, model) VALUES
('sk-abc123', 'gpt-4'),
('sk-def456', 'gpt-3.5-turbo');



-- VISTAS
create view vista_auditorias_inspector_admin as 
select auditory_id, auditory_name, client_name, username, auditory_date_init, auditory_date_limit, auditory_state from auditory 
join auditory_access on auditory_id=aud_access_auditory_id
join user on user_id=aud_access_user_id
join client on client_id=auditory_client_id;

create view all_users as 
select user_id, username, user_rol, user_last_session, user_state from user;


create view all_observations as 
select observation_auditory_id, username, observation_title, observation_description, observation_datetime 
from observation join user where user_id=observation_user_id;


create view all_findings as
select 
    finding.finding_auditory_id,
    finding.find_id,
    finding.finding_security_risk,
    user.username,
    auditory.auditory_name,
    finding.finding_title,
    finding.finding_description,
    finding.finding_datetime
from finding
join user on user.user_id = finding.finding_user_id
join auditory on auditory.auditory_id = finding.finding_auditory_id;




-- PROCEDIMIENTOS
delimiter $$
create procedure get_auditorias_by_user(in p_username varchar(45))
begin
    declare v_rol varchar(20);

    select user_rol into v_rol from user where username = p_username limit 1;

    if v_rol = 'auditor' then
        select auditory_name, client_name, auditory_date_init, auditory_date_limit, auditory_state
        from vista_auditorias_inspector_admin
        where username = p_username;
    elseif v_rol = 'inspector' or v_rol = 'admin' then
        select * from vista_auditorias_inspector_admin;
    else
        select 'Rol no válido o usuario no encontrado' as mensaje;
    end if;
end $$
delimiter ;


delimiter $$
create procedure get_users_by_role(in p_username varchar(45))
begin
    declare v_rol varchar(20);

    select user_rol into v_rol from user where username = p_username limit 1;

    if v_rol = 'admin' then
        select * from all_users;
    elseif v_rol = 'inspector' then
        select * from all_users where user_rol = 'auditor';
    else
        select 'Rol no válido para esta consulta' as mensaje;
    end if;
end $$
delimiter ;


delimiter $$
create procedure get_observations_by_auditory_id(in p_auditory_id int)
begin
    select * from all_observations where observation_auditory_id=p_auditory_id;
end $$
delimiter ; 



delimiter $$
create procedure get_findings_by_auditory_id(in p_auditory_id int)
begin
    select * from all_findings where finding_auditory_id = p_auditory_id;
end $$
delimiter ;


DROP ROLE IF EXISTS 'admin';
DROP ROLE IF EXISTS 'inspector';
DROP ROLE IF EXISTS 'auditor';
DROP ROLE IF EXISTS'cliente';


CREATE ROLE 'admin';
CREATE ROLE 'inspector';
CREATE ROLE 'auditor';

CREATE ROLE 'cliente';


-- ADMIN:
-- Puede ver y modificar usuarios
GRANT SELECT, UPDATE ON spynetdb.user TO 'admin';

-- Puede consultar pero NO modificar auditorías ni observaciones
GRANT SELECT ON spynetdb.auditory TO 'admin';
GRANT SELECT ON spynetdb.auditory_access TO 'admin';
GRANT SELECT ON spynetdb.observation TO 'admin';
GRANT SELECT ON spynetdb.finding TO 'admin';
GRANT SELECT ON spynetdb.client TO 'admin';
GRANT SELECT ON spynetdb.request TO 'admin';

-- procedimientos a los que puede llamar (devuelven vistas en funcion de un parametro)
GRANT EXECUTE ON PROCEDURE spynetdb.get_users_by_role TO 'admin';
GRANT EXECUTE ON PROCEDURE spynetdb.get_auditorias_by_user TO 'admin';
GRANT EXECUTE ON PROCEDURE spynetdb.get_observations_by_auditory_id TO 'admin';
GRANT EXECUTE ON PROCEDURE spynetdb.get_findings_by_auditory_id TO 'admin';




-- INSPECTOR:
-- Puede leer auditorías, observaciones, hallazgos
GRANT SELECT ON spynetdb.auditory TO 'inspector';
GRANT SELECT ON spynetdb.auditory_access TO 'inspector';
GRANT SELECT ON spynetdb.observation TO 'inspector';
GRANT SELECT ON spynetdb.finding TO 'inspector';
GRANT SELECT ON spynetdb.user TO 'inspector';
GRANT SELECT ON spynetdb.client TO 'inspector';

-- Puede aceptar solicitudes
GRANT SELECT, UPDATE ON spynetdb.request TO 'inspector';

-- Puede crear clientes y auditorías nuevas
GRANT INSERT ON spynetdb.client TO 'inspector';
GRANT INSERT ON spynetdb.auditory TO 'inspector';
GRANT INSERT ON spynetdb.auditory_access TO 'inspector';


-- procedimientos a los que puede llamar (devuelven vistas en funcion de un parametro)
GRANT EXECUTE ON PROCEDURE spynetdb.get_users_by_role TO 'inspector';
GRANT EXECUTE ON PROCEDURE spynetdb.get_auditorias_by_user TO 'inspector';
GRANT EXECUTE ON PROCEDURE spynetdb.get_observations_by_auditory_id TO 'inspector';
GRANT EXECUTE ON PROCEDURE spynetdb.get_findings_by_auditory_id TO 'inspector';





-- AUDITOR
-- Puede leer sus auditorías
GRANT SELECT ON spynetdb.auditory TO 'auditor';
GRANT SELECT ON spynetdb.auditory_access TO 'auditor';
GRANT SELECT ON spynetdb.client TO 'auditor';
-- Puede insertar observaciones y hallazgos
GRANT SELECT, INSERT ON spynetdb.observation TO 'auditor';
GRANT SELECT, INSERT ON spynetdb.finding TO 'auditor';

-- Puede actualizar el estado o fechas de auditorías
GRANT UPDATE ON spynetdb.auditory TO 'auditor';

-- procedimientos a los que puede llamar (devuelven vistas en funcion de un parametro)
GRANT EXECUTE ON PROCEDURE spynetdb.get_auditorias_by_user TO 'auditor';
GRANT EXECUTE ON PROCEDURE spynetdb.get_observations_by_auditory_id TO 'auditor';
GRANT EXECUTE ON PROCEDURE spynetdb.get_findings_by_auditory_id TO 'auditor';



-- CLIENTE

GRANT SELECT, INSERT, UPDATE, DELETE ON spynetdb.request TO "cliente";