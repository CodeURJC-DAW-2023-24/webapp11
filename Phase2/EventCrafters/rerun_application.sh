docker compose down
sudo rm -r mysql
docker rm $(docker ps -a -q)
docker rmi $(docker images -q)
docker volume prune
cd ~
sudo rm -r webapp11
git clone https://github.com/CodeURJC-DAW-2023-24/webapp11.git
cd webapp11/Phase2/EventCrafters
docker build -t luciadominguezrodrigo/eventcrafters .
docker compose up