CREATE TABLE vote
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    vote         BOOLEAN,
    id_associate BIGINT,
    id_session    BIGINT,
    FOREIGN KEY (id_session) REFERENCES vote_session (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
