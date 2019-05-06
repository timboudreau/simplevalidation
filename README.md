Simple Validation
=================

This is a set of libraries, originally hosted on Kenai.com, and used in NetBeans, for ad-hoc validation of
objects, with a number of very useful built in validators (email addresses, IP addresses, URLs, that sort of thing),
and with an emphasis on good, meaningful, human-readable error messages.

All error messages from the built in validators are localized into English, French, Italian and German.

Originally intended for Swing applications, the GUI classes have been factored out into a separate project.

The `nbstubs` project includes stub versions of localization-related classes from NetBeans and is needed
when used outside of NetBeans - the `simplevalidation-standalone` project includes that in its JAR file.

The [original project documentation can be viewed here](http://htmlpreview.github.io/?https://github.com/timboudreau/simplevalidation/blob/master/ValidationAPI/doc/overview.html)


Maven Coordinates
-----------------

As of May 6, 2019, the groupId has been changed to `com.mastfrog` in order to be able to deploy the
artifacts to Maven central.

