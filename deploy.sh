

docker push razear/mega-electric-app:latest
docker push razear/mega-electric-app:$SHA

fandogh service deploy --image razear/mega-electric-app --version latest --name mega-electric-app -p 8080  -d
