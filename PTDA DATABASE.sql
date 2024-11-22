USE exerciciosql;
/*DROP TABLE IF EXISTS passenger,ticket,bag,airplane,flight,seat,service,checkin,crew,assistant,pilot;*/

CREATE TABLE passenger (
name_passenger VARCHAR(40),
age SMALLINT,
email VARCHAR(40),
id INTEGER PRIMARY KEY NOT NULL
);

CREATE TABLE ticket(
id_passenger INTEGER 
REFERENCES passenger(id),
destination VARCHAR(40),
price INTEGER,
trip VARCHAR(40),
refundable BOOLEAN,
id INTEGER PRIMARY KEY NOT NULL
);

CREATE TABLE bag(
id_Ticket INTEGER
REFERENCES ticket(id),
size INTEGER,
weight INTEGER,
quantity INTEGER
);

CREATE TABLE airplane(
id INTEGER PRIMARY KEY,
destination VARCHAR(40),
source1 VARCHAR(40)
);

CREATE TABLE seat(
id_Ticket INTEGER
REFERENCES ticket(id),
id_Seat INTEGER PRIMARY KEY,
price INTEGER,
place INTEGER
);

CREATE TABLE service(
id INTEGER PRIMARY KEY,
nome VARCHAR(40)
);

CREATE TABLE flight(
id_plane INTEGER
REFERENCES airplane(id),
id INTEGER,
maxPassengers INTEGER,
timeTakeOff INTEGER,
timeLanding INTEGER,
destination VARCHAR(40),
source1 VARCHAR(40), 
codename VARCHAR(40)
);

CREATE TABLE CheckIn(
id_Ticket INTEGER 
REFERENCES ticket(id),
id_flight INTEGER
REFERENCES flight(id),
checkIn VARCHAR(40) PRIMARY KEY,
TimeCheckIn INTEGER
); 

CREATE TABLE crew(
id INTEGER PRIMARY KEY,
nome VARCHAR(40),
shift VARCHAR(40),
experience VARCHAR(40),
ranq VARCHAR(20)
);

CREATE TABLE assistant (
    id_assistant INTEGER PRIMARY KEY,
    FOREIGN KEY (id_assistant) REFERENCES crew(id)
);

CREATE TABLE pilot (
    id_pilot INTEGER PRIMARY KEY,
    FOREIGN KEY (id_pilot) REFERENCES crew(id)
);


/*DELIMITER //
CREATE PROCEDURE addPlane(
IN id INTEGER,
IN destination VARCHAR(40),
IN source1 VARCHAR(40)
)
BEGIN 
INSERT INTO airplane(id,destination,source1)
VALUES (id_airplane,destination_airplane,source1_airplane);
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE addFlight(
IN id_plane INTEGER,
IN id INTEGER,
IN maxPassengers INTEGER,
IN timeTakeOff INTEGER,
IN timeLanding INTEGER,
IN destination VARCHAR(40),
IN source1 VARCHAR(40), 
IN codename VARCHAR(40)
)
BEGIN 
INSERT INTO flight(id_plane,id,maxPassengers,timeTakeOff,timeLanding,destination,source1,codename)
VALUES (id_plane_flight,id_flight,maxPassengers_flight,timeTakeOff_flight,timeLanding_flight,destination_flight,source1_flight,codename_flight);
END //
DELIMITER ;*/
SELECT * FROM flight;
SELECT * FROM airplane;
SELECT * FROM passenger;
SELECT * FROM ticket;


