CREATE TABLE vote_session
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    duration_minutes INT,
    session_start    DATETIME,
    id_agenda        BIGINT,
    FOREIGN KEY (id_agenda) REFERENCES agenda (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
