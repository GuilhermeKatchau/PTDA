CREATE TABLE person (
name CHAR(20),
age SMALLINT,
email CHAR(40),
id INTEGER PRIMARY KEY
);

CREATE TABLE ticket(
id_person INTEGER 
REFERENCES person(id),
destination CHAR(40),
price INTEGER,
trip CHAR(40),
refundable BOOLEAN,
num INTEGER PRIMARY KEY
);

CREATE TABLE bag(
num_Ticket INTEGER
REFERENCES ticket(num),
--os dois são por causa que não estava a dar com size e weight,
size2 INTEGER,
weight2 INTEGER,
quantity INTEGER
);

CREATE TABLE airplane(
id INTEGER PRIMARY KEY,
destination CHAR(40),
-- mesma coisa aqui,
source1 CHAR(40)
);

CREATE TABLE seat(
numTicket INTEGER
REFERENCES ticket(num),
numSeat INTEGER PRIMARY KEY,
price INTEGER,
place INTEGER
);

CREATE TABLE service(
id INTEGER PRIMARY KEY,
nome CHAR(20)
);

CREATE TABLE flight(
id_plane INTEGER
REFERENCES airplane(id),
id INTEGER,
maxPassangers INTEGER,
timeTakeOf DATE,
timeLanding DATE,
destination CHAR(40),
--same
source1 CHAR(40), 
codename CHAR(40)
);

CREATE TABLE CheckIn(
num_Ticket INTEGER 
REFERENCES ticket(num),
num_flight INTEGER
REFERENCES flight(num),
checkIn CHAR(20) PRIMARY KEY,
TimeCheckIn DATE
); 

CREATE TABLE crew(
id INTEGER PRIMARY KEY,
nome CHAR(20),
shift CHAR(20),
experience CHAR(20),
ranq INTEGER
);

CREATE TABLE assistant (
    id_assistant INTEGER PRIMARY KEY,
    FOREIGN KEY (id_assistant) REFERENCES crew(id)
);

CREATE TABLE pilot (
    id_pilot INTEGER PRIMARY KEY,
    FOREIGN KEY (id_pilot) REFERENCES crew(id)
);

