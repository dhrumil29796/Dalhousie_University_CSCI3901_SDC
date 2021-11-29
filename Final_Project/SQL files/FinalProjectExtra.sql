use drshah;

CREATE TABLE Contact (
	MobileDeviceHash VARCHAR(100) NOT NULL,
	ContactDeviceHash VARCHAR(100),
	Contact_Date DATE NOT NULL,
	Contact_Duration INT NOT NULL,
    PRIMARY KEY (MobileDeviceHash,ContactDeviceHash,Contact_Date,Contact_Duration),
	FOREIGN KEY (MobileDeviceHash) REFERENCES MobileDevice(MobileDeviceHash),
	FOREIGN KEY (ContactDeviceHash) REFERENCES MobileDevice(MobileDeviceHash)
);

# Inserting values into MobileDevice table
insert ignore into MobileDevice values ("m1");
insert ignore into MobileDevice values ("m2");
insert ignore into MobileDevice values ("m3");
insert ignore into MobileDevice values ("m4");

# Inserting values into MobileDevice table for findgathering
insert ignore into MobileDevice values ("1");
insert ignore into MobileDevice values ("2");
insert ignore into MobileDevice values ("3");
insert ignore into MobileDevice values ("4");
insert ignore into MobileDevice values ("5");
insert ignore into MobileDevice values ("6");
insert ignore into MobileDevice values ("7");

# Inserting values into TestResult table
insert ignore into TestResult values ("t1", '2021-01-13', true);
insert ignore into TestResult values ("t2", '2021-01-14', false);
insert ignore into TestResult values ("t3", '2021-01-16', true);
insert ignore into TestResult values ("t4", '2021-01-26', true);

# Inserting values into MobileDevice_TestResult table
insert ignore into MobileDevice_TestResult values ("m1", "t1");
insert ignore into MobileDevice_TestResult values ("m2", "t2");
insert ignore into MobileDevice_TestResult values ("m3", "t3");
insert ignore into MobileDevice_TestResult values ("m4", "t4");

# Inserting values into Contact table
insert into Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) values ("m1", "m2", '2021-01-03', 45, false);
insert into Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) values ("m2", "m1", '2021-01-03', 45, false);
insert into Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) values ("m1", "m4", '2021-01-05', 26, false);
insert into Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) values ("m4", "m1", '2021-01-05', 26, false);

