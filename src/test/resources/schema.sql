CREATE TABLE question
(
    id         bigint   NOT NULL,
    text       varchar(500) NOT NULL,
    epoch_time bigint      NOT NULL,
    CONSTRAINT question_pk PRIMARY KEY (id)
);

CREATE TABLE answer_option
(
    id           bigint    NOT NULL,
    id_question  bigint    NOT NULL,
    text         varchar(500) NOT NULL,
    vote_count   int NOT NULL,
    CONSTRAINT answer_option_pk PRIMARY KEY (id),
    CONSTRAINT id_question_fk FOREIGN KEY (id_question) REFERENCES question (id)
    );


CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 10000;