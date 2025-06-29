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



select * from vista_auditorias_inspector_admin;
select * from vista_auditorias_inspector_admin where username="auditor_ana";
select * from all_users;
select * from all_observations;

select * from finding;
select * from all_findings;

