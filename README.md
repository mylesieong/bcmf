# BCM Form Maintenance Tool

This tool helps to search, summary and add entry to the "User Application Form" Excel Sheet. 

## Help text of this tool

```
NAME
       bcmf - user access form management utility for bcm

SYNOPSIS
       bcmf [ -a | -s | -b | -h | user_id ]

DESCRIPTION
       The user application form viewer and modifier.

OPTIONS
       -s       Summary the day's all forms(C/M/U/D/MB), date format is DD/MM/YYYY
       -h       Show this help text
       -b       Backup the excel file to a specified location
       -a       Launch a wizard for new entry adding
       user_id  Show the users' all application sorted by UAA number
       
EXAMPLES
       bcmf B999
       Present user B999 history, sort by descending UAA number.
       
       bcmf -s
       Present the day's all forms(C/M/U/D/MB)

**
```

## How to install

1. Build the exe by running maven command: `mvn package`
1. Create a folder and set the its path as system environment `%BCMF%`. Put the config file `bcmf.conf` into this folder.
1. update the config file if necessary.
1. Set the `bcmf.exe` to the system path so that it can run as a command.