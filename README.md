## Zadatak: Izrada funkcionalnosti „ping“-a sa unaprijeđenim funkcionalnostima,  program je namijenjen za testiranje rada IP mreže i određivanje RTT (Round Trip Time)Application

### Upotreba

```console
Usage: tcping [-c] [-p] [-port <port>] [-bind <ip_address>] [-mps <rate>] [-size <size>] hostname
Options:
-c 			 Run in Catcher mode.
-p 			 Run in Pitcher mode.
-port <port> 		 TCP socket port used for connection. Used in both Catcher and Pitcher mode.
-bind <ip_address> 	 IP address to listen on. Used in Catcher mode.
-mps <rate> 		 Speed of sending messaged "messages per second".Default 1. Used in Pitcher mode.
-size <size> 		 Size of message in bytes Min: 50,  Max: 3000, Default: 300. Used in Pitcher mode.
hostname 		 Computer name where Catcher is running. Used in Pitcher mode.
```

Example of catcher
```
tcping -c -port 5001 -bind 127.0.0.1
```
Example for pitcher
```
tcping -p -port 5001 -mps 150 -size 1000  localhost
```
