# Using the drshah schema
use drshah;

# Creating the MobileDevice Table
CREATE TABLE MobileDevice (
	MobileDeviceHash VARCHAR(100) PRIMARY KEY
);

# Creating the TestResult Table
CREATE TABLE TestResult (
	TestHash VARCHAR(100) PRIMARY KEY,
	Test_Date DATE,
	Test_Result BOOLEAN
);

# Creating the MobileDevice_TestResult Table
CREATE TABLE MobileDevice_TestResult (
	MobileDeviceHash VARCHAR(100) NOT NULL,
	TestHash VARCHAR(100) NOT NULL,
	PRIMARY KEY (MobileDeviceHash,TestHash),
	FOREIGN KEY (MobileDeviceHash) REFERENCES MobileDevice(MobileDeviceHash),
	FOREIGN KEY (TestHash) REFERENCES TestResult(TestHash)
);

# Creating the Contact Table
CREATE TABLE Contact (
	ContactID INT PRIMARY KEY AUTO_INCREMENT,
	MobileDeviceHash VARCHAR(100) NOT NULL,
	ContactDeviceHash VARCHAR(100),
	Contact_Date DATE NOT NULL,
	Contact_Duration INT NOT NULL,
    Contact_Notified BOOLEAN,
	FOREIGN KEY (MobileDeviceHash) REFERENCES MobileDevice(MobileDeviceHash),
	FOREIGN KEY (ContactDeviceHash) REFERENCES MobileDevice(MobileDeviceHash)
);


