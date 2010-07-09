## Backlog

* optional scaffolding using HTML5 inputs rather than pickers
* Input for DateTime with zone
* support GrailsUI, RichUI
* allow minutes on dateTimePicker to be restricted, e.g. only allow selection of every 5 minutes
* selection tag for Chronology
* JSON/XML support for Chronology

## Known Bugs

* auto timestamping broken in Grails 1.1.1
* PersistentDuration columns are not properly sortable
* DateTimeZone columns persist as VARBINARY instead of VARCHAR as there is no persistent type