# Inserting values into Contact table for findGathering
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("1", "2", "2021-04-03", 30,false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("1", "2", "2021-04-03", 20,false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("1", "3", "2021-04-03", 30, false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("1", "4", "2021-04-03", 30, false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("2", "1", "2021-04-03", 30, false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("2", "3", "2021-04-03", 30, false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("3", "4", "2021-04-03", 30, false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("4", "3", "2021-04-03", 30, false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("5", "6", "2021-04-03", 30, false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("5", "7", "2021-04-03", 30, false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("5", "1", "2021-04-03", 30, false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("5", "2", "2021-04-03", 30, false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("7", "6", "2021-04-03", 30, false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("7", "5", "2021-04-03", 30, false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("1", "5", "2021-04-03", 30, false);
INSERT INTO Contact(MobileDeviceHash, ContactDeviceHash, Contact_Date, Contact_Duration, Contact_Notified) VALUES ("2", "5", "2021-04-03", 30, false);

# Selecting the tables
SELECT * FROM MobileDevice;
SELECT * FROM TestResult;
SELECT * FROM MobileDevice_TestResult;
SELECT * FROM Contact;

# Old query
SELECT
	* 
FROM 
	Contact AS co, MobileDevice_TestResult AS dtr, TestResult AS tr
WHERE
	co.ContactDeviceHash = dtr.MobileDeviceHash AND
	dtr.TestHash = tr.TestHash AND
	tr.Test_Result = true AND
	co.MobileDeviceHash = "mike" AND
	(tr.Test_Date - co.Contact_Date) BETWEEN 0 AND 14;

# New query for synchronize
SELECT
	co.ContactID
FROM
    Contact AS co, MobileDevice_TestResult AS dtr, TestResult AS tr
WHERE
	co.ContactDeviceHash = dtr.MobileDeviceHash AND
    dtr.TestHash = tr.TestHash AND
    co.MobileDeviceHash = "m2" AND
    DATEDIFF(tr.Test_Date,co.Contact_Date) BETWEEN 0 AND 14 AND
    tr.Test_Result = true AND
    co.Contact_Notified = false
LIMIT 1;

# Updated query for synchronize
SELECT
	co.ContactID,
    co.ContactDeviceHash,
    dtr.MobileDeviceHash
FROM
    Contact AS co, MobileDevice_TestResult AS dtr, TestResult AS tr
WHERE
	co.ContactDeviceHash = dtr.MobileDeviceHash AND
    dtr.TestHash = tr.TestHash AND
    co.MobileDeviceHash = "m2" AND
    ABS(DATEDIFF(co.Contact_Date,tr.Test_Date)) BETWEEN 0 AND 14 AND 
    tr.Test_Result = true AND
    co.Contact_Notified = false;

# New query for findGatherings
SELECT
	co.MobileDeviceHash,
    co.ContactDeviceHash
FROM
	Contact AS co
WHERE
	co.Contact_Date = "2021-04-03" AND
    co.Contact_Duration >=10;

# Updated query for findGatherings
SELECT
	co.MobileDeviceHash,
    co.ContactDeviceHash,
    SUM(co.Contact_Duration) AS Total
FROM
	Contact AS co
WHERE
	co.Contact_Date = "2021-04-03"
GROUP BY
	co.MobileDeviceHash,co.ContactDeviceHash
HAVING
	SUM(co.Contact_Duration) >=10;
    
UPDATE Contact SET Contact_Notified = TRUE WHERE ContactID = 2;
UPDATE TestResult SET Test_Result = TRUE WHERE TestHash = "62a60117164d072612875dacf0722a597093afc4570d55b3d896d39447fcb69ecovidtest1";
UPDATE TestResult SET Test_Result = TRUE WHERE TestHash = "ae9b0fbe1580df0189b67b6e50ef1fa712f1952cd0d7a91b0ac1b7c2bd1cee73covidtest3";
UPDATE TestResult SET Test_Result = TRUE WHERE TestHash = "f0f7c01fb5c6a4d3a752ff2b982f5b1405246664a0a3980a1cdb7e08b17ab984covidtest4";

------------------------------------------------------
# Extra query
SELECT
c._id
FROM
contact AS c, mobile_device_test_result AS mdtr, test_result AS tr
WHERE
c.person_two_id = mdtr.mobile_device_id AND
mdtr.test_result_id = tr._id AND
c.person_one_id = (SELECT _id FROM mobile_device WHERE mobile_device_hash = "m1") AND
DATEDIFF(c.contact_date, tr.test_date) BETWEEN 0 AND 14 AND
tr.test_result = true AND
c.contact_notified = false
LIMIT 1;

SELECT DISTINCT userID, contactID FROM contacts WHERE date = +date+ && duration >= +minTime

SELECT DISTINCT c.person_one_id, c.person_two_id FROM contact AS c
WHERE c.contact_date = "2021-04-18" AND c.contact_duration >= 10;



------------------------------
select
Result.DTRHash,
CDHash,
Result.TDays
from 
(select
TR.Days AS TDays,
DTR.MDHash AS DTRHash
from
TestResult AS TR, Device_TResult AS DTR
where
TR.TestHash = DTR.THash AND 
Outcome = "positive") AS Result;

select
TR.Days AS ResultDay,
CO.Days AS ContactDay,
DTR.MDHash,
MD.MobileDeviceHash,
CO.MDHash,
CO.CDHash,
TR.Outcome
from
TestResult AS TR, Device_TResult AS DTR, MobileDevice AS MD, Contact AS CO
where
TR.TestHash = DTR.THash AND
MD.MobileDeviceHash = DTR.MDHash AND
CO.MDHash = MD.MobileDeviceHash AND
TR.Outcome = "positive" AND
(TR.Days - CO.Days) BETWEEN 0 AND 14;

select
CO.CDHash,
CO.Days AS ContactDate,
DTR.THash
from
MobileDevice AS MD, Contact AS CO, Device_TResult AS DTR, TestResult AS TR
where
CO.MDHash = MD.MobileDeviceHash AND
DTR.MDHash =  MD.MobileDeviceHash AND
TR.TestHash = DTR.THash AND
TR.Outcome = "positive" AND
CO.MDHash = "dhrumilM";

