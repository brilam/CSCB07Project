# CSCB07Project
This is the Android project done in CSCB07 (Software Design). For this project, we are to write a flight booking application where a user can search for flights and itineraries and book it for themselves (there are specific requirements for the connecting flights specified in the assignment), and the client is able to edit their personal information. As an admin, you can edit the personal information of clients, upload flight information and client information, and book itineraries for clients.


# Requirements
This project was written for Android with API versions between 19 and 23. Any other versions above 23 were not tested as they were not present at the time that the code was created.

# Getting Started
Loaded information (persistent data) is stored app/saved_data. There are two files tha twill be in app/saved_data. These two files are flight_info.json and user_info.json. Parsing of JSON files are done with Android's native library. No external libraries are required.


This application will automatically create app/saved_data and user_info.json on the first run of the program with the following credentials:


Username: Brian


Email: notmyemail@email.com


Password: brian!


This user is an Administrator account. When uploading user info and flight information, please let the application create its own app/upload by going to upload flight information or upload user information and press the upload button. If this is not done, there will be permission issues with accessing any files located in that folder. Next, any flight information and user information in the form of CSVs should be pushed into the directory.
# Additional Information
Included are a few additional files. meetings.txt contains the weekly meetings and status meetings that we had during the project, and crc.pdf contains our design displayed as CRC cards. This was the intial state of our project before we implemented the code, and it was continually being updated during the process of the project.
