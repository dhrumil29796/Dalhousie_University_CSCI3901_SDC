# Query for synchronize
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
    
# Query for findGatherings
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
    
# Query to update the Contact_Notified in Contact table
UPDATE Contact SET Contact_Notified = TRUE WHERE ContactID = 2;

