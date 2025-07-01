DROP ROLE 'admin';
DROP ROLE 'inspector';
DROP ROLE 'auditor';
DROP ROLE 'cliente';


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