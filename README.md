# SteamGameInfoRetrieval

This program is designed to take in three arguments:

 args[0]: the name of the output file 
 args[1]: The ID of the person in question
 args[2]: Steam's API access key
 
 It will then use Steam's web API (assuming that the proper ID and key have been input) to retrieve a Json
file with the data about all the games attached to that ID. 
The information will be printed to the output file with the format:
[game title] : [number of minutes played] minutes