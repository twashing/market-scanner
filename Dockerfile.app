FROM twashing/market-scanner-app-base:latest
MAINTAINER Timothy Washington

COPY . /app

ENTRYPOINT [ "lein" , "with-profile" , "+app" , "run" , "-m" , "com.interrupt.market-scanner.core/-main" ]