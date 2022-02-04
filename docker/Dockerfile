FROM gradle:7.3.3-jdk11-alpine AS build

ENV discord_token=""
ENV riot_token=""

RUN git clone https://github.com/pirakao8/yuko.git \
    && echo "Downloading source from git"

WORKDIR yuko/

RUN gradle shadowjar \
    && echo "Building .jar with gradle"

WORKDIR ..

RUN mkdir jar \
    && echo "Creating jar directory" \
    && mv yuko/build/libs/yuko-1.0-SNAPSHOT.jar jar/yuko.jar \
    && echo "Moving .jar into jar/" \
    && rm -rf yuko \
    && echo "Deleting sources"

CMD java -jar jar/yuko.jar --discord-token=$discord_token  --riot-token=$riot_token -Dfile.encoding=utf-8 -encoding utf8