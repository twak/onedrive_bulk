# bulk permission setter for OneDrive

Leeds' Minerva lacks the ability to return complex feedback in bulk. Given a bunch of zip files (named for the usernames sc99xx@leeds.ac.uk) on onedrive - then run this script to allow the named student(s) permission to view the file. Also messages the students.

If you call `Graph.doDrive` (as in Bulk.java) it will set access permisions for all the files in the given top-level OneDrive folder from their usernames. Not well tested. 

for example if you have a top-level OneDrive folder called `toemail` containing:
```
   scsxxx_sc19yyy_sc17zzz.zip 
   scsxxx.zip
```
It will give the named users access to those files - sending them an email with `message` in the process.
```
new Graph( new Authentication(), "coursework_2_results", "message", "leeds.ac.uk" );
```

You will need to set up a new app as [here](https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=2) - I had to set "Supported account types" to "Accounts in any organizational directory (Any Azure AD directory - Multitenant)" and "Allow public client flows" to "True" (and then click save). Then add the "Application (client) ID" to 'Application.java'.

[based on this code](https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=3). Sorry about the Java.

Will write out a `temp.txt` with your secret auth token - delete this when permissions start failing.

