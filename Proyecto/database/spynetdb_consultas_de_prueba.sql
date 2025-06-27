-- comsulta para ver ips relacionadas a una empresa o auditoria

select * from client;
select * from auditory;

select * from ip_by_client;

-- comsulta para ver ips relacionadas a una empresa
select client_name, ip_direction from client join ip_by_client where client_id=ip_client_id;

-- comsulta para ver ips relacionadas a una auditoria
select * from auditory join ip_by_client join client where auditory_client_id=ip_client_id;