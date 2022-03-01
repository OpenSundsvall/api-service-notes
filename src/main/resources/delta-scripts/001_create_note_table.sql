-- Creation of table and indexes for handling notes
    create table note (
       id varchar(255) not null,
        body longtext,
        created datetime(6),
        created_by varchar(255),
        modified datetime(6),
        modified_by varchar(255),
        party_id varchar(255),
        subject varchar(255),
        primary key (id)
    ) engine=InnoDB;

create index note_party_id_index on note (party_id);


-- Necessary line in order to document the change. 
insert into schema_history (schema_version,comment,applied) VALUES ('001','Created notes tables and indexes', NOW());
