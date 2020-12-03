# bulk permission setter for OneDrive

Damn you Minerva - why no bulk upload of feedback?

If you call `Graph.doDrive` (as in Bulk.java) it will set access permisions for all the files in the given top-level OneDrive folder from their usernames. Not well tested. 

for example if you have a folder called `toemail` containing:
```
   scsxxx_sc19yyy_sc17zzz.zip 
   scsxxx.zip
```
It will give the named users access to those files - sending them an email with `message` in the process.
```
Graph.doDrive( accessToken, "toemail", "message" );
```

You will need to set up a new app as [here](https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=2) - I had to set "Allow public client flows" at the bottom of the Authentication page. You will then need add the client and setup the [oAuth.properties as here](https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=3). Hardcoded for leeds.ac.uk. Sorry about the Java.

Will write out a temp.txt with your secret auth token - delete this if permissions fail.

