# HTML Link Hopper
This program is used to learn about the web and cloud built on HTTP. HTTP is utilized to hops to differnet parts of the HTML based web. 

# Usage
First, build the Java file with
javac WebCrawler.java

then compile with
java WebCrawler <link>

# What it Does
This program takes two arguments which is:
1) The starting URL
2) The number of times we hop to another link from that URL.

The HTML Link Hopper will then parse the html by looking through <a href> that
references other URLs, (for example: https://github.com/alan-lai1738). It will
then hop to the next link on the page, then on next link on that page IF it hasn't
been visited yet. This link hopper only visits http/https URLs. Will occurr hop times
and stop if no more unique links are found. Every hop will print out the link visited.
  
