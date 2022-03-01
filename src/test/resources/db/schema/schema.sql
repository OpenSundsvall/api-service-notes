
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

    create table schema_history (
       schema_version varchar(255) not null,
        applied datetime(6) not null,
        comment varchar(8192) not null,
        primary key (schema_version)
    ) engine=InnoDB;
create index note_party_id_index on note (party_id);
