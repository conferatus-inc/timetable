#!/bin/sh

cd /tmp;
ls -al;
sudo docker ps;
sudo docker stop timetable_container;
sudo docker load -i timetable_backend.tar;
sudo docker run --rm -d -p 8090:8090 --name timetable_container timetable_backend:latest;