#!/bin/sh

cd /tmp;
ls -al;
sudo docker ps;
sudo docker stop timetable_container || true;
sudo docker load -i timetable_backend.tar;
sudo docker run --rm -d -p 8080:8080 --name timetable_container timetable_backend:latest;