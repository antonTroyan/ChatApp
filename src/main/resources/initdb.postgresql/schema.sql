create table if not exists messages
(
  content varchar(50) not null,
  date timestamp not null,
  sender varchar(15) not null,
  id bigserial not null
    constraint messages_id_pk
    primary key
)
;

create unique index if not exists messages_id_uindex
  on messages (id)
;

