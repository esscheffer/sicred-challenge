CREATE TABLE vote
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    vote         BOOLEAN,
    id_associate BIGINT,
    id_agenda    BIGINT,
    FOREIGN KEY (id_agenda) REFERENCES agenda (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
