CREATE TABLE Account (
  createdAt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  id int(11) NOT NULL AUTO_INCREMENT,
  userName varchar(50) DEFAULT NULL,
  pass varchar(60) DEFAULT NULL,
  state varchar(50) NOT NULL,
  name varchar(50) NOT NULL,
  secret varchar(50) NOT NULL,
  email varchar(50) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY userName (userName),
  KEY state (state),
  KEY email (email)
) ENGINE=InnoDB;

CREATE TABLE Access (
  id int(11) NOT NULL AUTO_INCREMENT,
  access varchar(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY access (access)
) ENGINE=InnoDB;

CREATE TABLE AccountAccess (
  createdAt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  accountId int(11) NOT NULL,
  accessId int(11) NOT NULL,
  PRIMARY KEY (accountId,accessId),
  CONSTRAINT AccountAccess_ibfk_1 FOREIGN KEY (accountId) REFERENCES Account (id),
  CONSTRAINT AccountAccess_ibfk_2 FOREIGN KEY (accessId) REFERENCES Access (id)
) ENGINE=InnoDB;

# Raw password for Account here is password
INSERT INTO Access SET access = 'ADMIN';
INSERT INTO Access SET access = 'USER';
INSERT INTO Account SET userName = 'truong1', pass='$2a$06$GWCw1AsaWE83shb9WjwTeuKWzsUERLr0CruAaik7yPknqaSzqHo4u', state='ACTIVE', name='truong admin', secret='H3LFYHVBLEVX54N4', email='truongnguyen1610@gmail.com';
INSERT INTO Account SET userName = 'truong2', pass='$2a$06$d/5Yw6WKsS3YQETtmcGFz.heTtGHVzFkqGQDiMRA58ywkE016fG8K', state='ACTIVE', name='truong user', secret='H3LFYHVBLEVX54N4', email='truongnguyen1611@gmail.com';
INSERT INTO AccountAccess set accountId = 1, accessId = 1;
INSERT INTO AccountAccess set accountId = 2, accessId = 2;