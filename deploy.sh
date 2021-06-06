
docker push razear/mega-electric-app:latest
docker push razear/mega-electric-app:$SHA

fandogh service apply -f app-deployment.yml  \
                 -p SHA=$SHA