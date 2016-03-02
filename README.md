# System Dependencies

* Stores system component dependencies as a graph.
* Uses topological sorting to install/uninstall components.
* Parses three command types `DEPEND`, `ADD`, `REMOVE`, and `LIST`.

## Sample Input
```
DEPEND TELNET TCPIP NETCARD
DEPEND TCPIP NETCARD
DEPEND DNS TCPIP NETCARD
DEPEND BROWSER TCPIP HTML
ADD NETCARD
ADD TELNET
ADD foo
REMOVE NETCARD
ADD BROWSER
ADD DNS
LIST
REMOVE TELNET
REMOVE NETCARD
REMOVE DNS
REMOVE NETCARD
ADD NETCARD
REMOVE TCPIP
REMOVE BROWSER
EMOVE TCPIP
LIST
```
## Sample Output
```
DEPEND TELNET TCPIP NETCARD
DEPEND TCPIP NETCARD
DEPEND DNS TCPIP NETCARD
DEPEND BROWSER TCPIP HTML
ADD NETCARD
    Installing NETCARD
ADD TELNET
    Installing TCPIP
    Installing TELNET
ADD foo
    Installing foo
REMOVE NETCARD
    NETCARD is still needed.
ADD BROWSER
    Installing HTML
    Installing BROWSER
ADD DNS
    Installing DNS
LIST
    HTML
    BROWSER
    DNS
    NETCARD
    foo
    TCPIP
    TELNET
REMOVE TELNET
    Removing TELNET
REMOVE NETCARD
    NETCARD is still needed.
REMOVE DNS
    Removing DNS
REMOVE NETCARD
    NETCARD is still needed.
ADD NETCARD
    NETCARD is already installed.
REMOVE TCPIP
    TCPIP is still needed.
REMOVE BROWSER
    Removing BROWSER
    Removing HTML
    Removing TCPIP
REMOVE TCPIP
    TCPIP is not installed.
LIST
    NETCARD
    foo
```