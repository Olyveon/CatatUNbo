
-- Este pedazo fue hecho con CHATGPT
-- 📦 DATOS DE PRUEBA MASIVOS PARA SPYNETDB (con nombres de columnas actualizados)
-- Suponiendo que el esquema ya fue creado con las tablas finales y FK claras
SELECT * FROM user;
SELECT * FROM client;
SELECT * FROM ip_by_client;
SELECT * FROM auditory;
SELECT * FROM auditory_access;
SELECT * FROM finding;
SELECT * FROM observation;
SELECT * FROM request;
SELECT * FROM open_ai_api;

-- 🔐 Usuarios
INSERT INTO user (username, user_password_hash, user_rol, user_state) VALUES
('admin_1', 'hash_admin1', 'admin', 'ACTIVO'),
('admin_2', 'hash_admin2', 'admin', 'ACTIVO'),
('auditor_ana', 'hash_ana', 'auditor', 'ACTIVO'),
('auditor_jose', 'hash_jose', 'auditor', 'ACTIVO'),
('auditor_lina', 'hash_lina', 'auditor', 'BLOQUEADO'),
('inspector_diego', 'hash_diego', 'inspector', 'ACTIVO'),
('cliente_maria', 'hash_maria', 'cliente', 'ACTIVO'),
('cliente_juan', 'hash_juan', 'cliente', 'ACTIVO'),
('cliente_carla', 'hash_carla', 'cliente', 'BLOQUEADO');

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
