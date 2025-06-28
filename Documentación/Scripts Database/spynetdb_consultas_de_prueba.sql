-- comsulta para ver ips relacionadas a una empresa o auditoria
use spynetdb;
select * from client;
select * from auditory;
select * from user;
select * from auditory_access;
select * from ip_by_client;

-- comsulta para ver ips relacionadas a una empresa
select client_name, ip_direction from client join ip_by_client where client_id=ip_client_id;

-- comsulta para ver ips relacionadas a una auditoria
select * from auditory join ip_by_client join client where auditory_client_id=ip_client_id;

select user_state, count(*) as total from user where user_state="BLOQUEADO" group by user_state ;



-- vista para consultar auditorias por nombre de auditoria, cliente, fechas, y estado de la auditoria
select auditory_name, client_name, username, auditory_date_init, auditory_date_limit, auditory_state from auditory 
join auditory_access on auditory_id=aud_access_auditory_id
join user on user_id=aud_access_user_id
join client on client_id=auditory_client_id;


select * from auditory 
join auditory_access on auditory_id=aud_access_auditory_id
join user on user_id=aud_access_user_id
join client on client_id=auditory_client_id;

-- ver todos los usuarios
select username, user_rol, user_last_session, user_state from user;

-- ver todas las observaciones
select observation_auditory_id, username, observation_title, observation_description, observation_datetime from observation join user where user_id=observation_user_id;

-- ver todos los hallazgos
select * from finding;