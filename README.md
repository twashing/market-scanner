# market-scanner

Edgarly microservice for interfacing with the TWS Gateway. From here, the platform makes historical, stock and scanner requests.


## Notes

A) You can connect to TWS, with a VNC viewer (ex: TightVNC).

```
cd ~/Downloads/tvnjviewer-2.8.3-bin-gnugpl/
java -jar tightvnc-jviewer.jar
```

B) You have to do an Initial build of base docker images.
```
docker build --force-rm -f Dockerfile.tws.base -t twashing/market-scanner-tws-base:latest -t twashing/market-scanner-tws-base:`git rev-parse HEAD` .
docker build --force-rm -f Dockerfile.app.base -t twashing/market-scanner-app-base:latest -t twashing/market-scanner-app-base:`git rev-parse HEAD` .
```

C) Bringing up docker-compose 
```
# Basic
docker-compose up 

# Force a rebuild of containers
docker-compose up --force-recreate --build
```

## Lifecycle functions

- return channels
- expose scanner results in system map
- expose request ids in system map (filter by request type)
- check that system is started
- check that connection is valid
