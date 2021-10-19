# HTML Link Hopper
The HTML Link Hopper takes in a link and number of hops then hops to different links until there are no more
unique links or no more hops. 

# Usage
First, build the Java file in terminal with:

javac HTMLLinkHopper.java

then compile with

java HTMLLinkHopper <link> <number of hops>

# What it Does
This program takes two arguments which is:
1) The starting URL
2) The number of times we hop to another link from that URL.

The HTML Link Hopper will then parse the html by looking through <a href> that
references other URLs, (for example: https://github.com/alan-lai1738). It will
then hop to the next link on the page, then on next link on that page IF it hasn't
been visited yet. This link hopper only visits http/https URLs. Will occurr hop times
and stop if no more unique links are found. Every hop will print out the link visited.
  
# Why I built HTML Link Hopper
I made this to learn about the web and cloud built on HTTP. HTTP is utilized to hops to differnet parts of the HTML based web. 
