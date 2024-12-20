USE PTDA24_BD_05;


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
source1 VARCHAR(40),
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

CREATE TABLE class(
id INTEGER PRIMARY KEY,
name VARCHAR(40),
service VARCHAR(40),
price INT
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
checkIn VARCHAR(40) PRIMARY KEY,  -- deve ser booleano ?
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
    FOREIGN KEY (id_assistant) REFERENCES crew(id) -- id_crew REFERENCES crew(id) ?
);

CREATE TABLE pilot (
    id_pilot INTEGER PRIMARY KEY,
    FOREIGN KEY (id_pilot) REFERENCES crew(id) -- id_crew REFERENCES crew(id) ?
);


DELIMITER //
CREATE PROCEDURE addPlane(
IN id INTEGER,
IN destination VARCHAR(40),
IN source1 VARCHAR(40)
)
BEGIN 
INSERT INTO airplane(id,destination,source1)
VALUES (id,destination,source1); 
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
VALUES (id_plane_flight,id_flight,maxPassengers_flight,timeTakeOff_flight,timeLanding_flight,destination,source1,codename);
END //
DELIMITER ;
DELIMITER //
CREATE TRIGGER add_flight_to_airplane BEFORE INSERT ON flight 
FOR EACH ROW 
BEGIN 
IF NOT EXISTS (
    SELECT 1 FROM flight WHERE id_plane = NEW.id_plane) THEN 
(SET NEW.id_plane =(SELECT id FROM airplane WHERE id NOT IN (SELECT id_plane FROM flight )
LIMIT 1)
);
END IF;

IF NEW.id_plane IS NULL THEN
SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No planes available';
END IF;
END
DELIMITER;


ALTER TABLE ticket
ADD COLUMN id_flight INTEGER REFERENCES flight(id);

DELIMITER //
CREATE TRIGGER associate_flight_with_passenger 
AFTER INSERT ON ticket
FOR EACH ROW 
BEGIN
DECLARE id_flight INTEGER;
SELECT id  INTO flight_id
    FROM flight
    WHERE destination = NEW.destination AND source1 = NEW.trip
    LIMIT 1;
    
    IF id_flight IS NOT NULL THEN 
    UPDATE ticket 
    SET id_flight = flight_id
    WHERE id = NEW.id;
	ELSE
	SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'No matching flight found for the ticket.';
    END IF;
END //

DELIMITER ;



SELECT * FROM flight;
SELECT * FROM airplane;
SELECT * FROM passenger;
SELECT * FROM ticket;


