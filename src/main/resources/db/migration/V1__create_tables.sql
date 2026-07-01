create table passwords (
    id bigserial PRIMARY KEY,
    iv text not null,
    title varchar(100) not null,
    username varchar(100),
    password varchar(255),
    email varchar(100),
    passkey varchar(255)
);

create table vault_meta(

    key varchar(100) PRIMARY KEY,
    Value text NOT NULL
);
