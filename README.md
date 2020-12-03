# bulk permission setter for OneDrive

Damn you Minerva - why no bulk upload of feedback?

If you call `Graph.doDrive` (as in Bulk.java) it will set access permisions for all the files in the given top-level OneDrive folder from their usernames. Not well tested. Hardcoded for leeds.ac.uk.

for example if you have a folder called `toemail` containing:
```
   scsxxx_sc19yyy_sc17zzz.zip 
   scsxxx.zip
```
It will give the named users access to those files - sending them an email with `message` in the process.
```
Graph.doDrive( accessToken, "toemail", "message" );
```



