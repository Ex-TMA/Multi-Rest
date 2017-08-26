CREATE TABLE AccountToken (
  createdAt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  id int(11) NOT NULL AUTO_INCREMENT,
  userName varchar(50) NOT NULL,
  token varchar(1000) NOT NULL,
  ip varchar(100) DEFAULT NULL,
  expiresAt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY token (token),
  KEY userName (userName),
  KEY createdAt (createdAt)
) ENGINE=InnoDB;
