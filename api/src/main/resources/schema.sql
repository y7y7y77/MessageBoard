CREATE TABLE IF NOT EXISTS operation_log (
    id      INT PRIMARY KEY AUTO_INCREMENT,
    time    VARCHAR(20)  NOT NULL,
    ip      VARCHAR(50),
    username VARCHAR(100),
    action  VARCHAR(50)  NOT NULL,
    detail  VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
